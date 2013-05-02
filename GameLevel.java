package SFCave2;

import java.io.IOException;
import java.util.*;
import java.awt.*;

import javax.imageio.ImageIO;

public class GameLevel {
	private int height;
	private int width;
	//Number of horizontal rectangles
	private int pieces; 
	private int pieceWidth;
	//Height of the gap between the upper and lower bounds
	private int gap;
	private Random random;
	//Upper rectangles
	private LinkedList<Rectangle> upperBounds;
	//Lower rectangles
	private LinkedList<Rectangle> lowerBounds;
	private Rectangle rock;
	private int lastUpperBound;
	//Point that the upperbound is trying to reach
	private int destination;
	//Difference in vertical pixels between two connecting pieces
	private int step;

	public GameLevel() {
		height = 600;
		width = 1000;
		pieces = 200;
		gap = 400;
		step = 2;
		destination = 80;
		destination -= destination % step;
		lastUpperBound = 30;
		lastUpperBound -= lastUpperBound % step;
		random = new Random();
		upperBounds = new LinkedList<Rectangle>();
		lowerBounds = new LinkedList<Rectangle>();
		rock = new Rectangle(-20, 0, 20, 20);
		pieceWidth = width / pieces;
		for (int i = 0; i < pieces; i++) {
			addRandomPair();
		}
		updateX();
	}
	
	public Rectangle getRock() {
		return rock;
	}
	
	public void moveRock() {
		int x = (int) rock.getX() - pieceWidth;
		int y = (int) rock.getY();
		if (x < -rock.getWidth()) {
			newRock();
		} else {
			rock.setLocation(x, y);	
		}
	}
	
	private void newRock() {
		int x = width;
		int y = random.nextInt(height);
		int width = 30 + random.nextInt(30);
		int height = 30 + random.nextInt(30);
		rock = new Rectangle(x, y, width, height);
	}
	
	public void decreaseGap() {
		gap -= 10;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

	private void addRandomPair() {
		if (lastUpperBound == destination) {
			destination = 1 + random.nextInt(height - gap);
			destination -= destination % step;			
		}
		else if (lastUpperBound > destination) {
			lastUpperBound -= step;
		} else {
			lastUpperBound += step;
		}
		
		int upperHeight = lastUpperBound;
		int lowerHeight = height - upperHeight - gap;
		upperBounds.add(new Rectangle(0, 0, pieceWidth, upperHeight));
		lowerBounds.add(new Rectangle(0, upperHeight + gap, pieceWidth, lowerHeight));
	}
	
	private void removeFirstPair() {
		upperBounds.removeFirst();
		lowerBounds.removeFirst();
	}
	
	public LinkedList<Rectangle> getUpperBounds() {
		return upperBounds;
	}
	
	public LinkedList<Rectangle> getLowerBounds() {
		return lowerBounds;
	}
	
	private void updateX() {
		int i = 0;
		for (Rectangle r : upperBounds) {
			r.x = i * pieceWidth;
			i++;
		}
		
		i = 0;
		for (Rectangle r : lowerBounds) {
			r.x = i * pieceWidth;
			i++;
		}
	}
	
	public void update() {
		removeFirstPair();
		addRandomPair();
		updateX();
		moveRock();
	}
}
