package test;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.pear.core.AbstractGame;
import com.pear.core.GameContainer;
import com.pear.core.Input;
import com.pear.core.Renderer;
import com.pear.core.fx.ShadowType;
import com.pear.fx.drawables.Button;
import com.pear.fx.drawables.ButtonSprite;
import com.pear.geometry.Point;
import com.pearisgreen.client.Client;
import com.pearisgreen.server.Server;

public class Game extends AbstractGame
{
	private GameContainer gc;
	
	public static int WIDTH = 400;
	public static int HEIGHT = 300;
	public static int SCALE = 2;
	public static String TITLE  = "Game";
	
	private Client client;
	private Server server;
	
	private boolean hServer = false;
	
	private Button hostServer = new Button("HOST", ButtonSprite.STANDART, 10,10, ShadowType.NONE);
	private Button connectServer = new Button("CONN", ButtonSprite.STANDART, 60,10, ShadowType.NONE);
	
	Player player1 = new Player(0xffff0000, 50,50);
	
	NetPlayer netplayer = new NetPlayer(0xffff0000, 100,50);
	
	private boolean connectedToServer = false;
	
	public static void main(String[] args)
	{
		GameContainer gc = new GameContainer(new Game());
		
		gc.setTitle(TITLE);
		gc.setWidth(WIDTH);
		gc.setHeight(HEIGHT);
		gc.setScale(SCALE);
		
		gc.setLightEnabled(false);
		
		gc.setDebug(false);
		gc.setFrameCap(30);
		
		gc.start();
	}
	
	@Override
	public void init(GameContainer gc)
	{
		this.gc = gc;
	}

	@Override
	public void update(float dt)
	{	
		int x = Input.getMouseX();
		int y = Input.getMouseY();
		
		hostServer.mouseMoved(x, y);
		connectServer.mouseMoved(x, y);
		
		if(Input.isButtonPressed(1) && hostServer.contains(x, y))
		{
			System.out.println("hosting");
			server = new Server(14000);
			server.startListening();
		}
		
		if(Input.isButtonPressed(1) && connectServer.contains(x, y))
		{
			netplayer.startListening();
			netplayer.connectWithRandom();
			connectedToServer = true;
		}
		
		if(Input.isKey(KeyEvent.VK_D))
		{
			player1.setXpos(player1.getXpos() + (int)(200 * dt));
		}
		if(Input.isKey(KeyEvent.VK_A))
		{
			player1.setXpos(player1.getXpos() - (int)(200 * dt));
		}
		if(Input.isKey(KeyEvent.VK_W))
		{
			player1.setYpos(player1.getYpos() - (int)(200 * dt));
		}
		if(Input.isKey(KeyEvent.VK_S))
		{
			player1.setYpos(player1.getYpos() + (int)(200 * dt));
		}
		
		netplayer.update(dt);
		
		if(connectedToServer)
		{
			netplayer.sendPosOtherPlayer(new DataPoint(player1.getXpos(), player1.getYpos()));
		}
		
		//calc pos of player2
	}

	@Override
	public void render(Renderer r)
	{
		r.draw(player1);
		r.draw(netplayer);
		
		r.draw(hostServer);
		r.draw(connectServer);
	}

}
