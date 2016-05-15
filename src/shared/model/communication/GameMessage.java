package shared.model.communication;

/**
 * A message which indicates a game has started, stopped, or stalled.
 */
public class GameMessage implements IMessage
{
	/**
	 * The action for the game.
	 */
	private final Action action;

	/**
	 * The number of connected players.
	 */
	private final int players;

	/**
	 * Enumerated type for actions related to the beginning and end of the game.
	 * 
	 */
	public enum Action
	{
		/**
		 * The game has started.
		 */
		START,

		/**
		 * The game has stopped.
		 */
		STOP,

		/**
		 * The game is waiting.
		 */
		WAITING
	}

	/**
	 * Constructs a GameMessage with the given number of players and action.
	 * 
	 * @param action
	 *            The action of the game.
	 * @param players
	 *            The number of connnected players.
	 */
	public GameMessage(Action action, int players)
	{
		this.players = players;
		this.action = action;
	}

	/**
	 * Returns the number of players.
	 * 
	 * @return The number of players.
	 */
	public int numberOfPlayers()
	{
		return players;
	}

	/**
	 * Returns the Action of the game.
	 * 
	 * @return The Action of the game.
	 */
	public Action action()
	{
		return action;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "GAME " + action() + " " + numberOfPlayers();
	}
}
