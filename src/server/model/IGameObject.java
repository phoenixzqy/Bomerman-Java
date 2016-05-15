package server.model;

import shared.model.*;
/**
 * Represents an object in the game.  Specifically, an IGameObject must be able to exist within the 
 * game board.
 */
public interface IGameObject 
{
	/**
	 * Returns an identifier which is unique for this IGameObject.
	 * @return An identifier which is unique for this IGameObject.
	 */
	public int identifier();
	
	/**
	 * Returns the type of the game object.
	 * @return The type of the game object.
	 */
	public GameObjectType type();

	/**
	 * Returns the row this object is currently occupying.  If this object is occupying more than 
	 * one row, this method returns the row the majority of this object is occupying.
	 * @return The row of this IGameObject.
	 */
	public int row();

	/**
	 * Returns the column this object is currently occupying.  If this object is occupying more 
	 * than one column, this method returns the column the majority of this object is occupying.
	 * @return The column of this IGameObject.
	 */
	public int column();
	
	/**
	 * Returns true if this IGameObject is occupying a board position and false otherwise.
	 * @return True if this IGameObject is occupying a board position and false otherwise.
	 */
	public boolean onBoard();
	
	/**
	 * Sets the row and column of this IGameObject.  onBoard() should return true after this method is 
	 * called.
	 * @param row The row on which this IGameObject should be placed.
	 * @param column The column on which this IGameObject should be placed.
	 * @throws IllegalArgumentException Thrown if row or column are negative.
	 */
	public void setPosition(int row, int column);
	
	/**
	 * Makes onBoard() return false.
	 */
	public void removeFromBoard();

	/**
	 * Returns true if this IGameObject is solid and false otherwise.
	 * @return True if this IGameObject is solid and false otherwise. 
	 */
	public boolean solid();

	/**
	 * Returns true if this IGameObject has an owner and false otherwise.
	 * @return True if this IGameObject has an owner and false otherwise.
	 */
	public boolean hasOwner();
	
	/**
	 * Returns the game object identifier of the owner of this IGameObject.  If this IGameObject does 
	 * not have an owner, this method's return value is undefined.
	 * @return The game object identifier of the owner game object.
	 */
	public IGameObject owner();

	/**
	 * Returns true if this IGameObject has a score and false otherwise.
	 * @return True if this IGameObject has a score and false otherwise.
	 */
	public boolean hasScore();
	
	/**
	 * Returns the score of this IGameObject.
	 * @return The score of this IGameObject.  If this IGameObject does not have a score, the return
	 * value for this method is undefined.
	 */
	public int score();
	
	/**
	 * Increments the score of this IGameObject.
	 * @throws IllegalStateException Thrown if this IGameObject does not have a score.
	 */
	public void incrementScore();
	
	/**
	 * Decrements the score of this IGameObject.
	 * @throws IllegalStateException Thrown if t IGameObject does not have a score.
	 */
	public void decrementScore();

	/**
	 * Returns true if this IGameObject can be destroyed and false otherwise.
	 * @return True if this IGameObject can be destroyed and false otherwise.
	 */
	public boolean destructible();
	
	/**
	 * Returns the number of steps until this object should be destroyed.  If this object cannot be
	 * destroyed, the return value for this method is undefined.
	 * @return The number of steps until this object should be destroyed.
	 */
	public int numberOfStepsUntilDestruction();
	
	/**
	 * Returns the DestructionAction for this IGameObject.  If this object cannot be destroyed, the
	 * return value for this method should be NONE.
	 * @return The DestructionAction for this IGameObject.
	 */
	public DestructionAction destructionAction();
	
	/**
	 * Returns true if this IGameObject can move and false otherwise.
	 * @return True if this IGameObject can move and false otherwise.
	 */
	public boolean canMove();
	
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
	 * Returns the direction this IGameObject should move.
	 * @return The direction this IGameObject should move.  Returns NONE if the IGameObject can't move.
	 */
	public Direction directionToMove();
	
	/**
	 * Decrements the number of steps until destruction by one.  If this object is not destructible,
	 * this method does nothing.
	 * @throws IllegalStateException Thrown if the number of steps until destruction is already 0.
	 */
	public void decrementNumberOfStepsUntilDestruction();
	
	/**
	 * Returns true if this IGameObject should attempt to place a bomb at its current location and
	 * false otherwise.
	 * @return True if this IGameObject should attempt to place a bomb at its current location and
	 * false otherwise.
	 */
	public boolean placeBomb();
	
	/**
	 * Sets the value of place bomb.
	 * @param placeBomb True if a bomb should be placed and false otherwise.
	 */
	public void setPlaceBomb(boolean placeBomb);

	/**
	 * Sets shouldPlaceBomb to return false.
	 * Returns true if this IGameObject can place a bomb and false otherwise.
	 * @return True if this IGameObject can place a bomb and false otherwise.
	 */
	public boolean canPlaceBomb();
	
	/**
	 * Returns true if the destructible behavior is timed and false otherwise.  If the object is not
	 * destructible, the return value of this method is undefined.
	 * @return True if the destructible behavior is timed and false otherwise.  If the object is not
	 * destructible, the return value of this method is undefined.
	 */
	public boolean destructionTimed();
	
	/**
	 * Return the current number of bombs
	 * @return bombCout Number of bomb 
	 */
	public int bombCount();
	
	/**
	 * Decrements the number of bomb until destruction by one. If the number of bomb is already 0, then
	 * do nothing. If this object can not place bomb,this method does nothing.
	 * @throws IllegalStateException Thrown if the number of bomb count is already 0.
	 */
	public void decrementBombCount();
	
	/**
	 * Increments the number of bomb until destruction by one. If the current number of bomb is the max number,
	 * then do nothing. If this object can not place bomb, this method does nothing.
	 * @throws IllegalStateException Thrown if the number of bomb count is already max.
	 */
	public void incrementBomb();
	
	/**
	 * Return the number of steps until this object is respawn. If this object cannot be destroyed, the 
	 * return value for this method is undefined
	 * @return the number of steps until this object should be respawn.
	 */
	public int numberOfStepUntilRespawn();
	
	/**
	 * Reset the number of steps until this object is respawn to its initial number. If this object cannot be destroyed,
	 * then do nothing.
	 */
	public void resetNumberOfStepUntilRespawn();
	
	/**
	 * Decrements the number of steps until respawn by one. If this object is not destructible,
	 * this method does nothing.
	 * @throws IllegalStateException Thrown if the number of steps until respawn is already 0.
	 */
	public void decrementNumberOfStepUntilRespawn();
	
	/**
	 * Increments the number of steps until respawn by one. If this object is not destructible,
	 * this method does nothing.
	 * @throws IllegalStateException Thrown if the number of steps until respawn is already 0.
	 */
	public void incrementNumberOfStepUntilRespawn();
}
