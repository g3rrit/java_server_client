package com.pearisgreen.client;

import com.pearisgreen.server.SClient;
import com.pearisgreen.server.Server;

public class ServerCommand extends DataObject
{
	public enum Command
	{
		SETNAME, //uses string0
		CONNECTWITHRANDOM;
	}
	
	public Command command;
	
	private String string0;
	private String string1;
	
	private int int0;
	private int int1;
	
	public ServerCommand(Command command)
	{
		super.setServerCommand(true);
		
		this.command = command;
	}

	public Command getCommand()
	{
		return command;
	}

	public String getString0()
	{
		return string0;
	}

	public void setString0(String string0)
	{
		this.string0 = string0;
	}

	public String getString1()
	{
		return string1;
	}

	public void setString1(String string1)
	{
		this.string1 = string1;
	}

	public int getInt0()
	{
		return int0;
	}

	public void setInt0(int int0)
	{
		this.int0 = int0;
	}

	public int getInt1()
	{
		return int1;
	}

	public void setInt1(int int1)
	{
		this.int1 = int1;
	}
	
}
