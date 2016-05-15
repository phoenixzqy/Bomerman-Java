package shared.model.communication;

import shared.model.GameObjectType;

/**
 * Message which represents a game object that has been created.
 */
public class GameObjectCreatedMessage extends Message
{
	// the type of the game object
	private final GameObjectType gameObjectType;

	// the row of the game object
	private final int row;

	// the column of the game object
	private final int column;

	/**
	 * Constructor.
	 * 
	 * @param gameObjectIdentifier
	 *            The unique identifier of the game object.
	 * @param gameObjectType
	 *            The type of the game object.
	 * @param row
	 *            The new row of the game object.
	 * @param column
	 *            The new column of the game object.
	 * @throws IllegalArgumentException
	 *             Thrown if row or column is less than 0.
	 */
	public GameObjectCreatedMessage(int gameObjectIdentifier,
			GameObjectType gameObjectType, int row, int column)
	{
		super(gameObjectIdentifier);

		if (gameObjectType == null)
			throw new NullPointerException();

		if (row < 0 || column < 0)
			throw new IllegalArgumentException();

		this.gameObjectType = gameObjectType;
		this.row = row;
		this.column = column;
	}

	/**
	 * Returns the new row of the game object.
	 * 
	 * @return The new row of the game object.
	 */
	public int row()
	{
		return row;
	}

	/**
	 * Returns the new column of the game object.
	 * 
	 * @return The new column of the game object.
	 */
	public int column()
	{
		return column;
	}

	/**
	 * Returns the type of the game object.
	 * 
	 * @return The type of the game object.
	 */
	public GameObjectType gameObjectType()
	{
		return gameObjectType;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return "GAME_OBJECT_CREATED " + gameObjectIdentifier() + " "
				+ gameObjectType.toString() + " " + row() + " " + column();
	}
}
