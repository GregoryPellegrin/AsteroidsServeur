/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Serveur;

public class StartServeur
{
	public static void main (String args [])
	{
		Thread serveur = new Thread (new Serveur ());
		
		System.out.println("[SERVEUR] Starting...");
		
		serveur.start();
		
		System.out.println("[SERVEUR] Runing...");
	}
}