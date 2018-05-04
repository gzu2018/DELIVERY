package Main;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 * @author Conor Mai
 *
 */
public class Handler {

	public LinkedList<Character> objects;
	
	/** Creates a linked list of characters
	 * 
	 */
	public Handler() 
	{
		// TODO Auto-generated constructor stub
		objects = new LinkedList<Character>();
	}
	
	/**Goes through all of the Characters and updates them
	 * 
	 */
	public void update()
	{
		for(Character tempCharacter : objects)
		{
			tempCharacter.update();
		}
	}
	
	/**Goes through all of the Characters and makes them draw themselves
	 * @param g
	 */
	public void draw(Graphics g)
	{
		for(Character tempCharacter : objects)
		{
			tempCharacter.draw(g);
		}
	}
	
	/**Adds an object to the list of characters
	 * @param object
	 */
	public void addObject(Character object)
	{
		objects.add(object);
	}
	
	/**removes an object from the list of characters
	 * @param object
	 */
	public void removeObject(Character object)
	{
		objects.remove(object);
	}
	
	/**Goes through the collection of characters and finds the player 1 
	 * and makes it move to the left
	 */
	public void moveP1Left()
	{
		for(Character tempCharacter : objects)
		{
			if(tempCharacter.getId() == 1)
			{
				tempCharacter.setVelX(-5);
			}
		}
	}
	
	/**Goes through the collection of characters and finds the player 1 
	 * and makes it move to the right
	 */
	public void moveP1Right()
	{
		for(Character tempCharacter : objects)
		{
			if(tempCharacter.getId() == 1)
			{
				tempCharacter.setVelX(5);
			}
		}
	}
	
	/**Goes through the collection of characters and finds the player 1 
	 * and stops it if it is moving to the left
	 */
	public void stopP1Left() 
	{
		for(Character tempCharacter : objects)
		{
			if(tempCharacter.getId() == 1)
			{
				if(tempCharacter.getVelX() < 0)
					tempCharacter.setVelX(0);
			}
		}
	}
	
	/**Goes through the collection of characters and finds the player 1 
	 * and stops it if it is moving to the right
	 */
	public void stopP1Right()
	{
		for(Character tempCharacter : objects)
		{
			if(tempCharacter.getId() == 1)
			{
				if(tempCharacter.getVelX() > 0)
					tempCharacter.setVelX(0);
			}
		}
	}
}