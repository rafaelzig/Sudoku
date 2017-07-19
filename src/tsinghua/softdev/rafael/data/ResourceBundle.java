package tsinghua.softdev.rafael.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ResourceBundle
{
	private ServerSocket	serverSocket;
	private Socket			socket;
	private BufferedReader	in;
	private PrintWriter		out;

	public ResourceBundle()
	{
		super();
	}

	public ResourceBundle(ServerSocket serverSocket, Socket socket, BufferedReader in,
			PrintWriter out)
	{
		super();
		this.serverSocket = serverSocket;
		this.socket = socket;
		this.in = in;
		this.out = out;
	}

	/**
	 * @return the serverSocket
	 */
	public ServerSocket getServerSocket()
	{
		return serverSocket;
	}

	/**
	 * @param serverSocket
	 *            the serverSocket to set
	 */
	public void setServerSocket(ServerSocket serverSocket)
	{
		this.serverSocket = serverSocket;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket()
	{
		return socket;
	}

	/**
	 * @param socket
	 *            the socket to set
	 */
	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}

	public BufferedReader getReader()
	{
		return in;
	}

	/**
	 * @param in
	 *            the in to set
	 */
	public void setReader(BufferedReader in)
	{
		this.in = in;
	}

	public PrintWriter getWriter()
	{
		return out;
	}

	public void setWriter(PrintWriter out)
	{
		this.out = out;
	}

	/**
	 * TODO
	 * 
	 * @throws IOException
	 */
	public void closeAll() throws IOException
	{
		if (in != null)
			in.close();

		if (out != null)
			out.close();

		if (socket != null)
			socket.close();

		if (serverSocket != null)
			serverSocket.close();
	}

	/**
	 * TODO
	 * 
	 * @throws IOException
	 */
	public void closeAllQuietly()
	{
		if (in != null)
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
				// TODO Log in error text file
				e.printStackTrace();
			}
		}

		if (out != null)
			out.close();

		if (socket != null)
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				// TODO Log in error text file
				e.printStackTrace();
			}
		}

		if (serverSocket != null)
		{
			try
			{
				serverSocket.close();
			}
			catch (IOException e)
			{
				// TODO Log in error text file
				e.printStackTrace();
			}
		}
	}
}