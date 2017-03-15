import java.util.Arrays;
import java.util.ArrayList;

public class KirkmanSolver {

	static class Girl {

		char name;
		Girl[] adjacencies;
		int totalNumberOfGirls;
		int x;
		Girl generic;

		public Girl(char n) {
			this.name = n;
			totalNumberOfGirls = 15;
			this.x = 88;
			this.adjacencies = new Girl[this.totalNumberOfGirls];
			this.adjacencies[0] = this;
			this.generic = new Girl();
			Arrays.fill(this.adjacencies, this.generic);
		}

		public Girl() {
			this.name = (char) 88;
		}

		public char getName() {
			return this.name;
		}

		public Girl[] getAdjacencies() {
			return this.adjacencies;
		}

		public boolean isAdjacentTo(Girl g) {
			for (int i = 0; i < this.totalNumberOfGirls; i++) {
				if (this.adjacencies[i].equals(g)) {
					return true;
				}
			}
			return false;
		}

		public boolean addAdjacency(Girl g) {
			for (int i = 0; i < this.totalNumberOfGirls; i++) {
				if (this.adjacencies[i].equals(this.generic)) {
					this.adjacencies[i] = g;
					return true;
				}
			}
			return false;
		}

		public boolean removeAdjacency(Girl g) {
			if (this.isAdjacentTo(g)) {
				for (int i = 0; i < this.totalNumberOfGirls; i++) {
					if (this.adjacencies[i].equals(g)) {
						this.adjacencies[i] = this.generic;
						if (i == this.totalNumberOfGirls) {return true;}
					}
				}
			}
			return false;
		}

		public String toString() {
			return String.valueOf(this.name);
		}

		public boolean equals(Girl g) {
			return this.name == g.getName() && Arrays.equals(this.adjacencies, g.getAdjacencies());
		}

	}

	static class Day {

		Girl[] order;
		Girl generic;
		int totalNumberOfGirls;
		int numberOfGirls;

		public Day() {
			this.totalNumberOfGirls = 15;
			this.order = new Girl[totalNumberOfGirls];
			this.generic = new Girl();
			Arrays.fill(this.order, this.generic);
			this.numberOfGirls = 0;
		}

		public Day(Girl[] girls) {
			this.totalNumberOfGirls = 15;
			this.order = girls;
			this.numberOfGirls = 15;
		}

		public int hasNextPosition(Girl g, int startIndex) {
			int size = this.numberOfGirls;
			int beginAt;
			Girl firstGirl;
			Girl secondGirl;
			Girl thirdGirl;
			beginAt = this.contains(g) ? ((startIndex / 3) + 1) * 3 : 0;
			for (int i = beginAt; i < this.order.length; i += 3) {
				firstGirl = !this.order[i].equals(this.generic) ? this.order[i] : this.generic;
				secondGirl = !this.order[i + 1].equals(this.generic) ? this.order[i + 1] : this.generic;
				thirdGirl = !this.order[i + 2].equals(this.generic) ? this.order[i + 2] : this.generic;

				if (firstGirl.equals(this.generic)) {
					if (i >= 3 && i % 3 == 0 && this.order[i - 3].equals(g)) {
						return 29;
					} else {
						return i;
					}
				} else if (secondGirl.equals(this.generic) && !firstGirl.isAdjacentTo(g)) {
					return i + 1;
				} else if (thirdGirl.equals(this.generic) && !firstGirl.isAdjacentTo(g) && !secondGirl.isAdjacentTo(g)) {
					return i + 2;
				}
			}
			return 16;
		}

		public boolean remove(Girl g) {
			for (int i = 0; i < this.totalNumberOfGirls; i++) {
				if (this.order[i].equals(g)) {
					if (i % 3 == 1) {
						this.order[i].removeAdjacency(this.order[i - 1]);
						this.order[i - 1].removeAdjacency(this.order[i]);
					} else if (i % 3 == 2) {
						this.order[i].removeAdjacency(this.order[i - 2]);
						this.order[i - 2].removeAdjacency(this.order[i]);
						this.order[i].removeAdjacency(this.order[i - 1]);
						this.order[i - 1].removeAdjacency(this.order[i]);
					}
					this.order[i] = this.generic;
					this.numberOfGirls -= 1;
					return true;
				}
			}
			return false;
		}

		public boolean contains(Girl g) {
			for (int i = 0; i < this.totalNumberOfGirls; i++) {
				if (this.order[i].equals(g)) {
					return true;
				}
			}
			return false;
		}

		public int indexOf(Girl g) {
			for (int i = 0; i < this.totalNumberOfGirls; i++) {
				if (this.order[i].equals(g)) {
					return i;
				}
			}
			return 0;
		}

		public boolean toNextPosition(Girl g) {
			int nextPosition = this.hasNextPosition(g, this.indexOf(g));
			if (nextPosition < 16) {
				this.remove(g);
				this.order[nextPosition] = g;
				if (nextPosition % 3 == 1) {
					this.order[nextPosition].addAdjacency(this.order[nextPosition - 1]);
					this.order[nextPosition - 1].addAdjacency(this.order[nextPosition]);
				} else if (nextPosition % 3 == 2) {
					this.order[nextPosition].addAdjacency(this.order[nextPosition - 2]);
					this.order[nextPosition - 2].addAdjacency(this.order[nextPosition]);
					this.order[nextPosition].addAdjacency(this.order[nextPosition - 1]);
					this.order[nextPosition - 1].addAdjacency(this.order[nextPosition]);
				}
				return true;
			}
			return false;
		}

		public String toString() {
			String output = "";
			String currentElement;
			for (int i = 0; i < this.totalNumberOfGirls; i++) {
				//currentElement = this.order[i] == null ? "n" : this.order[i].toString();
				output += i % 3 == 0 ? "|" : "";
				output += this.order[i].toString();
				output += i % 3 == 2 ? "|\n" : ", ";
			}
			return output;
		}
	}

	public static void printWeek(Day[] week) {
		String[][] allStrings = new String[7][5];
		String toPrint = "";
		for (int i = 0; i < 7; i++) {
			allStrings[i] = week[i].toString().split("\n");
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 7; j++) {
				toPrint += allStrings[j][i];
			}
			toPrint += "\n";
		}
		System.out.println(toPrint);
	}

	public static void kirkman() {
		int totalNumberOfGirls = 15;
		int numberOfDays = 7;
		int dayNumber = 0;
		int previousGirl = 0;
		int count = 0;
		Girl[] initialSet = new Girl[totalNumberOfGirls];
		Girl newGirl;
		Girl currentGirl;
		Day[] week = new Day[numberOfDays];
		Day currentDay;

		for (int i = 0; i < totalNumberOfGirls; i++) {
			newGirl = new Girl((char) (65 + i));
			initialSet[i] = newGirl;
		}

		for (int i = 0; i < numberOfDays; i++) {
			week[i] = new Day();
		}

		for (int i = 0; i < totalNumberOfGirls; i++) {

			currentGirl = initialSet[i];
			dayNumber = i >= previousGirl ? 0 : dayNumber;
			for (int j = dayNumber; j < numberOfDays; j++, count++) {
				currentDay = week[j];
				if (!currentDay.toNextPosition(currentGirl)) {

					currentDay.remove(currentGirl);
					previousGirl = i;

					j -= j >= 2 ? 2 : 1;
					if (j == 0 && week[1].hasNextPosition(currentGirl, week[1].indexOf(currentGirl)) > 15) {
						week[0].remove(currentGirl);
						week[1].remove(currentGirl);
						i -= i >= 2 ? 2 : 1;
						j = numberOfDays;
						dayNumber = 6;
					}
				}
			}
		}
		printWeek(week);
		System.out.println("Count: " + count);
	}

	public static void main(String[] args) {
		kirkman();

	}
}
