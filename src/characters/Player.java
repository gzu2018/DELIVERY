/** 
 *  Creates a Player character to be controlled by the user.
 *  Can be drawn and updated. Handles all collisions with
 *  other game variables. Can stand on platforms, collect
 *  letters, open or close houses, and be killed by monsters.
 *  Ends game if dead or delivers all letters
 *  @author Conor Mai, Guangze Zu, Emily Lam
 *  Teacher: Ishman
 *  Period: 04
 *  Date: 05-18-18
 */
package characters;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import main.Character;
import main.Handler;
import main.MapViewer;
import main.Obstacle;
import obstacles.Platform;
import obstacles.WallSegment;

public class Player extends Character 
{
	private static final int ID = 1;
	private int height;
	private int width;
	private boolean hasLetter;
	private boolean alive;
	private boolean win;
	private Handler handler;
	private Letter letter;
	
	/** Creates a Player character with an initial position and given
	 *  the handler
	 *  @param x the initial x coordinate of the player
	 *  @param y the initial y coordinate of the player
	 *  @param handler the handler containing all the game variables
	 */
	public Player(int x, int y, Handler handle) 
	{
		super(x, y);
		handler = handle;
		alive = true;
		hasLetter = false;
		height = 0;
		width = 0;
		win = false;
		letter = null;
	}
	
	/** Returns the player's id
	 *  @return the player's id
	 */
	public int getID()
	{
		return ID;
	}
	
	/** Updates the player's position depending on where it is in the frame
	 */
	@Override
	public void update()
	{
		if (alive)
		{
			setX(getX() + getVelX());
			// Gravity
			int FPS = 60;
			if (getVelY() > 0 && getVelY() < FPS - 2)
				setVelY(getVelY() + 2);
			else
				setVelY(getVelY() + 1);
			setY(getY() + getVelY());
			
			setBoundingRectangle(new Rectangle(getX(), getY() - height, width, height));
			checkCollisions();
		}
	}
	
	/** Draws the player and points the player has
	 *  Ends game if player is dead or player won
	 *  @param gr the Graphics2D Object to draw with
	 */
	@Override
	public void draw(Graphics2D gr) 
	{
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 50);
		gr.setFont(font);
		
		if (win && alive)
		{
			gr.setColor(Color.WHITE);
			gr.fillRect(0, 0, MapViewer.WIDTH, MapViewer.HEIGHT);
			gr.setColor(Color.CYAN);
			gr.drawString("CONGRATULATIONS! YOU WIN!", 0, MapViewer.HEIGHT / 2);
			setBoundingRectangle(null);
		}
		else if (alive)
		{
			gr.setColor(Color.BLACK);
			FontRenderContext frc = gr.getFontRenderContext();
			TextLayout layout = new TextLayout("@", font, frc);
			layout.draw(gr, getX(), getY());
			Rectangle2D bounds = layout.getBounds();

			height = (int) bounds.getHeight();
			width = (int) bounds.getWidth();
		}
		else
		{
			gr.setColor(Color.BLACK);
			gr.fillRect(0, 0, MapViewer.WIDTH, MapViewer.HEIGHT);
			gr.setColor(Color.RED);
			gr.drawString("GAME OVER", MapViewer.WIDTH / 2, MapViewer.HEIGHT / 2);
			setBoundingRectangle(null);
		}
		String text = "Points: " + getPoints();
	    FontMetrics fontMetrics = gr.getFontMetrics();
	    gr.drawString(text, 0, fontMetrics.getAscent() * 2 / 3);
	}
	
	/** Determines whether the player has collided with any other
	 *  game variables and handles each collision accordingly. Monsters
	 *  kill player. Houses open when letter is picked up or close when
	 *  letters are delivered. Letters can be picked up. Obstacles
	 *  stop player.
	 */
	private void checkCollisions()
	{
		// if it falls off the bottom it is no longer alive
		if(getY() - height > MapViewer.HEIGHT)
			alive = false;
		boolean needLetter = false;
		
		for(Character tempCharacter : handler.getCharacters())
		{
			if(getBoundingRectangle().intersects(tempCharacter.getBoundingRectangle()))
			{
				if(tempCharacter instanceof Monster)
					alive = false;
				if(tempCharacter instanceof House)
					needLetter = hitHouse(tempCharacter);
				if(tempCharacter instanceof Letter)
					hitLetter(tempCharacter);
			}
		}
		
		if (needLetter)
			addLetter();
			
		for(Obstacle tempObstacle : handler.getObstacles())
		{
			if(getBoundingRectangle().intersects(tempObstacle.getBoundingRectangle()))
			{
				if(tempObstacle instanceof Platform)
					platformHit(tempObstacle);
				if(tempObstacle instanceof WallSegment)
					wallHit(tempObstacle);
			}
		}
	}
	
	/** If touched while carrying a letter, house closes, and points
	 *  are added
	 *  @param tempChar house to check
	 *  @return true if new letter needs to be added to map, false if else
	 */
	private boolean hitHouse(Character tempChar)
	{
		if(((House)tempChar).isOpen() && hasLetter)
		{
			addPoints(tempChar.getPoints());
			((House)tempChar).close();
			hasLetter = false;
			return true;
		}
		return false;
	}
	
	/** Removes letter when hit, adds points, and opens the corresponding house
	 *  @param tempChar the letter to be removed
	 */
	private void hitLetter(Character tempChar)
	{
		handler.removeCharacter(tempChar);
		addPoints(tempChar.getPoints());
		hasLetter = true;
		letter = (Letter) tempChar;
		for (House house : handler.getHouses())
		{
			if ((house.isOdd() && letter.isOdd()) || 
					(!house.isOdd() && !letter.isOdd()))
				house.open();
		}
	}
	
	/** Adds a letter to the map randomly if houses are closed and there
	 *  are no letters
	 *  @param needLetter whether the map needs a new letter
	 */
	private void addLetter()
	{
		if (Letter.getOrder() < Letter.ABC_ARRAY.length)
		{
			int avgDimension = 30;
			int randX = (int) (Math.random() * (MapViewer.WIDTH - avgDimension));
			int randY = (int) (Math.random() * (MapViewer.HEIGHT * 9 / 10)) + avgDimension;
			handler.addCharacter(new Letter(randX, randY));
		}
		if (letter.getLetter().equals("Z"))
			win = true;
	}
	
	/** Stops player when it hits a platform
	 *  @param obs the obstacle that was hit
	 */
	private void platformHit(Obstacle obs)
	{
		// if it is over the platform
		if(getVelY() > 0)
		{
			setVelY(0);
			setY((int) (obs.getY() - obs.getBoundingRectangle().getHeight()));
			setBoundingRectangle(new Rectangle(getX(), getY() - height, width, height));
		}
	}
	
	/** Stops player when it hits a wall
	 *  @param obs the obstacle that was hit
	 */
	private void wallHit(Obstacle obs)
	{
		// if it hits the left side of the wall
		if(getVelX() > 0)
		{
			handler.stopPlayerRight(1);
			setX((int) (obs.getX() - obs.getBoundingRectangle().getWidth()));
		}
		// if it hits the right side of the wall
		if(getVelX() < 0)
		{
			handler.stopPlayerLeft(1);
			setX((int) (obs.getX() + obs.getBoundingRectangle().getWidth()));
		}
		// if it is under the wall
		if(getVelY() < 0)
		{
			setVelY(0);
			setY((int) (obs.getY() + height));
		}
		// if it is over the wall
		if(getVelY() > 0)
		{
			setVelY(0);
			setY((int) (obs.getY() - obs.getBoundingRectangle().getHeight()));
		}
		setBoundingRectangle(new Rectangle(getX(), getY() - height, width, height));
	}
}
