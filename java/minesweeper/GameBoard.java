package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameBoard extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private ImageIcon img = new ImageIcon("resources\\icon.png");
	private GridLayout grid = new GridLayout();
	private JPanel felso = new JPanel();
	private JPanel also = new JPanel();
	private JPanel felsoPanel2 = new JPanel();
	private JPanel felsoPanel3 = new JPanel();
	private JPanel felsoPanel4 = new JPanel();
	private MouseListener buttonListener = new ButtonMouseListener();
	protected Field[][] board;
	private int Flags;
	private int gridWidth;
	private int gridHeight;
	private JLabel flagNumbers = new JLabel();
	private JLabel winorlose = new JLabel();
	private JLabel time = new JLabel();
	private GameTimer timer = new GameTimer(time);
	private RanklistData rlDataEasy;
	private RanklistData rlDataMedium;
	private RanklistData rlDataHard;
	private int firstFieldIndex;
	private boolean gameEnded = false;
	private boolean firstPress = true;
	private boolean nearbyRevealGameEnds = false;
	
	public GameBoard(RanklistData dE, RanklistData dM, RanklistData dH, int height, int width, int mines, int currentTime, boolean fromFile) throws IOException {
		this.setIconImage(img.getImage());
		this.setTitle("Aknakereső");
		this.setResizable(false);
		
		rlDataEasy = dE;
		rlDataMedium = dM;
		rlDataHard = dH;
		
		felso.setLayout(new BorderLayout());
		BufferedImage img = ImageIO.read(new File("resources\\11.png"));
		JLabel picLabel = new JLabel(new ImageIcon(img));
		Flags = mines;
		if(!fromFile) {
	
			board = new Field[height][width];
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					board[i][j] = new Field(i,j);
					board[i][j].addMouseListener(buttonListener);
					also.add(board[i][j]);
				}
			}
		} else {
				firstPress = false;
			try {
				Load loadBoard = new Load("boarddata.ser");
				board = loadBoard.loadField();
				loadBoard.closeFile();
			} catch (ClassNotFoundException | IOException e1) {e1.printStackTrace();}
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					board[i][j].addMouseListener(buttonListener);
					also.add(board[i][j]);
				}
			}
			timer.setTime(currentTime-1);
			timer.start();
		}
		
		gridWidth = width;
		gridHeight = height;
		felsoPanel2.add(picLabel);
		flagNumbers.setText(": " + Flags);
		flagNumbers.setFont(new Font("Serif",Font.BOLD, 16));
		time.setText("000");
		time.setFont(new Font("Serif",Font.BOLD, 16));
		time.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		felsoPanel2.add(flagNumbers);
		felsoPanel3.add(time);
		felsoPanel4.add(winorlose);
		felso.add(felsoPanel2, BorderLayout.WEST);
		felso.add(felsoPanel4, BorderLayout.CENTER);
		felso.add(felsoPanel3, BorderLayout.EAST);
		
		grid.setRows(height);
		grid.setColumns(width);
		
		also.setLayout(grid);
		
		this.add(felso, BorderLayout.NORTH);
		this.add(also, BorderLayout.SOUTH);
		this.pack();
		
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e){
                Menu.setVisibility(false);
            }
            public void windowClosed(WindowEvent e) {
            	if(!gameEnded && firstPress == false) {
	            	try {
						Save save = new Save("boarddata.ser");
						save.saveField(board);
						save.closeFile();
						save = new Save("boardinfo.ser");
						save.saveBoardAdditionalData(gridHeight, gridWidth, Flags, timer.getTime());
						save.closeFile();
						timer.terminate();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
            	}
            	Menu.setVisibility(true);
            	Menu.RefreshBoardData();
            }
        });
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
	
	public int[] randomMines(int x, int y, int num, int notAcceptable) {
		int[] tomb = new int[num];
		Arrays.fill(tomb, -1);
		for(int i=0; i<num; i++) {
			int randomolt = getRandomNumber(0, x*y);
			while(isExists(tomb, randomolt) || randomolt == notAcceptable) {
				randomolt = getRandomNumber(0, x*y);
			}
			tomb[i] = randomolt;
		}
		return tomb;
	}
	
	public boolean isExists(int[] hol, int mi) {
		for(int i=0; i<hol.length; i++) {
			if(hol[i] == mi) return true;
		}
		return false;
	}
	
	public int[] getCoordinatesFromIndex(int x, int y, int index) {
		int rows = index / y;
		int columns = index % y;
		return new int[] {rows,columns};
	}
	
	public int getIndexFromMatrix(int x, int y, int wantedx, int wantedy) {
		return y*wantedx+wantedy;
	}
	
	public int getNeighbourMines(Field field, int h, int w) {
		int numOfMines = 0;
		int y = field.getYCoordinate();
		int x = field.getXCoordinate();
		if(x != 0 && board[x-1][y].isMine()) { numOfMines++; }
		if(x != 0 && y != w-1 && board[x-1][y+1].isMine()) { numOfMines++; }
		if(y != w-1 && board[x][y+1].isMine()) { numOfMines++; }
		if(x != h-1 && y != w-1 && board[x+1][y+1].isMine()) { numOfMines++; }
		if(x != h-1 && board[x+1][y].isMine()) { numOfMines++; }
		if(x != h-1 && y != 0 && board[x+1][y-1].isMine()) { numOfMines++; }
		if(y != 0 && board[x][y-1].isMine()) { numOfMines++; }
		if(x != 0 && y != 0 && board[x-1][y-1].isMine()) { numOfMines++; }
		return numOfMines;
	}
	
	public void setEmptysClickedFull(int x, int y) {
		setEmptysClicked(x, y);
		if(gameWon()) {
			winGame();
		}
	}
	
	public void setEmptysClicked(int x, int y) {
		if(x < 0 || x > gridHeight-1 || y < 0 || y > gridWidth-1) return;
		if(board[x][y].getType() == 0 && !board[x][y].getMarked() && board[x][y].getClickable()) {
			board[x][y].click();
			setEmptysClicked(x+1, y);
			setEmptysClicked(x-1, y);
			setEmptysClicked(x, y+1);
			setEmptysClicked(x, y-1);
			setEmptysClicked(x+1, y+1);
			setEmptysClicked(x+1, y-1);
			setEmptysClicked(x-1, y-1);
			setEmptysClicked(x-1, y+1);
			if(x != gridHeight-1 && board[x+1][y].getClickable() && board[x+1][y].getType() != 0) {board[x+1][y].click();}
			if(x != 0 && board[x-1][y].getClickable() && board[x-1][y].getType() != 0) 	{board[x-1][y].click();}
			if(y != gridWidth-1 && board[x][y+1].getClickable() && board[x][y+1].getType() != 0) 	{board[x][y+1].click();}
			if(y != 0 && board[x][y-1].getClickable() && board[x][y-1].getType() != 0) 	{board[x][y-1].click();}
			if(x != 0 && y != 0 && board[x-1][y-1].getClickable() && board[x-1][y-1].getType() != 0 ) {board[x-1][y-1].click();}
			if(x != 0 && y != gridWidth-1 && board[x-1][y+1].getClickable() && board[x-1][y+1].getType() != 0) {board[x-1][y+1].click();}
			if(x != gridHeight-1 && y != 0 && board[x+1][y-1].getClickable() && board[x+1][y-1].getType() != 0) {board[x+1][y-1].click();}
			if(x != gridHeight-1 && y != gridWidth-1 && board[x+1][y+1].getClickable() && board[x+1][y+1].getType() != 0) {board[x+1][y+1].click();}
		}  else return;
		
	}
	
	public void loseGame() {
		for(int i=0; i<gridHeight; i++) {
			for(int j=0; j<gridWidth; j++) {
				if(board[i][j].getType() == 9 && board[i][j].getClickable()) {
					board[i][j].click();
				}
				if(board[i][j].getType() != 9 && board[i][j].getMarked()) board[i][j].setIcon(12);
				if(board[i][j].getClickable()) board[i][j].setClickable(false);
			}
		}
		timer.terminate();
		winorlose.setText("Vesztettél!");
		gameEnded = true;
	}
	
	public boolean gameWon() {
		for(int i=0; i<gridHeight; i++) {
			for(int j=0; j<gridWidth; j++) {
				if((board[i][j].getClickable() && board[i][j].getType() != 9) || (!board[i][j].getClickable() && board[i][j].getMarked() && board[i][j].getType() != 9)) return false;
			}
		}
		return true;
	}
	
	public void winGame() {
		timer.terminate();
		for(int i=0; i<gridHeight; i++) {
			for(int j=0; j<gridWidth; j++) {
				if(board[i][j].getType() == 9) {
					board[i][j].setIcon(11);
					board[i][j].setClickable(false);
				}
			}
		}
		winorlose.setText("Nyertél!");
		gameEnded = true;
		WinFrame winFrame = new WinFrame(rlDataEasy, rlDataMedium, rlDataHard, gridWidth, timer.getTime());
		winFrame.setVisible(true);
	}
	
	public void revealNearbyHelper(Field field) {
		if(field.getClickable()) {
			if(field.getType() == 9) {
				field.setType(13);
				nearbyRevealGameEnds = true;
			} 
			if(field.getType() == 0) {
				setEmptysClickedFull(field.getXCoordinate(), field.getYCoordinate());
			} else {
				field.click();
			}
		}
	}
	
	public void revealNearby(int x, int y) {
		revealNearbyHelper(board[x][y]);
		if(x != gridHeight-1) { revealNearbyHelper(board[x+1][y]);}
		if(x != 0) { revealNearbyHelper(board[x-1][y]);}
		if(y != gridWidth-1){ revealNearbyHelper(board[x][y+1]);}
		if(y != 0) { revealNearbyHelper(board[x][y-1]);}
		if(x != 0 && y != 0 ) { revealNearbyHelper(board[x-1][y-1]);}
		if(x != 0 && y != gridWidth-1) { revealNearbyHelper(board[x-1][y+1]);}
		if(x != gridHeight-1 && y != 0) { revealNearbyHelper(board[x+1][y-1]);}
		if(x != gridHeight-1 && y != gridWidth-1) { revealNearbyHelper(board[x+1][y+1]);}
		if(nearbyRevealGameEnds == true) { loseGame();}
		else if(gameWon()) { winGame();}
	}
	
	//Ezt csak teszteléshez használtam, jelenleg nincs használatban
	public void cheatForTesting() {
		for(int i=0; i<gridHeight; i++) {
			for(int j=0; j<gridWidth; j++) {
				if(board[i][j].getType() == 9) {
					board[i][j].setIcon(board[i][j].getType());
				}
			}
		}
	}
	//
	
	class ButtonMouseListener implements MouseListener {
		private static final int B1DM = MouseEvent.BUTTON1_DOWN_MASK;
		private static final int B3DM = MouseEvent.BUTTON3_DOWN_MASK;
		boolean bothDown = false;
		Field button;
		
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {
			button = (Field)e.getSource();
			int both = B1DM | B3DM;
			bothDown = (e.getModifiersEx() & both) == both;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				if(bothDown) {
					if((e.getModifiersEx() & B3DM) != B3DM && firstPress == false && button.getClickable()) {
						//mindkettő felengedve
						revealNearby(button.getXCoordinate(), button.getYCoordinate());
					}
				} else {
					//bal felengedve
					if(button.getClickable()) {
						if(firstPress) {
							firstPress = false;
							firstFieldIndex = getIndexFromMatrix(gridHeight, gridWidth, button.getXCoordinate(), button.getYCoordinate());
							int[] minesPlaces = randomMines(gridHeight,gridWidth,Flags,firstFieldIndex);
							int kordi[];
							for(int i=0; i<minesPlaces.length; i++) {
								kordi = getCoordinatesFromIndex(gridHeight, gridWidth, minesPlaces[i]);
								board[kordi[0]][kordi[1]].setMine();
							}
							for(int i=0; i<gridHeight; i++) {
								for(int j=0; j<gridWidth; j++) {
									if(!(board[i][j].isMine())){
										board[i][j].setType(getNeighbourMines(board[i][j], gridHeight, gridWidth));
									}
								}
							}
							timer.start();
							//cheatForTesting();
						}
						if(button.getType() == 0) {
							setEmptysClickedFull(button.getXCoordinate(), button.getYCoordinate());
						} else {
							if(button.getType() == 9) {
								button.setType(13);
								button.click();
								loseGame();
							} else {
								button.click();
								if(firstPress == false && gameWon()) {
									winGame();
								}
							}
						}
					}
				}
			} else if(e.getButton() == MouseEvent.BUTTON3) {
				if(bothDown) {
					if((e.getModifiersEx() & B1DM) != B1DM && firstPress == false && button.getClickable()) {
						//mindkettő felengedve
						revealNearby(button.getXCoordinate(), button.getYCoordinate());
					}
				} else {
					//jobb felengedve
					if(firstPress == false) {
						if(button.getMarked() == true) {
							button.markField();
							Flags++;
						} else {
							if(button.getClickable()) {
								if(Flags > 0) {
									button.markField();
									Flags--;
								}
							}
						}
						flagNumbers.setText(": " + Flags);
					}
				}
			}
		}
		public void mouseDragged(MouseEvent e) {
			Field b2 = (Field)e.getSource();
			if(button.getXCoordinate() != b2.getXCoordinate() || button.getYCoordinate() != b2.getYCoordinate()) {
				bothDown = false;
			}	
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
}
