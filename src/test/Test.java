package test;

import java.io.IOException;

import com.pearisgreen.client.Client;
import com.pearisgreen.server.Server;

public class Test
{

	public static void main(String[] args)
	{
		Server server = new Server(14000);
		server.startListening();
		
		
		Client client = new Client("127.0.0.1", 14000);
		client.connectToServer();
		client.start();
		
		Client client2 = new Client("127.0.0.1", 14000);
		client2.connectToServer();
		client2.start();
		
		while(true)
		{
			System.out.println("reading client input: ");
			
			client.sendToServer((byte)30);
			
			client.sendToServer((byte)50);
			
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
