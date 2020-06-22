// SERBOI FLOREA-DAN 325CB
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Bomboane {
	static class Task {
		public static final String INPUT_FILE = "bomboane.in";
		public static final String OUTPUT_FILE = "bomboane.out";
		static final int MOD = 1000000007;
		int N, M;
		int[] x, y;
		
		private void readInput() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
				String linie = br.readLine();
				String[] campuri = linie.split(" ");
				N = Integer.parseInt(campuri[0]);
				M = Integer.parseInt(campuri[1]);
				x = new int[N + 1];
				y = new int[N + 1];
				for (int i = 1; i <= N; i++) {
					linie = br.readLine();
					campuri = linie.split(" ");
					x[i] = Integer.parseInt(campuri[0]);
					y[i] = Integer.parseInt(campuri[1]);
				}
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int result) {
			try {
				FileWriter fw = new FileWriter(OUTPUT_FILE);
				fw.write(result + "\n");				
				fw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int computeSolution() {
			// dp[i][j] reprezinta nr de moduri in care se pot imparti j bomboane primilor i copii
			int[][] dp = new int[N + 1][M + 1];
			// cazul de baza reprezinta nr de moduri in care poate imparti suma j primului copil
			// = 1 mod pentru fiecare valoare din intervalul sau
			for (int j = x[1]; j <= y[1] && j <= M; j++) {
				dp[1][j] = 1;
			}
			for (int cop = 2; cop <= N; cop++) {
				// fiecare numar de bomboane din intervalul copilului curent
				// trebuie adaugat la sumele deja existente, dar fara a depasi M;
				for (int nrBombCop = x[cop]; nrBombCop <= y[cop] && nrBombCop <= M; nrBombCop++) {
					// doar daca avem o suma diferita de 0 la pasul precedent,
					// inseamna ca am impartit bomboane tuturor copiilor precedenti
					int nrBombAnt = 0;
					while (dp[cop - 1][nrBombAnt] == 0) {
						nrBombAnt++;
					}
					// la numarul de moduri pentru suma curenta se adauga
					// numarul de moduri pentru suma deja existenta
					for (; nrBombAnt + nrBombCop <= M && dp[cop - 1][nrBombAnt] != 0; nrBombAnt++) {
						dp[cop][nrBombAnt + nrBombCop] = 
								(dp[cop][nrBombAnt + nrBombCop] + dp[cop - 1][nrBombAnt]) % MOD;
					}
				}
			}
			// astfel, obtinem numarul de moduri in care se pot imparti M bomboane
			return dp[N][M];
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
