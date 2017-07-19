package tsinghua.softdev.rafael.gui;

import javax.swing.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

class ExceptionHandler
{
	StringBuilder msg = new StringBuilder();
	JFrame parent;
	
	ExceptionHandler(JFrame parent)
	{
		super();
		this.parent = parent;
	}

	void handle(Exception e)
	{
		msg.delete(0, msg.length());
		Throwable cause = e.getCause();
		
		if (e instanceof InterruptedException)
		{
			msg.append("An error occured when finalizing the session:\n")
			.append(cause != null ? cause.getMessage() : e.getMessage());
		}
		else if (e instanceof ExecutionException)
		{
			msg.append("The connection ended abnormally:\n")
			.append(cause != null ? cause.getMessage() : e.getMessage());
		}
		else if (e instanceof UnknownHostException)
		{
			msg.append("The specified host address could not be determined:\n")
					.append(cause != null ? cause.getMessage() : e.getMessage());
		}
		else if (e instanceof IOException)
		{
			msg.append("An error occured when setting up the session:\n")
			.append(cause != null ? cause.getMessage() : e.getMessage());
		}
		
		JOptionPane.showMessageDialog(parent, msg.toString(), "Error",
				JOptionPane.ERROR_MESSAGE);
	}
}