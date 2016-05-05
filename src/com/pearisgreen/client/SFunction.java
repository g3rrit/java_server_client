package com.pearisgreen.client;

import java.io.Serializable;

import com.pearisgreen.server.SClient;
import com.pearisgreen.server.Server;

public interface SFunction extends Serializable
{
	public abstract void apply(SClient scl, Server sv);
}
