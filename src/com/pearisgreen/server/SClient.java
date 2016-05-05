package com.pearisgreen.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import com.pearisgreen.client.SocketMethod;

public class SClient implements Runnable, Serializable
{
	
	public enum State
	{
		LFG, 
		LFM,
		GROUPED
	};
	
	private State state = State.LFM;
	
	private Server server;
	
	private ClientGroup clientGroup;

	private Thread thread;
	
	private Socket socket;
	private int ID;
	
	private String name = "null";
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private volatile boolean listening = false;
	
	public SClient(Server server, ClientGroup clientGroup, int ID)
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
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
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
		
		SocketMethod sm;
		
		while(listening)
		{
			try
			{
				sm = (SocketMethod) in.readObject();
				
				if(!sm.serverCommand(this, server))
				{
					clientGroup.sendToClients(this, sm);
				}
				
			} catch (IOException | ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
	}
	
	public void sendToClient(SocketMethod sm)
	{
		try
		{
			out.writeObject(sm);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void setClientGroup(ClientGroup clientGroup)
	{
		this.clientGroup = clientGroup;
	}
	
	public ClientGroup getClientGroup()
	{
		return clientGroup;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

}
