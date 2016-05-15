package server.model.behaviors;

import server.model.IGameObject;

/**
 * This class represents the IOwnedshipBehavior of a owned object.
 *
 */
public class OwnedBehavior implements IOwnershipBehavior
{
	// the identifier of the owner
	private IGameObject owner;
	
	/**
	 * Creates a new OwnedBehaior with the provided owner.
	 * @param owner The owner of this OwnedBehavior.
	 */
	public OwnedBehavior(IGameObject owner)
	{
		if (owner == null)
			throw new NullPointerException();
		
		this.owner = owner;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasOwner() 
	{
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameObject owner() 
	{
		if (!hasOwner())
		{
			return null;
		}
		return owner;
	}

}
