package tsinghua.softdev.rafael.data;

public class Cell implements SudokuConstants
{
	private int		value;
	private boolean	isHost;
	private boolean	isFixed;

	public Cell(String value)
	{
		int temp = Integer.parseInt(value);

		if (temp < MIN_VALUE || temp > MAX_VALUE)
		{
			throw new NumberFormatException();
		}

		this.value = Integer.parseInt(value);
		this.isHost = true;
	}

	public Cell(boolean isHost, int value)
	{
		this.isHost = isHost;
		this.value = value;
	}

	public Cell()
	{
		this(true, 0);
	}

	public Cell(int value, boolean isFixed)
	{
		this.value = value;
		this.isFixed = isFixed;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public boolean isEmpty()
	{
		return value == MIN_VALUE;
	}

	public void setIsHost(boolean isHost)
	{
		this.isHost = isHost;
	}

	public boolean isHost()
	{
		return isHost;
	}

	@Override
	public String toString()
	{
		return isEmpty() ? "" : String.valueOf(value);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (isHost ? 1231 : 1237);
		result = prime * result + value;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Cell other = (Cell) obj;
		if (isHost != other.isHost)
		{
			return false;
		}
		if (value != other.value)
		{
			return false;
		}
		return true;
	}

	public void setIsFixed(boolean isFixed)
	{
		this.isFixed = isFixed;
	}

	public boolean isFixed()
	{
		return isFixed;
	}
}