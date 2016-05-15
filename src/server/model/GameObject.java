package server.model;

import server.model.behaviors.*;
import shared.model.*;


/**
 * A basic implementation of IGameObject which uses provided behaviors to implement its members.
 */
public class GameObject implements IGameObject
{
	// the game object identifier
	private final int identifier;
	
	// a static counter used to ensure the game object identifier is unique
	private static int identifierCounter;
	
	// the game object type
	private final GameObjectType type;
	
	// the mobility behavior
	private final IMobilityBehavior mobilityBehavior;
	
	// the solidity behavior
	private final ISolidityBehavior solidityBehavior;
	
	// the destruction behavior
	private final IDestructionBehavior destructionBehavior;
	
	// the ownership behavior
	private final IOwnershipBehavior ownershipBehavior;
	
	// the score behavior
	private final IScoreBehavior scoreBehavior;

	// the bomb behavior
	private final IBombBehavior bombBehavior;
	
	/**
	 * A constructor which creates game objects using various provided behaviors.
	 * @param type The game object type.
	 * @param scoreBehavior The score behavior.
	 * @param mobilityBehavior The mobility behavior.
	 * @param solidityBehavior The solidity behavior.
	 * @param ownershipBehavior The ownership behavior.
	 * @param destructionBehavior The destruction behavior.
	 * @param bombBehavior The bomb behavior.
	 * @throws NullPointerException Thrown if any of the provided behaviors are null.
	 */
	public GameObject(GameObjectType type, IMobilityBehavior mobilityBehavior, 
			ISolidityBehavior solidityBehavior,IDestructionBehavior destructionBehavior, 
			IScoreBehavior scoreBehavior, IOwnershipBehavior ownershipBehavior, 
			IBombBehavior bombBehavior)
	{
		// check for null
		if (type == null || mobilityBehavior == null || solidityBehavior == null || 
				destructionBehavior == null || scoreBehavior == null || ownershipBehavior == null
				|| bombBehavior == null)
			throw new NullPointerException();
		
		// set the behaviors
		this.type = type;
		this.mobilityBehavior = mobilityBehavior;
		this.solidityBehavior = solidityBehavior;
		this.destructionBehavior = destructionBehavior;
		this.scoreBehavior = scoreBehavior;
		this.ownershipBehavior = ownershipBehavior;
		this.bombBehavior = bombBehavior;
		
		// set the game identifier
		identifier = identifierCounter;
		// automatically add 1 to the identifier counter when the new game object is created
		identifierCounter++;
	}

	/**
	 * {@inheritDoc}
	 */
	public int identifier()
	{
		return identifier;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public GameObjectType type()
	{
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	public int row()
	{
		return mobilityBehavior.row();
	}

	/**
	 * {@inheritDoc}
	 */
	public int column()
	{
		return mobilityBehavior.column();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean onBoard()
	{
		return mobilityBehavior.onBoard();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPosition(int row, int column)
	{
		mobilityBehavior.setPosition(row, column);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeFromBoard()
	{
		mobilityBehavior.removeFromBoard();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean solid()
	{
		return solidityBehavior.solid();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasOwner()
	{
		return ownershipBehavior.hasOwner();
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameObject owner()
	{
		return ownershipBehavior.owner();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasScore()
	{
		return scoreBehavior.hasScore();
	}

	/**
	 * {@inheritDoc}
	 */
	public int score()
	{
		return scoreBehavior.score();
	}

	/**
	 * {@inheritDoc}
	 */
	public void incrementScore()
	{
		scoreBehavior.incrementScore();
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementScore()
	{
		scoreBehavior.decrementScore();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean destructible()
	{
		return destructionBehavior.destructible();
	}

	/**
	 * {@inheritDoc}
	 */
	public int numberOfStepsUntilDestruction()
	{
		return destructionBehavior.numberOfStepsUntilDestruction();
	}

	/**
	 * {@inheritDoc}
	 */
	public DestructionAction destructionAction()
	{
		return destructionBehavior.destructionAction();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canMove()
	{
		return mobilityBehavior.canMove();
	}

	/**
	 * {@inheritDoc}
	 */
	public void startMovingInDirection(Direction direction)
	{
		mobilityBehavior.startMovingInDirection(direction);
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopMovingInDirection(Direction direction)
	{
		mobilityBehavior.stopMovingInDirection(direction);

	}

	/**
	 * {@inheritDoc}
	 */
	public Direction directionToMove()
	{
		return mobilityBehavior.directionToMove();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void decrementNumberOfStepsUntilDestruction()
	{
		destructionBehavior.decrementNumberOfStepsUntilDestruction();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean placeBomb()
	{
		return bombBehavior.placeBomb();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPlaceBomb(boolean placeBomb)
	{
		bombBehavior.setPlaceBomb(placeBomb);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canPlaceBomb()
	{
		return bombBehavior.canPlaceBomb();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean destructionTimed()
	{
		return destructionBehavior.destructionTimed();
	}

	/**
	 * {@inheritDoc}
	 */
	public int bombCount() 
	{
		return bombBehavior.countBomb();
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementBombCount() 
	{
		bombBehavior.decrementBomb();
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void incrementBomb() 
	{
		bombBehavior.incrementBomb();
		
	}

	/**
	 * {@inheritDoc}
	 */
	public int numberOfStepUntilRespawn() 
	{
		
		return destructionBehavior.numberOfStepUntilRespawn();
	}

	/**
	 * {@inheritDoc}
	 */
	public void resetNumberOfStepUntilRespawn() 
	{
		destructionBehavior.resetNumberOfStepUntilRespawn();
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void decrementNumberOfStepUntilRespawn() 
	{
		destructionBehavior.decrementNumberOfStepUntilRespawn();
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void incrementNumberOfStepUntilRespawn() 
	{
		destructionBehavior.incrementNumberOfStepUntilRespawn();
		
	}
}

