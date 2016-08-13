/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */
package Serveur;

import Entity.Entity;
import Entity.Ship;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Serveur implements Runnable
{
	public static final String HOST_NAME = "127.0.0.1";
	public static final int BYTE_SIZE = 5000;
	public static final int PORT = 2345;
	
	List <Entity> entities = new LinkedList <> ();
	
	public Serveur () {}
	
	private void addEntity (Entity entity)
	{
		boolean find = false;
		
		for (int i = 0; ((i < this.entities.size()) && (! find)); i++)
			if (this.entities.get(i).getId() == entity.getId())
			{
				find = true;
				this.entities.remove(i);
			}
		
		this.entities.add(entity);
		
		for (int i = 0; i < ((Ship) entity).missile.size(); i++)
		{
			find = false;
			
			for (int j = 0; ((j < this.entities.size()) && (! find)); j++)
				if (((Ship) entity).missile.get(i).getId() == this.entities.get(j).getId())
					find = true;
			
			if (find == false)
				this.entities.add(((Ship) entity).missile.get(i));
		}
	}
	
	private void updateEntities ()
	{
		for (Entity entity : this.entities)
			entity.update();
		
		for (int i = 0; i < this.entities.size(); i++)
		{
			Entity a = this.entities.get(i);

			for (int j = i + 1; j < this.entities.size(); j++)
			{
				Entity b = this.entities.get(j);
				
				if ((i != j) && a.isCollision(b))
				{
					a.checkCollision(b);
					b.checkCollision(a);
				}
			}
		}
		
		Iterator <Entity> iter = this.entities.iterator();
		while (iter.hasNext())
			if (iter.next().needsRemoval())
				iter.remove();
	}
	
	@Override
	public void run ()
	{
		try
		{
			DatagramSocket serveur = new DatagramSocket (Serveur.PORT);
			
			while (true)
			{
				//println("[SERVEUR] Total Entities : " + this.entities.size());
				
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
				this.addEntity(entity);
				this.updateEntities();
				
				println("[SERVEUR] Paquet recu du client " + entity.getId());
				
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