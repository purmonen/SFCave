package SFCave2;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.*;

/**
 * This is the unit controlled by the player
 *
 * @author Sami Purmonen and Nils Dahlberg
 * @version 2013.05.01
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
			image = image.getScaledInstance(width + 4, height + 4, 1);
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
	
	public void dropTail() {
		for (Rectangle r : tail) {
			r.y++;
		}
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
	
	
	/**
	 * Change the heroes acceleration so it will go up
	 */
	public void goUp() {
		acceleration = -1;
	}
	
	/**
	 * Change the heroes acceleration so it will go down
	 */
	public void goDown() {
		acceleration = 1;
	}
	
	/**
	 * Change the speed in dy direction an change it's y position
	 */
	public void move() {
		dy += acceleration;
		updateTail();
		y += dy;
	}
}
