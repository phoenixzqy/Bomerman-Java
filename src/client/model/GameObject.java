package client.model;

import shared.model.GameObjectType;

/**
 * Implementation of IGameObject.
 */
public class GameObject implements IGameObject
{
	// the game object type
	private final GameObjectType gameObjectType;
	
	// the row
	private int row;
	
	// the column
	private int column;
	
	// a unique identifier for this IGameObject;
	private final int identifier;
	
	/**
	 * Constructor for GameObject.
	 * @param identifier The identifier.
	 * @param gameObjectType The type of this GameObject.
	 * @param row The row of this GameObject.
	 * @param column The column of this GameObject.
	 */
	public GameObject(int identifier, GameObjectType gameObjectType, int row, int column)
	{
		if (gameObjectType == null)
			throw new NullPointerException();
		
		this.gameObjectType = gameObjectType;
		this.row = row;
		this.column = column;
		this.identifier = identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	public int row()
	{
		return row;
	}

	/**
	 * {@inheritDoc}
	 */
	public int column()
	{
		return column;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * {@inheritDoc}
	 */
	public GameObjectType gameObjectType()
	{
		return gameObjectType;
	}

	/**
	 * {@inheritDoc}
	 */
	public int identifier()
	{
		return identifier;
	}
}
