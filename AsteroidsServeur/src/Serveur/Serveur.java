/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */
package Serveur;

import Entity.Entity;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Serveur implements Runnable
{
	private static final String HOST_NAME = "127.0.0.1";
	private static final int BYTE_SIZE = 5000;
	private static final int PORT = 2345;
	
	private final List <Entity> entities = new LinkedList <> ();
	private final List <Entity> pendingEntities = new LinkedList <> ();
	
	public Serveur () {}
	
	public List <Entity> getEntities ()
	{
		return this.pendingEntities;
	}
	
	public void clearEntities ()
	{
		this.pendingEntities.clear();
	}
	
	public void update (List <Entity> entities)
	{
		this.entities.clear();
		this.entities.addAll(entities);
	}
	
	@Override
	public void run ()
	{
		try
		{
			DatagramSocket serveur = new DatagramSocket (Serveur.PORT);
			
			while (true)
			{
				byte [] bufferGetFromClient = new byte [Serveur.BYTE_SIZE];
				DatagramPacket paquetGetFromClient = new DatagramPacket
				(
					bufferGetFromClient,
					bufferGetFromClient.length
				);
				
				serveur.receive(paquetGetFromClient);
				
				ByteArrayInputStream objectByteGetFromClient = new ByteArrayInputStream (bufferGetFromClient);
				ObjectInputStream objectStreamGetFromClient = new ObjectInputStream (new BufferedInputStream (objectByteGetFromClient));
				
				Entity entity = (Entity) objectStreamGetFromClient.readObject();
				boolean find = false;
				for (int i = 0; ((i < this.pendingEntities.size()) && (! find)); i++)
					if (this.pendingEntities.get(i).getId().equals(entity.getId()))
					{
						find = true;

						this.pendingEntities.remove(i);
					}
				this.pendingEntities.add(entity);
				
				objectStreamGetFromClient.close();
				paquetGetFromClient.setLength(bufferGetFromClient.length);
				
				ByteArrayOutputStream objectByteSendToClient = new ByteArrayOutputStream (Serveur.BYTE_SIZE);
				ObjectOutputStream objectStreamSendToClient = new ObjectOutputStream (new BufferedOutputStream (objectByteSendToClient));
				
				objectStreamSendToClient.writeObject(this.entities);
				objectStreamSendToClient.flush();
				
				byte [] bufferSendToClient = objectByteSendToClient.toByteArray();
				DatagramPacket paquetSendToClient = new DatagramPacket
				(
					bufferSendToClient,
					bufferSendToClient.length,
					paquetGetFromClient.getAddress(),
					paquetGetFromClient.getPort()
				);
				
				paquetSendToClient.setData(bufferSendToClient);
				serveur.send(paquetSendToClient);
				
				objectStreamSendToClient.close();
				paquetSendToClient.setLength(bufferSendToClient.length);
			}
		}
		catch (SocketException e)
		{
			System.out.println("[SERVEUR] SocketException : " + e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("[SERVEUR] ClassNotFoundException : " + e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
		catch (NotSerializableException e)
		{
			System.out.println("[SERVEUR] NotSerializableException : " + e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
		catch (IOException e)
		{
			System.out.println("[SERVEUR] IOException : " + e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
	}
	
	public static synchronized void println (String chaine)
	{
		System.out.println(chaine);
	}
}