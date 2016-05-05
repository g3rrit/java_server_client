package test;

import java.io.Serializable;

import com.pearisgreen.client.DataObject;

public class DataPoint extends DataObject implements Serializable
{
	public int x;
	public int y;
	
	public DataPoint(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
