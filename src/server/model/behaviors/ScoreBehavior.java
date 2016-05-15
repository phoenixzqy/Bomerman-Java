package server.model.behaviors;

/**
 * This class represents the IScoreBehavior of an object with score.
 *
 */
public class ScoreBehavior implements IScoreBehavior
{
	//Integer that record the score
	private int score;
	
	/**
	 * Creates a new ScoreBehavior with a score of 0.
	 */
	public ScoreBehavior()
	{
		score = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasScore() 
	{
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public int score() 
	{
		return score;
	}

	/**
	 * {@inheritDoc}
	 */
	public void incrementScore() 
	{
		score++;
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementScore() 
	{
	    score--;
	}
}
