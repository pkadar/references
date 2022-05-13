package minesweeper;

import java.io.Serializable;

public class Ranklist implements Serializable{
	private static final long serialVersionUID = 1L;
	private int placement;
	private String name;
	private int time;
	
	public Ranklist(int p, String pName, int pTime) {
		placement = p;
		name = pName;
		time = pTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String n) {
		name = n;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int t) {
		time = t;
	}
	public int getPlacement() {
		return placement;
	}
	public void setPlacement(int v) {
		placement = v;
	}
}
