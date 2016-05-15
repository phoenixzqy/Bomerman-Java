package server.model;

import server.model.behaviors.*;
import server.model.IGameObject;
import shared.model.GameObjectType;

/**
 * A basic implementation of IGameObjectFactory which used to generate 
 * different type GameObject.
 *
 */
public class GameObjectFactory implements IGameObjectFactory
{
	/**
	 * The number of steps before a bomb is destroyed.
	 */
	private static final int BOMB_INITIAL_NUMBER_OF_STEPS_UNTIL_DESTRUCTION = 18;
	
	/**
	 * The number of steps before a explosion disappear
	 */
	private static final int EXPLOSION_INITIAL_NUMBER_OF_STEPS_UNTIL_DESTRUCTION = 5;
	
	/**
	 * The initial max number of bomb that player can put.
	 */
	private static final int BOMB_MAX_COUNT_NUMBER = 3;
	
	/**
	 * The initial max number of player to respawn
	 */
	private static final int NUMBER_OF_STEPS_UNTIL_RESPAWN = 10;
	
	/**
	 * {@inheritDoc}
	 */
	public IGameObject createPlayer() 
	{
		// create the behaviors
		IMobilityBehavior mobilityBehavior = new MobileBehavior();
		ISolidityBehavior solidityBehavior = new SolidBehavior();
		IDestructionBehavior destructibleBehavior = new DestructibleBehavior(
				NUMBER_OF_STEPS_UNTIL_RESPAWN,DestructionAction.RESPAWN);
		IScoreBehavior scoreBehavior = new ScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new UnownedBehavior();
		IBombBehavior bombBehavior = new CanPlaceBombBehavior(BOMB_MAX_COUNT_NUMBER);
		
		// compose behaviors into game object
		return new GameObject(GameObjectType.PLAYER, mobilityBehavior, solidityBehavior, 
				destructibleBehavior, scoreBehavior, ownershipBehavior, bombBehavior);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGameObject createUnbreakableBlock() 
	{
		// create the behaviors
		IMobilityBehavior immobilityBehavior = new ImmobileBehavior();
		ISolidityBehavior solidityBehavior = new SolidBehavior();
		IDestructionBehavior destructibleBehavior = new IndestructibleBehavior();
		IScoreBehavior noScoreBehavior = new NoScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new UnownedBehavior();
		IBombBehavior bombBehavior = new CanNotPlaceBombBehavior();
		
		// compose behaviors into game object
		return new GameObject(GameObjectType.UNBREAKABLE_BLOCK, immobilityBehavior, solidityBehavior, 
				destructibleBehavior, noScoreBehavior, ownershipBehavior, bombBehavior);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGameObject createBreakableBlock() 
	{
		// create the behaviors
		IMobilityBehavior immobilityBehavior = new ImmobileBehavior();
		ISolidityBehavior solidityBehavior = new SolidBehavior();
		IDestructionBehavior destructibleBehavior = new DestructibleBehavior(
				NUMBER_OF_STEPS_UNTIL_RESPAWN,DestructionAction.DISAPPEAR);
		IScoreBehavior noScoreBehavior = new NoScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new UnownedBehavior();
		IBombBehavior bombBehavior = new CanNotPlaceBombBehavior();
		
		// compose behaviors into game object
		return new GameObject(GameObjectType.BREAKABLE_BLOCK, immobilityBehavior, solidityBehavior, 
				destructibleBehavior, noScoreBehavior, ownershipBehavior, bombBehavior);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGameObject createBomb(IGameObject owner) 
	{
		if (owner == null)
		{
			throw new NullPointerException(); 
		}
		// create the behaviors
		IMobilityBehavior mobilityBehavior = new MobileBehavior();
		ISolidityBehavior solidityBehavior = new SolidBehavior();
		IDestructionBehavior destructibleBehavior = new TimedDestructibleBehavior(
				BOMB_INITIAL_NUMBER_OF_STEPS_UNTIL_DESTRUCTION, DestructionAction.EXPLODE);
		IScoreBehavior scoreBehavior = new ScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new OwnedBehavior(owner);
		IBombBehavior bombBehavior = new CanNotPlaceBombBehavior();
		
		// compose behaviors into game object
		return new GameObject(GameObjectType.BOMB, mobilityBehavior, solidityBehavior, 
				destructibleBehavior, scoreBehavior, ownershipBehavior, bombBehavior);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGameObject createExplosion(IGameObject owner) 
	{
		if (owner == null)
		{
			throw new NullPointerException(); 
		}
		// create the behaviors
		IMobilityBehavior immobilityBehavior = new ImmobileBehavior();
		ISolidityBehavior unSolidityBehavior = new UnsolidBehavior();
		IDestructionBehavior destructibleBehavior = new TimedDestructibleBehavior(
				EXPLOSION_INITIAL_NUMBER_OF_STEPS_UNTIL_DESTRUCTION,DestructionAction.DISAPPEAR);
		IScoreBehavior scoreBehavior = new ScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new OwnedBehavior(owner);
		IBombBehavior bombBehavior = new CanNotPlaceBombBehavior();
		
		// compose behaviors into game object
		return new GameObject(GameObjectType.EXPLOSION, immobilityBehavior, unSolidityBehavior, 
				destructibleBehavior, scoreBehavior, ownershipBehavior, bombBehavior);
	}
}
