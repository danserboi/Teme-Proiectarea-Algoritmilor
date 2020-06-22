// SERBOI FLOREA-DAN 325CB

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Bonus {
	static class Task {
		public static final String INPUT_FILE = "bonus.in";
		public static final String OUTPUT_FILE = "bonus.out";
		// timpul pe care il are Robin la dispozitie
		int T;
		// numarul de busteni
		int N;
		// coordonatele lui Maid
		int xMaid;
		int yMaid;
		// energia consumata pentru fiecare tip de decizie
		int E1;
		int E2;
		int E3;
		// coordonatele capetelor bustenilor la fiecare moment de timp
		int[][] xStart;
		int[][] yStart;
		int[][] xEnd;
		int[][] yEnd;
		// directiile in care se misca bustenii la fiecare moment de timp
		char[][] m;

		private void readInput() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
				String line = br.readLine();
				String[] fields = line.split(" ");
				T = Integer.parseInt(fields[0]);
				N = Integer.parseInt(fields[1]);
				line = br.readLine();
				fields = line.split(" ");
				xMaid = Integer.parseInt(fields[0]);
				yMaid = Integer.parseInt(fields[1]);
				line = br.readLine();
				fields = line.split(" ");
				E1 = Integer.parseInt(fields[0]);
				E2 = Integer.parseInt(fields[1]);
				E3 = Integer.parseInt(fields[2]);
				xStart = new int[N + 1][T + 1];
				yStart = new int[N + 1][T + 1];
				xEnd = new int[N + 1][T + 1];
				yEnd = new int[N + 1][T + 1];
				for (int i = 1; i <= N; i++) {
					line = br.readLine();
					fields = line.split(" ");
					xStart[i][0] = Integer.parseInt(fields[0]);
					yStart[i][0] = Integer.parseInt(fields[1]);
					xEnd[i][0] = Integer.parseInt(fields[2]);
					yEnd[i][0] = Integer.parseInt(fields[3]);
				}
				m = new char[N + 1][T];
				for (int i = 1; i <= N; i++) {
					line = br.readLine();
					fields = line.split(" ");
					for (int j = 0; j <= T - 1; j++) {
						m[i][j] = fields[0].charAt(j);
						// calculam coordonatele capetelor bustenilor pentru fiecare moment de timp
						switch (m[i][j]) {
							case 'N':
								// inc y
								xStart[i][j + 1] = xStart[i][j];
								yStart[i][j + 1] = yStart[i][j] + 1;
								xEnd[i][j + 1] = xEnd[i][j];
								yEnd[i][j + 1] = yEnd[i][j] + 1;
								break;
							case 'S':
								// dec y
								xStart[i][j + 1] = xStart[i][j];
								yStart[i][j + 1] = yStart[i][j] - 1;
								xEnd[i][j + 1] = xEnd[i][j];
								yEnd[i][j + 1] = yEnd[i][j] - 1;
								break;
							case 'E':
								// inc x
								xStart[i][j + 1] = xStart[i][j] + 1;
								yStart[i][j + 1] = yStart[i][j];
								xEnd[i][j + 1] = xEnd[i][j] + 1;
								yEnd[i][j + 1] = yEnd[i][j];
								break;
							case 'V':
								// dec x
								xStart[i][j + 1] = xStart[i][j] - 1;
								yStart[i][j + 1] = yStart[i][j];
								xEnd[i][j + 1] = xEnd[i][j] - 1;
								yEnd[i][j + 1] = yEnd[i][j];
								break;
							default:
						}
					}
				}
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		// se genereaza pornind de la primul capat al busteanului 1
		// toate posibilele decizii pe care le poate Robin la un moment de timp
		// se verifica daca in urma deciziei se ajunge la Maid si daca energia consumata e minima
		// se scrie in fisierul de output energia minima pe care trebuie sa o consume Robin
		// pentru a reusi sa o salveze pe Maid la timp, numarul de miscari pe care trebuie sa le 
		// faca Robin pentru a ajunge la destinatie si miscarile efectuate
		private void computeSolution() {
			long min = Integer.MAX_VALUE;
			Pos minPos = null;
			int xRobin = xStart[1][0];
			int yRobin = yStart[1][0];
			Queue<Pos> q = new LinkedList<Pos>();
			q.add(new Pos(xRobin, yRobin, 1, 0, 0, null, '\0'));
			while (!q.isEmpty()) {
				Pos currNode = q.poll();
				if (currNode.x == xMaid && currNode.y == yMaid) {
					if (min > currNode.en) {
						min = currNode.en;
						minPos = currNode;
					}
				}
				if (currNode.t < T) {
					q.addAll(generateDecisions(currNode, min));
				}
			}
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(OUTPUT_FILE));
				pw.println(min);
				pw.println(minPos.t);
				char[] decisions = new char[minPos.t + 1];
				int[] jumpIds = new int[minPos.t + 1];
				while (minPos.lastPos != null) {
					decisions[minPos.t] = minPos.typeOfMove;
					if (minPos.typeOfMove == 'J') {
						jumpIds[minPos.t] = minPos.bustean;
					}
					minPos = minPos.lastPos;
				}
				for (int i = 1; i < decisions.length; i++) {
					pw.print(decisions[i]);
					if (decisions[i] == 'J') {
						pw.println(" " + jumpIds[i]);
					} else {
						pw.println();
					}
				}
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		// aceasta functie genereaza toate deciziile posibile data find o pozitie si minimul curent
		public ArrayList<Pos> generateDecisions(Pos pos, long min) {
			ArrayList<Pos> decisions = new ArrayList<Pos>();
			if (pos.t < T) {
				char dir = m[pos.bustean][pos.t];
				// H
				if (pos.en + E1 < min) {
					int[] newCoord = newCoord(pos.x, pos.y, dir);
					decisions.add(new Pos(newCoord[0], newCoord[1], 
							pos.bustean, pos.t + 1, pos.en + E1, pos, 'H'));
				}
				if (pos.en + E2 < min) {
					// N: y + 1
					if (pos.y + 1 <= yEnd[pos.bustean][pos.t]) {
						int[] newCoord = newCoord(pos.x, pos.y + 1, dir);
						decisions.add(new Pos(newCoord[0], newCoord[1], 
								pos.bustean, pos.t + 1, pos.en + E2, pos, 'N'));
					}
					// S: y - 1
					if (pos.y - 1 >= yStart[pos.bustean][pos.t]) {
						int[] newCoord = newCoord(pos.x, pos.y - 1, dir);
						decisions.add(new Pos(newCoord[0], newCoord[1], 
								pos.bustean, pos.t + 1, pos.en + E2, pos, 'S'));
					}
					// E: x + 1
					if (pos.x + 1 <= xEnd[pos.bustean][pos.t]) {
						int[] newCoord = newCoord(pos.x + 1, pos.y, dir);
						decisions.add(new Pos(newCoord[0], newCoord[1], 
								pos.bustean, pos.t + 1, pos.en + E2, pos, 'E'));
					}
					// V: x - 1
					if (pos.x - 1 >= xStart[pos.bustean][pos.t]) {
						int[] newCoord = newCoord(pos.x - 1, pos.y, dir);
						decisions.add(new Pos(newCoord[0], newCoord[1], 
								pos.bustean, pos.t + 1, pos.en + E2, pos, 'V'));
					}
				}
				// J
				if (pos.en + E3 < min) {
					for (int i = 1; i < pos.bustean; i++) {
						if (pos.x >= xStart[i][pos.t] 
								&& pos.x <= xEnd[i][pos.t] && pos.y >= yStart[i][pos.t]
								&& pos.y <= yEnd[i][pos.t]) {
							dir = m[i][pos.t];
							int[] newCoord = newCoord(pos.x, pos.y, dir);
							decisions.add(new Pos(newCoord[0], newCoord[1], i, 
									pos.t + 1, pos.en + E3, pos, 'J'));
						}
					}
					for (int i = pos.bustean + 1; i <= N; i++) {
						if (pos.x >= xStart[i][pos.t] 
								&& pos.x <= xEnd[i][pos.t] && pos.y >= yStart[i][pos.t]
								&& pos.y <= yEnd[i][pos.t]) {
							dir = m[i][pos.t];
							int[] newCoord = newCoord(pos.x, pos.y, dir);
							decisions.add(new Pos(newCoord[0], newCoord[1], i, 
									pos.t + 1, pos.en + E3, pos, 'J'));
						}
					}
				}
			}
			return decisions;
		}
		
		// se genereaza noile coordonate x si y dupa miscarea in directia data
		public int[] newCoord(int x, int y, char dir) {
			int[] newCoord = new int[2];
			switch (dir) {
				case 'N':
					// inc y
					newCoord[0] = x;
					newCoord[1] = y + 1;
					break;
				case 'S':
					// dec y
					newCoord[0] = x;
					newCoord[1] = y - 1;
					break;
				case 'E':
					// inc x
					newCoord[0] = x + 1;
					newCoord[1] = y;
					break;
				case 'V':
					// dec x
					newCoord[0] = x - 1;
					newCoord[1] = y;
					break;
				default:
			}
			return newCoord;
		}
		
		public void solve() {
			readInput();
			computeSolution();
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}

// aceasta clasa reprezinta pozitia/stare lui Robin la un moment de timp
class Pos {
	// coordonatele lui Robin
	public int x;
	public int y;
	// busteanul pe care se afla
	public int bustean;
	// timpul trecut
	public int t;
	// energia consumata
	public long en;
	// ultima pozitie/stare
	public Pos lastPos;
	// tipul miscarii
	public char typeOfMove;

	public Pos(int x, int y, int bustean, int t, long en, Pos lastPos, char typeOfMove) {
		super();
		this.x = x;
		this.y = y;
		this.bustean = bustean;
		this.t = t;
		this.en = en;
		this.lastPos = lastPos;
		this.typeOfMove = typeOfMove;
	}

}
