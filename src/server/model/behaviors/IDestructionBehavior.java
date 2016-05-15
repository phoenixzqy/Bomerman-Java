package server.model.behaviors;

import server.model.DestructionAction;

/**
 * Behavior which determines what an IGameObject does when it's destroyed.
 */
public interface IDestructionBehavior
{
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
	 * Decrements the number of steps until destruction by one.  If this object is not destructible,
	 * this method does nothing.
	 * @throws IllegalStateException Thrown if the number of steps until destruction is already 0.
	 */
	public void decrementNumberOfStepsUntilDestruction();
	
	/**
	 * Returns true if the destructible behavior is timed and false otherwise.  If the object is not
	 * destructible, the return value of this method is undefined.
	 * @return True if the destructible behavior is timed and false otherwise.  If the object is not
	 * destructible, the return value of this method is undefined.
	 */
	public boolean destructionTimed();
	
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
