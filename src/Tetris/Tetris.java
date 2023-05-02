package Tetris;

import javax.swing.JFrame;

public class Tetris {
	public static final int WIDTH = 445, HEIGHT = 629;

	private Board board;
	private JFrame Window;

	public Tetris() {
		Window = new JFrame("Tetris");
		Window.setSize(WIDTH, HEIGHT);
		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Window.setResizable(false);
		Window.setLocationRelativeTo(null);

		board = new Board();
		Window.add(board);
		Window.addKeyListener(board);
		Window.setVisible(true);
	}

	public static void main(String[] args) {
		new Tetris();
	}

}
