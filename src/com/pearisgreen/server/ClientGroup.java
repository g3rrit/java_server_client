package com.pearisgreen.server;

import java.io.Serializable;
import java.util.ArrayList;

import com.pearisgreen.client.SocketMethod;

public class ClientGroup implements Serializable
{
	public Server server;
	
	public ArrayList<SClient> clients = new ArrayList<SClient>();
	
	public ClientGroup()
	{
		
	}
	
	public void addClient(SClient cl)
	{
		cl.setClientGroup(this);
		
		clients.add(cl);
	}
	
	public void removeClient(SClient cl)
	{
		clients.remove(cl);
	}
	
	public synchronized void sendToClients(SClient sender, SocketMethod sm)
	{
		System.out.println("conn clients: " + clients.size());
		
		for(SClient cl : clients)
		{	
			if(cl != sender)
			{
				cl.sendToClient(sm);
			}
		}
	}
}
