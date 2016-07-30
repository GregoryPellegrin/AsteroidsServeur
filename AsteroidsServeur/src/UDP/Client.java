/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package UDP;

import static UDP.Serveur.print;
import static UDP.Serveur.println;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client implements Runnable
{
	private String name;
	private long sleepTime;

	public Client (String name, long sleepTime)
	{
		this.name = name;
		this.sleepTime = sleepTime;
	}

	@Override
	public void run ()
	{
		while (true)
		{
			String dataSendToServeur = "Coucou :) je suis " + this.name;
			byte [] bufferSendToServeur = dataSendToServeur.getBytes();
			
			try
			{
				DatagramSocket client = new DatagramSocket ();
				
				InetAddress adresse = InetAddress.getByName("127.0.0.1");
				DatagramPacket paquetSendToServeur = new DatagramPacket
				(
					bufferSendToServeur,
					bufferSendToServeur.length,
					adresse,
					Serveur.PORT
				);
				
				paquetSendToServeur.setData(bufferSendToServeur);
				client.send(paquetSendToServeur);
				
				byte [] bufferGetFromServeur = new byte [8196];
				DatagramPacket paquetGetFromServeur = new DatagramPacket
				(
					bufferGetFromServeur,
					bufferGetFromServeur.length,
					adresse,
					Serveur.PORT
				);
				
				client.receive(paquetGetFromServeur);
				
				print(dataSendToServeur + " a reçu une réponse du serveur : ");
				println(new String(paquetGetFromServeur.getData()));
				
				try
				{
					Thread.sleep(sleepTime);
				}
				catch (InterruptedException e)
				{
					System.out.println(e.getMessage());
				}
			}
			catch (SocketException e)
			{
					System.out.println(e.getMessage());
			}
			catch (UnknownHostException e)
			{
					System.out.println(e.getMessage());
			}
			catch (IOException e)
			{
					System.out.println(e.getMessage());
			}
		}
	}
}