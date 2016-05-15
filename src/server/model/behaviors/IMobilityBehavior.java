package server.model.behaviors;

import shared.model.*;

/**
 * Behavior which determines how a IGameObject moves.
 */
public interface IMobilityBehavior
{
	/**
	 * Returns the column of the IGameObject.
	 * @return The column of the IGameObject.
	 */
	public int column();
	
	/**
	 * Returns the row of the IGameObject.
	 * @return The row of the IGameObject.
	 */
	public int row();
	
	/**
	 * Sets the column of the IGameObject.
	 * @param row The new row of the IGameObject.
	 * @param column The new column of the IGameObject.
	 */
	public void setPosition(int row, int column);
	
	/**
	 * Returns true if the IGameObject can move and false otherwise.
	 * @return True if the IGameObject can move and false otherwise.
	 */
	public boolean canMove();

	/**
	 * Makes onBoard() return false.
	 */
	public void removeFromBoard();

	/**
	 * Returns true if this IGameObject is occupying a board position and false otherwise.
	 * @return True if this IGameObject is occupying a board position and false otherwise.
	 */
	public boolean onBoard();
	
	/**
	 * Indicates the IGameObject should start moving in the provided direction.  If the IGameObject
	 * is already moving in that direction, this method does nothing.  If the IGameObject moving in 
	 * another direction, it should stop moving.
	 * @param direction The new direction to move.
	 * @throws IllegalArgumentException Thrown if direction is NONE.
	 * @throws NullPointerException Thrown if direction is null.
	 * @throws IllegalStateException Thrown if the IGameObject can't move.
	 */
	public void startMovingInDirection(Direction direction);
	
	/**
	 * Indicates the IGameObject should stop moving in the provided direction.  If the IGameObject
	 * has already stopped moving in the provided direction, this method shouldn't do anything.  If
	 * the IGameObject was stopped because it was moving in multiple directions, it should start
	 * moving again if it is only moving in one direction.
	 * @param direction The new direction to move.
	 * @throws IllegalArgumentException Thrown if direction is NONE.
	 * @throws NullPointerException Thrown if direction is null.
	 * @throws IllegalStateException Thrown if the IGameObject can't move.
	 */
	public void stopMovingInDirection(Direction direction);
	
	/**
	 * Returns the direction the IGameObject should move.
	 * @return The direction the IGameObject should move.  Returns NONE if the IGameObject can't move.
	 */
	public Direction directionToMove();
}
