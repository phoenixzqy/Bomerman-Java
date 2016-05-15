package server.model.behaviors;

import java.util.*;

import shared.model.Direction;

/**
 * This class represents the IMobilityBehavior of an object with mobility.
 *
 */
public class MobileBehavior implements IMobilityBehavior
{
	// the current directions that this player owned, true if the player own this direction currently
	private final HashMap<Direction, Boolean> currentDirections;
	
	// row of the player
	private int row;
	
	// column of the player
	private int column;
	
	// this indicates whether the game object is on the board or not
	private boolean onBoard;
	
	/**
	 * The constructor to initialize all.
	 */
	public MobileBehavior()
	{
		currentDirections = new HashMap<Direction, Boolean>();
		currentDirections.put(Direction.LEFT, false);
		currentDirections.put(Direction.RIGHT, false);
		currentDirections.put(Direction.UP, false);
		currentDirections.put(Direction.DOWN, false);
		onBoard = false;
		//initialize the position of the game object out of the board
		row = -1;
		column = -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canMove() 
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void startMovingInDirection(Direction direction) 
	{
		if (direction == null)
		{
			throw new NullPointerException();
		}
		
		if (direction == Direction.NONE)
		{
			throw new IllegalArgumentException();
		}
		// set the value of the proper direction of this player to be true
		currentDirections.put(direction, true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void stopMovingInDirection(Direction direction) 
	{
		if (direction == null)
		{
			throw new NullPointerException();
		}
		
		if (direction == Direction.NONE)
		{
			throw new IllegalArgumentException();
		}
		// set the value of the proper direction of this player to be false
		currentDirections.put(direction, false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Direction directionToMove() 
	{
		int directionCount = 0;
		
		for (boolean movingInDirection : currentDirections.values())
			if (movingInDirection)
				directionCount++;
		
		if (directionCount != 1)
			return Direction.NONE;

		for (Direction direction : currentDirections.keySet())
			if (currentDirections.get(direction))
				return direction;
		
		// this should never happen
		throw new IllegalStateException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int column() 
	{
		return column;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int row() 
	{
		return row;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setPosition(int row, int column) 
	{
		this.row = row;
		this.column = column;
		onBoard = true;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeFromBoard() 
	{
		this.row = -1;
		this.column = -1;
		onBoard = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean onBoard() 
	{
		return onBoard;
	}
	
}
