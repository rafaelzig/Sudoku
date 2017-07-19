package tsinghua.softdev.rafael.gui;

import tsinghua.softdev.rafael.data.ResourceBundle;
import tsinghua.softdev.rafael.data.SudokuConstants;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
class Dashboard extends JFrame implements SudokuConstants, ActionListener
{
	private JFormattedTextField[]				txtHostIP;
	private JFormattedTextField					txtHostPort, txtMyPort;
	private JButton								btnHost, btnJoin;
	private LoadingWindow						loading;
	private SwingWorker<ResourceBundle, Void>	worker;
	private Timer								timer;
	private ExceptionHandler					handler;

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(Dashboard::new);
	}

	/**
	 * Constructor - Initialises the frame and its widgets, ultimately
	 * displaying itself to the user.
	 */
	Dashboard()
	{
		super("Sudoku");
		handler = new ExceptionHandler(this);
		setLayout(new BorderLayout(APP_PADDING, APP_PADDING));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setIconImages(getIcons());
		setWidgets();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void setWidgets()
	{
		JPanel pnlNorth = new JPanel(); // FlowLayout
		JPanel pnlSouth = new JPanel(); // FlowLayout
		JPanel pnlEast = new JPanel(new GridLayout(2, 1, APP_PADDING, APP_PADDING));
		JPanel pnlWest = new JPanel(new BorderLayout(APP_PADDING, APP_PADDING)); // FlowLayout
		JPanel pnlCenter = new JPanel(new BorderLayout(APP_PADDING, APP_PADDING));

		pnlNorth.setBorder(BorderFactory.createTitledBorder("Host a new game"));
		pnlSouth.setBorder(BorderFactory.createTitledBorder("Join an existing game"));
		pnlWest.setBorder(BorderFactory.createTitledBorder("Single player game"));

		pnlNorth.add(new JLabel("Port:"));

		NumberFormatter portFormatter = new NumberFormatter(new DecimalFormat("#####"));
		portFormatter.setMinimum(SudokuConstants.MIN_PORT);
		portFormatter.setMaximum(SudokuConstants.MAX_PORT);

		txtMyPort = new JFormattedTextField(portFormatter);
		txtMyPort.setColumns(5);
		txtMyPort.setValue(DEFAULT_PORT);
		pnlNorth.add(txtMyPort);

		pnlSouth.add(new JLabel("Address:"));

		NumberFormatter ipFormatter = new NumberFormatter(new DecimalFormat("###"));
		ipFormatter.setMinimum(0);
		ipFormatter.setMaximum(255);

		txtHostIP = new JFormattedTextField[4];

		for (int i = 0; i < txtHostIP.length; i++)
		{
			txtHostIP[i] = new JFormattedTextField(ipFormatter);
			txtHostIP[i].setColumns(3);
			txtHostIP[i].setValue(DEFAULT_IP[i]);
			pnlSouth.add(txtHostIP[i]);

			if (i < txtHostIP.length - 1)
				pnlSouth.add(new JLabel("."));
			else
				pnlSouth.add(new JLabel(":"));
		}

		txtHostPort = new JFormattedTextField(portFormatter);
		txtHostPort.setColumns(5);
		txtHostPort.setValue(DEFAULT_PORT);
		pnlSouth.add(txtHostPort);

		btnHost = new JButton(HOST_ACTION);
		btnHost.addActionListener(this);

		timer = new Timer(TIMEOUT, this);
		timer.setActionCommand(TIMEOUT_ACTION);
		timer.setRepeats(false);

		btnJoin = new JButton(JOIN_ACTION);
		btnJoin.addActionListener(this);

		pnlEast.add(btnHost);
		pnlEast.add(btnJoin);

		pnlWest.add(new JLabel("AI Level"), BorderLayout.WEST);
		
		JRadioButton rbtNormal = new JRadioButton("Normal");
		rbtNormal.setEnabled(false); // Not yet implemented
		pnlWest.add(rbtNormal, BorderLayout.CENTER);
		
		JRadioButton rbtSmart = new JRadioButton("Smart");
		rbtSmart.setEnabled(false); // Not yet implemented
		pnlWest.add(rbtSmart, BorderLayout.EAST);
		
		JButton btnStartGame = new JButton(START_GAME_ACTION);
		btnStartGame.addActionListener(this);
		pnlWest.add(btnStartGame, BorderLayout.SOUTH);

		pnlCenter.add(pnlNorth, BorderLayout.NORTH);
		pnlCenter.add(pnlSouth, BorderLayout.SOUTH);
		add(pnlCenter, BorderLayout.CENTER);
		add(pnlEast, BorderLayout.EAST);
		add(pnlWest, BorderLayout.WEST);
	}

	/**
	 * Loads the icons from resources.
	 * 
	 * @return List of icons to be used in the application.
	 */
	private List<Image> getIcons()
	{
		List<Image> icons = new LinkedList<Image>();

		for (String path : ICONS)
		{
			icons.add(new ImageIcon(getClass().getResource(path)).getImage());
		}

		return icons;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case START_GAME_ACTION:
				new Sudoku(this);
				break;
			case HOST_ACTION:
				toggleButtons(HOST_ACTION);
				hostGame();
				break;
			case CANCEL_ACTION:
				worker.cancel(true);
				timer.start();
				break;
			case JOIN_ACTION:
				toggleButtons(JOIN_ACTION);
				joinGame();
				break;
			case TIMEOUT_ACTION:
				enableButtons();
				break;
		}
	}

	private void hostGame()
	{
		loading = new LoadingWindow(this, "Listening");

		worker = new SwingWorker<ResourceBundle, Void>()
		{
			@Override
			public ResourceBundle doInBackground()
			{
				ResourceBundle b = new ResourceBundle();
				
				try
				{
					b.setServerSocket(new ServerSocket((int) txtMyPort.getValue()));
					b.getServerSocket().setSoTimeout(TIMEOUT);

					while (!isCancelled())
					{
						try
						{
							b.setSocket(b.getServerSocket().accept());
							b.setWriter(new PrintWriter(b.getSocket().getOutputStream(), true));
							b.setReader(new BufferedReader(new InputStreamReader(b.getSocket().getInputStream())));

							return b;
						}
						catch (SocketTimeoutException e)
						{
							// Retry until user cancels or connection
							// established
						}
					}
					
					b.closeAll();
				}
				catch (IOException e)
				{
					b.closeAllQuietly();
					cancel(true);
					SwingUtilities.invokeLater(() -> {
						timer.start();
						handler.handle(e);
					});
				}

				return null;
			}

			@Override
			protected void done()
			{
				loading.dispose();
				toggleButtons(HOST_ACTION);

				if (!isCancelled())
				{
					try
					{
						ResourceBundle b = get();
						new Sudoku(Dashboard.this, true, b.getReader(), b.getWriter());
						b.closeAll();
						enableButtons();
					}
					catch (InterruptedException | ExecutionException | IOException  e)
					{
						handler.handle(e);
					}
				}
			}
		};

		worker.execute();
	}

	private void joinGame()
	{
		// Convert elements to strings and concatenate them, separated by commas
		String host = Arrays.stream(txtHostIP).map(c -> c.getValue().toString())
				.collect(Collectors.joining("."));

		loading = new LoadingWindow(this, "Connecting");

		worker = new SwingWorker<ResourceBundle, Void>()
		{
			@Override
			public ResourceBundle doInBackground()
			{
				ResourceBundle b = new ResourceBundle();
				
				try
				{
					b.setSocket(new Socket(host, (int) txtHostPort.getValue()));
					b.setWriter(new PrintWriter(b.getSocket().getOutputStream(), true));
					b.setReader(new BufferedReader(new InputStreamReader(b.getSocket().getInputStream())));
					
					return b;
				}
				catch (IOException e)
				{
					b.closeAllQuietly();
					cancel(true);
					SwingUtilities.invokeLater(() -> {
						timer.start();
						handler.handle(e);
					});
				}

				return null;
			}

			@Override
			protected void done()
			{
				loading.dispose();
				toggleButtons(JOIN_ACTION);

				if (!isCancelled())
				{
					try
					{
						ResourceBundle b = get();
						new Sudoku(Dashboard.this, false, b.getReader(), b.getWriter());
						b.closeAll();
						enableButtons();
					}
					catch (InterruptedException | ExecutionException | IOException  e)
					{
						handler.handle(e);
					}
				}
			}
		};

		worker.execute();
	}

	private void toggleButtons(String action)
	{
		if (action.equals(HOST_ACTION))
		{
			String curr = btnHost.getText();

			if (curr.equals(HOST_ACTION))
			{
				btnHost.setText(CANCEL_ACTION);
				btnJoin.setEnabled(false);
			}
			else if (curr.equals(CANCEL_ACTION))
			{
				btnHost.setText(HOST_ACTION);
				btnHost.setEnabled(false);
			}
		}
		else
		{
			btnJoin.setEnabled(false);
			btnHost.setEnabled(false);
		}
	}

	/**
	 * 
	 */
	private void enableButtons()
	{
		btnHost.setEnabled(true);
		btnJoin.setEnabled(true);
	}
}