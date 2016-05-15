package server.model.behaviors;


/**
 * This class represents the IScoreBehavior of an object without score.
 *
 */
public class NoScoreBehavior implements IScoreBehavior
{

	/**
	 * {@inheritDoc}
	 */
	public boolean hasScore() 
	{
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public int score() 
	{
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void incrementScore() 
	{
		
		throw new IllegalStateException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementScore() 
	{
		throw new IllegalStateException();
		
	}

}
