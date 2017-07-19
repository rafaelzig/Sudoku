package tsinghua.softdev.rafael.gui;

import tsinghua.softdev.rafael.data.Cell;
import tsinghua.softdev.rafael.data.Move;
import tsinghua.softdev.rafael.data.SudokuConstants;
import tsinghua.softdev.rafael.gui.component.SudokuBoard;
import tsinghua.softdev.rafael.gui.component.SudokuBoardModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

class Sudoku extends JDialog implements SudokuConstants, ActionListener,
		TableModelListener
{
	private static final long serialVersionUID = -5766496294391617615L;
	private SwingWorker<Void, Void> worker;
	private BufferedReader			in;
	private PrintWriter				out;
	private SudokuBoard				board;
	private Timer					timer;
	private JLabel					lblTimer;
	private JButton					btnStartGame;
	private JComboBox<Integer>		cbbDifficulty;

	Sudoku(JFrame parent)
	{
		this(parent, true, null, null);
	}

	/**
	 * Constructs a new instance of the Sudoku game (Server instance).
	 * 
	 */
	Sudoku(JFrame parent, boolean isHost, BufferedReader in, PrintWriter out)
	{
		super(parent, "Sudoku", true);
		setLayout(new BorderLayout(APP_PADDING, APP_PADDING));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.in = in;
		this.out = out;

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				close();
			}
		});

		setLayout(new BorderLayout(APP_PADDING, APP_PADDING));
		setResizable(false);
		setIconImages(parent.getIconImages());
		setWidgets(isHost);
		pack();
		setLocationRelativeTo(null);

		if (in != null)
		{
			worker = new SwingWorker<Void, Void>()
			{
				@Override
				protected Void doInBackground()
				{
					listen();
					return null;
				}

				@Override
				protected void done()
				{
					dispose();
				}
			};
			worker.execute();
		}

		setVisible(true);
	}

	void listen()
	{
		while (!worker.isCancelled())
		{
			try
			{
				Move move = new Move(in.readLine());

				if (move.isGameStarted())
				{
					SwingUtilities.invokeLater(() -> {
						board.setEnabled(true);
						timer.start();
					});
				}
				else if (move.isGameFinished())
				{
					worker.cancel(true);
				}
				else if (move.isGameInit())
				{
					SwingUtilities.invokeLater(() -> {
						int value = move.getValue();
						board.setValueAt(new Cell(value, value != 0), move.getRow(),
								move.getColumn());
					});
				}
				else
				{
					SwingUtilities.invokeLater(() -> board.setValueAt(new Cell(false,
							move.getValue()), move.getRow(), move.getColumn()));
				}
			}
			catch (IOException ignored)
			{
				SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
						"An error has occured whilst communicating with the remote host",
						"Error", JOptionPane.ERROR_MESSAGE));
				worker.cancel(true);
			}
		}
	}

	private void setWidgets(boolean isHost)
	{
		if (isHost)
		{
			JPanel pnlNorth = new JPanel(); // FlowLayout

			pnlNorth.add(new JLabel("Game Difficulty:"));
			cbbDifficulty = new JComboBox<>(DIFFICULTIES);
			pnlNorth.add(cbbDifficulty);

			btnStartGame = new JButton(SudokuConstants.START_GAME_ACTION);
			btnStartGame.addActionListener(this);
			pnlNorth.add(btnStartGame);

			add(pnlNorth, BorderLayout.PAGE_START);
		}

		board = new SudokuBoard(new SudokuBoardModel());
		board.getModel().addTableModelListener(this);
		board.setEnabled(false);
		add(board, BorderLayout.CENTER);

		lblTimer = new JLabel("0", SwingConstants.CENTER);
		lblTimer.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		add(lblTimer, BorderLayout.PAGE_END);

		timer = new Timer(1000, this);
		timer.setActionCommand(SudokuConstants.TIMER_ACTION);
	}

	/**
	 * Closes the Sudoku window disposing of its resources.
	 */
	void close()
	{
		int userResponse = JOptionPane.showConfirmDialog(this,
				"Are you sure you would like to quit the current session?",
				"Confirm Action", JOptionPane.YES_NO_OPTION);

		if (userResponse == JOptionPane.YES_OPTION)
		{
			if (out == null)
			{
				dispose();
			}
			else
			{
				out.println(new Move(GAME_ENDED));
				worker.cancel(true);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case START_GAME_ACTION:
				startGame();
				break;
			case TIMER_ACTION:
				lblTimer.setText(String.valueOf(Integer.parseInt(lblTimer.getText()) + 1));
				break;
		}
	}

	/**
	 * 
	 */
	private void startGame()
	{
		((SudokuBoardModel) board.getModel()).setBoard(cbbDifficulty
				.getItemAt(cbbDifficulty.getSelectedIndex()));
		timer.start();
		flickButtons();

		if (out != null)
		{
			out.println(new Move(GAME_STARTED));

			for (int i = 0; i < BOARD_SIZE; i++)
			{
				for (int j = 0; j < BOARD_SIZE; j++)
				{
					int value = ((Cell) board.getValueAt(i, j)).getValue();
					out.println(new Move(value, i, j, GAME_INIT));
				}
			}
		}
	}

	@Override
	public void tableChanged(TableModelEvent e)
	{
		if (out != null)
		{
			int row = e.getLastRow();
			int col = e.getColumn();
			Cell cell = (Cell) board.getValueAt(row, col);

			if (cell.isHost())
			{
				out.println(new Move(cell.getValue(), row, col));
			}
		}

		if (((SudokuBoardModel) e.getSource()).isWon())
		{
			board.setEnabled(false);
			timer.stop();
			JOptionPane.showMessageDialog(this,
					"Sudoku board solved in " + lblTimer.getText() + " seconds!");
		}
	}

	/**
	 * 
	 */
	private void flickButtons()
	{
		btnStartGame.setEnabled(!btnStartGame.isEnabled());
		cbbDifficulty.setEnabled(!cbbDifficulty.isEnabled());
		board.setEnabled(!board.isEnabled());
	}
}