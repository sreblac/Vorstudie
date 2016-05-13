import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Vorstudie {
	public static void main(String[] args) {
		char[][] level = readLevelFile("Level99.txt");
		printLevel(level);
	}
	public static char[][] readLevelFile(String fileName) {
		// http://stackoverflow.com/a/4716623
		char[][] rows = null;
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			//StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			int i = 0;
			int longest = 0;

			while (line != null) {
				longest = line.length();
				char[][] temp = new char[i+1][longest];
				for (int j = 0; j < i; j++) {
					temp[j] = rows[j];
				}
				char[] newLine = line.toCharArray();
				temp[i] = newLine;
				rows = temp;
				i++;
				//sb.append(line);
				//sb.append(System.lineSeparator());
				line = br.readLine();
			}
			//String everything = sb.toString();
			//System.out.print(everything);
			return rows;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void printLevel(char[][] level) {
		if (level == null) {
			return;
		}
		int rows = level.length;
		for (int i = 0; i < rows; i++) {
			int cols = level[i].length;
			for (int j = 0; j < cols; j++) {
				System.out.print(level[i][j]);
			}
			System.out.print("\n");
		}
	}
}