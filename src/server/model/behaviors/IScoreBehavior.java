package server.model.behaviors;

/**
 * Behavior which determines how an IGameObject handles score.
 */
public interface IScoreBehavior
{
	/**
	 * Returns true if the IGameObject has a score and false otherwise.
	 * @return True if the IGameObject has a score and false otherwise.
	 */
	public boolean hasScore();
	
	/**
	 * Returns the score of the IGameObject.
	 * @return The score of the IGameObject.  If the IGameObject does not have a score, the return
	 * value for this method is undefined.
	 */
	public int score();
	
	/**
	 * Increments the score of the IGameObject.
	 * @throws IllegalStateException Thrown if the IGameObject does not have a score.
	 */
	public void incrementScore();
	
	/**
	 * Decrements the score of the IGameObject.
	 * @throws IllegalStateException Thrown if the IGameObject does not have a score.
	 */
	public void decrementScore();
}
