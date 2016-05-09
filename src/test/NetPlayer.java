package test;

import com.pear.core.Renderer;
import com.pear.drawablegeometry.DCircle;
import com.pear.fx.drawables.Drawable;
import com.pear.geometry.Point;
import com.pearisgreen.client.Client;
import com.pearisgreen.client.DataObject;
import com.pearisgreen.server.ClientIdentifier;

public class NetPlayer extends Client implements Drawable
{
	private int color;
	
	private int xpos;
	private int ypos;
	
	private DCircle circle;
	
	public NetPlayer(int color, int xpos, int ypos)
	{
		super("territ");
		
		this.color = color;
		
		this.xpos = xpos;
		this.ypos = ypos;
		
		this.circle = new DCircle(new Point(xpos,ypos), 5, color);
	}
	
	public void startListening()
	{
		connectToServer("127.0.0.1", 14000);
		start();
	}
	
	public void connectWithRandom()
	{
		commandServerConnectToRandom();
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
	protected void processObject(DataObject obj)
	{
		System.out.println(obj.getClientIdentifier().name);
		
		setXpos(((DataPoint) obj).x);
		setYpos(((DataPoint) obj).y);
	}
}
