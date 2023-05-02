package Tetris;

import static Tetris.Board.BLOCK_SIZE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JPanel;

public class Board extends JPanel implements KeyListener {
	public static int STATE_GAME_PLAY = 0;
	public static int STATE_GAME_PAUSE = 1;
	public static int STATE_GAME_OVER = 2;

	private int state = STATE_GAME_PLAY;

	private static int FPS = 60;
	private static int delay = FPS / 1000;
	
	private static final long serialVesionUID = 1l;
	public static final int BOARD_WIDTH = 10;
	public static final int BOARD_HEIGHT = 20;
	public static final int BLOCK_SIZE = 30;
	private Timer looper;

	private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];

	private Random random;

	private Color[] colors = { Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"),
			Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc") };

	private Shape[] shapes = new Shape[7];
	private Shape currentShape;
	
	public int score = 0;

	public Board() {
		

		random = new Random();

		shapes[0] = new Shape(new int[][] { { 1, 1, 1, 1 } }, this, colors[0]);

		shapes[1] = new Shape(new int[][] { { 1, 1, 1 }, { 0, 1, 0 }, }, this, colors[1]);

		shapes[2] = new Shape(new int[][] { { 1, 1, 1 }, { 1, 0, 0 }, }, this, colors[2]);

		shapes[3] = new Shape(new int[][] { { 1, 1, 1 }, { 0, 0, 1 }, }, this, colors[3]);

		shapes[4] = new Shape(new int[][] { { 0, 1, 1 }, { 1, 1, 0 }, }, this, colors[4]);

		shapes[5] = new Shape(new int[][] { { 1, 1, 0 }, { 0, 1, 1 }, }, this, colors[5]);

		shapes[6] = new Shape(new int[][] { { 1, 1 }, { 1, 1 }, }, this, colors[6]);

		currentShape = shapes[0];

		looper = new Timer(delay, new ActionListener() {
			int n = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				repaint();
			}
		});
		looper.start();
	}

	private void update() {
		if (state == STATE_GAME_PLAY) {
			currentShape.update();
		}
	}

	public void setCurrentShape() {
		currentShape = shapes[random.nextInt(shapes.length)];
		currentShape.reset();
		checkOverGame();

	}

	private void checkOverGame() {
		int[][] coords = currentShape.getcoords();
		for (int row = 0; row < coords.length; row++) {
			for (int col = 0; col < coords[0].length; col++) {
				if (coords[row][col] != 0) {
					if (board[row + currentShape.getY()][col + currentShape.getX()] != null) {
						state = STATE_GAME_OVER;
					}
				}
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		currentShape.render(g);

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] != null) {
					g.setColor(board[row][col]);
					g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}
		// draw the shape
		g.setColor(Color.WHITE);
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * row);
		}
		

		for (int col = 0; col < BOARD_WIDTH + 1; col++) {
			g.drawLine(col * BLOCK_SIZE, 0, col * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
		}

		if (state == STATE_GAME_OVER) {
			g.setColor(Color.white);
			g.drawString("GAME OVER", 50, 200);
		}
		
		if (state == STATE_GAME_PAUSE) {
			g.setColor(Color.white);
			g.drawString("GAME PAUSE", 50, 200);
		}
		
		g.setColor(Color.WHITE);

		g.setFont(new Font("Georgia", Font.BOLD, 15));

		g.drawString("SCORE", Tetris.WIDTH - 125, Tetris.HEIGHT / 2);
		g.drawString(score + "", Tetris.WIDTH - 125, Tetris.HEIGHT / 2 + 20);
	}

	public Color[][] getBoard() {
		return board;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.speedUp();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentShape.moveRight();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentShape.moveLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			currentShape.rotateShape();
		}

		if (state == STATE_GAME_OVER) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				for (int row = 0; row < board.length; row++) {
					for (int col = 0; col < board[0].length; col++) {
						board[row][col] = null;
					}
				}
				setCurrentShape();
				state = STATE_GAME_PLAY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (state == STATE_GAME_PLAY) {
				state = STATE_GAME_PAUSE;
			} else if(state == STATE_GAME_PAUSE) {
				state = STATE_GAME_PLAY;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.speedDown();
		}
	}
	public void addScore() {
		score ++;
	}


}