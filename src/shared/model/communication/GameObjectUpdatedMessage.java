package shared.model.communication;

/**
 * Message which represents a game object whose position has been updated.
 */
public class GameObjectUpdatedMessage extends Message implements IMessage
{
	// the row of the object
	private int row;
	
	// the column of the object
	private int column;
	
	/**
	 * Constructor.
	 * @param gameObjectIdentifier The unique identifier of the game object.
	 * @param row The new row of the game object.
	 * @param column The new column of the game object.
	 * @throws IllegalArgumentException Thrown if row or column is less than 0.
	 */
	public GameObjectUpdatedMessage(int gameObjectIdentifier, int row, 
		int column)
	{
		super(gameObjectIdentifier);
		
		if (row < 0 || column < 0)
			throw new IllegalArgumentException();
		
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Returns the new row of the game object. 
	 * @return The new row of the game object.
	 */
	public int row()
	{
		return row;
	}
	
	/**
	 * Returns the new column of the game object.
	 * @return The new column of the game object.
	 */
	public int column()
	{
		return column;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return "GAME_OBJECT_POSITION_UPDATED " + gameObjectIdentifier() + " " + row() + " " + column();
	}
}
