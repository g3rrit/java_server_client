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
		
		
		Client client = new Client("gerri", "127.0.0.1", 14000);
		client.connectToServer();
		client.start();
		
		Client client2 = new Client("domin", "127.0.0.1", 14000);
		client2.connectToServer();
		client2.start();
		
		while(true)
		{
			System.out.println("reading client input: ");
			
			client.sendToServer("hallo");
			
			client.sendToServer((byte)2);
			
			client.sendToServer("testsending");
			
			String[] str = client.getStack();
			
			for(int i = 0 ; i < str.length; i++)
			{
				System.out.println(str[i]);
			}
			
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*
		String testS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!'§$%&/()=?";
		
		for(int i = 0; i < testS.length(); i++)
		{
			System.out.println(testS.getBytes()[i]);
		}*/
	}
}
