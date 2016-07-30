/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Test;

import UDP.Client;
import UDP.Serveur;

public class Test
{
	public static void main (String args [])
	{
		Thread serveur = new Thread (new Serveur ());

		Thread player1 = new Thread (new Client ("John Conor", 1000));
		Thread player2 = new Thread (new Client ("Pikachu", 1000));
		
		serveur.start();
		
		player1.start();
		player2.start();
	}
}