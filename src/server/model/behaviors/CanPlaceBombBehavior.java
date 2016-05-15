package server.model.behaviors;

/**
 * A bomb behavior for a game object which can place a bomb.
 */
public class CanPlaceBombBehavior implements IBombBehavior
{
	// indicates whether this IBombBehavior should place a bomb
	private boolean placeBomb;
	
	// count the number of bombs
	private int bombCount;
	
	// the max number of bombs that one player can place at one time
	private int maxNumOfBomb;
	
	/**
	 * Creates a new CanPlaceBombBehavior.
	 * @param MaxBomb The maximum number of bombs which can be placed.
	 */
	public CanPlaceBombBehavior(int MaxBomb)
	{
		//initialize the number and max number of bombs
		bombCount = MaxBomb;
		
		maxNumOfBomb = MaxBomb;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canPlaceBomb()
	{
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean placeBomb()
	{
		return placeBomb;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPlaceBomb(boolean placeBomb)
	{
		this.placeBomb = placeBomb;
	}


	/**
	 * {@inheritDoc}
	 */
	public int countBomb() 
	{
		return bombCount;
	}


	/**
	 * {@inheritDoc}
	 */
	public void decrementBomb() 
	{
		if (bombCount == 0)
			throw new IllegalStateException();
		
		bombCount--;
		
	}


	/**
	 * {@inheritDoc}
	 */
	public void incrementBomb() 
	{
		if (bombCount == maxNumOfBomb)
		{
			throw new IllegalStateException();
		}
		
		bombCount++;
	}
}
