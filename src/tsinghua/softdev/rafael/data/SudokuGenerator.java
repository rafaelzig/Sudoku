package tsinghua.softdev.rafael.data;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Rafael da Silva Costa - 2015280364
 */
public class SudokuGenerator implements SudokuConstants
{
	private static final int[][] solution =
			{{4, 3, 5, 8, 7, 6, 1, 2, 9}, {8, 7, 6, 2, 1, 9, 3, 4, 5},
					{2, 1, 9, 4, 3, 5, 7, 8, 6}, {5, 2, 3, 6, 4, 7, 8, 9, 1},
					{9, 8, 1, 5, 2, 3, 4, 6, 7}, {6, 4, 7, 9, 8, 1, 2, 5, 3},
					{7, 5, 4, 1, 6, 8, 9, 3, 2}, {3, 9, 2, 7, 5, 4, 6, 1, 8},
					{1, 6, 8, 3, 9, 2, 5, 7, 4}};

	private static final Random random = new Random();

	public static int[][] generate(int difficulty)
	{
		int[][] board = new int[BOARD_SIZE][];

		for (int i = 0; i < board.length; i++)
		{
			board[i] = Arrays.copyOf(solution[i], solution[i].length);
		}

		int limit = BOARD_SIZE + 1 + random.nextInt(BOARD_SIZE + 1);
		int[] next = new int[BOARD_SIZE];

		for (int i = 0; i < limit; i++)
		{
			randomizeBoard(board, next);
			// randomizeBoard(board, next);
		}

		randomizeBlocks(board, next);

		limit = BLOCK_SIZE + random.nextInt(6);

		for (int k = 0; k < limit; k++)
		{
			shuffleBoard(board);
		}

		for (int i = TOTAL_CELLS; i > DEFAULT_CELLS - difficulty; i--)
		{
			board[random.nextInt(BOARD_SIZE)][random.nextInt(BOARD_SIZE)] = 0;
		}

		return board;
	}

	private static void shuffleBoard(int[][] board)
	{
		int first = 1 + random.nextInt(BOARD_SIZE);
		int second = 1 + random.nextInt(BOARD_SIZE);

		for (int i = 0; i < BOARD_SIZE; i++)
		{
			for (int j = 0; j < BOARD_SIZE; j++)
			{
				if (board[i][j] == first)
				{
					board[i][j] = second;
					continue;
				}

				if (board[i][j] == second)
				{
					board[i][j] = first;
				}
			}
		}
	}

	private static void randomizeBlocks(int[][] board, int[] next)
	{
		int first = 1 + random.nextInt(BLOCK_SIZE);
		int second = 1 + random.nextInt(BLOCK_SIZE);

		if ((first == 1 && second == 2) || (first == 2 && second == 1))
		{
			for (int i = 0; i < BLOCK_SIZE; i++)
			{
				for (int j = 0; j < BOARD_SIZE; j++)
				{
					next[j] = board[i][j];
					board[i][j] = board[i + BLOCK_SIZE][j];
					board[i + BLOCK_SIZE][j] = next[j];
				}
			}
		}
		else if ((first == 2 && second == BLOCK_SIZE)
				|| (first == BLOCK_SIZE && second == 2))
		{
			for (int i = BLOCK_SIZE; i < 6; i++)
			{
				for (int j = 0; j < BOARD_SIZE; j++)
				{
					next[j] = board[i][j];
					board[i][j] = board[i + BLOCK_SIZE][j];
					board[i + BLOCK_SIZE][j] = next[j];
				}
			}
		}
		else if ((first == 1 && second == BLOCK_SIZE)
				|| (first == BLOCK_SIZE && second == 1))
		{
			for (int i = 0; i < BLOCK_SIZE; i++)
			{
				for (int j = 0; j < BOARD_SIZE; j++)
				{
					next[j] = board[i][j];
					board[i][j] = board[i + 6][j];
					board[i + 6][j] = next[j];
				}
			}
		}
	}

	private static void randomizeBoard(int[][] board, int[] next)
	{
		for (int a = 0; a < BLOCK_SIZE; a++)
		{
			int first = 0;
			int second = 0;

			if (a == 0)
			{
				first = random.nextInt(BLOCK_SIZE);
				second = random.nextInt(BLOCK_SIZE);
			}

			else if (a == 1)
			{
				first = BLOCK_SIZE + random.nextInt(BLOCK_SIZE);
				second = BLOCK_SIZE + random.nextInt(BLOCK_SIZE);
			}

			else if (a == 2)
			{
				first = 6 + random.nextInt(BLOCK_SIZE);
				second = 6 + random.nextInt(BLOCK_SIZE);
			}

			for (int i = 0; i < BOARD_SIZE; i++)
			{
				next[i] = board[first][i];
				board[first][i] = board[second][i];
				board[second][i] = next[i];
			}
		}
	}
}