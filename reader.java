import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader; 
import java.util.ArrayList;

public class reader {

	/**
	 * Retuerns how many times the strings are related
	 * @param firs: first string
	 * @param second: second string
	 * @return how many times first and second are related
	 */
	public static int check(String first, String second, String[][] elem, int i) {
		int c=0;

		for(int k=0;k<i;k++) {
			if((elem[k][0].equalsIgnoreCase(first) && elem[k][2].equalsIgnoreCase(second)) ||
			   (elem[k][0].equalsIgnoreCase(second) && elem[k][2].equalsIgnoreCase(first))) {
				c++;
			}
		}

		return c;
	}

	/**
	 * Create the context term matrix
	 * @return the matrix containing nouns on rows and contexts on columns
	 */
	public static String[][] generaMatrice(String[][] m, int righe, int colonne) {
		for(int k=0;k<=f;k++) {
			for(int j=1;j<=col;j++) {
				m[k+1][j]=""+check(m[0][j],m[k+1][0],elem,i);
			}
		}

		return m;
	}

	/**
	 * Check if the string p is contained into the array e
	 * @param p: the string to search
	 * @param e: the array to check
	 * @param h: the array size
	 */
	public static boolean esiste(String p, String[] e, int h) {
		for(int k=0;k<h;k++) {
			if(e[k].equals(p)) return true;
		}
		return false;
	}

	public static boolean esiste(ArrayList<Integer> a, double d) {
		for(int k=0;k<a.size();k++) {
			if(a.get(k)==d) return true;
		}
		return false;
	}

	/**
	 * Removes the repetition from the words
	 * @return the word set without repetitions
	 */
	public static String[] filtraRighe(String[][] elem) {
		String[] toReturn = new String[1024];
		String parola="";

		toReturn[f++]=elem[0][2];
		for(int k=1;k<i;k++) {
			parola=elem[k][2];
			for(int j=0;j<f;j++) {
				if(!(esiste(parola,toReturn,f))) toReturn[f++]=parola;
			}
		}

		return toReturn;
	}

	/**
	 * Removes the repetition from the contexts
	 * @return the contexts without repetitions
	 */
	public static String[] filtraColonne(String[][] elem) {
		String[] toReturn = new String[1024];
		String parola="";

		toReturn[col++]=elem[0][0];
		for(int k=1;k<i;k++) {
			parola=elem[k][0];
			for(int j=0;j<col;j++) {
				if(!(esiste(parola,toReturn,col))) toReturn[col++]=parola;
			}
		}

		return toReturn;
	}

	private static String[] filtraColonne(String[][] elem, ArrayList<Double> rif, int size) {
		String[] toReturn = new String[1024];
		String parola="";
		
		toReturn[col++]=elem[0][0];
		for(int k=1;k<i;k++) {
			for(int j=0;j<col;j++) {
				parola=elem[j][0];
				if(rif.get(j)!=TOREMOVE) toReturn[size++]=parola;
			}
		}

		return toReturn;
	}

	/** 
	 * Read the parser output
	 */
	public static void leggi(String file) {
		String s=null;

		try {
			FileReader f = new FileReader(file);
			BufferedReader br = new BufferedReader(f);
			do {
				if((s=br.readLine())!=null) {
					if(s.equalsIgnoreCase("")) continue;
					read.add(s);
				}
			} while (s!=null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a vector wich elements are triples containing two terms and a relationship
	 */
	public static void crea() {
		String[] app=null;

		for(int j=0;j<read.size();j++) {
			app=c.extract_word(read.get(j));
			if((((app[1].equals("V:subj:N") || app[1].equals("N:subj:N") || app[1].equals("V:obj:N")) || app[1].equals("N:nn:N")) && !(app[0].equalsIgnoreCase(app[2])))) { 
				if(!(app[0].equalsIgnoreCase("they")) && !(app[0].equalsIgnoreCase("do")) && !(app[0].equalsIgnoreCase("get"))) elem[i++]=app;
			}
		}
	}

	/**
	 * Print all the relationship taken from the parser
	 */
	public static void stampa() {
		System.out.print("Relazioni considerate");
		System.out.print("\n");

		for(int k=0;k<i;k++) {
			for(int j=0;j<3;j++) {
				if(elem[k]!=null) {
					System.out.print(elem[k][j]+" ");
				}
			}
			System.out.print("\n");
		}
	}

	private static double idf(int h) {
		return Math.log(f/c.checkSulleColonne(h,f,matrix));
	}

	private static double tf(int x, int y) {
		return Double.parseDouble(matrix[x][y]);
	}

	/**
	 * Computes the tf-idf functions
	 * @param h: l'indice del contesto
	 * @return: il valore di tf
	 */
	public static void tfIdf() {
		ArrayList<Double> toReturn=new ArrayList<Double>();
		double tfIdfValue=0;

		for(int k=1;k<=f;k++) {
			for(int j=1;j<=col;j++) {
				matrix[k][j]=""+tf(k,j)*idf(j);
			}
		}

		for(int k=1;k<col;k++) {
			toReturn.add(c.sommaSulleColonne(k, f, matrix));
		}

		for(int k=1;k<toReturn.size();k++) {
			if(toReturn.get(k)<SOGLIA) {
				eliminaColonna(k);
			}
		}

	}

	/**
	 * Prints the matrix
	 * @param: matrix, is the matrix to print
	 */
	public static void stampa(String[][] matrix) {
		System.out.print("Matrice");
		System.out.print("\n");

		for(int k=0;k<=f;k++) {
			for(int j=0;j<=col;j++) {
				System.out.print(matrix[k][j]+"\t");
			}
			System.out.print("\n");
		}
	}

	/**
	 * Prints the vector v with size g
	 * @param: v is the vector
	 * @param: g is the size
	 */
	public static void stampa(String[] v, int g) {
		for(int k=0;k<g;k++) {
			System.out.println(v[k]+" ");
		}
	}

	/**
	 * Prints the v list
	 * @param: v is the arrayList to print
	 */
	public static void stampa(ArrayList<Integer> v) {
		System.out.println("Il vettore dei tfIdf che hanno superato il test");
		for(int k=0;k<v.size();k++) {
			System.out.println(v.get(k)+" ");
		}
	}

	/*
	 * The next three methods computes the mutual information
	 */
	public static double elemento(int x, int y) {
		return Double.parseDouble(matrix[x][y]);
	}
	public static double totale() {
		double s=0;

		for(int k=1;k<=f;k++) {
			for(int j=1;j<=col;j++) {
				s+=elemento(k,j);
			}
		}

		return s;
	}
	public static double denominatore(int k, int j) {
		double s=c.sommaSulleRighe(k,col,matrix)/totale();
		double d=c.sommaSulleColonne(j,f,matrix)/totale();
		return s*d;
	}

	/**
	 * Computes the mutual information on the matrix
	 */
	public static double[][] mutualInformation(String[][] matrix) {
		double N=totale();
		double[][] toReturn = new double[1024][1024];

		for(int k=1;k<=f;k++) {
			for(int j=1;j<=col;j++) {
				if(denominatore(k,j)!=0) { toReturn[k][j]=(elemento(k,j)/N)/denominatore(k,j); }
				else toReturn[k][j]=-1;
			}
		}
	
		return toReturn;
	}

	/**
	 * Print a real number matrix
	 */
	public static void stampa(double[][] m) {
		System.out.print("A=[");
		for(int k=1;k<f-1;k++) {
			for(int j=1;j<col-1;j++) {
				System.out.print(m[k][j]+",");
			}
			System.out.print(";");
		}
		System.out.print("]");
	}

	public static String[] eliminaNull(String[] vet) {
		String[] toReturn = new String[1024];
		int d=0;

		for(int k=0;k<sizes;k++) {
			if(vet[k]!=null) { 
				toReturn[d]=vet[k]; 
				d++; 
			}
		}

		sizes=d;
		return toReturn;
	}

	public static void eliminaRiga(int riga) {
		for(int k=riga+1;k<=f;k++) {
			for(int j=0;j<=col;j++) {
				matrix[k-1][j]=matrix[k][j];
			}
		}
		f--;
	}

	public static void filtra(ArrayList<Integer> d) {
		for(int k=0;k<d.size();k++) {
			eliminaColonna(d.get(k));
		}
	}
	public static void eliminaColonna(int colonna) {
		for(int k=colonna+1;k<=col;k++) {
			for(int j=0;j<=f;j++) {
				matrix[j][k-1]=matrix[j][k];
			}
		}
		col--;
	}

	public static void pulisciMatriceR() {
		for(int k=1;k<=f;k++) {
			if(c.sommaSulleRighe(k,col,matrix)==0) eliminaRiga(k);
		}
	}

	public static void pulisciMatriceC() {
		for(int k=1;k<=col;k++) {
			if(c.sommaSulleColonne(k,f,matrix)==0) eliminaColonna(k);
		}
	}

	public static void main(String[] argv) {
		return 0; // Notning special
	} 

	private static Controller c=new Controller();
	private static ArrayList<String> read=new ArrayList<String>();
	private static String[][] elem=new String[4024][4024];
	private static String[][] matrix=new String[4024][4024];
	private static int sizes=0;
	private static int i=0;
	private static int f=0; // Matrix rows
	private static int col=0; // Matrix columns
	private static final double SOGLIA = 2;
	private static final double TOREMOVE = -1;

} // END CLASS
