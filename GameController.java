package SFCave2;

import java.awt.Rectangle;
import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;

/**
 * The game controller handles the game logic.
 * 
 * @author Sami Purmonen and Nils Dahlberg
 * @version 2013.05.02
 */
public class GameController {
	private GameView view;
	private Timer heroTimer;
	private Timer levelTimer;
	private GameHero hero;
	private GameLevel level;
	private ScoreBoard scoreBoard;
	private boolean running = false;
	private boolean gameover = false;
	
	public GameController() {
		hero = new GameHero(150, 200, 30, 30);
		level = new GameLevel();
		scoreBoard = new ScoreBoard(5);
		view = new GameView(level, hero, scoreBoard, this);
		view.addKeyListener(new KeyController());
		heroTimer = new Timer(36, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int score = scoreBoard.getScore();
				scoreBoard.setScore(score + 1);
				if (score % 100 == 0) {
					level.decreaseGap();
				}
				hero.move();
				if (isColliding()) {
					endGame();
				}
			}
		});
		
		levelTimer = new Timer(18, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				level.update();
				view.update();
			}
		});
	}
	
	/**
	 * Game ends - called when the hero crashes
	 */
	public void endGame() {
		pause();
		gameover = true;
		view.update();
		if (scoreBoard.isHighScore()) {
			String name = view.showAddScore();
			scoreBoard.addScore(name);
		}
		view.showScoreBoard();
	}
	
	public boolean isGameover() {
		return gameover;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Restarts the game
	 */
	public void restart() {
		gameover = false;
		hero = new GameHero(150, 300, 30, 30);
		level = new GameLevel();
		scoreBoard.setScore(0);
		view.reset(hero, level);
		view.update();
	}

	/**
	 * Pauses or unpauses the game
	 */
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
		view.update();
	}
	
	/**
	 * Checks if the hero has crashed
	 */
	private boolean isColliding() {
		
		//Calculate which rectangles we must collide check
		int start = (int) (hero.getX()) / level.getPieceWidth();
		int end = (int) (hero.getWidth() + hero.getX()) / level.getPieceWidth();
		
		
		Iterator<Rectangle> upper = level.getUpperBounds().iterator();
		Iterator<Rectangle> lower = level.getLowerBounds().iterator();		
		
		for (int i = 0; i < end; i++) {
			Rectangle up = upper.next();
			Rectangle low = lower.next();
			if (i >= start) {
				if (hero.intersects(up) || hero.intersects(low)) {
					return true;
				}
			}
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
					restart();
		 		} else {
					pause();
				}
				break;
			case KeyEvent.VK_ESCAPE:
				if (running) {
					pause();
				}
				view.showExitDialog();
				break;
			}
		}
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				hero.goDown();
			}
		}
	}
	
	public void exit() {
		scoreBoard.writeScores();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		new GameController();
	}
}
