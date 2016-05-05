package com.pearisgreen.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable, Serializable
{	
	private int port;
	private String ipAdress;
	
	private Thread thread;
	
	private boolean listening = false; 
	
	private ServerSocket serverSocket;
	
	private HashMap<Integer, SClient> clients = new HashMap<Integer, SClient>();
	
	public Server(int port)
	{
		this.port = port;
		
		try
		{
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e)
		{
			System.out.println("couldnt host server on port: " + port);
			e.printStackTrace();
		}
	}
	
	public void startListening()
	{
		System.out.println("starting to listen on port: " + port);
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		listening = true;
		
		int clientCount = 0;
		
		while(listening)
		{
			ClientGroup clientGroup = new ClientGroup();
			
			SClient client = new SClient(this,clientGroup, clientCount);
			clientGroup.addClient(client);
			
			if(client.bindClient())
			{	
				System.out.println("client connected");
				
				client.start();

				clients.put(clientCount, client);

				clientCount++;
			}
		}
	}
	
	private void stop()
	{
		listening = false;
		
		for(Map.Entry<Integer, SClient> entry : clients.entrySet())
		{
			entry.getValue().stop();
		}
		
		try
		{
			thread.join();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addClients(int acceptor, int donor)
	{
		clients.get(acceptor).getClientGroup().addClient(clients.get(donor));
	}
	
	public void removeClient(int id)
	{
		clients.get(id).stop();
		
		clients.remove(id);
	}
	
	public Socket acceptClientSocket()
	{
		try
		{
			return serverSocket.accept();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<SClient> getClients(SClient.State state)
	{
		ArrayList<SClient> tempArrayList = new ArrayList<SClient>();
		
		for(Map.Entry<Integer, SClient> entry : clients.entrySet())
		{
			if(entry.getValue().getState() == state)
				tempArrayList.add(entry.getValue());
		}
		
		return tempArrayList;
	}
	
	public String getClientsString(SClient.State state)
	{
		String tempStr = "";
		 
		ArrayList<SClient> tempArrayList = getClients(state);
		
		for(SClient cl : tempArrayList)
		{
			tempStr = tempStr + cl.getName() + " - ";
		}
		
		return tempStr;
	}
	
	public void connectWithRandom(SClient cl)
	{
		for(Map.Entry<Integer, SClient> entry : clients.entrySet())
		{
			if(entry.getValue().getState() == SClient.State.LFM && entry.getValue() != cl)
			{
				entry.getValue().getClientGroup().addClient(cl);
			}
		}
	}
}
