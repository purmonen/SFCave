package SFCave2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GameView extends JFrame {
	private static final long serialVersionUID = 1L;
	private GameBoard board;
	private GameHero hero;
	private GameLevel level;
	private GameController controller;
	private int score;
	
	public GameView(GameLevel level, GameHero hero, GameController controller) {
		this.hero = hero;
		this.level = level;
		this.controller = controller;
		score = 0;
		
		setTitle("SFCave");
		setSize(level.getWidth(), level.getHeight());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container contentPane = getContentPane();
		board = new GameBoard();
		contentPane.add(board);
		makeMenu();
		setBackground(Color.WHITE);
		setVisible(true);
	}
	
	public void reset(GameHero hero, GameLevel level) {
		score = 0;
		this.hero = hero;
		this.level = level;
	}
	
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Game");
		menuBar.add(menu);
		
		JMenuItem item = new JMenuItem("New game");
		menu.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.restart();
			}
		});
		
		item = new JMenuItem("High score");
		menu.add(item);
		
		item = new JMenuItem("Exit");
		menu.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void update() {
		board.repaint();
	}
	
	private class GameBoard extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			paintLevel(g);
			paintHero(g);
			paintInfo(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.GRAY);
			g2d.fill(level.getRock());
		}
		
		public void paintInfo(Graphics g) {
			g.drawString("Score: " + score, 20, 20);
			if (controller.isGameover()) {
				g.setFont(new Font("serif", 40, 40));
				g.drawString("GAME OVER - Press enter to restart", 100, 50);
			} else if (!controller.isRunning()) {
				g.drawString("Press enter to continue", 300, 200);
			}
			
		}
		
		public void paintHero(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.fill(hero);
			g2d.drawImage(hero.getImage(), (int) hero.getX(), (int) hero.getY(), null);
			
			for (Rectangle r : hero.getTail()) {
				g2d.fill(r);
			}
		}
		
		public void paintLevel(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g.setColor(Color.GREEN);
			for (Rectangle r : level.getUpperBounds()) {
				g2d.fill(r);
			}
			g.setColor(Color.BLUE);
			for (Rectangle r : level.getLowerBounds()) {
				g2d.fill(r);
			}
		}
	}
}
