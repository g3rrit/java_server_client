package com.pearisgreen.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import com.pearisgreen.client.DataObject;
import com.pearisgreen.client.ServerCommand;

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
	
	private ClientIdentifier clientIdentifier = new ClientIdentifier("nll", 0);
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private volatile boolean listening = false;
	
	public SClient(Server server, ClientGroup clientGroup, long ID)
	{
		this.server = server;
		
		this.clientGroup = clientGroup;
		
		this.clientIdentifier = new ClientIdentifier("nll", ID);
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
		
		server.removeClient(clientIdentifier.ID);
		clientGroup.removeClient(this);
		
		System.out.println("client cleaned up");
	}

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
				
				if(!dob.isServerCommand())
				{
					dob.setClientIdentifier(this.clientIdentifier);
					
					clientGroup.sendToClients(this, dob);
				}
				else
				{
					ServerCommand sec = (ServerCommand) dob;
					
					switch(sec.getCommand())
					{
					case CONNECTWITHRANDOM:
						server.connectWithRandom(this);
						break;
					}
				}
				
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
		SocketMethod sm = null;
		try
		{
			sm = (SocketMethod) in.readObject();
			
			sm.serverCommand(this, server);
			
		} catch (ClassNotFoundException | IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			return false;
		}
		
		sendToClient(new SocketMethod()
		{
			@Override
			public void apply(Client cl)
			{
				cl.setClientIdentifier(clientIdentifier);
			}	
		});
		
		return true;
	}*/
	
	public void sendToClient(DataObject dob)
	{
		try
		{
			out.writeObject(dob);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			stop();
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
		return this.clientIdentifier.name;
	}

	public void setName(String name)
	{
		this.clientIdentifier.name = name;
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
