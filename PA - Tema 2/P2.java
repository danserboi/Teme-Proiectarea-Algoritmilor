// SERBOI FLOREA-DAN 325CB

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class P2 {
	static class Task {
		public static final String INPUT_FILE = "p2.in";
		public static final String OUTPUT_FILE = "p2.out";
		public static final int MAX = 1000007;
		// numarul oraselor
		int N;
		// numarul drumurilor
		int M;
		// orasul sursa
		int sursa;
		// orasul destinatie
		int dest;
		// listele drumurilor unidirectionale de la orasul i la orasul j care necesita efortul e
		ArrayList<Drum>[] adj;

		// clasa reprezinta drumul catre un oras destinatie
		// contine orasul destinatie si efortul pe care trebuie sa il depuna Robin pentru a ajunge
		public class Drum {
			public int oras;
			public int efort;

			Drum(int oras, int efort) {
				this.oras = oras;
				this.efort = efort;
			}
		}

		@SuppressWarnings("unchecked")
		private void readInput() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
				String line = br.readLine();
				String[] fields = line.split(" ");
				N = Integer.parseInt(fields[0]);
				adj = new ArrayList[N + 1];
				M = Integer.parseInt(fields[1]);
				line = br.readLine();
				fields = line.split(" ");
				sursa = Integer.parseInt(fields[0]);
				dest = Integer.parseInt(fields[1]);
				for (int i = 1; i <= N; i++) {
					adj[i] = new ArrayList<>();
				}
				for (int k = 1; k <= M; k++) {
					line = br.readLine();
					fields = line.split(" ");
					int i, j, e;
					i = Integer.parseInt(fields[0]);
					j = Integer.parseInt(fields[1]);
					e = Integer.parseInt(fields[2]);
					adj[i].add(new Drum(j, e));
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

		// Folosim Bellman-Ford optimizat
		// pe graful orientat cu N noduri, M arce, stocat in adj
		// pentru a afla distanta minima de la nodul sursa la destinatie

		// Optimizarea consta in faptul ca
		// daca d[v] nu se modifica la pasul curent atunci nu trebuie sa mai relaxam
		// niciunul din arcele care pleaca din v la pasul urmator
		// astfel, folosim o coada de prioritate in care tinem varfurile modificate

		// Complexitatea algoritmului este O(M*N).
		// Pentru graf rar, complexitatea este O(N^2).

		private int getResult() {
			// d[oras] = efortul minim pentru a ajunge de la sursa la oras
			int[] d = new int[N + 1];
			// initial efortul este infinit
			for (int i = 0; i <= N; i++) {
				d[i] = MAX;
			}
			// marcam nodurile pentru care am facut relaxare
			int[] marcat = new int[N + 1];
			PriorityQueue<Integer> pq = new PriorityQueue<Integer>(N + 1);
			d[sursa] = 0;
			marcat[sursa] = 1;
			pq.add(sursa);
			while (!pq.isEmpty()) {
				// extrag minimul
				int u = pq.poll();
				marcat[u] = 0;
				for (int i = 0; i < adj[u].size(); i++) {
					int v = adj[u].get(i).oras;
					// incercam sa relaxam muchia u->v
					if (d[v] > d[u] + adj[u].get(i).efort) {
						d[v] = d[u] + adj[u].get(i).efort;
						if (marcat[v] == 0) {
							marcat[v] = 1;
							pq.add(v);
						}
					}
				}
			}
			// efortul minim pentru a ajunge de la sursa la destinatie
			return d[dest];
		}

		public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
