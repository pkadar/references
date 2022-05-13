package minesweeper;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Menu{
	private ImageIcon img = new ImageIcon("resources\\icon.png");
	private static String[] menuOptions = {"Új játék", "Játék folytatása", "Ranglista", "Kilépés"};
	private JButton newGame = new JButton(menuOptions[0]);
	private static JButton continueGame = new JButton(menuOptions[1]);
	private JButton ranklist = new JButton(menuOptions[2]);
	private JButton exit = new JButton(menuOptions[3]);
	private JPanel menuPanel = new JPanel();
	private ActionListener ALMenu = new MenuActionListener();
	private static JFrame frame = new JFrame();
	private RanklistData rlDataEasy = new RanklistData();
	private RanklistData rlDataMedium = new RanklistData();
	private RanklistData rlDataHard = new RanklistData();
	int gHeight=0, gWidth=0, gFlags=0, gCounter=0;

	public Menu()  {
		frame.setTitle("Aknakereső");
		frame.setSize(300, 400);
		frame.setIconImage(img.getImage());
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

		menuPanel.add(Box.createVerticalGlue());
		menuPanel.add(newGame);
		menuPanel.add(Box.createRigidArea(new Dimension(2,30)));
		menuPanel.add(continueGame);
		menuPanel.add(Box.createRigidArea(new Dimension(2,30)));
		menuPanel.add(ranklist);
		menuPanel.add(Box.createRigidArea(new Dimension(2,30)));
		menuPanel.add(exit);
		menuPanel.add(Box.createVerticalGlue());
		
		newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		continueGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		ranklist.setAlignmentX(Component.CENTER_ALIGNMENT);
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		newGame.addActionListener(ALMenu);
		continueGame.addActionListener(ALMenu);
		ranklist.addActionListener(ALMenu);
		exit.addActionListener(ALMenu);
		
		frame.add(menuPanel);
		
		try {
			Load loadRanklist = new Load("ranklist.ser");
			loadRanklist.loadRanklist(rlDataEasy, rlDataMedium, rlDataHard);
			loadRanklist.closeFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		File file = new File("boarddata.ser");
		File file2 = new File("boardinfo.ser");
		if(!file.exists() || file.length() == 0 || !file2.exists() || file2.length() == 0) {
			continueGame.setEnabled(false);
		}
		
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void RefreshBoardData() {
		File file = new File("boarddata.ser");
		if(!file.exists() || file.length() == 0) {
			continueGame.setEnabled(false);
		} else {
			continueGame.setEnabled(true);
		}
	}
	
	class MenuActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(menuOptions[0])) {
				NewGameSettings settings = new NewGameSettings(rlDataEasy, rlDataMedium, rlDataHard);
				frame.setVisible(false);
				settings.setVisible(true);
			} else if(e.getActionCommand().equals(menuOptions[1])) {
				File file = new File("boarddata.ser");
				File file2 = new File("boardinfo.ser");
				if(!file.exists() || file.length() == 0 || !file2.exists() || file2.length() == 0) {
					System.out.println("Hiba! A program futása közben a fájlok változtak.");
				} else {
					try {
						Load loadContinueData = new Load("boardinfo.ser");
						loadContinueData.loadBoardAdditionalData(Menu.this);
						loadContinueData.closeFile();
						GameBoard gameBoard = new GameBoard(rlDataEasy, rlDataMedium, rlDataHard, gHeight, gWidth, gFlags, gCounter, true);
						gameBoard.setVisible(true);
						file = new File("boarddata.ser");
						file2 = new File("boardinfo.ser");
						if(file.exists()) {
							file.delete();
						}
						if(file2.exists()) {
							file2.delete();
						}
					} catch (IOException e1) {e1.printStackTrace();}
				}
			} else if(e.getActionCommand().equals(menuOptions[2])) {
				RanklistFrame rlf = new RanklistFrame(rlDataEasy, rlDataMedium, rlDataHard);
				frame.setVisible(false);
				rlf.setVisible(true);
			} else {
				System.exit(0);
			}
		}
	}
	
	public static void setVisibility(boolean value) {
		frame.setVisible(value);
	}
}
