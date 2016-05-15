package server.model.behaviors;

import server.model.DestructionAction;

/**
 * This class represents the IDestructionBehavior of an indestructible object.
 *
 */
public class IndestructibleBehavior implements IDestructionBehavior
{
	/**
	 * {@inheritDoc}
	 */
	public int numberOfStepsUntilDestruction() 
	{
		return (Integer) null;
	}

	/**
	 * {@inheritDoc}
	 */
	public DestructionAction destructionAction() 
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean destructible() 
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementNumberOfStepsUntilDestruction()
	{
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean destructionTimed()
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public int numberOfStepUntilRespawn() 
	{
		// Do Nothing
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void resetNumberOfStepUntilRespawn() 
	{
		// Do Nothing
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementNumberOfStepUntilRespawn() 
	{
		// Do Nothing
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void incrementNumberOfStepUntilRespawn() 
	{
		// Do Nothing
		
	}
}
