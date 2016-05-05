package test;

import com.pear.core.Renderer;
import com.pear.drawablegeometry.DCircle;
import com.pear.fx.drawables.Drawable;
import com.pear.geometry.Point;
import com.pearisgreen.client.Client;
import com.pearisgreen.client.DataObject;

public class NetPlayer extends Client implements Drawable
{
	private int color;
	
	private int xpos;
	private int ypos;
	
	private DCircle circle;
	
	public NetPlayer(int color, int xpos, int ypos)
	{
		super("gerrit", "127.0.0.1", 14000);
		
		this.color = color;
		
		this.xpos = xpos;
		this.ypos = ypos;
		
		this.circle = new DCircle(new Point(xpos,ypos), 5, color);
	}
	
	public void startListening()
	{
		connectToServer();
		start();
	}
	
	public void connectWithRandom()
	{
		commandServer(Client.Scommand.CONNECTTORANDOM);
	}
	
	public void update(float dt)
	{
		//by popping the stack methods are called
		popStack();
	}
	
	@Override
	public void draw(Renderer r)
	{
		r.draw(circle);
	}

	public int getXpos()
	{
		return xpos;
	}

	public void setXpos(int xpos)
	{
		this.xpos = xpos;
		
		this.circle.setP0(new Point(xpos, ypos));
	}

	public int getYpos()
	{
		return ypos;
	}

	public void setYpos(int ypos)
	{
		this.ypos = ypos;
		
		this.circle.setP0(new Point(xpos, ypos));
	}
	
	public void sendPosOtherPlayer(DataPoint p)
	{
		sendToServer(p);
	}

	@Override
	protected void processString(String name, String str)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processInteger(String name, int num)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processDouble(String name, double num)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processObject(String name, DataObject obj)
	{
		setXpos(((DataPoint) obj).x);
		setYpos(((DataPoint) obj).y);
	}
}
