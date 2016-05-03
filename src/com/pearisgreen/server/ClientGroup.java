package com.pearisgreen.server;

import java.util.ArrayList;

public class ClientGroup
{
	public Server server;
	
	public ArrayList<Client> clients = new ArrayList<Client>();
	
	public ClientGroup()
	{
		
	}
	
	public void addClient(Client cl)
	{
		cl.setClientGroup(this);
		
		clients.add(cl);
	}
	
	public void removeClient(Client cl)
	{
		clients.remove(cl);
	}
	
	public synchronized void sendToClients(Client sender, Byte by)
	{
		for(Client cl : clients)
		{	
			if(cl != sender)
			{
				cl.sendToClient(by);
			}
			
			System.out.println("test");
		}
	}
}
