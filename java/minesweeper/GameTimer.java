package minesweeper;

import javax.swing.JLabel;

public class GameTimer extends Thread {
	private int countable;
	private volatile boolean stopSignal;
	private JLabel counterLabel;
	
	public GameTimer(JLabel label) {
		countable = -1;
		stopSignal = false;
		counterLabel = label;
	}
	
	public void go() {
		while(!stopSignal) {
			try {
				countable++;
			//	System.out.println(countable);
				setTimeLabel(counterLabel);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setTimeLabel(JLabel label) {
		label.setText(timeFormatted(getTime()));
	}
	
	public void run() {
		go();
	}
	
	public void terminate() {stopSignal=true;}
	
	public int getTime() {
		return countable;
	}
	
	public void setTime(int value) {
		countable = value;
	}
	
	public String timeFormatted(int i) {
		String formatted = String.format("%03d", i);
		return formatted;
	}
	 
}
