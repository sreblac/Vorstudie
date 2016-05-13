import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** Project for OPJ.
<p>Copyright (c) 2016 Conrad Albers, University of Bielefeld 2016</p>

<p>This class is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.</p>

<p>This class is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the <a href="http://www.gnu.org/copyleft/gpl.html">
GNU General Public License</a> for more details.</p>
<hr>

<p>This class is a project work for: 
392026 Objektorientierte Programmierung in Java (V) (SoSe 2016)
</p>

<p>The functions readLevelFile and printLevel represent stage 1/4.</p>
*/

class Vorstudie {
	static char[][] level = null;
	static int playerX = 0;
	static int playerY = 0;
	static boolean won = false;
	static boolean lost = false;

	public static void main(String[] args) {
		level = readLevelFile("Level99.txt");
		printLevel();
		//start game
		while ((!lost) && (!won)) {
			byte input[] = new byte[256];
			int numBytes = -1;
			try {
				numBytes = System.in.read(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(numBytes);
			String s = null;
			char[] chars = null;
			if (numBytes > 0){
				s = new String(input, 0, numBytes-2); //numBytes-2 is a horrible hack fix. I have no idea about error source.
				chars = s.toCharArray();
			}
			for (int i = 0; i < chars.length; i++) {
				if (!movePlayer(chars[i])) {
					System.out.println("Autsch!");
					lost = true;
					break;
				}
				if (won) {
					System.out.println("Gewonnen!");
					return;
				}
			}
			printLevel();
		}
	}
	public static char[][] readLevelFile(String fileName) {
		// http://stackoverflow.com/a/4716623
		char[][] rows = null;
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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
				line = br.readLine();
			}
			return rows;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void printLevel() {
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
	public static void getPlayerPosition() {
		if (level == null) {
			return;
		}
		int rows = level.length;
		for (int i = 0; i < rows; i++) {
			int cols = level[i].length;
			for (int j = 0; j < cols; j++) {
				if(level[i][j] == '@') {
					playerX = j;
					playerY = i;
				}
			}
		}
		System.out.println(playerX+":"+playerY);
	}
	public static boolean movePlayer(char direction) {
		if ((direction != 'w') && (direction != 's') && (direction != 'a') && (direction != 'd')) {
			return true;
		}
		getPlayerPosition();
		int newX = playerX;
		int newY = playerY;
		switch (direction) {
			case 'w':
				newY -= 1;
				break;
			case 's':
				newY += 1;
				break;
			case 'a':
				newX -= 1;
				break;
			case 'd':
				newX += 1;
				break;
		}
		switch (level[newY][newX]) {
			case '#':
				return false;
			case ' ':
				level[playerY][playerX] = ' ';
				level[newY][newX] = '@';
				playerX = newX;
				playerY = newY;
				return true;
			case '$':
				return false;
			case '.':
				won = true;
				level[playerY][playerX] = ' ';
				level[newY][newX] = '@';
				playerX = newX;
				playerY = newY;
				return true;
			default:
				return false;
		}
	}
}
