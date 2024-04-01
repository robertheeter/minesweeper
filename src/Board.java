import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Board {

	Tile[][] tiles;
	private int flags;
	private int moves = 0;
	private boolean gameOver = false;
	public static final int SQUARE_SIZE = MineSweeperPanel.squareSize;
	private ArrayList<Tile> previouslyCircled = new ArrayList<Tile>();

	//Constructor for the board
	public Board(int rows, int cols, int mines) {
		//Creates 2D tile array
		tiles = new Tile[rows][cols];
		flags = mines;

		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				tiles[r][c] = new Tile(r, c, 9);
				tiles[r][c].setActualState(0);
			}
		}

		//Adds mine tiles
		while(mines > 0) {
			int r = (int)(Math.random()*rows);
			int c = (int)(Math.random()*cols);
			if(tiles[r][c].isBomb() == 0) {
				tiles[r][c].setActualState(11);
				mines--;
			}
		}

		//Adds number tiles
		for(int r = 0; r < tiles.length; r++) {
			for(int c = 0; c < tiles[r].length; c++) {
				int bombs = bombAddition(r, c);
				if(tiles[r][c].isBomb() == 0) {
					tiles[r][c].setActualState(bombs);
				}
			}
		}
	}

	//Adds bombs around a coordinate
	private int bombAddition(int row, int col) {
		int num = 0;
		for(int r = row-1; r <= row+1; r++) {
			for(int c = col-1; c <= col+1; c++) {
				if(isInBounds(r, c)) {
					num += tiles[r][c].isBomb();
				}
			}
		}
		return num;
	}

	//Checks if a given coordinate is in the bounds of the game
	private boolean isInBounds(int row, int col) {
		if(row < tiles.length && row >= 0 && col < tiles[row].length && col >= 0) {
			return true;
		}
		return false;
	}

	//Graphics
	public void draw(Graphics g) {
		for(int r = 0; r < tiles.length; r++) {
			for(int c = 0; c < tiles[r].length; c++) {
				tiles[r][c].draw(g);
			}
		}
	}

	//Converts click position to coordinates and runs appropriate methods
	public void handleClick(MouseEvent me) {
		//System.out.println("You just clicked: "+me);
		int x = me.getX();
		//System.out.println(x);
		int y = me.getY();
		//System.out.println(y);
		int colClicked = -1;
		int rowClicked = -1;
		int button = me.getButton();

		if(0 <= x && x < tiles[0].length*SQUARE_SIZE) {
			colClicked = (x)/SQUARE_SIZE;
		}
		if(0 <= y && y < tiles.length*SQUARE_SIZE) {
			rowClicked = (y)/SQUARE_SIZE;
		}
		if(gameOver == false) {
			moves++;
			System.out.println("MOVE " + moves);

			if(isInBounds(rowClicked, colClicked)){
				System.out.print(" >> Click: (" + rowClicked + "," + colClicked + ")");
				if(button == 1) {
					System.out.println("  | Type: Left-click");
					leftClick(rowClicked, colClicked);
				} else if(button == 3) {
					System.out.println("  | Type: Right-click");
					rightClick(rowClicked, colClicked);
				}
			} else{
				System.out.println(" !! Click on a valid tile.");
				moves--;
			}
			System.out.println(" >> Flags: " + flags);
			System.out.println();

			gameOver = checkGameOver();
		} else {
			System.out.println("GAME IS OVER!");
			System.out.println();
		}
	}

	//Right click method for flag placement/removal
	private void rightClick(int row, int col) {
		if(tiles[row][col].getDisplayState() == 10) {
			tiles[row][col].setDisplayState(9);
			flags++;
		} else if(tiles[row][col].getDisplayState() == 9) {
			tiles[row][col].setDisplayState(10);
			flags--;
		} else {
			System.out.println(" !! Flag cannot be placed here.");
			moves--;
		}

	}

	//Left click method for revealing tiles
	private void leftClick(int row, int col) {
		//if unclicked, unflagged tile, reveal
		if(tiles[row][col].getDisplayState() == 9) {
			reveal(row, col);
			//if it is a bomb, detonate
			//if it is blank, circle
			if(tiles[row][col].getActualState() == 11) {
				tiles[row][col].setActualState(12);
				tiles[row][col].setDisplayState(tiles[row][col].getActualState());
			} else if(tiles[row][col].getActualState() == 0){
				circle(row, col);
			}
		} else if(tiles[row][col].getDisplayState() == 10){
			System.out.println(" !! This tile is flagged.");
			moves--;
		} else {
			System.out.println(" !! Already clicked here.");
			moves--;
		}
	}

	//Reveals the actual state of a tile
	private void reveal(int row, int col) {
		tiles[row][col].setDisplayState(tiles[row][col].getActualState());
	}

	//Checks all tiles around a blank tile if they need to be auto-clicked
	private void circle(int row, int col) {
		previouslyCircled.add(tiles[row][col]);
		for(int r = row-1; r <= row+1; r++) {
			for(int c =  col-1; c <= col+1; c++) {
				if(isInBounds(r, c) && !tiles[r][c].isIn(previouslyCircled)) {
					if(tiles[r][c].getDisplayState() == 10) {
						flags++;
					}
					if(tiles[r][c].getDisplayState() == 10 || tiles[r][c].getDisplayState() == 9) {
						reveal(r, c);

					}
					if(tiles[r][c].getActualState() == 0) {
						circle(r, c);
					}
				}
			}
		}
	}

	private boolean checkGameOver() {
		boolean detonated = false;
		boolean allMinesMarked = true;
		boolean allTilesClicked = true;
		for(int r = 0; r < tiles.length; r++) {
			for(int c = 0; c < tiles[r].length; c++) {
				if(tiles[r][c].getActualState() == 12) {
					detonated = true;
					break;
				}
				if(tiles[r][c].getActualState() == 11 && !(tiles[r][c].getDisplayState() == 10)) {
					allMinesMarked = false;
				}
				if(tiles[r][c].getDisplayState() == 9) {
					allTilesClicked = false;
				}
			}
		}
		if(detonated == true) {
			allMinesMarked = false;
			uncoverAllMines();
			System.out.println("SORRY, YOU LOSE :(");
			System.out.println();
			return true;
		} else if(flags == 0 && allMinesMarked == true && allTilesClicked == true) {
			uncoverAllMines();
			System.out.println("YAY, YOU WIN :)");
			System.out.println();
			return true;
		}
		return false;
	}

	private void uncoverAllMines() {
		for(int r = 0; r < tiles.length; r++) {
			for(int c = 0; c < tiles[r].length; c++) {
				if(tiles[r][c].getDisplayState() == 10 && !(tiles[r][c].getActualState() == 11)) {
					tiles[r][c].setActualState(13);
				}
				reveal(r, c);
			}
		}
	}
}
