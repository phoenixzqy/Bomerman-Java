package server.model.behaviors;

import shared.model.Direction;

/**
 * This class represents the IMobilityBehavior of an object without mobility.
 *
 */
public class ImmobileBehavior implements IMobilityBehavior
{
	// the column of this object
	private int column;
	
	// the row of this object
	private int row;
	
	// returns true if this object is on the board and false otherwise
	private boolean onBoard;
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canMove() {
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void startMovingInDirection(Direction direction) 
	{
		throw new IllegalStateException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopMovingInDirection(Direction direction) 
	{
		throw new IllegalStateException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Direction directionToMove() 
	{
		return null;
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
