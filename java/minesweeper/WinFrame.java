package minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WinFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private ImageIcon img = new ImageIcon("resources\\icon.png");
	private JLabel name = new JLabel("Név: ");
	private JTextField nameField = new JTextField(35);
	private JButton b = new JButton("Mehet");
	private JPanel panel = new JPanel();
	private ActionListener bAL = new ButtonActionListener();
	private RanklistData rlDataEasy;
	private RanklistData rlDataMedium;
	private RanklistData rlDataHard;
	private int timeCounter;
	private int gridWidth;
	
	public WinFrame(RanklistData dE, RanklistData dM, RanklistData dH, int gWidth, int timeCount) {
		setIconImage(img.getImage());
		setResizable(false);
		setTitle("Nyertél, add meg a neved!");
		rlDataEasy = dE;
		rlDataMedium = dM;
		rlDataHard = dH;
		gridWidth = gWidth;
		timeCounter = timeCount;
		panel.add(name);
		panel.add(nameField);
		panel.add(b);
		b.addActionListener(bAL);
		this.add(panel);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
	}

	
	class ButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Mehet")) {
				if(nameField.getText().trim().isEmpty() == false && nameField.getText().length() >= 3) {
					if(gridWidth == 10) {
						rlDataEasy.addPlayer(nameField.getText(), timeCounter);
					}
					else if(gridWidth == 16) {
						rlDataMedium.addPlayer(nameField.getText(), timeCounter);
					}
					else if(gridWidth == 30) {
						rlDataHard.addPlayer(nameField.getText(), timeCounter);
					}
					try {
						Save save = new Save("ranklist.ser");
						save.saveRanklist(rlDataEasy.rlist);
						save.saveRanklist(rlDataMedium.rlist);
						save.saveRanklist(rlDataHard.rlist);
						save.closeFile();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					dispose();
				}
			}
		}
	}
}