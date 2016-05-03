package com.pearisgreen.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client implements Runnable
{
	private Server server;
	
	private ClientGroup clientGroup;

	private Thread thread;
	
	private Socket socket;
	private int ID;
	
	private InputStream in;
	private OutputStream out;
	
	private volatile boolean listening = false;
	
	public Client(Server server, ClientGroup clientGroup, int ID)
	{
		this.server = server;
		
		this.clientGroup = clientGroup;
		
		this.ID = ID;
	}
	
	public boolean bindClient()
	{
		this.socket = server.acceptClientSocket();
		
		if(socket != null)
		{
			try
			{
				in = socket.getInputStream();
				out = socket.getOutputStream();
			} catch (IOException e)
			{
				e.printStackTrace();
				
				return false;
			}
			
			return true;
		}
		return false;
	}
	
	public void start()
	{
		thread = new Thread(this);
		
		thread.start();
	}
	
	public void stop()
	{
		listening = false;
		
		try
		{
			thread.join();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
		{
			socket.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server.removeClient(ID);
		clientGroup.removeClient(this);
		
		System.out.println("client cleaned up");
	}

	@Override
	public void run()
	{
		listening = true;
		
		Byte by = 0;
		
		while(listening)
		{
			try
			{
				by = (byte) in.read();
				
				System.out.println("receiving message");
				System.out.println(by);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//check here for commands
			
			clientGroup.sendToClients(this, by);
		}
	}
	
	public boolean sendToClient(Byte by)
	{
		//byte[] by = message.getBytes();
		
		try
		{
			out.write(by);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println("couldnt send to client");
			
			return false;
		}
		
		return true;
	}

	public void setClientGroup(ClientGroup clientGroup)
	{
		this.clientGroup = clientGroup;
	}
	
	public ClientGroup getClientGroup()
	{
		return clientGroup;
	}

}
