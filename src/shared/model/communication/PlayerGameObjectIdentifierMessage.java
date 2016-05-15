package shared.model.communication;

/**
 * A message for communicating a game object identifier to a given player.
 * 
 */
public class PlayerGameObjectIdentifierMessage extends Message
{

	/**
	 * Constructs a PlayerGameObjectIdentifierMessage with the given
	 * gameObjectIdentifier.
	 * 
	 * @param gameObjectIdentifier
	 *            The game object identifier of the player.
	 */
	public PlayerGameObjectIdentifierMessage(int gameObjectIdentifier)
	{
		super(gameObjectIdentifier);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return "PLAYER_GAME_OBJECT_IDENTIFIER " + gameObjectIdentifier();
	}

}
