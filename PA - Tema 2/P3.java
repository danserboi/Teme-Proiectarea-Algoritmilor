// SERBOI FLOREA-DAN 325CB

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class P3 {
	static class Task {
		public static final String INPUT_FILE = "p3.in";
		public static final String OUTPUT_FILE = "p3.out";
		public static final double MAX = 10000007;
		// numarul oraselor
		int N;
		// numarul drumurilor
		int M;
		// valoarea initiala a energiei lui Robin
		int E;
		// valoarea finala a energiei lui Robin
		double energ;
		// listele drumurilor unidirectionale de la orasul i la orasul j
		// pentru care Robin pierde procentul de energie p
		ArrayList<Drum>[] adj;

		// clasa reprezinta drumul catre un oras destinatie
		// contine orasul destinatie si energia pe care o pierde Robin(in procente) pentru a ajunge
		public class Drum {
			public int oras;
			public int procEnerg;

			Drum(int oras, int procEnerg) {
				this.oras = oras;
				this.procEnerg = procEnerg;
			}
		}
		
		// clasa contine orasul si costul corespunzator(calculat prin logaritmare)
		class Oras implements Comparable<Oras> {
			public int oras;
			public double cost;
			
			public Oras(int oras, double cost) {
				this.oras = oras;
				this.cost = cost;
			}

			@Override
			public int compareTo(Oras oras) {
				if (this.cost < oras.cost) {
					return -1;
				}
				if (this.cost > oras.cost) {
					return 1;
				}
				return 0;
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
				E = Integer.parseInt(fields[2]);
				for (int i = 1; i <= N; i++) {
					adj[i] = new ArrayList<>();
				}
				for (int k = 1; k <= M; k++) {
					line = br.readLine();
					fields = line.split(" ");
					int i, j, p;
					i = Integer.parseInt(fields[0]);
					j = Integer.parseInt(fields[1]);
					p = Integer.parseInt(fields[2]);
					adj[i].add(new Drum(j, p));
				}
				br.close();
			} catch (IOException E) {
				throw new RuntimeException(E);
			}
		}

		private void writeOutput(LinkedList<Integer> drum) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(OUTPUT_FILE));
				pw.println(energ);
				int len = drum.size();
				for (int i = 0; i < len - 1; i++) {
					pw.print(drum.get(i) + " ");
				}
				pw.print(drum.get(len - 1));
				pw.close();
			} catch (IOException E) {
				throw new RuntimeException(E);
			}
		}

		// Energia pierduta de Robin este egala cu E(1-(1-p1/100)*(1-p2/100)*...*(1-pn/100))
		// Observam ca a obtine energia pierduta minima este totuna cu a obtine valoarea minima 
		// pentru (-log(1-p1/100))+(-log(1-p2/100))+...+(-log(1-pn/100))
		// Astfel vom aplica Dijkstra inlocuind costurile initiale cu costul (-log(1-p1/100))
		private LinkedList<Integer> getResult() {
			PriorityQueue<Oras> pq = new PriorityQueue<Oras>(N);
			// vector de parinti pentru fiecare nod
			int[] p = new int[N + 1];
			// vector cu costul pentru fiecare muchie(energia in procente pe care Robin o pierde)
			int[] costuri = new int[N + 1];
			// vector cu estimarea distantei de la sursa la nod
			double[] d = new double[N + 1];
			// distanta pana la sursa este 0
			d[1] = 0;
			// adaugam sursa(orasul York) in coada de prioritate
			pq.add(new Oras(1, 0));
			// distanta initiala catre orice nod diferit de sursa este infinit
			for (int i = 2; i <= N; i++) {
				d[i] = MAX;
				pq.add(new Oras(i, MAX));
			}
			// vectorul spune daca un nod a fost sau nu tratat(1-da, 0-nu)
			int[] tratat = new int[N + 1];
			// cat timp mai avem noduri de tratat
			while (!pq.isEmpty()) {
				// scoatem nodul cu distanta minima
				int u = pq.remove().oras;
				// il adaugam in multimea celor tratate
				tratat[u] = 1;
				// parcurgem toti vecinii lui u
				for (int i = 0; i < adj[u].size(); i++) {
					Drum v = adj[u].get(i);
					// incercam sa relaxam muchiile
					// daca drumul de la sursa la nod prin u este mai mic decat cel curent
					if (tratat[v.oras] == 0 
						&& d[u] + (-Math.log(1 - v.procEnerg / 100.0)) < d[v.oras]) {
						// scoatem nodul din coada de prioritate
						// pentru a-l reintroduce cu valoarea actualizata
						pq.remove(new Oras(v.oras, d[v.oras]));
						// actualizam distanta si parintele
						d[v.oras] = d[u] + (-Math.log(1 - v.procEnerg / 100.0));
						p[v.oras] = u;
						costuri[v.oras] = v.procEnerg;
						// introducem nodul cu valoarea actualizata
						pq.add(new Oras(v.oras, d[v.oras]));
					}
				}
			}
			LinkedList<Integer> drum = new LinkedList<Integer>();
			energ = E;
			// construim drumul de la sursa York la destinatia Notthingam
			// si calculam energia pe care Robin o va avea la final
			int oras = N;
			while (oras != 0) {
				drum.addFirst(oras);
				energ *= (1 - costuri[oras] / 100.0);
				oras = p[oras];
			}
			return drum;
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