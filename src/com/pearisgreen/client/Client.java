package com.pearisgreen.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Stack;

import com.pearisgreen.server.SClient;
import com.pearisgreen.server.Server;

public abstract class Client implements Runnable, Serializable
{
	private transient Socket socket;
	
	private boolean listening = false;
	
	private String name;
	
	private String ipAdress;
	private int port;
	
	private transient ObjectOutputStream out;
	private transient ObjectInputStream in;
	
	private transient volatile Stack<SocketMethod> messageStack = new Stack<SocketMethod>();
	
	private transient Thread thread;
	
	public Client(String name, String ipAdress, int port)
	{
		this.name = name;
		this.ipAdress = ipAdress;
		this.port = port;
	}
	
	protected abstract void processString(String name, String str);
	
	protected abstract void processInteger(String name, int num);
	
	protected abstract void processDouble(String name, double num);
	
	protected abstract void processObject(String name, DataObject obj);

	
	public boolean connectToServer()
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
			
			System.out.println("couldnt bin to server");
			
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
		sendToServer(new SocketMethod()
		{

			@Override
			public void apply(Client cl)
			{
				cl.processObject(cl.getName(), obj);
			}
			
		});
	}
	
	public void sendToServer(double num)
	{
		sendToServer(new SocketMethod()
		{

			@Override
			public void apply(Client cl)
			{
				cl.processDouble(cl.getName(), num);
			}
			
		});
	}
	
	public void sendToServer(int num)
	{
		sendToServer(new SocketMethod()
		{

			@Override
			public void apply(Client cl)
			{
				cl.processInteger(cl.getName(), num);
			}
			
		});
	}
	
	public void sendToServer(String str)
	{
		sendToServer(new SocketMethod()
		{

			@Override
			public void apply(Client cl)
			{
				cl.processString(cl.getName(), str);
			}
			
		});
	}
	
	private void sendToServer(SocketMethod sm)
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
	////////////////////////////////////////
	
	/*
	 *	SERVER COMMANDS
	 */
	
	
	public enum Scommand
	{
		SETNAME(new SFunction()
		{
			@Override
			public void apply(SClient scl, Server sc)
			{
				scl.setName("test");
			}	
		}),
		
		CONNECTTORANDOM(new SFunction()
		{
			@Override
			public void apply(SClient scl, Server sv)
			{
				sv.connectWithRandom(scl);
			}
		});
		
		private SFunction sf;
		
		Scommand(SFunction ssf)
		{
			sf = ssf;
		}
		
	}
	
	public void commandServer(Scommand sc)
	{
		sendToServer(new SocketMethod()
		{
			@Override
			public void apply(Client cl)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean serverCommand(SClient scl, Server sv)
			{	
				sc.sf.apply(scl, sv);
				
				return true;
			}
		});
	}
	/////////////////////////////////////////////

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
				
				messageStack.push(sm);
				
			} catch (IOException | ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected synchronized void popStack()	
	{
		if(!messageStack.empty())
			messageStack.pop().apply(this);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
