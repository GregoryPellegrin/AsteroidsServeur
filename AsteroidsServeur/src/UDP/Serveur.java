/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */
package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Serveur implements Runnable
{
	public final static int PORT = 2345;
	
	public Serveur () {}
	
	@Override
	public void run ()
	{
		try
		{
			DatagramSocket serveur = new DatagramSocket (Serveur.PORT);

			while (true)
			{
				byte [] bufferGetFromClient = new byte [8192];
				DatagramPacket paquetGetFromClient = new DatagramPacket
				(
						bufferGetFromClient,
						bufferGetFromClient.length
				);
				
				serveur.receive(paquetGetFromClient);

				String dataGetFromClient = new String (paquetGetFromClient.getData());
				print("Reçu de la part de " + paquetGetFromClient.getAddress() + " sur le port " + paquetGetFromClient.getPort() + " : ");
				println(dataGetFromClient);

				paquetGetFromClient.setLength(bufferGetFromClient.length);
				
				byte [] bufferSendToClient = new String ("Réponse du serveur à " + dataGetFromClient).getBytes();
				DatagramPacket paquetSendToClient = new DatagramPacket
				(
						bufferSendToClient,
						bufferSendToClient.length,
						paquetGetFromClient.getAddress(),
						paquetGetFromClient.getPort()
				);

				serveur.send(paquetSendToClient);
				paquetSendToClient.setLength(bufferSendToClient.length);
			}
		}
		catch (SocketException e)
		{
			System.out.println(e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static synchronized void print (String str)
	{
		System.out.print(str);
	}

	public static synchronized void println (String str)
	{
		System.err.println(str);
	}
}