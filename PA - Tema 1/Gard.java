// SERBOI FLOREA-DAN 325CB
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Gard {
	static class Task {
		public static final String INPUT_FILE = "gard.in";
		public static final String OUTPUT_FILE = "gard.out";
		int N;
		Bucata[] bucati;
		
		private void readInput() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
				String linie = br.readLine();
				String[] campuri = linie.split(" ");
				N = Integer.parseInt(campuri[0]);
				bucati = new Bucata[N];
				for (int i = 0; i < N; i++) {
					linie = br.readLine();
					campuri = linie.split(" ");
					int start = Integer.parseInt(campuri[0]);
					int end = Integer.parseInt(campuri[1]);
					bucati[i] = new Bucata(start, end);
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
		// pentru a rezolva aceasta problema vom implementa un algoritm Greedy
		// sortam bucatile de gard descrescator dupa end si crescator dupa start
		// astfel, vom alege intotdeauna doar bucatile care aduc un plus in constructia gardului
		// adica cele care au start mai mic decat start-ul curent(pe care il actualizam)
		// altfel, daca start-ul este mai mare decat start-ul curent 
		// cum end-ul actual este mai mic decat orice end precedent
		// inseamna ca gardul este inclus in altul
		private int computeSolution() {
			Arrays.sort(bucati);
			int currStart = bucati[0].start;
			int bucatiRedundante = 0;
			for (int i = 1; i < N; i++) {
				if (currStart <= bucati[i].start) {
					bucatiRedundante++;
				} else {
					currStart = bucati[i].start;
				}
			}
			return bucatiRedundante;
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

//aceasta clasa reprezinta bucatile de gard care au coordonatele start si end
class Bucata implements Comparable<Bucata> {
	int start;
	int end;

	public Bucata(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}
	
	// am implementat metoda compareTo pentru a sorta descrecator dupa start si crescator dupa end
	@Override
	public int compareTo(Bucata arg) {
		if (this.end == arg.end) {
			return this.start - arg.start;
		} else {
			return arg.end - this.end;
		}
	}

}
