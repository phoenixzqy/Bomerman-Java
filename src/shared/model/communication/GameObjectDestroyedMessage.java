package shared.model.communication;

/**
 * A message which indicates a game object has been destroyed.
 */
public class GameObjectDestroyedMessage extends Message
{
	/**
	 * Constructor.
	 * 
	 * @param gameObjectIdentifier
	 *            The unique identifier of the game object.
	 */
	public GameObjectDestroyedMessage(int gameObjectIdentifier)
	{
		super(gameObjectIdentifier);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return "GAME_OBJECT_DESTROYED " + gameObjectIdentifier();
	}
}
