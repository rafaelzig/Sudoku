package tsinghua.softdev.rafael.gui.component;

import tsinghua.softdev.rafael.data.Cell;
import tsinghua.softdev.rafael.data.SudokuConstants;
import tsinghua.softdev.rafael.data.SudokuGenerator;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;

/**
 * This class extends AbstractTableModel, inheriting its default functionality,
 * it also contains implementations for the abstract methods in order for the
 * Sudoku game to function properly.
 *
 * @author Rafael da Silva Costa - 2015280364
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SudokuBoardModel extends AbstractTableModel implements SudokuConstants
{
	private final Cell[][]	board		= new Cell[BOARD_SIZE][BOARD_SIZE];
	private final int[][]	rowValues	= new int[BOARD_SIZE][BOARD_SIZE];
	private final int[][]	colValues	= new int[BOARD_SIZE][BOARD_SIZE];
	private final int[][]	blockValues	= new int[BOARD_SIZE][BOARD_SIZE];

	public SudokuBoardModel()
	{
		super();
		initBoard();
	}

	private void initBoard()
	{
		Arrays.stream(board).forEach(array -> Arrays.setAll(array, index -> new Cell()));
	}

	public void setBoard(int difficulty)
	{
		int[][] b = SudokuGenerator.generate(difficulty);

		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board.length; j++)
			{
				board[i][j].setValue(b[i][j]);

				if (b[i][j] != 0)
				{
					board[i][j].setIsFixed(true);
					setSubsetValue(i, j, b[i][j], true);
				}
			}
		}
	}

	@Override
	public int getColumnCount()
	{
		return board[0].length;
	}

	@Override
	public int getRowCount()
	{
		return board.length;
	}

	@Override
	public Cell getValueAt(int rowIndex, int columnIndex)
	{
		return board[rowIndex][columnIndex];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		setValueAt((Cell) aValue, rowIndex, columnIndex);
	}

	private void setValueAt(Cell cell, int row, int col)
	{
		Cell oldCell = board[row][col];

		int value = cell.getValue();
		int oldValue = board[row][col].getValue();

		if (cell.equals(oldCell))
		{
			return;
		}

		board[row][col] = cell;

		if (!cell.isEmpty())
		{
			if (!oldCell.isEmpty())
			{
				setSubsetValue(row, col, oldValue, false);
			}

			setSubsetValue(row, col, value, true);

		}
		// cell.isEmpty()
		else if (!oldCell.isEmpty())
		{
			setSubsetValue(row, col, oldValue, false);
		}

		fireTableCellUpdated(row, col);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return Cell.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return !board[rowIndex][columnIndex].isFixed();
	}

	/**
	 * TODO
	 */
	boolean isDuplicate(int row, int col)
	{
		int value = board[row][col].getValue();

		if (value == MIN_VALUE)
		{
			return false;
		}

		return rowValues[row][value - 1] > 1 || colValues[col][value - 1] > 1
				|| blockValues[getBlockNumber(row, col)][value - 1] > 1;
	}

	private static int getBlockNumber(int row, int col)
	{
		return (row / BLOCK_SIZE) * BLOCK_SIZE + (col / BLOCK_SIZE);
	}

	public boolean isWon()
	{
		return isValid(rowValues) && isValid(colValues) && isValid(blockValues);
	}

	private static boolean isValid(int[][] values)
	{
		return Arrays.stream(values).flatMapToInt(Arrays::stream).noneMatch(n -> n != 1);
	}

	private void setSubsetValue(int row, int col, int value, boolean isAddition)
	{
		int n = isAddition ? 1 : -1;

		rowValues[row][value - 1] += n;
		colValues[col][value - 1] += n;
		blockValues[getBlockNumber(row, col)][value - 1] += n;
	}
}