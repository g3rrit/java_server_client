package com.pearisgreen.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Stack;

public class Client implements Runnable
{
	private Socket socket;
	
	private boolean listening = false;
	
	private String ipAdress;
	private int port;
	
	private InputStream in;
	private OutputStream out;
	
	private volatile Stack<Byte> messageStack = new Stack<Byte>();
	
	Thread thread;
	
	public Client(String ipAdress, int port)
	{
		this.ipAdress = ipAdress;
		this.port = port;
		
	}
	
	public boolean connectToServer()
	{
		try
		{
			socket = new Socket(ipAdress, port);
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
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
	
	public void sendToServer(byte by)
	{
		try
		{
			out.write((byte) by);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println("couldnt send to server");
		}
	}

	@Override
	public void run()
	{
		listening = true;
		
		while(listening)
		{
			//delete this later
			System.out.println("receiving message");
			
			try
			{
				pushStack((byte)in.read());
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private synchronized void pushStack(Byte by)
	{
		messageStack.push(by);
	}
	
	public synchronized Byte[] getStack()
	{
		int size = messageStack.size();
		
		Byte[] byA = new Byte[size];
		
		for(int i = 0; i < size; i++)
		{
			byA[i] = messageStack.pop();
		}
		
		return byA;
	}
}
