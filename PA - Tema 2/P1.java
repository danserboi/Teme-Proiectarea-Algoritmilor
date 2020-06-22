// SERBOI FLOREA-DAN 325CB

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class P1 {
	static class Task {
		public static final String INPUT_FILE = "p1.in";
		public static final String OUTPUT_FILE = "p1.out";
		// Robin se afla in orasul cu indexul 1
		public static final int ORAS_ROBIN = 1;
		// deblocam pe rand maxim 100 orase pentru a converge mai repede catre solutie
		// respectand ordinea din permutare
		public static final int NR_ORASE_DEBLOCATE = 100;
		// numarul de orase
		int N;
		// orasele sunt conectate intre ele prin M drumuri bidirectionale
		int M;
		// numarul de orase din care lorzii vor incepe cautarile
		int K;
		// vector cu orasele din care lorzii vor incepe cautarile
		int[] oraseStart;
		// vector cu permutarea oraselor pe care armata lui Robin le va bloca
		int[] permOraseBlocate;
		// liste de adiacenta pentru orase
		ArrayList<Integer>[] adj;

		@SuppressWarnings("unchecked")
		private void readInput() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
				String linie = br.readLine();
				String[] campuri = linie.split(" ");
				N = Integer.parseInt(campuri[0]);
				adj = new ArrayList[N + 1];
				M = Integer.parseInt(campuri[1]);
				K = Integer.parseInt(campuri[2]);
				oraseStart = new int[K + 1];
				linie = br.readLine();
				campuri = linie.split(" ");
				// citim orasele din care lorzii vor incepe cautarile
				for (int i = 1; i <= K; i++) {
					oraseStart[i] = Integer.parseInt(campuri[i - 1]);
				}
				permOraseBlocate = new int[N];
				linie = br.readLine();
				campuri = linie.split(" ");
				// citim permutarea oraselor pe care armata lui Robin poate bloca
				for (int i = 1; i <= N - 1; i++) {
					permOraseBlocate[i] = Integer.parseInt(campuri[i - 1]);
				}
				for (int i = 1; i <= N; i++) {
					adj[i] = new ArrayList<Integer>();
				}
				// citim drumurile dintre 2 orase conectate direct
				for (int i = 1; i <= M; i++) {
					linie = br.readLine();
					campuri = linie.split(" ");
					int u = Integer.parseInt(campuri[0]);
					int v = Integer.parseInt(campuri[1]);
					adj[u].add(v);
					adj[v].add(u);
				}
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int result) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(OUTPUT_FILE));
				pw.print(result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int computeSolution() {
			// initial, toate orasele sunt blocate, cu exceptia celui in care se afla Robin
			// prin urmare, lista de adiacenta pentru orase, este goala
			@SuppressWarnings("unchecked")
			ArrayList<Integer>[] currAdj = new ArrayList[N + 1];
			for (int i = 1; i <= N; i++) {
				currAdj[i] = new ArrayList<Integer>(adj[i].size());
			}
			int b = N - 1;
			// in acest vector tinem minte daca un oras este deblocat
			int[] deblocat = new int[N + 1];
			// orasul in care se afla Robin este deblocat
			deblocat[ORAS_ROBIN] = 1;
			for (; b > NR_ORASE_DEBLOCATE; b -= NR_ORASE_DEBLOCATE) {
				deblocareOraseInt(currAdj, b - NR_ORASE_DEBLOCATE + 1, b, deblocat);
				int descoperit = descoperit(currAdj, deblocat);
				// daca lorzii l-au gasit pe Robin, nu are sens sa mai deblocam orase
				// inseamna ca am gasit intervalul unde este descoperit Robin si, implicit, 
				// intervalul care contine numarul minim de orase care trebuiesc blocate
				if (descoperit == 1) {
					// blocam orasele din acest interval  pentru a reveni la starea pasului anterior
					blocareOraseInt(currAdj, b - NR_ORASE_DEBLOCATE + 1, b, deblocat);
					// iesim din aceasta bucla pentru a afla apoi care este cu exactitate
					// numarul minim de orase care trebuiesc blocate
					break;
				}
			}
			// deblocam pe rand cate un oras pana cand Robin devine descoperit
			for (; b >= 1; b--) {
				deblocareOras(currAdj, b, deblocat);
				int descoperit = descoperit(currAdj, deblocat);
				// daca Robin a devenit descoperit dupa deblocarea orasului curent
				// inseamna ca trebuie sa il blocam si pe acesta
				// deci numarul minim de orase blocate este egal cu indexul acestui oras
				if (descoperit == 1) {
					return b;
				}
			}
			return b;
		}

		// verifica daca pe graful curent, Robin poate fi descoperit sau nu
		// returneaza 1 daca poate fi descoperit, 0 altfel
		private int descoperit(ArrayList<Integer>[] currAdj, int[] deblocat) {
			// tinem minte daca un nod a mai fost sau nu vizitat
			// 1 daca nodul a fost vizitat, 0 altfel
			int[] vizitat = new int[N + 1];
			// plecam din fiecare K orase si vedem daca putem ajunge la Robin (in orasul 1)
			// efectuand o parcurgere de tip BFS
			for (int j = 1; j <= K; j++) {
				// putem porni din acest oras doar daca nu a mai fost vizitat
				// si daca este deblocat
				if (vizitat[oraseStart[j]] == 0 && deblocat[oraseStart[j]] == 1) {
					bfs(currAdj, oraseStart[j], vizitat);
				}
				// daca Robin a fost gasit, nu are sens sa mai continuam
				if (vizitat[ORAS_ROBIN] == 1) {
					return 1;
				}
			}
			return 0;
		}
		
		// metoda efectueaza o parcurgere BFS a oraselor
		private void bfs(ArrayList<Integer>[] currAdj, int u, int[] vizitat) {
			vizitat[u] = 1;
			// daca lorzii l-au gasit pe Robin, nu are sens sa mai continuam
			if (u == ORAS_ROBIN) {
				return;
			}
			Queue<Integer> q = new LinkedList<Integer>();
			q.add(u);
			while (!q.isEmpty()) {
				Integer oras = q.peek();
				for (int i = 0; i < currAdj[oras].size(); i++) {
					int succesor = currAdj[oras].get(i);
					if (vizitat[succesor] == 0) {
						vizitat[succesor] = 1;
						// daca Robin a fost gasit, nu are sens sa mai continuam
						if (succesor == ORAS_ROBIN) {
							return;
						}
						q.add(succesor);
					}
				}
				q.poll();
			}
		}

		// metoda blocheaza orasele dintr-un interval dat(elimina drumurile adiacente lor)
		private void blocareOraseInt(ArrayList<Integer>[] currAdj, int l, int r, int[] deblocat) {
			for (int b = l; b <= r; b++) {
				int orasBlocat = permOraseBlocate[b];
				deblocat[orasBlocat] = 0;
				currAdj[orasBlocat].clear();
				for (int i = 1; i <= N; i++) {
					currAdj[i].remove((Integer) orasBlocat);
				}
			}
		}

		// metoda deblocheaza orasele dintr-un interval dat 
		// adauga drumurile adiacente oraselor deblocate
		private void deblocareOraseInt(ArrayList<Integer>[] currAdj, int l, int r, int[] deblocat) {
			for (int b = r; b >= l; b--) {
				deblocareOras(currAdj, b, deblocat);
			}
		}

		// metoda deblocheaza un oras
		private void deblocareOras(ArrayList<Integer>[] currAdj, int b, int[] deblocat) {
			int orasBlocat = permOraseBlocate[b];
			deblocat[orasBlocat] = 1;
			// desi graful este bidirectional, vedem muchiile ca si cum pleaca din/intra in oras
			// d.p.d.v. al listelor de adiacenta
			for (int i = 0; i <= adj[orasBlocat].size() - 1; i++) {
				int orasAdiacent = adj[orasBlocat].get(i);
				// adaugam muchie doar daca orasul adiacent nu este blocat
				if (deblocat[orasAdiacent] == 1) {
					// adaugam muchiile care ajung in oras
					currAdj[orasAdiacent].add(orasBlocat);
					// adaugam muchiile care pleaca din oras
					currAdj[orasBlocat].add(orasAdiacent);
				}
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
