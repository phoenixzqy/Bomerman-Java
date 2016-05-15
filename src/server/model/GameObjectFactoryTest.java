package server.model;

import org.junit.Before; 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import server.model.behaviors.CanNotPlaceBombBehavior;
import server.model.behaviors.CanPlaceBombBehavior;
import server.model.behaviors.DestructibleBehavior;
import server.model.behaviors.IBombBehavior;
import server.model.behaviors.IDestructionBehavior;
import server.model.behaviors.IMobilityBehavior;
import server.model.behaviors.IOwnershipBehavior;
import server.model.behaviors.IScoreBehavior;
import server.model.behaviors.ISolidityBehavior;
import server.model.behaviors.ImmobileBehavior;
import server.model.behaviors.IndestructibleBehavior;
import server.model.behaviors.NoScoreBehavior;
import server.model.behaviors.TimedDestructibleBehavior;
import server.model.behaviors.MobileBehavior;
import server.model.behaviors.OwnedBehavior;
import server.model.behaviors.ScoreBehavior;
import server.model.behaviors.SolidBehavior;
import server.model.behaviors.UnownedBehavior;
import server.model.behaviors.UnsolidBehavior;
import shared.model.GameObjectType;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * A test class for GameObjectFactory class
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GameObjectFactory.class)
public class GameObjectFactoryTest
{
	private GameObjectFactory factory;
	
	/**
	 *Set up the new instance for
	 */
	@Before
	public void Setup()
	{
		factory = new GameObjectFactory();
	}
	
	/**
	 * Test if createPlayer method return right gameObject.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void creatPlayerTest() throws Exception
	{
		IMobilityBehavior mobilityBehavior = new MobileBehavior();
		ISolidityBehavior solidityBehavior = new SolidBehavior();
		IDestructionBehavior destructibleBehavior = new DestructibleBehavior(10, DestructionAction.RESPAWN);
		IScoreBehavior scoreBehavior = new ScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new UnownedBehavior();
		IBombBehavior bombBehavior = new CanPlaceBombBehavior(3);
		GameObject testGameObject = new GameObject(GameObjectType.PLAYER, mobilityBehavior, solidityBehavior, 
				destructibleBehavior, scoreBehavior, ownershipBehavior, bombBehavior);
		
		
		assertEquals(testGameObject.canMove(), factory.createPlayer().canMove());
		assertEquals(testGameObject.canPlaceBomb(), factory.createPlayer().canPlaceBomb());
		assertEquals(testGameObject.destructible(), factory.createPlayer().destructible());
		assertEquals(testGameObject.hasOwner(), factory.createPlayer().hasOwner());
		assertEquals(testGameObject.hasScore(), factory.createPlayer().hasScore());
		assertEquals(testGameObject.solid(), factory.createPlayer().solid());
	}


	
	/**
	 * Test if createUnbreakBlock method return right gameObject.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void creatUnbreakBlockTest() throws Exception
	{
		// create the behaviors
		IMobilityBehavior immobilityBehavior = new ImmobileBehavior();
		ISolidityBehavior solidityBehavior = new SolidBehavior();
		IDestructionBehavior destructibleBehavior = new IndestructibleBehavior();
		IScoreBehavior noScoreBehavior = new NoScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new UnownedBehavior();
		IBombBehavior bombBehavior = new CanNotPlaceBombBehavior();
		GameObject testGameObject = new GameObject(GameObjectType.UNBREAKABLE_BLOCK, immobilityBehavior, solidityBehavior, 
				destructibleBehavior, noScoreBehavior, ownershipBehavior, bombBehavior);

		assertEquals(testGameObject.canMove(), factory.createUnbreakableBlock().canMove());
		assertEquals(testGameObject.canPlaceBomb(), factory.createUnbreakableBlock().canPlaceBomb());
		assertEquals(testGameObject.destructible(), factory.createUnbreakableBlock().destructible());
		assertEquals(testGameObject.hasOwner(), factory.createUnbreakableBlock().hasOwner());
		assertEquals(testGameObject.hasScore(), factory.createUnbreakableBlock().hasScore());
		assertEquals(testGameObject.solid(), factory.createUnbreakableBlock().solid());

	}
	
	
	/**
	 * Test if createBreakableBlock method return right gameObject.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void creatBreakableBlockTest() throws Exception
	{
		// create the behaviors
		IMobilityBehavior immobilityBehavior = new ImmobileBehavior();
		ISolidityBehavior solidityBehavior = new SolidBehavior();
		IDestructionBehavior destructibleBehavior = new DestructibleBehavior(10,DestructionAction.RESPAWN);
		IScoreBehavior noScoreBehavior = new NoScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new UnownedBehavior();
		IBombBehavior bombBehavior = new CanNotPlaceBombBehavior();
		
		GameObject testGameObject = new GameObject(GameObjectType.BREAKABLE_BLOCK, immobilityBehavior, solidityBehavior, 
				destructibleBehavior, noScoreBehavior, ownershipBehavior, bombBehavior);
		
		assertEquals(testGameObject.canMove(), factory.createBreakableBlock().canMove());
		assertEquals(testGameObject.canPlaceBomb(), factory.createBreakableBlock().canPlaceBomb());
		assertEquals(testGameObject.destructible(), factory.createBreakableBlock().destructible());
		assertEquals(testGameObject.hasOwner(), factory.createBreakableBlock().hasOwner());
		assertEquals(testGameObject.hasScore(), factory.createBreakableBlock().hasScore());
		assertEquals(testGameObject.solid(), factory.createBreakableBlock().solid());
	}
	
	
	/**
	 * Test if createBomb method return right gameObject.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void creatBombTest() throws Exception
	{
		GameObject mockGameObject = mock(GameObject.class);
		// create the behaviors
		IMobilityBehavior mobilityBehavior = new MobileBehavior();
		ISolidityBehavior solidityBehavior = new SolidBehavior();
		IDestructionBehavior destructibleBehavior = new TimedDestructibleBehavior(
				18, DestructionAction.EXPLODE);
		IScoreBehavior scoreBehavior = new ScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new OwnedBehavior(mockGameObject);
		IBombBehavior bombBehavior = new CanNotPlaceBombBehavior();
		GameObject testGameObject = new GameObject(GameObjectType.BOMB, mobilityBehavior, solidityBehavior, 
				destructibleBehavior, scoreBehavior, ownershipBehavior, bombBehavior);
		
		
		assertEquals(testGameObject.canMove(), factory.createBomb(mockGameObject).canMove());
		assertEquals(testGameObject.canPlaceBomb(), factory.createBomb(mockGameObject).canPlaceBomb());
		assertEquals(testGameObject.destructible(), factory.createBomb(mockGameObject).destructible());
		assertEquals(testGameObject.hasOwner(), factory.createBomb(mockGameObject).hasOwner());
		assertEquals(testGameObject.hasScore(), factory.createBomb(mockGameObject).hasScore());
		assertEquals(testGameObject.solid(), factory.createBomb(mockGameObject).solid());
		assertEquals(testGameObject.owner(), factory.createBomb(mockGameObject).owner());
		assertEquals(testGameObject.destructionAction(), factory.createBomb(mockGameObject).destructionAction());
		assertEquals(testGameObject.numberOfStepsUntilDestruction(), factory.createBomb(mockGameObject).numberOfStepsUntilDestruction());
	}
	
	
	/**
	 * Test if createExplosion method return right gameObject.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void creatExplosionTest() throws Exception
	{
		GameObject mockGameObject = mock(GameObject.class);
		// create the behaviors
		// create the behaviors
		IMobilityBehavior immobilityBehavior = new ImmobileBehavior();
		ISolidityBehavior unSolidityBehavior = new UnsolidBehavior();
		IDestructionBehavior destructibleBehavior = new TimedDestructibleBehavior(
				5,DestructionAction.DISAPPEAR);
		IScoreBehavior scoreBehavior = new ScoreBehavior();
		IOwnershipBehavior ownershipBehavior = new OwnedBehavior(mockGameObject);
		IBombBehavior bombBehavior = new CanNotPlaceBombBehavior();
		GameObject testGameObject = new GameObject(GameObjectType.EXPLOSION, immobilityBehavior, unSolidityBehavior, 
				destructibleBehavior, scoreBehavior, ownershipBehavior, bombBehavior);

		
		
		assertEquals(testGameObject.canMove(), factory.createExplosion(mockGameObject).canMove());
		assertEquals(testGameObject.canPlaceBomb(), factory.createExplosion(mockGameObject).canPlaceBomb());
		assertEquals(testGameObject.destructible(), factory.createExplosion(mockGameObject).destructible());
		assertEquals(testGameObject.hasOwner(), factory.createExplosion(mockGameObject).hasOwner());
		assertEquals(testGameObject.hasScore(), factory.createExplosion(mockGameObject).hasScore());
		assertEquals(testGameObject.solid(), factory.createExplosion(mockGameObject).solid());
		assertEquals(testGameObject.owner(), factory.createExplosion(mockGameObject).owner());
		assertEquals(testGameObject.destructionAction(), factory.createExplosion(mockGameObject).destructionAction());
		assertEquals(testGameObject.numberOfStepsUntilDestruction(), factory.createExplosion(mockGameObject).numberOfStepsUntilDestruction());
	}
	
	/**
	 * Test the createExplosion method will throw NullPointerException correctly when the
	 * owner is null. 
	 */
	@Test (expected = NullPointerException.class)
	public void createExplosionNullPointerTest()
	{
		factory.createExplosion(null);
	}
	
	/**
	 * Test the createBomb method will throw NullPointerException correctly when the
	 * owner is null. 
	 */
	@Test (expected = NullPointerException.class)
	public void createBombNullPointerTest()
	{
		factory.createBomb(null);
	}
}
