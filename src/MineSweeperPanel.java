import java.awt.Dimension;
import java.util.Scanner;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MineSweeperPanel extends JPanel {

	protected static int squareSize = 20;
	private int sizePanelX = 600;
	private int sizePanelY = 600;
	private Board board;
	private int rows, cols, mines;
	public static void main(String[] args) {
		JFrame frame = new JFrame("Robert's Minesweeper!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			frame.add(new MineSweeperPanel());
		} catch (InterruptedException e) {
			System.out.println();
			System.out.println("ERROR! Restart game.");
			e.printStackTrace();
		}
		frame.pack();
		frame.setVisible(true);
		
	}
	
	//Creates board
	public MineSweeperPanel() throws InterruptedException {
		//User input for board parameters
		System.out.println("ROBERT'S MINESWEEPER!");
		System.out.println();
		
		System.out.print(" >> Tile size: \"small,\" \"medium,\" or \"large\"? ");
		Scanner boardScan = new Scanner(System.in);
		String size = boardScan.nextLine().toLowerCase();
		if(size.equals("small") || size.equals("s")) {
			squareSize = 20;
		} else if(size.equals("medium") || size.equals("m")) {
			squareSize = 30;
		} else if(size.equals("large") || size.equals("l")) {
			squareSize = 40;
		} else {
			squareSize = 30;
		}
		
		System.out.print(" >> Number of Rows: ");
		Scanner rowsScan = new Scanner(System.in);
		int rows = rowsScan.nextInt();
		
		System.out.print(" >> Number of Columns: ");
		Scanner colsScan = new Scanner(System.in);
		int cols = colsScan.nextInt();
		
		System.out.print(" >> Number of Mines (<" + rows*cols + "): ");
		Scanner minesScan = new Scanner(System.in);
		int mines = minesScan.nextInt();
		
		sizePanelX = cols*squareSize;
		sizePanelY = rows*squareSize;
		
		//Delay for start
		System.out.println();
		System.out.print("Making grid");
		TimeUnit.MILLISECONDS.sleep(200);
		System.out.print(".");
		TimeUnit.MILLISECONDS.sleep(200);
		System.out.print(".");
		TimeUnit.MILLISECONDS.sleep(200);
		System.out.print(".");
		TimeUnit.MILLISECONDS.sleep(200);
		System.out.print(" done!");
		System.out.println();
		System.out.println();
		
		board = new Board(rows, cols, mines);
		this.setPreferredSize(new Dimension(this.sizePanelX, sizePanelY));
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				board.handleClick(me);
				repaint();
			}		
		});
	}
	
	//Graphics
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		board.draw(g);
	}
	
}
