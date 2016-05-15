package shared.model;

/**
 * This class represents the score of a GameObject.
 * 
 */
public interface IScore
{
	/**
	 * Returns the ID of the GameObject that has this Score.
	 * 
	 * @return The ID of the GameObject that has this Score.
	 */
	public int getGameObjectId();

	/**
	 * Returns the value of this Score as an integer.
	 * 
	 * @return The value of this score, as an integer.
	 */
	public int getScore();
}
