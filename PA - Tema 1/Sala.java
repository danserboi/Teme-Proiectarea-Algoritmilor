// SERBOI FLOREA-DAN 325CB
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Sala {
	static class Task {
		public static final String INPUT_FILE = "sala.in";
		public static final String OUTPUT_FILE = "sala.out";
		int N, M;
		Gantera[] gantere;

		private void readInput() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
				String linie = br.readLine();
				String[] campuri = linie.split(" ");
				N = Integer.parseInt(campuri[0]);
				M = Integer.parseInt(campuri[1]);
				gantere = new Gantera[N];
				for (int i = 0; i < N; i++) {
					linie = br.readLine();
					campuri = linie.split(" ");
					int greutate = Integer.parseInt(campuri[0]);
					int repetari = Integer.parseInt(campuri[1]);
					gantere[i] = new Gantera(greutate, repetari);
				}
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
			Arrays.sort(gantere);
			// maxim reprezinta Σ(Ri)*min(G(i))
			long maxim = 0;
			long numarTotalRepetari = 0;
			PriorityQueue<Integer> pq = new PriorityQueue<Integer>(M);
			for (int i = 0; i < M; i++) {
				pq.add(gantere[i].repetari);
				numarTotalRepetari += gantere[i].repetari;
				if (gantere[i].greutate * numarTotalRepetari > maxim) {
					maxim = gantere[i].greutate * numarTotalRepetari;
				}
			}
			long peek = pq.peek();
			for (int i = M; i < N; i++) {
				// daca putem avea un numar de repetari mai mare folosind gantera curenta
				if (peek < gantere[i].repetari) {
					// actualizam numarul total de repetari
					numarTotalRepetari -= peek;
					numarTotalRepetari += gantere[i].repetari;
					// scoatem din coada de prioritate varful
					pq.remove();
					// adaugam numarul de repetari pentru gantera curenta
					pq.add(gantere[i].repetari);
					// actualizam peek
					peek = pq.peek();
					// verificam daca avem un nou maxim
					if (gantere[i].greutate * numarTotalRepetari > maxim) {
						maxim = gantere[i].greutate * numarTotalRepetari;
					}
				}
			}
			return maxim;
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

class Gantera implements Comparable<Gantera> {
	int greutate;
	int repetari;

	public Gantera(int greutate, int repetari) {
		super();
		this.greutate = greutate;
		this.repetari = repetari;
	}

	@Override
	public int compareTo(Gantera arg) {
		if (this.greutate == arg.greutate) {
			return arg.repetari - this.repetari;
		}
		return arg.greutate - this.greutate;
	}

}
