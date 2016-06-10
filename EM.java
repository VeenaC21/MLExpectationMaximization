import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class EM {

	static int[][] traininput = new int[1000][4];

	static double[] proba = new double[2];
	static double[] probb = new double[4];
	static double[] probc = new double[4];
	static double[] probd = new double[8];
	static ArrayList<Row> rows = new ArrayList<Row>();

	public static void main(String[] args) throws NumberFormatException,
			IOException {

		int count = 0;

		BufferedReader br = new BufferedReader(new FileReader(
				"/Users/veenac/Downloads/bn.data"));
		String line = null;
		int colcount = 0;
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			for (String str : values) {
				if (str.equals("?")) {
					traininput[count][colcount] = 2;
				} else
					traininput[count][colcount] = Integer.parseInt(str);
				colcount++;
			}
			colcount = 0;
			count++;
		}
		System.out.println(count);
		 initializeProb();

		em();
		for (int i = 0; i <= 1; i++) {
			System.out.print(proba[i]+" ");
		}
		System.out.println();
		for (int i = 0; i <= 3; i++) {
			System.out.print(probb[i]+" ");
		}
		System.out.println();
		for (int i = 0; i <= 3; i++) {
			System.out.print(probc[i]+" ");
		}
		System.out.println();
		for (int i = 0; i <= 7; i++) {
			System.out.print(probd[i]+" ");
		}
		System.out.println();
		
		int count0=0;
		int count1=0;
		int count2=0;
		int count3=0;
		
		 for(int i=0;i<=999;i++){
			 if(traininput[i][0]==2){
				 count0++;
			 }
			 if(traininput[i][1]==2){
				 count1++;
			 }
			 if(traininput[i][2]==2){
				 count2++;
			 }
			 if(traininput[i][3]==2){
				 count3++;
			 }
		 }
		 
		 System.out.println(count0/(double)1000+" "+count1/(double)1000+" "+count2/(double)1000+" "+count3/(double)1000+" ");

	}

	public static void initializeProb() {
		Random rn = new Random();
		double value;
		int sum = 0;

		proba[0] = 0.5;
		proba[1] = 0.5;

		probb[0] = 0.50;
		probb[1] = 0.50;
		probb[2] = 0.50;
		probb[3] = 0.50;

		probc[0] = 0.50;
		probc[1] = 0.50;
		probc[2] = 0.75;
		probc[3] = 0.25;
		
		probd[0] = 0.50;
		probd[1] = 0.50;
		probd[2] = 0.25;
		probd[3] = 0.75;
		probd[4] = 0.50;
		probd[5] = 0.50;
		probd[6] = 0.25;
		probd[7] = 0.75;
		
	}

	public static void em() {

		double weight;
		ArrayList<Integer> missingList = new ArrayList<Integer>();
		ArrayList<Integer> obsList = new ArrayList<Integer>();

		for (int i = 0; i <= 999; i++) {
			obsList = new ArrayList<Integer>();
			missingList = new ArrayList<Integer>();
			for (int j = 0; j <= 3; j++) {
				if (traininput[i][j] == 2) {
					missingList.add(j);
				} else {
					obsList.add(j);
				}
			}

			int l = missingList.size();
			int no = (int) Math.pow(2, l);
			ArrayList<Integer> x = new ArrayList<Integer>(Arrays.asList(
					traininput[i][0], traininput[i][1], traininput[i][2],
					traininput[i][3]));
			if (l != 0) {
				weight = 1 / (double) no;
				for (int k = 0; k <= no - 1; k++) {
					x = new ArrayList<Integer>(Arrays.asList(traininput[i][0],
							traininput[i][1], traininput[i][2],
							traininput[i][3]));

					String bin = Integer.toBinaryString(k);
					if (bin.length() < l) {
						for (int b = 1; b <= l; b++) {
							bin = "0" + bin;
						}
					}
					int c = 0;
					for (int m : missingList) {
						x.remove(m);
						x.add(m, bin.charAt(c) - 48);
						c++;
					}
					if (c != l) {
						System.out.println("wrong");
					}

					Row row = new Row(i, missingList, obsList, x, 100);
					// System.out.println(x);
					rows.add(row);
				}
			} else {
				Row row = new Row(i, missingList, obsList, x, 1.0);
				rows.add(row);
			}

		}
		/*
		 * for(Row r: rows){ //System.out.println(r.getList()+" "+r.weight); }
		 */
		//mStep();
		for (int i = 0; i <= 20; i++) {
			eStep();
			mStep();
		}
	}

	public static void eStep() {
		// ArrayList<Integer> missingList = new ArrayList<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		double count = 0;
		double subTotal = 0;
		int r = 0;
		for (Row row : rows) {

			if (row.getMissingcol().isEmpty()) {
				if (row.getRowId() != r) {

					if (subTotal != 0)
						updateSubTotal(r, subTotal);

					subTotal = 0;
				}
				continue;
			} else {

				// new missing row started
				if (row.getRowId() != r) {

					if (subTotal != 0) {
						// System.out.println("Row: "+row.getRowId()+" r: "+r);
						updateSubTotal(r, subTotal);
					}

					subTotal = 0;
				}

				r = row.getRowId();

				// System.out.println(r);
				list = row.getList();

				count = getProb(list.get(0), list.get(1), list.get(2),
						list.get(3),row.getWeight());
				// System.out.println(count);
				subTotal += count;
				// add according to mit slides
				// System.out.println(total+" "+obsList.size());
				// row.weight = count / (double) total;

				row.weight = count;
			}
		}
	}

	public static double getProb(int i0, int i1, int i2, int i3, double w) {

		String sb = i0 + "" + i1;
		String sc = i0 + "" + i2;
		String sd = i1 + "" + i2 + "" + i3;
		int b = Integer.parseInt(sb, 2);
		int c = Integer.parseInt(sc, 2);
		int d = Integer.parseInt(sd, 2);
		//System.out.println("indexing: "+i0+" "+i1+" "+i2+" "+i3);
		//System.out.println(i0+" "+b+" "+c+" "+d);
		double prod = 0;
		
		
		prod=proba[i0]*probb[b]*probc[c]*probd[d];

		 //System.out.println("prod: "+prod+" w: "+w);

		if(prod!=0){
			return Math.log(prod);
			}
		else
			return 0;
	//	return prod;
	}

	public static void updateSubTotal(int r, double total) {
		// System.out.println(total);
		for (Row row : rows) {
			if (row.getRowId() == r) {
				row.weight = row.weight / total;

			}

		}
	}

	// calculate conditional probabilities and log likelihood
	public static void mStep() {

		double counta = 0;
		double countanot = 0;

		double countba = 0;
		double countbnota = 0;
		double countbanot = 0;
		double countbnotanot = 0;

		double countca = 0;
		double countcnota = 0;
		double countcanot = 0;
		double countcnotanot = 0;

		double Cdnotbnotcnot = 0;
		double Cdnotbnotc = 0;
		double Cdnotbcnot = 0;
		double Cdnotbc = 0;
		double Cdbnotcnot = 0;
		double Cdbnotc = 0;
		double Cdbcnot = 0;
		double Cdbc = 0;

		double Cbc = 0;
		double Cbnotc = 0;
		double Cbcnot = 0;
		double Cbnotcnot = 0;

		double countc = 0;
		double countcnot = 0;
		double countb = 0;
		double countbnot = 0;
		
		double countdb= 0;
		double countdnotb=0;
		double countdbnot= 0;
		double countdnotbnot=0;
		
		double countdcnot = 0;
		double countdnotcnot=0;
		double countdc = 0;
		double countdnotc=0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		double w;
		double totalW = 0;
		for (Row row : rows) {
			list = row.getList();
			w = row.weight;
			totalW += w;
			// System.out.println(w);
			// b given a and c given a
			if (list.get(0) == 1) {
				counta = counta + w;
				if (list.get(1) == 1) {
					countba = countba + w;
				}
				if (list.get(2) == 1) {
					countca = countca + w;
				}

			}
			if (list.get(0) == 0) {
				countanot = countanot + w;
				if (list.get(1) == 1) {
					countbanot = countbanot + w;
				}
				if (list.get(2) == 1) {
					countcanot = countcanot + w;
				}
			}

			if (list.get(1) == 0 && list.get(2) == 0) {
				Cbnotcnot += w;
				if (list.get(3) == 0) {
					Cdnotbnotcnot += w;
				} else {
					Cdbnotcnot += w;
				}
			}
			if (list.get(1) == 0 && list.get(2) == 1) {
				Cbnotc += w;
				if (list.get(3) == 0) {
					Cdnotbnotc += w;
				} else {
					Cdbnotc += w;
				}
			}
			if (list.get(1) == 1 && list.get(2) == 0) {
				Cbcnot += w;
				if (list.get(3) == 0) {
					Cdnotbcnot += w;
				} else {
					Cdbcnot += w;
				}
			}
			if (list.get(1) == 1 && list.get(2) == 1) {
				Cbc += w;
				if (list.get(3) == 0) {
					Cdnotbc += w;
				} else {
					Cdbc += w;
				}
			}
			
			if (list.get(1) == 1) {
				countb=countb+w;
				if (list.get(3) == 1) {
					countdb = countdb + w;
				}
			}
			if (list.get(1) == 0 ) {
				countbnot+=w;
				if (list.get(3) == 1) {
					countdbnot = countdbnot + w;
				}
			}
			if (list.get(2) == 1 ) {
				countc+=w;
				if (list.get(3) == 1) {
					countdc = countdc + w;
				}
			}
			if (list.get(2) == 0) {
				countcnot+=w;
				if (list.get(3) == 1) {
					countdcnot = countdcnot + w;
				}
			}

		}
		countbnota = counta - countba;
		countbnotanot = countanot - countbanot;

		countcnota = counta - countca;
		countcnotanot = countanot - countcanot;
		// System.out.println("total " + totalW);

		double likelihood = 0;

		proba[0] = countanot / totalW;
		proba[1] = counta / totalW;
		probb[0] = countbnotanot / countanot;
		probb[1] = countbanot / countanot;
		probb[2] = countbnota / counta;
		probb[3] = countba / counta;
		probc[0] = countcnotanot / countanot;
		probc[1] = countcanot / countanot;
		probc[2] = countcnota / counta;
		probc[3] = countca / counta;

		// System.out.println(counta);
		probd[0] = Cdnotbnotcnot / Cbnotcnot;
		probd[1] = Cdbnotcnot / Cbnotcnot;
		probd[2] = Cdnotbnotc / Cbnotc;
		probd[3] = Cdbnotc / Cbnotc;
		probd[4] = Cdnotbcnot / Cbcnot;
		probd[5] = Cdbcnot / Cbcnot;
		probd[6] = Cdnotbc / Cbc;
		probd[7] = Cdbc / Cbc;

		// System.out.println(probb[0]+" "+probb[1]+" "+probb[2]+" "+probb[3]);
		for (Row row : rows) {
			list = row.getList();

			double count = getProb(list.get(0), list.get(1), list.get(2),
					list.get(3),row.weight)-Math.log(row.getWeight());
			// System.out.println(count);
			
				likelihood += count*row.weight;
			// System.out.println(likelihood);

		}
	
		
		System.out.println(likelihood);
	}

	public static double countOccurrences(ArrayList<Integer> given) {
		double count = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		int c = 0;
		first: for (Row row : rows) {
			list = row.getList();
			for (int i : list) {

				if (i != given.get(c)) {
					c = 0;
					continue first;
				}
				c++;
			}
			count = count + row.weight;
			c = 0;

		}
		return count;
	}

	public static double countTotal1(int index, int value) {
		double count = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (Row row : rows) {
			list = row.getList();
			if (list.get(index) == value) {
				count = count + row.weight;
			}
		}
		return count;

	}

	public static double countTotal2(int i1, int v1, int i2, int v2) {
		double count = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (Row row : rows) {
			list = row.getList();
			if (list.get(i1) == v1 && list.get(i2) == v2) {
				count = count + row.weight;
			}
		}
		return count;

	}

	public static double countTotal3(int i1, int v1, int i2, int v2, int i3,
			int v3) {
		double count = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (Row row : rows) {
			list = row.getList();
			if (list.get(i1) == v1 && list.get(i2) == v2 && list.get(i3) == v3) {
				count = count + row.weight;
			}
		}
		return count;

	}

}
