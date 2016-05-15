package server.model.behaviors;

import server.model.DestructionAction;

/**
 * Implementation of IDestructibleBehavior that is destructible but is not timed.
 */
public class DestructibleBehavior implements IDestructionBehavior
{
	// Record the destructionAction
	private DestructionAction destructionAction;
	
	// Record initial number of steps until respawn.
	private int initialNumberOfStepUntilRespawn;
	
	// Record the current number of step until respawn.
	private int numberOfStepUntilRespawn;
	
	/**
	 * Creates a new DestructibleBehavior.
	 * @param numberOfStepUntilRespawn The number of steps that the destruciton behavior will have to
	 * wait after being destroyed before it can be respawned.
	 * @param destructionAction The destruction action.
	 * @throws NullPointerException Thrown if destructionAction is null.
	 */
	public DestructibleBehavior(int numberOfStepUntilRespawn, DestructionAction destructionAction)
	{
		if (destructionAction == null)
		{
			throw new NullPointerException();
		}
		this.destructionAction = destructionAction;
		this.numberOfStepUntilRespawn = numberOfStepUntilRespawn;
		this.initialNumberOfStepUntilRespawn = numberOfStepUntilRespawn;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean destructible()
	{
			return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public int numberOfStepsUntilDestruction()
	{
			return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public DestructionAction destructionAction()
	{
		return destructionAction;
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementNumberOfStepsUntilDestruction()
	{
		// Do nothing
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
		
		return numberOfStepUntilRespawn;
	}

	/**
	 * {@inheritDoc}
	 */
	public void resetNumberOfStepUntilRespawn() 
	{
		numberOfStepUntilRespawn = initialNumberOfStepUntilRespawn;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementNumberOfStepUntilRespawn() 
	{
		//decrement the number of steps until the player respawn, 
		//throw exceptions if trying to decrement it below 0
		if (numberOfStepUntilRespawn == 0)
		{
			throw new IllegalStateException();
		}
		numberOfStepUntilRespawn--;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void incrementNumberOfStepUntilRespawn() 
	{
		//increment the number of steps until the player respawn, 
		//throw exceptions if trying to increment it over the initial number
		if (numberOfStepUntilRespawn == initialNumberOfStepUntilRespawn)
		{
			throw new IllegalStateException();
		}
		numberOfStepUntilRespawn++;
		
	}
}
