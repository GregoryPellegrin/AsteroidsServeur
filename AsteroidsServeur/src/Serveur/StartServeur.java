/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Serveur;

import Ship.MotherShip;
import Ship.SpeedShip;
import Ship.SuperSpeedShip;
import Character.Computer;
import Entity.Entity;
import Client.Client;
import java.util.Arrays;

public class StartServeur
{
	public static void main (String args [])
	{
		Thread serveur = new Thread (new Serveur ());

		/*Thread player1 = new Thread (new Client (new SuperSpeedShip (50, 100, Computer.START_LEFT, Entity.COMPUTER)));
		Thread player2 = new Thread (new Client (new SpeedShip (99, 999, Computer.START_DOWN, Entity.COMPUTER)));
		Thread player3 = new Thread (new Client (new MotherShip (99, 999, Computer.START_DOWN, Entity.COMPUTER)));
		*/
		
		System.out.println("[SERVEUR] Starting...");
		serveur.start();
		System.out.println("[SERVEUR] Runing");
		
		/*player1.start();
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
		
		player3.start();*/
	}
}