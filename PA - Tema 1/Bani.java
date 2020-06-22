// SERBOI FLOREA-DAN 325CB
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Bani {
	static class Task {
		public static final String INPUT_FILE = "bani.in";
		public static final String OUTPUT_FILE = "bani.out";
		static final int MOD = 1000000007;
		int tipInst, N;
		
		private void readInput() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
				String linie = br.readLine();
				String[] campuri = linie.split(" ");
				tipInst = Integer.parseInt(campuri[0]);
				N = Integer.parseInt(campuri[1]);
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long result) {
			try {
				FileWriter fw = new FileWriter(OUTPUT_FILE);
				fw.write(result + "\n");
				fw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private long computeSolution() {
			// rezultatul pentru cazul de baza N = 1
			long rez = 5;
			if (N >= 2) {
				//
				if (tipInst == 1) {
					// pentru tipul 1 observam ca suma modurilor in care pot fi alese n bancnote
					// este de 2 ori suma modurilor in care pot fi alese n - 1 bancnote
					// astfel S(n) = S(n-1) * 2 = S(n-2) * 2 * 2 = ... = 2^(n-1) * S(1).
					rez = (fastPow(2, N - 1, MOD) * rez) % MOD;
				} else {
					// pentru tipul 2 calculam fiecare recurenta separat deoarece
					// nu putem scrie o relatie de recurenta pentru numarul total de moduri
					// fara a tine minte numarul anterior de moduri
					// cu ultima bancnota aleasa fiind cea de 200,
					// ceea ce implica sa tinem minte nr de moduri pentru X ultima bancnota aleasa
					// unde X apartine 10, 50, 100, 200 sau 500
					
					// cazul de baza: daca nr de bancnote este 1, putem folosi cate una din fiecare
					long nr10Ant = 1, nr50Ant = 1, nr100Ant = 1, nr200Ant = 1, nr500Ant = 1;
					long nr10Cur = 0, nr50Cur = 0, nr100Cur = 0, nr200Cur = 0, nr500Cur = 0;
					for (int i = 2; i <= N; i++) {
						// astfel, avem urmatoarele recurente:
						nr10Cur = ((nr50Ant + nr100Ant) % MOD + nr500Ant) % MOD;
						nr50Cur = (nr10Ant + nr200Ant) % MOD;
						nr100Cur = ((nr10Ant + nr100Ant) % MOD + nr200Ant) % MOD;
						nr200Cur = (nr50Ant + nr500Ant) % MOD;
						nr500Cur = nr200Ant;
						// pregatim urmatorul pas
						nr10Ant = nr10Cur;
						nr50Ant = nr50Cur;
						nr100Ant = nr100Cur;
						nr200Ant = nr200Cur;
						nr500Ant = nr500Cur;
					}
					// rezultatul este suma variabilelor care reprezinta ca s-au folosit N bancnote,
					// ultima fiind X unde X este 10, 50, 100, 200 sau 500
					rez = (((((((nr10Ant + nr50Ant) % MOD) + nr100Ant) % MOD) + nr200Ant) % MOD) 
							+ nr500Ant) % MOD;
				}
			}
			return rez;
		}
		
		private static long fastPow(int base, int exponent, int mod) {
			if (base == 0) {
				return 0;
			}
			if (exponent == 0) {
				return 1;
			}
			if (exponent % 2 == 1) {
				return (base % mod) * (fastPow(base, exponent - 1, mod) % mod) % mod;
			} else {
				long f = fastPow(base, exponent / 2, mod) % mod;
				return (f * f) % mod;
			}
		}
		
		public void solve() {
			readInput();
			writeOutput(computeSolution());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
