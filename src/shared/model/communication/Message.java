package shared.model.communication;

/**
 * Abstract base implementation of IMessage.
 */
public abstract class Message implements IMessage
{
	// the game object identifier
	private final int gameObjectIdentifier;
	
	/**
	 * Creates an instance of Message.
	 * @param gameObjectIdentifier A unique identifier for the game object.
	 */
	protected Message(int gameObjectIdentifier)
	{
		this.gameObjectIdentifier = gameObjectIdentifier;
	}
	
	/**
	 * Returns the game object identifier.
	 * @return The game object identifier.
	 */
	public int gameObjectIdentifier()
	{
		return gameObjectIdentifier;
	}
}
