package tsinghua.softdev.rafael.data;

/**
 * This file declares several constants that are useful in the program in
 * different modules. Any class that implements this interface can use these
 * constants.
 */
public interface SudokuConstants
{
	/** The padding used in the application window */
	static final int		APP_PADDING			= 5;

	/** The path to the icons */
	static final String[]	ICONS				= new String[] { "res/icon16.png",
			"res/icon22.png", "res/icon24.png", "res/icon32.png", "res/icon48.png" };

	/** The action command for the "Join Game" button */
	static final String		JOIN_ACTION			= "Join Game";

	/** The action command for the "Host Game" button */
	static final String		HOST_ACTION			= "Host Game";

	/** The action command for the "Cancel" button */
	static final String		CANCEL_ACTION		= "Cancel";

	/** The action command for the "Start Game" button */
	static final String		START_GAME_ACTION	= "Start Game";

	/** The action command for the timer object */
	static final String		TIMEOUT_ACTION		= "Timed Out";

	/**
	 * The time, in milliseconds, the host will be listening for an incoming
	 * connection until a timeout occurs.
	 */
	static final int		TIMEOUT				= 2000;

	/**
	 * The size of the game board, which is also the number of cells in a block.
	 * The board should always be square, with equal height and width.
	 */
	static final int		BOARD_SIZE			= 9;

	/**
	 * The total number of cells in the game board.
	 */
	static final int		TOTAL_CELLS			= BOARD_SIZE * BOARD_SIZE;

	/** The size (how many cells) of a single block. */
	static final int		BLOCK_SIZE			= (int) Math.sqrt(BOARD_SIZE);			;

	/** The maximum value allowed in a cell. */
	static final int		MAX_VALUE			= BOARD_SIZE;

	/** The minimum value allowed in a cell. */
	static final int		MIN_VALUE			= 0;

	/** The heigth of a single cell in a block in pixels. */
	static final int		CELL_HEIGHT			= 50;

	/** The heigth of a single cell in a block in pixels. */
	static final int		CELL_WIDTH			= 50;

	/** The font size cells. */
	static final int		CELL_FONT_SIZE		= 20;

	/**
	 * The default port number for both server and client. It is shown as the
	 * default value for the text fields.
	 */
	static final int		DEFAULT_PORT		= 31416;

	/** The minimum port number for both server and client */
	static final int		MIN_PORT			= 1025;

	/** The maximum port number for both server and client */
	static final int		MAX_PORT			= 65535;

	/**
	 * The default IP address to connect for client. It is shown as the default
	 * value for the text fields.
	 */
	static final int		DEFAULT_IP[]		= new int[] { 127, 0, 0, 1 };

	/**
	 * The default difficulty level. It is shown as the default value for the
	 * difficulty text field. You can change this value to fit your own
	 * difficulty level system.
	 */
	static final int		DEFAULT_DIFFICULTY	= 5;

	static final Integer[]		DIFFICULTIES		= { 1, 2, 3, 4, 5, 6, 7, 8 };

	/**
	 * The action command for the timer object.
	 */
	static final String		TIMER_ACTION		= "Timer";

	/**
	 * The flag indicating the game has started.
	 */
	static final int		GAME_STARTED		= 1;

	/**
	 * The flag indicating the game has ended.
	 */
	static final int		GAME_ENDED			= 2;

	/**
	 * The flag indicating the game is being initialised.
	 */
	static final int		GAME_INIT			= 3;
	
	/**
	 * Default number of cells to be left fixed.
	 */
	static final int		DEFAULT_CELLS		= 35;
}