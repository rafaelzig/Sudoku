package tsinghua.softdev.rafael.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * A subclass of Window which adds functionality to display progress bars and
 * animated loading gifs.
 *
 * @author Rafael da Silva Costa - 2015280364
 */
class LoadingWindow extends Window
{
	private static final long serialVersionUID = 1461280336148445723L;
	/**
	 * Progress bar utilised on this window.
	 */
	private JProgressBar progressBar;

	/**
	 * Constructs a new instance of LoadingWindow to display an animated GIF
	 * with the specified parent and title.
	 *
	 * @param owner : Window from which the dialog is displayed.
	 * @param title : Title of this window.
	 */
	LoadingWindow(Window owner, String title)
	{
		super(owner);
		setLayout(new BorderLayout(5, 5));

		JLabel lblTitle = new JLabel(new ImageIcon(getClass().getResource("res/loading.gif")));
		lblTitle.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createRaisedBevelBorder(), title, TitledBorder.CENTER,
				TitledBorder.TOP, new Font("Dialog", Font.BOLD, 9), Color.BLACK));

		add(lblTitle);
		setVisibility();
	}

	/**
	 * Constructs a new instance of LoadingWindow to display a progress bar with
	 * the specified parent, title and max progress.
	 *
	 * @param parent : Parent window from which this object originates.
	 * @param title  : Title of this window.
	 * @param max    : Max progress utilised by the progress bar.
	 */
	LoadingWindow(Window parent, String title, int max)
	{
		super(parent);
		progressBar = new JProgressBar(0, max);
		progressBar.setStringPainted(true);

		setLayout(new BorderLayout(5, 5));
		JPanel pnlLoadingBar = new JPanel();
		pnlLoadingBar.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createRaisedBevelBorder(), title, TitledBorder.CENTER,
				TitledBorder.TOP, new Font("Dialog", Font.BOLD, 9), Color.BLACK));
		pnlLoadingBar.add(progressBar);
		add(pnlLoadingBar);

		setVisibility();
	}

	/**
	 * Sets the progress of the progress bar to the specified level.
	 *
	 * @param progress : Progress level to be set.
	 */
	void setProgress(int progress)
	{
		progressBar.setValue(progress);
	}

	/**
	 * Sets the visibility of the LoadingWindow.
	 */
	private void setVisibility()
	{
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}