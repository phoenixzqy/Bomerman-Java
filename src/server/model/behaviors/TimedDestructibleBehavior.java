package server.model.behaviors;

import server.model.DestructionAction;

/**
 * This class represents the IDestructableBehavior of an destructible object.
 *
 */
public class TimedDestructibleBehavior implements IDestructionBehavior
{
	// the number of steps until destruction
	private int numberOfStepsUntilDestruction;
	
	// the destruction action for the object
	private DestructionAction destructionAction;
	
	/**
	 * Creates a new DestructableBehavior with the provided step number of steps until destruction and 
	 * destruction action.
	 * @param numberOfStepsUntilDestruction The number of steps until object is destroyed.
	 * @param destructionAction The destruction action of the object.
	 */
	public TimedDestructibleBehavior(int numberOfStepsUntilDestruction, 
			DestructionAction destructionAction)
	{
		if (destructionAction == null)
			throw new NullPointerException();
		
		if (numberOfStepsUntilDestruction < 0)
			throw new IllegalArgumentException();
		
		this.numberOfStepsUntilDestruction = numberOfStepsUntilDestruction;
		this.destructionAction = destructionAction;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int numberOfStepsUntilDestruction() 
	{
	
		return numberOfStepsUntilDestruction;
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
	public boolean destructible() 
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void decrementNumberOfStepsUntilDestruction()
	{

		// decrement the number of steps until the destruction, 
		// throw exceptions if trying to decrement it below 0
		if (numberOfStepsUntilDestruction == 0)
			throw new IllegalStateException();
		
		numberOfStepsUntilDestruction--;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean destructionTimed()
	{
		return true;
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
