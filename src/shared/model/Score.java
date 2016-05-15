package shared.model;

/**
 * This class is an implementation of IScore.
 * 
 */
public class Score implements IScore
{

	/**
	 * The score, represented as an integer.
	 */
	private int score;

	/**
	 * The ID of the GameObject that has this score.
	 */
	private int gameObjectId;

	/**
	 * Constructs a new Score with the given GameObject ID and score.
	 * 
	 * @param gameObjectId
	 *            The ID of the GameObject that has this score.
	 * @param score
	 *            The integer value of the score.
	 */
	public Score(int gameObjectId, int score)
	{
		this.gameObjectId = gameObjectId;
		this.score = score;
	}

	/**
	 * Returns the ID of the GameObject that has this Score.
	 * 
	 * @return The ID of the GameObject that has this Score.
	 */
	public int getGameObjectId()
	{
		return gameObjectId;
	}

	/**
	 * Returns the value of this Score as an integer.
	 * 
	 * @return The value of this score, as an integer.
	 */
	public int getScore()
	{
		return score;
	}
}
