package test;

import com.pear.core.Renderer;
import com.pear.drawablegeometry.DCircle;
import com.pear.fx.drawables.Drawable;
import com.pear.geometry.Point;

public class Player implements Drawable
{
	private int color;
	
	private int xpos;
	private int ypos;
	
	private DCircle circle;
	
	public Player(int color, int xpos, int ypos)
	{
		this.color = color;
		
		this.xpos = xpos;
		this.ypos = ypos;
		
		this.circle = new DCircle(new Point(xpos,ypos), 5, color);
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
}
