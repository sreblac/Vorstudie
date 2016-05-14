import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;

/** Project for OPJ.
<p>Copyright (c) 2016 Conrad Albers, University of Bielefeld</p>

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

<p>The functions readLevelFile and printLevel represent assignment 1.</p>

<p>The function movePlayer represent assignment 2.</p>

<p>The function moveCrate represent assignment 3.</p>

<p>Assignment 4 is part of the last two.</p>
*/

class Vorstudie {
	static char[][] level = null;
	static int playerX = 0;
	static int playerY = 0;
	static boolean won = false;
	static boolean lost = false;

	public static void main(String[] args) {
		String run = "";
		do {
			run = playGame(run);
		} while (run != "e");
		return;
	}
	public static String playGame(String file) {
		if (file == "") {
			System.out.print("Enter file name for Level:");
			file = getConsoleInput();
		}
		getLevelFromFile(file);
		printLevel();
		System.out.println("Enter e to exit, w/a/s/d to move up/left/down/right.");

		//start game
		while ((!lost) && (!won)) {
			String input = getConsoleInput();
			if (input == "") {
				continue;
			}
			char[] chars = input.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == 'e') {
					return "e";
				}
				if (!movePlayer(chars[i])) {
					lost = true;
					break;
				}
				if (won) {
					break;
				}
			}
			printLevel();
		}

		// react to result
		if(lost){
			System.out.println("Autsch!");
		} else if (won){
			System.out.println("Gewonnen!");
		} else {
			System.out.println("Wut?");
		}
		// reset
		playerX = 0;
		playerY = 0;
		won = false;
		lost = false;
		level = null;
		
		// continue playing?
		System.out.println("(p)lay again");
		System.out.println("(o)ther level");
		System.out.println("(e)xit");
		while (true) {
			String options = getConsoleInput();
			switch (options) {
				case "p":
					return file;
				case "o":
					return "";
				case "e":
					return "e";
			}
		}
	}
	public static void getLevelFromFile(String fileName) {
		char[][] rows = null;
		level = null;
		// based on http://stackoverflow.com/a/4716623
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line = br.readLine();
			int i = 0;
			int longest = 0;

			while (line != null) {
				if (line.length() > longest) {
					longest = line.length();
				}
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
			level = rows;
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
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
				if (moveCrate(direction, newX, newY)){
					level[playerY][playerX] = ' ';
					playerX = newX;
					playerY = newY;
					return true;
				} else {
					return false;
				}
			case '*':
				if (moveCrate(direction, newX, newY)){
					won = true;
					level[playerY][playerX] = ' ';
					playerX = newX;
					playerY = newY;
					return true;
				} else {
					return false;
				}
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
	public static boolean moveCrate(char direction, int crateX, int crateY) {
		int newX = crateX;
		int newY = crateY;
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
				level[crateY][crateX] = '@';
				level[newY][newX] = '$';
				return true;
			case '$':
				return false;
			case '*':
				return false;
			case '.':
				level[crateY][crateX] = '@';
				level[newY][newX] = '*';
				return true;
			default:
				return false;
		}
	}
	public static String getConsoleInput() {
		// based on http://www.mkyong.com/java/how-to-get-the-standard-input-in-java/
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input;
			input = br.readLine();
			return input;
		} catch (IOException io) {
			io.printStackTrace();
		}
		return "";
	}
}
