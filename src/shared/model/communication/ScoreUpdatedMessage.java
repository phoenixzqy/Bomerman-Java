package shared.model.communication;

/**
 * Message which represents a game object whose score has been updated.
 */
public class ScoreUpdatedMessage extends Message
{
	/**
	 * The player's score.
	 */
	private final int score;

	/**
	 * Constructor.
	 * @param gameObjectIdentifier
	 *            The unique identifier of the player game object.
	 * @param score
	 *            The score of the player.
	 */
	public ScoreUpdatedMessage(int gameObjectIdentifier, int score)
	{
		super(gameObjectIdentifier);

		this.score = score;
	}
	
	/**
	 * Returns the score of the player.
	 * 
	 * @return The score of the player.
	 */
	public int score()
	{
		return score;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return "SCORE_UPDATED " + gameObjectIdentifier() + " " + score();
	}
}
