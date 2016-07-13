package com.pearisgreen.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Stack;

import com.pearisgreen.client.ServerCommand.Command;
import com.pearisgreen.server.ClientIdentifier;
import com.pearisgreen.server.SClient;
import com.pearisgreen.server.Server;

public abstract class Client implements Runnable, Serializable
{
	private Socket socket;
	
	private boolean listening = false;
	
	private String name;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private volatile Stack<DataObject> messageStack = new Stack<DataObject>();
	
	private Thread thread;
	
	public Client(String name)
	{
		this.name = name;
	}
	
	protected abstract void processObject(DataObject obj);

	
	public boolean connectToServer(String ipAdress, int port)
	{
		try
		{
			socket = new Socket(ipAdress, port);
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println("couldnt bind to server");
			
			return false;
		}
		
		System.out.println("client bound to server");
		
		return true;
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
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try
		{
			in.close();
			out.close();
			socket.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("client cleaned up");
	}
	
	/*
	 * SEND INFORMATION TO OTHER CLIENTS
	 */
	public void sendToServer(DataObject obj)
	{
		try
		{
			out.writeObject(obj);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("couldnt send dataobject to server");
		}
	}
	////////////////////////////////////////
	
	/*
	 *	SERVER COMMANDS
	 */
	
	public void commandServerConnectToRandom()
	{
		sendToServer(new ServerCommand(ServerCommand.Command.CONNECTWITHRANDOM));
	}
	/*
	public void commandServerSetName(String name)
	{
		commandServer(new SFunction()
		{
			@Override
			public void apply(SClient scl, Server sv)
			{
				scl.setName(name);
			}	
		});
	}
	
	private void commandServer(SFunction sf)
	{
		sendToServer(new SocketMethod()
		{
			@Override
			public void apply(Client cl)
			{
				// dont do anything here
			}
			
			@Override
			public boolean serverCommand(SClient scl, Server sv)
			{	
				sf.apply(scl, sv);
				
				return true;
			}
		});
	}*/
	/////////////////////////////////////////////

	@Override
	public void run()
	{
		listening = true;
		
		DataObject dob = null;
		
		while(listening)
		{	
			try
			{
				dob = (DataObject) in.readObject();
				
				messageStack.push(dob);
				
			} catch (IOException | ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				stop();
			}
		}
	}
	
	/*
	
	private boolean sendAndReceiveCI()
	{
		commandServerSetName(clientIdentifier.name);
		
		//get ClientIdentifier from server
		SocketMethod sm = null;
		try
		{
			sm = (SocketMethod) in.readObject();
		} catch (IOException | ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
		sm.apply(this);
		
		return true;
	}*/
	
	protected synchronized void popStack()	
	{
		while(!messageStack.empty())
			processObject(messageStack.pop());
	}

	public String getName()
	{
		return this.name;
	}
}
