package minesweeper;


import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Field extends JButton implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int fieldType;
	private int y, x;
	private boolean clickable;
	private final int defaultField = 10;
	private final int flagImg = 11;
	private boolean marked;
	
	public Field(int xcoord, int ycoord) {
		this.fieldType = 0;
		this.y = ycoord;
		this.x = xcoord;
		this.clickable = true;
		this.marked = false;
		this.setPreferredSize(new Dimension(25,25));
		setIcon(defaultField);
	}
	
	public void setIcon(int type) {
		try {
			Image img = ImageIO.read(new File("resources\\"+type+".png"));
			this.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void click() {
		setIcon(fieldType);
		setBorderPainted( false );
		setFocusPainted( true );
		setClickable(false);
	}

	public void markField() {
			if(getMarked() == true) {
				setIcon(defaultField);
				setClickable(true);
				setMarked(false);
				
			} else {
				setIcon(flagImg);
				setClickable(false);
				setMarked(true);
			}
	}
	
	public boolean getMarked() {
		return marked;
	}
	
	public void setMarked(Boolean value) {
		marked = value;
	}
	
	public int getXCoordinate() {
		return x;
	}
	
	public int getYCoordinate() {
		return y;
	}

	public boolean getClickable() {
		return clickable;
	}
	
	public void setClickable(boolean value) {
		clickable = value;
	}
	
	public boolean isMine() {
		return fieldType == 9 ? true : false;
	}
	
	public void setMine() {
		this.fieldType = 9;
	}
	
	public void setType(int value) {
		this.fieldType = value;
	}
	
	public int getType() {
		return fieldType;
	}
}
