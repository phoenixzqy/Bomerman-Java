package server.model.behaviors;

import server.model.IGameObject;

/**
 * Behavior which determines how the IGameObject is owned.
 */
public interface IOwnershipBehavior
{
	/**
	 * Returns true if the IGameObject has an owner and false otherwise.
	 * @return True if the IGameObject has an owner and false otherwise.
	 */
	public boolean hasOwner();
	
	/**
	 * Returns the game object owner of the IGameObject.  If the IGameObject does not have an owner, 
	 * this method's return value is null.
	 * @return The owner game object.
	 */
	public IGameObject owner();
}
