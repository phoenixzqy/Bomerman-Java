package server.model.behaviors;

import server.model.IGameObject;

/**
 * This class represents the IOwnedshipBehavior of a unowned object.
 */
public class UnownedBehavior implements IOwnershipBehavior
{
	/**
	 * {@inheritDoc}
	 */
	public boolean hasOwner() 
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameObject owner() 
	{
		return null;
	}
}
