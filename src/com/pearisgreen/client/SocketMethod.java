package com.pearisgreen.client;

import java.io.Serializable;

import com.pearisgreen.server.SClient;
import com.pearisgreen.server.Server;

public interface SocketMethod extends Serializable
{
	public abstract void apply(Client cl);
	
	//if this returns true the applay will not be sent
	public default boolean serverCommand(SClient scl, Server sv)
	{	
		return false;
	}
}
