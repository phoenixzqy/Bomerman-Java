package server.model;

/**
 * The game board, which contains the game objects.
 */
public interface IBoard
{
	/**
	 * Returns the number of rows in this IBoard.
	 * @return The number of rows in this IBoard.
	 */
	int numberOfRows();
	
	/**
	 * Returns the number of columns in this IBoard.
	 * @return The number of columns in this IBoard.
	 */
	int numberOfColumns();
	
	/**
	 * Returns the game objects at the specified row and column.
	 * @param row The row of the game objects.
	 * @param column The column of the game objects.
	 * @return The game objects at the specified row and column.  If no object is at the specified row
	 * and column, this method returns an empty array.
	 * @throws IllegalArgumentException Thrown if row is less than 0 or greater than or equal to the
	 * number of rows in this IBoard, or if the column is less than 0 or greater than or equal to the
	 * number of columns in this IBoard.
	 */
	public IGameObject[] gameObjectsAtSpace(int row, int column);
	
	/**
	 * Determines if the provided game object can move to the specified row and column.  If a game
	 * object is solid, it may not occupy the same space as another solid object.  A solid and a
	 * one or more non-solid objects or multiple non-solid objects may occupy the same space.
	 * @param row The row to check.
	 * @param column The column to check.
	 * @param gameObject The game object which should be checked.
	 * @return Returns true if the provided game object can occupy the specified row and column.
	 * @throws NullPointerException Thrown if gameObject is null.
	 * @throws IllegalArgumentException Thrown if row is less than 0 or greater than or equal to the
	 * number of rows in this IBoard, or if the column is less than 0 or greater than or equal to the
	 * number of columns in this IBoard.
	 */
	public boolean canMoveToSpace(int row, int column, IGameObject gameObject);
	
	/**
	 * Moves the specified game object to the provided space.  This method will automatically remove
	 * the object from its old occupied space, if it is occupying a space.  This method will also
	 * set the row, column and occupyingSpace properties of the provided game object.
	 * @param row The new row of the object.
	 * @param column The new column of the object.
	 * @param gameObject The game object to move.
	 * @throws NullPointerException Thrown if gameObject is null.
	 * @throws IllegalArgumentException Thrown if row is less than 0 or greater than or equal to the
	 * number of rows in this IBoard, or if the column is less than 0 or greater than or equal to the
	 * number of columns in this IBoard.  This exception may also be thrown if gameObject cannot be
	 * moved to the specified row or column.
	 */
	public void moveGameObjectToSpace(int row, int column, IGameObject gameObject);
	
	/**
	 * Removes the specified game object from this IBoard.  This method sets the occupyingSpace 
	 * property of the game object to false.
	 * @param gameObject The gameObject to remove from this IBoard.
	 * @throws NullPointerException Thrown if gameObject is null.
	 * @throws IllegalArgumentException Thrown if this gameObject is not currently occupying a space
	 * in this IBoard.
	 */
	public void removeGameObject(IGameObject gameObject);
	
	/**
	 * Returns true if the specified space is does not contain any objects and false otherwise.
	 * @param row The row of the space.
	 * @param column The column of the space.
	 * @return True if the specified space is does not contain any objects and false otherwise.
	 * @throws IllegalArgumentException Thrown if row is less than 0 or greater than or equal to the
	 * number of rows or if column is less than 0 or greater than or equal to the number of rows.
	 */
	public boolean spaceEmpty(int row, int column);
}
