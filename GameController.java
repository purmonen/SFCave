package SFCave2;

import java.awt.Rectangle;
import java.awt.event.*;
import javax.swing.*;

//import java.util.*;

public class GameController {
	private GameView view;
	private Timer heroTimer;
	private Timer levelTimer;
	private GameHero hero;
	
	private GameLevel level;
	private boolean running = false;
	private boolean gameover = false;
	
	public GameController() {
		hero = new GameHero(150, 300, 30, 30);
		level = new GameLevel();
		view = new GameView(level, hero, this);
		view.addKeyListener(new KeyController());
		heroTimer = new Timer(40, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int score = view.getScore();
				view.setScore(score + 1);
				if (score % 100 == 0) {
					level.decreaseGap();
				}
				hero.move();
				if (isColliding()) {
					view.update();
					gameOver();
				}
			}
		});
		
		levelTimer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				level.update();
				view.update();
			}
		});
	}
	
	public boolean isGameover() {
		return gameover;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void gameOver() {
		gameover = true;
		pause();
	}
	
	public void restart() {
		hero = new GameHero(150, 300, 30, 30);
		level = new GameLevel();
		view.reset(hero, level);
		view.update();
	}

	private void pause() {
		if (running) {
			running = false;
			heroTimer.stop();
			levelTimer.stop();
		} else {
			running = true;
			heroTimer.start();
			levelTimer.start();
		}
	}
	
	private boolean isColliding() {
		int margin = 8;
		int size = level.getUpperBounds().size();
		int i = 0;
		for (Rectangle r : level.getUpperBounds()) {
			r.y -= margin;
			if (r.intersects(hero)) {
				return true;
			}
			r.y += margin;
			i++;
			if (i > size / 2) {
				break;
			}
		}
		for (Rectangle r : level.getLowerBounds()) {
			r.y += margin;
			if (r.intersects(hero)) {
				return true;
			}
			r.y -= margin;
		}

		if (level.getRock().intersects(hero)) {
			return true;
		}
		return false;
	}
	
	private class KeyController extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				hero.goUp();
				break;
			case KeyEvent.VK_ENTER:
				if (gameover) {
					gameover = false;
					restart();
				} else {
					pause();
				}
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			}
		}
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				hero.goDown();
			}
		}
	}
}
