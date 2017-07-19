package tsinghua.softdev.rafael.data;

public class Move implements SudokuConstants
{
	private int		value;
	private int		row;
	private int		column;
	private int status;

	public Move(int value, int row, int col)
	{
		this.value = value;
		this.row = row;
		this.column = col;
	}

	public Move(String input)
	{
		this.value = Character.getNumericValue(input.charAt(0));
		this.row = Character.getNumericValue(input.charAt(1));
		this.column = Character.getNumericValue(input.charAt(2));
		this.status = Character.getNumericValue(input.charAt(3));
	}

	public Move(int gameStatus)
	{
		this(0, 0, 0);
		this.status = gameStatus;
	}

	public Move(int value, int row, int col, int gameStatus)
	{
		this(value, row, col);
		this.status = gameStatus;
	}

	@Override
	public String toString()
	{
		return "" + value + row + column + status;
	}

	/**
	 * @return the row
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * @param row
	 *            the row to set
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * @return the value
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value)
	{
		this.value = value;
	}

	public boolean isGameFinished()
	{
		return status == GAME_ENDED;
	}

	public boolean isGameStarted()
	{
		return status == GAME_STARTED;
	}

	public boolean isGameInit()
	{
		return status == GAME_INIT;
	}
}