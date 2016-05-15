package shared.model.communication;

import shared.model.Key;
import shared.model.KeyAction;

/**
 * A message which indicates a key action occurred on the client side.
 */
public class KeyMessage extends Message
{
	// the key
	private final Key key;

	// the action
	private final KeyAction action;

	/**
	 * Constructor.
	 * 
	 * @param gameObjectId
	 *            The id for the gameObject corresponding to the Key and Action.
	 * @param key
	 *            The key.
	 * @param action
	 *            The action for the key.
	 * @throws NullPointerException
	 *             Thrown if key or action are null.
	 */
	public KeyMessage(int gameObjectId, Key key, KeyAction action)
	{
		super(gameObjectId);
		if (key == null || action == null)
			throw new NullPointerException();

		this.key = key;
		this.action = action;
	}

	/**
	 * Returns the key for this message.
	 * 
	 * @return The key for this message.
	 */
	public Key key()
	{
		return key;
	}

	/**
	 * Returns the action for this message.
	 * 
	 * @return The action for this message.
	 */
	public KeyAction action()
	{
		return action;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return "KEY " + gameObjectIdentifier() + " " + key.toString() + " "
				+ action.toString();
	}
}
