package com.pearisgreen.server;

import java.io.Serializable;

public class ClientIdentifier implements Serializable
{
	public String name;
	public long ID;
	
	public ClientIdentifier(String name, long ID)
	{
		this.name = name;
		this.ID = ID;
	}
}
