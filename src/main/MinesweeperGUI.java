package main;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import resources.ResourceLoader;

import java.awt.event.*;

public class MinesweeperGUI
{
	static int NUM_ROWS = 0;
	static int NUM_COLS = 0;
	static int NUM_MINES = 0;
	static Square[][] squares;
	static boolean gameOver = false;
	static boolean gameWon = false;
	static int buttonSize = 40;
	static Icon flagIcon = new ImageIcon(ResourceLoader.loadImage("flag.png"));
	static Icon icon1 = new ImageIcon(ResourceLoader.loadImage("1.png"));
	static Icon icon2 = new ImageIcon(ResourceLoader.loadImage("2.png"));
	static Icon icon3 = new ImageIcon(ResourceLoader.loadImage("3.png"));
	static Icon icon4 = new ImageIcon(ResourceLoader.loadImage("4.png"));
	static Icon icon5 = new ImageIcon(ResourceLoader.loadImage("5.png"));
	static Icon icon6 = new ImageIcon(ResourceLoader.loadImage("6.png"));
	static Icon icon7 = new ImageIcon(ResourceLoader.loadImage("7.png"));
	static Icon icon8 = new ImageIcon(ResourceLoader.loadImage("8.png"));
	static Icon mineIcon = new ImageIcon(ResourceLoader.loadImage("mine.png"));
	static Icon easyIcon = new ImageIcon(ResourceLoader.loadImage("easy.png"));
	static Icon mediumIcon = new ImageIcon(ResourceLoader.loadImage("medium.png"));
	static Icon hardIcon = new ImageIcon(ResourceLoader.loadImage("hard.png"));
	static Icon customIcon = new ImageIcon(ResourceLoader.loadImage("custom.png"));
	static Icon closedIcon = new ImageIcon(ResourceLoader.loadImage("closedSquare.png"));
	static Icon openedIcon = new ImageIcon(ResourceLoader.loadImage("openedSquare.png"));
	static JPanel minesPanel;
	static JButton easyButton;
	static JButton mediumButton;
	static JButton hardButton;
	static JButton customButton;
	static JButton gameResetButton;
	static boolean firstClick = true;
	static JLabel minesLabel = new JLabel("0/" + NUM_MINES);
	static public int numMarkedMines = 0;
	static public JFrame mainFrame;
	static public JPanel mainPanel;
	static public JPanel bottomPanel;
	static public JPanel statsPanel;
	
	public static void main(String[] args) 
	{	
		introGUI();
	}
	public static void introGUI()
	{
		JFrame introFrame = new JFrame();
		introFrame.setTitle("JavaSweeper V 0.1.2");
		introFrame.setSize(500, 500);
		JPanel introPanel = (JPanel)introFrame.getContentPane();
		introPanel.setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		JLabel difficultyLabel = new JLabel("Select Difficulty");
		topPanel.add(difficultyLabel);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setSize(500, 500);
		buttonsPanel.setLayout(new GridLayout(2,2));
		easyButton = new JButton("");
		easyButton.setIcon(easyIcon);
		easyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				NUM_ROWS = 8;
				NUM_COLS = 8;
				NUM_MINES = 10;
				squares = new Square[NUM_ROWS][NUM_COLS];
				minesweeperWindow();
				makeField();
			}
		});
		mediumButton = new JButton("");
		mediumButton.setIcon(mediumIcon);
		mediumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				NUM_ROWS = 16;
				NUM_COLS = 16;
				NUM_MINES = 40;
				squares = new Square[NUM_ROWS][NUM_COLS];
				minesweeperWindow();
				makeField();
			}
		});
		hardButton = new JButton("");
		hardButton.setIcon(hardIcon);
		hardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				NUM_ROWS = 16;
				NUM_COLS = 30;
				NUM_MINES = 99;
				squares = new Square[NUM_ROWS][NUM_COLS];
				minesweeperWindow();
				makeField();
			}
		});
		customButton = new JButton("");
		customButton.setIcon(customIcon);
		/*
		customButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				
			}
		});
		*/
		buttonsPanel.add(easyButton);
		buttonsPanel.add(mediumButton);
		buttonsPanel.add(hardButton);
		buttonsPanel.add(customButton);
		
		introPanel.add(topPanel, BorderLayout.PAGE_START);
		introPanel.add(buttonsPanel, BorderLayout.CENTER);
		introFrame.setVisible(true);
		introFrame.setSize(500, 500+topPanel.getHeight());
	}
	static void minesweeperWindow()
	{
		mainFrame = new JFrame();
		mainFrame.setSize(NUM_COLS*buttonSize,NUM_ROWS*buttonSize);
		mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		mainFrame.setTitle("Minesweeper");
		mainPanel = (JPanel)mainFrame.getContentPane();
		mainPanel.setLayout(new BorderLayout());
		
		minesPanel = new JPanel();
		minesPanel.setSize(NUM_ROWS*buttonSize,NUM_COLS*buttonSize);
		minesPanel.setLayout(new GridLayout(NUM_ROWS,NUM_COLS));
		mainPanel.add(minesPanel, BorderLayout.CENTER);
		for (int i=0; i<NUM_ROWS; i++)
		{
			for (int j=0; j<NUM_COLS; j++)
			{
				squares[i][j] = new Square(false,i,j);
				minesPanel.add(squares[i][j].button);
				squares[i][j].button.addMouseListener(new MouseListener() {
					public void mousePressed(MouseEvent me)
					{
						if (gameOver || gameWon)
						{
							return;
						}
						JButton control = (JButton)me.getSource();
						int buttonIndexRow = 0;
						int buttonIndexCol = 0;
						for (int i=0; i<NUM_ROWS; i++)
						{
							for (int j=0; j<NUM_COLS; j++)
							{
								if (squares[i][j].button == control)
								{
									buttonIndexRow = i;
									buttonIndexCol = j;
								}
							}
						}
						if (me.getButton() == MouseEvent.BUTTON1)
						{
							while (firstClick)
							{
								if (!squares[buttonIndexRow][buttonIndexCol].isEmpty)
								{
									makeField();
								}
								else
								{
									firstClick = false;
								}
							}
							squares[buttonIndexRow][buttonIndexCol].open();
							if (!squares[buttonIndexRow][buttonIndexCol].isMine)
							{
								checkWon();
							}
						}
						else if (me.getButton() == MouseEvent.BUTTON3)
						{
							squares[buttonIndexRow][buttonIndexCol].mark();
							minesLabel.setText(numMarkedMines + "/" + NUM_MINES);
						}
						
						if (gameOver)
						{
							for (int i=0; i<NUM_ROWS; i++)
							{
								for (int j=0; j<NUM_COLS; j++)
								{
									if (squares[i][j].isMine)
									{
										squares[i][j].button.setIcon(mineIcon);
									}
									else
									{
										squares[i][j].open();
									}
								}
							}
							JOptionPane.showMessageDialog(null, "GAME OVER");
						}
					}
					public void mouseReleased(MouseEvent me){}
				    public void mouseEntered(MouseEvent me){}
				    public void mouseExited(MouseEvent me){}
				    public void mouseClicked(MouseEvent me){}
				});
				squares[i][j].button.setEnabled(false);
			}
		}
		
		bottomPanel = new JPanel();
		gameResetButton = new JButton("Reset");
		gameResetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				makeField();
			}
		});
		bottomPanel.add(gameResetButton);
		mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
		
		statsPanel = new JPanel();
		statsPanel.setLayout(new FlowLayout());
		statsPanel.add(minesLabel);
		minesLabel.setFont(new Font("Arial",Font.BOLD,20));
		mainPanel.add(statsPanel, BorderLayout.PAGE_START);
		
		mainFrame.setVisible(true);
		mainFrame.setSize(NUM_COLS*buttonSize,NUM_ROWS*buttonSize+bottomPanel.getHeight()+statsPanel.getHeight()+30);
	}
	static void makeField()
	{
		gameOver = false;
		gameWon = false;
		firstClick = true;
		numMarkedMines = 0;
		minesLabel.setText("0/" + NUM_MINES);
		boolean[][] mineLocations = new boolean[NUM_ROWS][NUM_COLS];
		int mineIndexRow = 0;
		int mineIndexCol = 0;
		Random r = new Random();
		for (int i=0; i<NUM_MINES;)
		{
			mineIndexRow = r.nextInt(NUM_ROWS);
			mineIndexCol = r.nextInt(NUM_COLS);
			if (mineLocations[mineIndexRow][mineIndexCol] == false)
			{
				mineLocations[mineIndexRow][mineIndexCol] = true;
				i++;
			}
		}
		for (int i=0; i<NUM_ROWS; i++)
		{
			for (int j=0; j<NUM_COLS; j++)
			{
				squares[i][j].close();
				squares[i][j].isMine = mineLocations[i][j];
				squares[i][j].button.setEnabled(true);
			}
		}
		for (int i=0; i<NUM_ROWS; i++)
		{
			for (int j=0; j<NUM_COLS; j++)
			{
				squares[i][j].computeValue();
				
			}
		}
	}
	public static void checkWon()
	{
		int openedSquares = 0;
		int correctMarks = 0;
		for (int i=0; i<NUM_ROWS; i++)
		{
			for (int j=0; j<NUM_COLS; j++)
			{
				if (squares[i][j].isOpened)
				{
					openedSquares++;
				}
			}
		}
		if ((NUM_ROWS*NUM_COLS)-openedSquares == NUM_MINES)
		{
			gameWon = true;
		}
		if (correctMarks == NUM_MINES)
		{
			gameWon = true;
		}
		
		if (gameWon)
		{
			for (int i=0; i<NUM_ROWS; i++)
			{
				for (int j=0; j<NUM_COLS; j++)
				{
					if (squares[i][j].isMine)
					{
						squares[i][j].button.setIcon(mineIcon);
					}
					else
					{
						squares[i][j].open();
					}
				}
			}
			JOptionPane.showMessageDialog(null, "YOU WON!");
		}
	}
}
