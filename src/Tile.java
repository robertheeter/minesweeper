import java.awt.Graphics;
import java.util.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile {

	protected final int SQUARE_SIZE = MineSweeperPanel.squareSize; //Size of an individual tile
	private static Image sprites;
	private static Image[] images; //Array of all images
	private int displayState;
	private int actualState;
	private int[] location = new int[2]; //Coordinates/location of this tile

	public Tile(int row, int col, int s) {
		setUpImages();
		location[0] = row;
		location [1] = col;
		this.displayState = s;
	}

	private void setUpImages() {
		if(sprites == null) {// only open the file once
			try {
				sprites = ImageIO.read(new File("minesweepersprites.PNG"));
			} catch (IOException e) {	
				e.printStackTrace();
			}
			images = new Image[14];
			
			//Number tiles
			images[1] = ((BufferedImage)sprites).getSubimage(3,991,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			images[2] = ((BufferedImage)sprites).getSubimage(250,991,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			images[3] = ((BufferedImage)sprites).getSubimage(497,991,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			images[4] = ((BufferedImage)sprites).getSubimage(743,991,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			images[5] = ((BufferedImage)sprites).getSubimage(990,991,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			images[6] = ((BufferedImage)sprites).getSubimage(1237,991,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			images[7] = ((BufferedImage)sprites).getSubimage(1484,991,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			images[8] = ((BufferedImage)sprites).getSubimage(1731,991,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			//Clicked blank tile
			images[0] = ((BufferedImage)sprites).getSubimage(250,744,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			
			//Unclicked blank tile (DISPLAY STATE ONLY)
			images[9] = ((BufferedImage)sprites).getSubimage(3,744,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			//Flag tile (DISPLAY STATE ONLY)
			images[10] = ((BufferedImage)sprites).getSubimage(497,744,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			
			//Undetonated bomb tile
			images[11] = ((BufferedImage)sprites).getSubimage(1237,744,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			//Detonated bomb tile
			images[12] = ((BufferedImage)sprites).getSubimage(1484,744,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			//Not bomb tile
			images[13] = ((BufferedImage)sprites).getSubimage(1731,744,232,233).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);

		}
	}

	//Getters and setters
	public void setDisplayState(int s) {
		this.displayState = s;
	}
	
	public void setActualState(int s) {
		this.actualState = s;
	}

	public int getDisplayState() {
		return this.displayState;
	}
	
	public int getActualState() {
		return this.actualState;
	}

	//Determines if this tile is a bomb
	public int isBomb() {
		if(this.actualState == 11) {
			return 1;
		} else {
			return 0;
		}
	}

	//Determines if this tile is in an ArrayList of tiles
	public boolean isIn(ArrayList<Tile> t) {
		for(int i = 0; i < t.size(); i++) {
			if(this.location[0] == t.get(i).location[0] && this.location[1] == t.get(i).location[1]) {
				return true;
			}
		}
		return false;
	}
	
	//Draws tile in the display state
	public void draw(Graphics g) {
		g.drawImage(images[displayState], location[1]*SQUARE_SIZE, location[0]*SQUARE_SIZE, null);

	}

}
