package minesweeper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NewGameSettings extends JFrame {
	private static final long serialVersionUID = 1L;
	private ImageIcon img = new ImageIcon("resources\\icon.png");
	private JLabel chooseDifficulty = new JLabel("Válaszd ki a nehézségi szintet:");
	private Object[] sDifficulty = {"Válassz nehézséget","Könnyű | 10x10 - 10 akna", "Haladó | 16x16 40 - akna", "Nehéz | 30x16 - 99 akna"};
	private JComboBox<Object> difficulty = new JComboBox<Object>(sDifficulty);
	private JButton submit = new JButton("Kezdés");
	private JPanel panel = new JPanel();
	private ActionListener aLButton = new ButtonActionListener();
	private RanklistData rlDataEasy;
	private RanklistData rlDataMedium;
	private RanklistData rlDataHard;
	private int chosenDifficulty;
	
	public NewGameSettings(RanklistData dE, RanklistData dM, RanklistData dH) {
		rlDataEasy = dE;
		rlDataMedium = dM;
		rlDataHard = dH;
		this.setIconImage(img.getImage());
		this.setTitle("Új játék beállítása");
		this.setSize(300,200);
		this.setResizable(false);
		
		this.setLayout(new BorderLayout());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		submit.addActionListener(aLButton);
		
		panel.add(Box.createVerticalGlue());
		panel.add(chooseDifficulty);		
		panel.add(difficulty);
		panel.add(Box.createRigidArea(new Dimension(2,30))); 
		panel.add(submit);
		panel.add(Box.createVerticalGlue());
		
		chooseDifficulty.setAlignmentX(Component.CENTER_ALIGNMENT);
		difficulty.setAlignmentX(Component.CENTER_ALIGNMENT);
		submit.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.add(panel, BorderLayout.CENTER);
		
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e){
                Menu.setVisibility(true);
            }
        });
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public int getDifficulty() {
		return chosenDifficulty;
	}
	
	public int[] getBoardSettings(int diff) {
		if(diff == 1) {
			return new int[] {10,10,10};
		} else if(diff == 2) {
			return new int[] {16,16,40};
		} else {
			return new int[] {16,30,99};
		}
	}
	
	class ButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Kezdés")) {
				String chosenItem = (String)difficulty.getSelectedItem();
				if(chosenItem.equals(sDifficulty[0]) == false) {
					for(int i=1; i<sDifficulty.length; i++) {
						if(sDifficulty[i].equals(chosenItem)) {
							chosenDifficulty = i;
							break;
						}
					}
					dispose();
					//setVisible(false);
					int[] xyakna = getBoardSettings(getDifficulty());
					try {
						GameBoard board = new GameBoard(rlDataEasy, rlDataMedium, rlDataHard, xyakna[0], xyakna[1], xyakna[2], 0, false);
						board.setVisible(true);
						File file = new File("boarddata.ser");
						File file2 = new File("boardinfo.ser");
						if(file.exists()) {
							file.delete();
						}
						if(file2.exists()) {
							file2.delete();
						}
					} catch (Exception ex) {}
				}
			}
		}
	}
}
