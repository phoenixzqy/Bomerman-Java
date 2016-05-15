package server.model.behaviors;

/**
 * A bomb behavior for a game object which can't place bombs.
 */
public class CanNotPlaceBombBehavior implements IBombBehavior
{
	/**
	 * {@inheritDoc}
	 */
	public boolean canPlaceBomb()
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean placeBomb()
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPlaceBomb(boolean placeBomb)
	{
		throw new IllegalStateException();
	}

	/**
	 * {@inheritDoc}
	 */
	public int countBomb() 
	{
		// Return undefined  number
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementBomb() 
	{
		// Do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	public void incrementBomb() 
	{
		// Do nothing
	}
}
