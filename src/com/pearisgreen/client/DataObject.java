package com.pearisgreen.client;

import java.io.Serializable;

import com.pearisgreen.server.ClientIdentifier;

public abstract class DataObject implements Serializable
{
	private boolean serverCommand = false;
	
	private ClientIdentifier clientIdentifier;
	
	protected void setServerCommand(boolean serverCommand)
	{
		this.serverCommand = serverCommand;
	}
	
	public boolean isServerCommand()
	{
		return serverCommand;
	}

	public ClientIdentifier getClientIdentifier()
	{
		return clientIdentifier;
	}

	public void setClientIdentifier(ClientIdentifier clientIdentifier)
	{
		this.clientIdentifier = clientIdentifier;
	}
	
}
