package SFCave2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

/**
 * The view is what you see on screen.
 * It handles all presentation of the
 * game components.
 * 
 * @author Sami Purmonen and Nils Dahlberg
 * @version 2013.05.02
 */
public class GameView extends JFrame {
	private static final long serialVersionUID = 1L;
	private GameBoard board;
	private GameHero hero;
	private GameLevel level;
	private GameController controller;
	private ScoreBoard scoreBoard;
	
	public GameView(GameLevel level, GameHero hero, ScoreBoard scoreBoard, GameController controller) {
		this.hero = hero;
		this.level = level;
		this.controller = controller;
		this.scoreBoard = scoreBoard;
		setTitle("SFCave");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		Container contentPane = getContentPane();
		board = new GameBoard();
		contentPane.add(board);
		makeMenu();
		setBackground(Color.WHITE);
		setSize(level.getWidth(), level.getHeight() + 40);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void reset(GameHero hero, GameLevel level) {
		this.hero = hero;
		this.level = level;
	}
	
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Menu");
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
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showScoreBoard();
			}
		});
		
		item = new JMenuItem("Clear high score");
		menu.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showClearScoreBoard();
			}
		});
		
		item = new JMenuItem("Information");
		menu.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showInformationDialog();
			}
		});
		
		item = new JMenuItem("Exit");
		menu.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showExitDialog();
			}
		});
	}
	
	public void showScoreBoard() {
		String[] entries = new String[5];
		ArrayList<HighScore> scores = scoreBoard.getScores();
		
		for (int i = 0; i < scores.size(); i++) {
			entries[i] = i + 1 + ". " + scores.get(i).toString();
		}

		JList list = new JList(entries);
		JPanel panel = new JPanel();
		panel.add(list);
		JOptionPane.showMessageDialog(list, panel,
				"High score", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showInformationDialog() {
		String title = "Information";
		String message = "In this game you control a flying cat trying not to " 
				    + "hit the surrounding terrain.\n You control the vertical "
				    + "position of the cat by holding the space button and "
				    + "releasing it.";
		JOptionPane.showMessageDialog(this, message,
				title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showClearScoreBoard() {
		String title = "Clear score board";
		String message = "Are you sure you want to clear the score board?";
		int answer = JOptionPane.showConfirmDialog(this, message,
				title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (answer == JOptionPane.OK_OPTION) {
			scoreBoard.clear();
		}
	}
	
	public void showExitDialog() {
		String title = "Exit";
		String message = "Are you sure you want to exit this game?";
		int answer = JOptionPane.showConfirmDialog(this, message,
				title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (answer == JOptionPane.OK_OPTION) {
			controller.exit();
		}
	}

	public void update() {
		board.repaint();
	}
	
	public String showAddScore() {
		String title = "New high score";
		String message = "You made it to the high score list, enter your name: ";
		String name = JOptionPane.showInputDialog(this, message,
				title, JOptionPane.PLAIN_MESSAGE);
		
		if (name == null || name.trim().equals("")) {
			name = "Anonymous";
		}
		return name.trim();
	}
	
	private class GameBoard extends JPanel {
		private static final long serialVersionUID = 1L;
		
		public GameBoard() {
			setBackground(Color.WHITE);
		}

		@Override
		public void paintComponent(Graphics g) {
			g.setFont(new Font("verdana", 30, 20));
			super.paintComponent(g);
			paintHero(g);
			paintLevel(g);
			paintInfo(g);
		}
		
		public void paintInfo(Graphics g) {
			g.setColor(Color.BLACK);
			g.drawString("Score: " + scoreBoard.getScore(), 20, 20);
			if (controller.isGameover()) {
				g.setFont(new Font("serif", 40, 40));
				g.drawString("GAME OVER - Press enter to restart", 100, 100);
				if (scoreBoard.isHighScore()) {
					g.drawString("NEW HIGH SCORE", 100, 180);
				}				
			} else if (!controller.isRunning()) {
				g.drawString("Press enter to continue", 300, 200);
				g.drawString("Use the space button to control the cat", 300, 230);
			}
		}
		
		
		public void paintHero(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.fill(hero);
			g2d.drawImage(hero.getImage(), (int) hero.getX() - 2, (int) hero.getY() - 2, null);
			
			if (controller.isGameover()) {
				g2d.setColor(Color.RED);
			}
			for (Rectangle r : hero.getTail()) {
				g2d.fill(r);
			}
		}
		
		public void paintLevel(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.GRAY);
			g2d.fill(level.getRock());
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
