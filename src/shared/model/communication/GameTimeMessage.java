package shared.model.communication;

/**
 * A Message for communicating the time remaining in a game.
 * 
 */
public class GameTimeMessage implements IMessage
{
	/**
	 * Instance variable for remaining time in the game, in seconds.
	 */
	private int remainingTime;

	/**
	 * The remaining time in the game, in seconds.
	 * 
	 * @param remainingTime The remaining time in the game, in seconds.
	 */
	public GameTimeMessage(int remainingTime)
	{
		this.remainingTime = remainingTime;
	}

	/**
	 * Returns the remaining time in the game, in seconds.
	 * 
	 * @return The remaining time in the game, in seconds.
	 */
	public int time()
	{
		return remainingTime;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "GAME_TIME " + time();
	}
}
