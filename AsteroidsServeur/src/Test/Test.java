/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Test;

import Ship.MotherShip;
import Ship.SpeedShip;
import Ship.SuperSpeedShip;
import Character.Computer;
import Entity.Entity;
import UDP.Client;
import UDP.Serveur;
import java.util.Arrays;

public class Test
{
	public static void main (String args [])
	{
		Thread serveur = new Thread (new Serveur ());

		Thread player1 = new Thread (new Client (new SuperSpeedShip (50, 100, Computer.START_LEFT, Entity.COMPUTER), 5000));
		Thread player2 = new Thread (new Client (new SpeedShip (99, 999, Computer.START_DOWN, Entity.COMPUTER), 10000));
		Thread player3 = new Thread (new Client (new MotherShip (99, 999, Computer.START_DOWN, Entity.COMPUTER), 1000));
		
		serveur.start();
		
		player1.start();
		player2.start();
		
		try
		{
			Thread.sleep(15000);
		}
		catch (InterruptedException e)
		{
			System.out.println("[TEST] InterruptedException : " + e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
		
		player3.start();
	}
}