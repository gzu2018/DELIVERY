package main;
/**
 * @author Conor Mai
 */

import java.awt.Graphics2D;
import java.util.LinkedList;

import characters.House;
import characters.Player;

public class Handler
{
	/*
	 * Conor I know this is more work but hear me out
	 * Ishman is gonna freak if she sees us with public instance variables
	 * I changed it everywhere so you don't even have to mess with it
	 * just use the get methods from now on
	 */
	private LinkedList<Character> characters;
	private LinkedList<Player> playerList;
	private LinkedList<Obstacle> obstacles;
	private LinkedList<House> houses;
	
	/** Creates a linked list of characters
	 * 
	 */
	public Handler() 
	{
		characters = new LinkedList<Character>();
		playerList = new LinkedList<Player>();
		obstacles = new LinkedList<Obstacle>();
		houses = new LinkedList<House>();
	}
	
	/**Goes through all of the Characters and updates them
	 * 
	 */
	public void update()
	{
		for(Character tempCharacter : characters)
			tempCharacter.update();
		
		for(Player tempPlayer : playerList)
			tempPlayer.update();
	}
	
	/**Goes through all of the Characters and makes them draw themselves
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		for(Obstacle tempObstacle : obstacles)
			tempObstacle.draw(g);
		
		for(Character tempCharacter : characters)
			tempCharacter.draw(g);
	
		for(Player tempPlayer : playerList)
			tempPlayer.draw(g);
	}
	
	/**Adds an object to the list of characters
	 * @param object
	 */
	public void addCharacter(Character object)
	{
		characters.add(object);
		if (object instanceof House)
			houses.add((House)object);
	}
	
	/**removes an object from the list of characters
	 * @param object
	 */
	public void removeCharacter(Character object)
	{
		characters.remove(object);
		if (object instanceof House)
			houses.remove((House)object);
	}
	
	public void addPlayer(Player p)
	{
		playerList.add(p);
	}
	
	public void removePlayer(Player p)
	{
		playerList.remove(p);
	}
	
	public void addObstacle(Obstacle obstacle)
	{
		obstacles.add(obstacle);
	}
	
	public void removeObstacle(Obstacle obstacle)
	{
		 obstacles.remove(obstacle);
	}
	
	public LinkedList<Character> getCharacters()
	{
		return characters;
	}
	
	public LinkedList<Player> getPlayerList()
	{
		return playerList;
	}
	
	public LinkedList<Obstacle> getObstacles()
	{
		return obstacles;
	}
	
	public LinkedList<House> getHouses()
	{
		return houses;
	}

	
	/**Goes through the collection of characters and finds the player 1 
	 * and makes it move to the left
	 */
	public void movePlayerLeft(int id)
	{
		for(Player player : playerList)
			if(player.getID() == id)
				player.setVelX(-5);
	}
	
	/**Goes through the collection of characters and finds the player 1 
	 * and makes it move to the right
	 */
	public void movePlayerRight(int id)
	{
		for(Player player : playerList)
			if(player.getID() == id)
				player.setVelX(5);
	}
	
	public void movePlayerUp(int id)
	{
		for(Player player : playerList)
			if(player.getID() == id)
				//if(player.getVelY() == 0)
					player.setVelY(-20);
		
	}
	
	/**Goes through the collection of characters and finds the player 1 
	 * and stops it if it is moving to the left
	 */
	public void stopPlayerLeft(int id) 
	{
		for(Player player : playerList)
			if(player.getID() == id)
			{
				if(player.getVelX() < 0)
					player.setVelX(0);
			}
	}
	
	/**Goes through the collection of characters and finds the player 1 
	 * and stops it if it is moving to the right
	 */
	public void stopPlayerRight(int id)
	{
		for(Player player : playerList)
			if(player.getID() == id)
			{
				if(player.getVelX() > 0)
					player.setVelX(0);
			}
	}

	public Player getPlayer(int id) 
	{
		// TODO Auto-generated method stub
		for(Player player : playerList)
			if(player.getID() == id)
			{
				return player;
			}
		return null;
	}
}
