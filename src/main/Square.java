package main;

import java.awt.*;
import javax.swing.*;

public class Square 
{
	public int value = 0;
	public int rowIndex = 0;
	public int colIndex = 0;
	public boolean isMine = false;
	public boolean isOpened = false;
	public boolean isMarked = false;
	public boolean isEmpty = false;
	public JButton button;
	public Square(boolean inputIsMine, int inRowIndex, int inColIndex)
	{
		isMine = inputIsMine;
		rowIndex = inRowIndex;
		colIndex = inColIndex;
		button = new JButton("");
		button.setSize(MinesweeperGUI.buttonSize, MinesweeperGUI.buttonSize);
		button.setBackground(Color.WHITE);
	}
	public void computeValue()
	{
		int surroundingMines = 0;
		for (int i=rowIndex-1; i<=rowIndex+1; i++)
		{
			for (int j=colIndex-1; j<=colIndex+1; j++)
			{
				try
				{
					if (MinesweeperGUI.squares[i][j].isMine)
					{
						surroundingMines++;
					}
				}
				catch(Exception e){}
			}
		}
		value = surroundingMines;
		if (value == 0)
		{
			isEmpty = true;
		}
	}
	public void open()
	{
		if (isOpened)
		{
			int surroundingMarks = 0;
			for (int i=rowIndex-1; i<=rowIndex+1; i++)
			{
				for (int j=colIndex-1; j<=colIndex+1; j++)
				{
					try
					{
						if (MinesweeperGUI.squares[i][j].isMarked)
						{
								surroundingMarks++;
						}
					}
					catch (Exception e){}
				}
			}
			if (surroundingMarks == value)
			{
				for (int i=rowIndex-1; i<=rowIndex+1; i++)
				{
					for (int j=colIndex-1; j<=colIndex+1; j++)
					{
						try
						{
							if (!MinesweeperGUI.squares[i][j].isOpened)
							{
								MinesweeperGUI.squares[i][j].open();
							}
						}
						catch (Exception e){}
					}
				}
			}
		}
		else if (!isMarked)
		{
			isOpened = true;
			if(isMine)
			{
				MinesweeperGUI.gameOver = true;
			}
			else if (isEmpty)
			{
				button.setIcon(MinesweeperGUI.openedIcon);
				for (int i=rowIndex-1; i<=rowIndex+1; i++)
				{
					for (int j=colIndex-1; j<=colIndex+1; j++)
					{
						if (i != rowIndex || j != colIndex)
						{
							try
							{
								MinesweeperGUI.squares[i][j].open();
							}
							catch(Exception e){}
						}
					}
				}
			}
			else  // need to use case statement instead
			{
				switch (value)
				{
				case 1:
					button.setIcon(MinesweeperGUI.icon1);
					break;
				case 2:
					button.setIcon(MinesweeperGUI.icon2);
					break;
				case 3:
					button.setIcon(MinesweeperGUI.icon3);
					break;
				case 4:
					button.setIcon(MinesweeperGUI.icon4);
					break;
				case 5:
					button.setIcon(MinesweeperGUI.icon5);
					break;
				case 6:
					button.setIcon(MinesweeperGUI.icon6);
					break;
				case 7:
					button.setIcon(MinesweeperGUI.icon7);
					break;
				case 8:
					button.setIcon(MinesweeperGUI.icon8);
					break;
				}	
			}
		}
	}
	public void mark()
	{
		if (!isOpened)
		{
			if(isMarked)
			{
				button.setIcon(null);
				button.setText("");
				isMarked = false;
				MinesweeperGUI.numMarkedMines--;
			}
			else
			{
				button.setIcon(MinesweeperGUI.flagIcon);
				isMarked = true;
				MinesweeperGUI.numMarkedMines++;
			}
		}
	}
	public void close()
	{
		button.setIcon(null);
		button.setIcon(MinesweeperGUI.closedIcon);
		isMarked = false;
		value = 0;
		isMine = false;
		isOpened = false;
		isEmpty = false;
	}
}
