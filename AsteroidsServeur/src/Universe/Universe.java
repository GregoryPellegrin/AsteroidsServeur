/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Universe;

import Entity.Entity;
import Entity.Missile;
import Entity.Ship;
import Serveur.Serveur;
import Util.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Universe
{
	public static final int FRAMES_PER_SECOND = 60;
	public static final long FRAME_TIME = (long) (1000000000.0 / FRAMES_PER_SECOND);
	
	private final ArrayList <Entity> entities = new ArrayList <> ();
	
	public Universe () {}
	
	public ArrayList <Entity> getEntities ()
	{
		return this.entities;
	}
	
	public void clearEntities ()
	{
		this.entities.clear();
	}
	
	public void addEntity (ArrayList <Entity> entities)
	{
		for (Entity entity : entities)
		{
			boolean find = false;
			
			for (int i = 0; ((i < this.entities.size()) && (! find)); i++)
				if (this.entities.get(i).getId().equals(entity.getId()))
				{
					find = true;

					this.entities.remove(i);
				}
			this.entities.add(entity);
			
			ArrayList <Missile> missiles = ((Ship) entity).missiles;

			for (int i = 0; i < missiles.size(); i++)
			{
				if (missiles.get(i).getId().isEmpty())
					((Ship) entity).missiles.get(i).setId(entity.getId() + missiles.size() + i);
				else
				{
					find = false;
					
					for (int j = 0; ((j < this.entities.size()) && (! find)); j++)
						if (missiles.get(i).getId().equals(this.entities.get(j).getId()))
						{
							find = true;

							this.entities.remove(j);
						}
				}

				this.entities.add(((Ship) entity).missiles.get(i));
			}
		}
	}
	
	public void updateEntities ()
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
	
	public static void main (String args [])
	{
		Clock logicTimer = new Clock (Universe.FRAMES_PER_SECOND);
		Universe universe = new Universe ();
		
		Serveur serveur = new Serveur ();
		Thread serveurThread = new Thread (serveur);
		
		serveurThread.start();
		
		while (true)
		{
			long start = System.nanoTime();
			
			logicTimer.update();
			
			universe.clearEntities();
			universe.addEntity(serveur.getEntities());
			serveur.addEntitiesTerminated();
			
			for (int i = 0; i < 5 && logicTimer.hasElapsedCycle(); i++)
				universe.updateEntities();
			
			serveur.update(universe.getEntities());
			
			long delta = Universe.FRAME_TIME - (System.nanoTime() - start);			
			if (delta > 0)
				try
				{
					Thread.sleep(delta / 1000000L, (int) delta % 1000000);
				}
				catch (Exception e)
				{
					System.out.println("[UNIVERSE] Exception : " + e.getMessage());
					System.out.println(Arrays.toString(e.getStackTrace()));
				}
		}
	}
}