package SFCave2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * A score board that stores the highest scores
 * in a file.
 * 
 * @author Sami Purmonen and Nils Dahlberg
 * @version 2013.05.01
 */
public class ScoreBoard {
	private ArrayList<HighScore> scores;
	//Maximum number of scores to be saved.
	private int maxSize;
	//File in which the scores are stored.
	private String fileName;
	
	/**
	 * Create a score board.
	 * 
	 * @param size The maximum size of scores that can be stored.
	 */
	public ScoreBoard(int size) {
		this(size, "highscore.txt");
	}
	
	/**
	 * @param fileName The file in which the scores are stored.
	 */
	public ScoreBoard(int size, String fileName) {
		if (size < 1) {
			throw new IllegalArgumentException();
		}
		scores = new ArrayList<HighScore>();
		maxSize = size;
		this.fileName = fileName;
		writeScores();
	}
	
	public boolean isHighScore(int score) {
		if (scores.size() < maxSize) {
			return true;
		}
		for (HighScore h : scores) {
			if (h.score < score) {
				return true;
			}
		}
		return false;
	}
	
	public boolean addScore(String name, int score) {
		boolean added = false;
		if (scores.size() < maxSize) {
			scores.add(new HighScore(name, score));
			added = true;
		} else if (score > scores.get(scores.size() - 1).score) {
			scores.remove(scores.size() - 1);
			scores.add(new HighScore(name, score));
			added = true;
		}
		Collections.sort(scores);
		readScores();
		return added;
	}
	
	public ArrayList<HighScore> getScores() {
		return scores;
	}
	
	private void writeScores() {
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName);
			for (HighScore h : scores) {
				writer.write(h.name + " " + h.score + "\n");
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (writer == null) {
				return;
			}
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}
	
	private void readScores() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			String[] temp;
			while ((line = reader.readLine()) != null) {
				temp = line.split(" ");
				scores.add(new HighScore(temp[0], Integer.parseInt(temp[1])));
			}
			reader.close();
		} catch (FileNotFoundException e) {
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		} finally {
			if (reader == null) {
				return;
			}
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
	}
	
	public void clear() {
		scores = new ArrayList<HighScore>();
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName);
			writer.write("");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (writer == null) {
				return;
			}
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}
}
