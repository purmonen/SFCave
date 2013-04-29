package SFCave2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;

import javax.imageio.*;
import javax.swing.ImageIcon;

/**
 * This is the unit controlled by the player
 *
 */
public class GameHero extends Rectangle {
	private static final long serialVersionUID = 1L;
	private int acceleration;
	private int dy;
	private LinkedList<Rectangle> tail;
	private int tailHeight;
    private Image image;

	
	public GameHero(int x, int y, int width, int height) {
		super(x, y, width, height);
		acceleration = 1;
		dy = 0;
		tailHeight = 8;
		createTail();

		try {
			image = ImageIO.read(getClass().getResource("cat.jpg"));
			image = image.getScaledInstance(width, height, 1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Image getImage() {
		return image;
	}
	
	private void createTail() {
		tail = new LinkedList<Rectangle>();
		for (int i = 0; i < 15; i++) {
			tail.add(new Rectangle(i * tailHeight, y + height / 2 - tailHeight / 2, tailHeight, tailHeight));
		}
		updateX();
	}
	
	private void updateTail() {
		tail.removeFirst();
		tail.add(new Rectangle(0, y + height / 2 - tailHeight / 2, tailHeight, tailHeight));
		updateX();
	}
	
	private void updateX() {
		int i = 0;
		int size = tail.size();
		for (Rectangle r : tail) {
			r.x = x - (size - i) * tailHeight;
			i++;
		}
	}
	
	public LinkedList<Rectangle> getTail() {
		return tail;
	}
	
	public void goUp() {
		acceleration = -1;
	}
	
	public void goDown() {
		acceleration = 1;
	}
	
	public void move() {
		dy += acceleration;
		updateTail();
		y += dy;
	}
}
