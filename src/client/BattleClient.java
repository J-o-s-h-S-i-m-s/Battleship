package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import common.ConnectionInterface;
import common.MessageListener;
import common.MessageSource;

/**
 * 
 * 
 * @author Evan Arroyo
 * @author Joshua Sims
 * @version 09 December 2016
 */
class BattleClient extends MessageSource implements MessageListener
{	
	private ConnectionInterface connectionInterface;
	
	private boolean isConnected;
		
	BattleClient(InetAddress host, int port, String username)
		throws IOException
	{					
		Socket clientSpecificSocket = new Socket(host, port);
		
		connectionInterface = new ConnectionInterface(clientSpecificSocket);
		connectionInterface.addMessageListener(this);
		Thread thread = new Thread(connectionInterface);
		thread.start();
		isConnected = true;
		
		sendCommand("/join " + username);
	}
	
	boolean isConnected()
	{
		return isConnected;
	}
	
	@Override
	public void messageReceived(String message, MessageSource source)
	{
		System.out.println(message);
		System.out.flush();
	}

	@Override
	public void sourceClosed(MessageSource source)
	{
		source.removeMessageListener(this);
		isConnected = false;
	}
	
	void sendCommand(String message)
	{
		connectionInterface.sendMessage(message);
	}
}
