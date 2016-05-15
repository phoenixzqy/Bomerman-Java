package client.model;

import shared.model.GameObjectType;

/**
 * Represents an object in the game. Specifically, an IGameObject must be able
 * to exist within the game board.
 */
public interface IGameObject 
{
	/**
	 * Returns the row this object is currently occupying. If this object is
	 * occupying more than one row, this method returns the row the majority of
	 * this object is occupying.
	 * @return Returns the row this object is currently occupying.
	 */
	public int row();

	/**
	 * Returns the column this object is currently occupying. If this object is
	 * occupying more than one column, this method returns the column the
	 * majority of this object is occupying.
	 * @return The column this object is currently occupying.
	 */
	public int column();
	
	/**
	 * Sets the row of this IGameObject.
	 * @param row The new row.
	 */
	public void setRow(int row);
	
	/**
	 * Sets the column of this IGameObject.
	 * @param column The column of this IGameObject.
	 */
	public void setColumn(int column);

	/**
	 * Returns the type of this IGameObject.
	 * 
	 * @return The type of this IGameObject.
	 */
	public GameObjectType gameObjectType();
	
	/**
	 * Returns a unique identifier for this IGameObject.
	 * @return A unique identifier for this IGameObject.
	 */
	public int identifier();
}
