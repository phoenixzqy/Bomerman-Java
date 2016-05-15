package server.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import shared.model.Direction;
import shared.model.GameObjectType;
import shared.model.Key;
import shared.model.KeyAction;
import shared.model.communication.*;

/**
 * Game logic class, which control the game objects behaviors. It receives message from clients
 * through communicator, and sends the reaction result back to clients through communicator.
 */

public class Game implements IGame
{
	
	// the server communicator
	private final IServerCommunicator serverCommunicator;
	
	// the game object factory
	private final IGameObjectFactory gameObjectFactory;
	
	// the game board
	private IBoard board;
	
	/**
	 * The default number of rows in the game.
	 */
	public static final int DEFAULT_NUMBER_OF_ROWS = 17;
	
	/**
	 * The default number of columns in the game.
	 */
	public static final int DEFAULT_NUMBER_OF_COLUMNS = 23;
	
	// the hash map to store the game objects
	
	private HashMap<Integer, IGameObject> gameObjects;
	
	// the number of remaining steps in the game
	private int remainingSteps;
	
	/**
	 * Creates a new Game. When created, this game will automatically start running.
	 * 
	 * @param serverCommunicator The server communicator used to host the game.
	 * @param gameObjectFactory The factory used to create game objects.
	 * @throws NullPointerException Thrown if the provided server communicator or game object factory
	 * is null.
	 * @throws IllegalArgumentException Thrown if the provided server communicator is listening for
	 * incoming connections.
	 * @throws CommunicationException Thrown if an error occurs when communicating with the clients.
	 */
	public Game(IServerCommunicator serverCommunicator, final IGameObjectFactory gameObjectFactory)
			throws CommunicationException
	{
		
		this(serverCommunicator, gameObjectFactory, null);
		
		// set up the game objects
		setUpUnbreakableBlocks();
		setUpBreakableBlocks();
		setUpPlayers();
		
		// send the start game message
		serverCommunicator.sendMessages(new GameMessage(GameMessage.Action.START, serverCommunicator
				.numberOfConnectedCommunicators()));
		
		// send the created objects messages to the client
		for (IGameObject gameObject : gameObjects.values())
		{
			// create the game objects which is on board
			if (gameObject.onBoard())
			{
				IMessage message = new GameObjectCreatedMessage(gameObject.identifier(), gameObject.type(),
						gameObject.row(), gameObject.column());
				serverCommunicator.sendMessages(message);
			}
		}
	}
	
	/**
	 * Creates a new instance of Game. This method is exposed for testing purposes only. It's purpose
	 * is to allow a person to create an instance of Game that does not have the game objects set up.
	 * The game objects will be set to the provided game objects array. THIS METHOD SHOULD NOT BE USED
	 * IN PRODUCTION CODE.
	 * 
	 * @param serverCommunicator The server communicator used to host the game.
	 * @param gameObjectFactory The factory used to create game objects.
	 * @param gameObjects The game objects which should be placed in this game. If this parameter is
	 * null, then no game objects will be initialized.
	 * @throws NullPointerException Thrown if the provided server communicator or game object factory
	 * is null.
	 * @throws IllegalArgumentException Thrown if the provided server communicator is listening for
	 * incoming connections.
	 * @throws CommunicationException Thrown if an error occurs when communicating with the clients.
	 */
	protected Game(IServerCommunicator serverCommunicator, IGameObjectFactory gameObjectFactory,
			IGameObject[] gameObjects)
	{
		if (serverCommunicator == null || gameObjectFactory == null)
			throw new NullPointerException();
		
		if (serverCommunicator.listening())
			throw new IllegalArgumentException();
		
		// set up member variables
		this.serverCommunicator = serverCommunicator;
		this.gameObjectFactory = gameObjectFactory;
		this.board = new Board(DEFAULT_NUMBER_OF_ROWS, DEFAULT_NUMBER_OF_COLUMNS);
		this.gameObjects = new HashMap<Integer, IGameObject>();
		remainingSteps = 1800;

		
		// add the game objects if they are provided
		if (gameObjects != null)
		{
			for (IGameObject gameObject : gameObjects)
			{
				this.gameObjects.put(gameObject.identifier(), gameObject);
			}
		}
	}
	
	/**
	 * Sets up the unbreakable block on the board. This method will lay them out in a grid pattern.
	 * 
	 * @throws CommunicationException Thrown if an error occurs while communicating with the client.
	 */
	private void setUpUnbreakableBlocks() throws CommunicationException
	{
		// add the unbreakable block around the edge of the board
		
		for (int row = 0; row < DEFAULT_NUMBER_OF_ROWS; row++)
		{
			for (int column = 0; column < DEFAULT_NUMBER_OF_COLUMNS; column++)
			{
				/*
				 * If the block is on the edge of the board or if it occupies an even space for both the row
				 * and the column, the place an unbreakable block there.
				 */
				if (row == 0 || row == (DEFAULT_NUMBER_OF_ROWS - 1) || column == 0
						|| column == (DEFAULT_NUMBER_OF_COLUMNS - 1) || (row % 2 == 0 && column % 2 == 0))
				{
					// place the unbreakable block in the game
					IGameObject unbreakableBlock = gameObjectFactory.createUnbreakableBlock();
					gameObjects.put(unbreakableBlock.identifier(), unbreakableBlock);
					board.moveGameObjectToSpace(row, column, unbreakableBlock);
				}
			}
		}
	}
	
	/**
	 * Sets up the breakable blocks, randomly placing them around the board.
	 * 
	 * @throws CommunicationException Thrown if an error occurs while communicating with the client.
	 */
	private void setUpBreakableBlocks() throws CommunicationException
	{
		Random random = new Random();
		// initialized the random row and random column
		int randomRow = -1;
		int randomColumn = -1;
		// initialize the count of breakable blocks
		int countForBreakableBlockGenerator = 0;
		// set up 130 breakable blocks
		while (countForBreakableBlockGenerator < 130)
		{
			// generate the random row between 0-16, the random column between
			// 0-22;
			randomRow = random.nextInt(DEFAULT_NUMBER_OF_ROWS - 2) + 1;
			randomColumn = random.nextInt(DEFAULT_NUMBER_OF_COLUMNS - 2) + 1;
			
			// make sure the player has enough space to place a bomb without getting killed
			if ((randomRow == 1 || randomRow == DEFAULT_NUMBER_OF_ROWS - 2)
					&& (randomColumn <= 2 || randomColumn >= DEFAULT_NUMBER_OF_COLUMNS - 3))
			{
				continue;
			}
			
			if ((randomColumn == 1 || randomColumn == DEFAULT_NUMBER_OF_COLUMNS - 2)
					&& (randomRow <= 2 || randomRow >= DEFAULT_NUMBER_OF_ROWS - 3))
			{
				continue;
			}
			
			// initialize a new breakable block
			IGameObject breakableBlock = gameObjectFactory.createBreakableBlock();
			
			// if the space is empty, then set the breakable block to this space
			if (board.canMoveToSpace(randomRow, randomColumn, breakableBlock))
			{
				// add the breakable block to the board
				board.moveGameObjectToSpace(randomRow, randomColumn, breakableBlock);
				// add the breakable block to the hash map
				gameObjects.put(breakableBlock.identifier(), breakableBlock);
				// count + 1 until it reach 130;
				countForBreakableBlockGenerator++;
			}
		}
	}
	
	/**
	 * Sets up the players on the board.
	 * 
	 * @throws CommunicationException Thrown if an error occurs with the server communicator.
	 */
	private void setUpPlayers() throws CommunicationException
	{
		IMessageGenerator messageGenerator = new IMessageGenerator()
		{
			public IMessage generateMessage()
			{
				
				// create the player and add it to the game objects map
				IGameObject player = gameObjectFactory.createPlayer();
				gameObjects.put(player.identifier(), player);
				
				// add the player to the board
				spawnPlayer(player);
				
				return new PlayerGameObjectIdentifierMessage(player.identifier());
			}
		};
		
		serverCommunicator.sendUniqueMessageToEachConnectedCommunicator(messageGenerator);
	}
	
	/**
	 * Determines the location farthest away from all other players and places the player there.
	 */
	private void spawnPlayer(IGameObject player)
	{
		LinkedList<IGameObject> players = new LinkedList<IGameObject>();
		
		for (IGameObject gameObject : gameObjects.values())
			if (gameObject.type() == GameObjectType.PLAYER)
				players.add(gameObject);
		
		players.remove(player);
		
		// the distance of the closest player to each corner
		int[] cornerDistances = new int[4];
		
		// the coordinates of each corner's row
		int[] cornerRows = { 1, DEFAULT_NUMBER_OF_ROWS - 2, 1, DEFAULT_NUMBER_OF_ROWS - 2 };
		
		// the coordinates of each corner's column
		int[] cornerColumns = { 1, 1, DEFAULT_NUMBER_OF_COLUMNS - 2, DEFAULT_NUMBER_OF_COLUMNS - 2 };
		
		for (int i = 0; i < 4; i++)
			cornerDistances[i] = Integer.MAX_VALUE;
		
		// iterate through each corner, determining the distance of closest
		// player to it
		for (int i = 0; i < 4; i++)
		{
			for (IGameObject gameObject : players)
			{
				if (!gameObject.onBoard())
					continue;
				
				int distance = Math.abs(cornerRows[i] - gameObject.row())
						+ Math.abs(cornerColumns[i] - gameObject.column());
				
				if (cornerDistances[i] > distance)
					cornerDistances[i] = distance;
			}
		}
		
		// determine the corner which is farthest from any player
		int farthestCorner = 0;
		for (int i = 1; i < 4; i++)
		{
			if (cornerDistances[farthestCorner] < cornerDistances[i])
				farthestCorner = i;
		}
		//if the space is currently occupied by other game objects, then increase the number of steps 
		if(!board.spaceEmpty(cornerRows[farthestCorner], cornerColumns[farthestCorner])){
			player.incrementNumberOfStepUntilRespawn();
			return;
		}
		
		player.resetNumberOfStepUntilRespawn();

		// place the player in the optimal corner
		board.moveGameObjectToSpace(cornerRows[farthestCorner], cornerColumns[farthestCorner], player);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void step() throws CommunicationException
	{
		// process the message from clients
		processMessages();
		
		// decrement the remaining steps
		remainingSteps--;
		
		//move game objects on the board
		moveGameObjects();
		
		//place bombs on the game board
		placeBombs();
		
		//destroy game objects on the game board
		destroyGameObjects();
		
		//check the collision between game objects
		checkCollision();
		
		//check the players if they should respawn or not, then respawn them if need
		checkRespawn();
	}
	
	/**
	 * A helper method which reads messages from the communicator and updates the game objects.
	 * @throws CommunicationException 
	 */
	private void processMessages() throws CommunicationException
	{
		//read the messages from clients
		for (final IMessage message : serverCommunicator.receivedMessages())
		{
			//process the key messages 
			if (message instanceof KeyMessage)
			{
				final KeyMessage keyMessage = (KeyMessage) message;
				final IGameObject gameObject = gameObjects.get(keyMessage.gameObjectIdentifier());
				
				if (gameObject == null)
					throw new IllegalStateException();
				//the direction of the proper player
				Direction direction = null;
				
				//change the direction to the direction which key message indicates
				switch (keyMessage.key())
				{
					case UP:
						direction = Direction.UP;
						break;
					case DOWN:
						direction = Direction.DOWN;
						break;
					case LEFT:
						direction = Direction.LEFT;
						break;
					case RIGHT:
						direction = Direction.RIGHT;
						break;
					case SPACE:
						// if the key action is PRESS, let the game object to place one bomb
						if (keyMessage.action() == KeyAction.PRESS)
							gameObject.setPlaceBomb(true);
						break;
				
				}
				
				// if the key message is not SPACE, then change the movement direction of the
				// game object
				if (keyMessage.key() != Key.SPACE)
				{
					if (keyMessage.action() == KeyAction.PRESS)
					{
						gameObject.startMovingInDirection(direction);
						
					}
					else
					{
						gameObject.stopMovingInDirection(direction);
						
					}
				}
			}
		}
	}

	/**
	 * Updates the game objects for this step.
	 * 
	 * @throws CommunicationException Thrown if an error occurs while communicating wit the client.
	 */
	private void moveGameObjects() throws CommunicationException
	{
		// iterate all game objects
		for (final IGameObject gameObject : gameObjects.values())
		{
			// if the game object cannot move, then go on iterating
			if (!gameObject.canMove() || gameObject.directionToMove() == Direction.NONE)
			{
				continue;
			}
			/*
			 * determine the movement of all game objects
			 */
			
			// determine where the game object should move
			int row = gameObject.row();
			int column = gameObject.column();
			//move to the proper direction
			switch (gameObject.directionToMove())
			{
				case UP:
					row--;
					break;
				case DOWN:
					row++;
					break;
				case LEFT:
					column--;
					break;
				case RIGHT:
					column++;
					break;
			}
			
			// move the game object, if possible
			if (gameObject.onBoard()&& board.canMoveToSpace(row, column, gameObject))
			{
				
				board.moveGameObjectToSpace(row, column, gameObject);
				
				serverCommunicator.sendMessages(new GameObjectUpdatedMessage(gameObject.identifier(),
						gameObject.row(), gameObject.column()));
			}
		}
	}
	
	/**
	 * Places bombs for the game objects which support placing bombs.
	 * 
	 * @throws CommunicationException Thrown if an error occurs while communicating with the Client.
	 */
	private void placeBombs() throws CommunicationException
	{
		// determine whether the player should place a bomb
		Integer[] gameObjectIdentifier = gameObjects.keySet().toArray(new Integer[0]);
		
		for (int i = 0; i < gameObjectIdentifier.length; i++)
		{
			IGameObject gameObject = gameObjects.get(gameObjectIdentifier[i]);
			
			// determine if the game object can place a bomb or needs to place bomb
			if(!gameObject.canPlaceBomb() || !gameObject.placeBomb())
				continue;
			
			// reset the place bomb flag for the game object
			gameObject.setPlaceBomb(false);
			
			// make sure the game object is on the board
			if (!gameObject.onBoard())
				continue;
			
			// attempt to place a bomb at the object's location
			IGameObject bomb = gameObjectFactory.createBomb(gameObject);
			
			// if the space has been occupied by another bomb, then continue
			if (!board.canMoveToSpace(gameObject.row(), gameObject.column(), bomb))
				continue;
			// if the player has used out the his/her bombs, then continue
			if (gameObject.bombCount() < 1)
				continue;
			
			// decrease the number of bombs the player left
			gameObject.decrementBombCount();
			
			// add the bomb to the game
			gameObjects.put(bomb.identifier(), bomb);
			
			// add the bomb to board
			board.moveGameObjectToSpace(gameObject.row(), gameObject.column(), bomb);
			
			// set the bomb creation message to the clients
			serverCommunicator.sendMessages(new GameObjectCreatedMessage(bomb.identifier(),
					GameObjectType.BOMB, bomb.row(), bomb.column()));
		}
	}
	
	/**
	 * Destroys game object when they need to be destroyed.
	 * 
	 * @throws CommunicationException Thrown if an error occurs while communicating with the clients.
	 */
	private void destroyGameObjects() throws CommunicationException
	{
		// get the game object identifiers
		Integer[] gameObjectIdentifier = gameObjects.keySet().toArray(new Integer[0]);
		
		// iterate through each game object
		for (int i = 0; i < gameObjectIdentifier.length; i++)
		{
			IGameObject gameObject = gameObjects.get(gameObjectIdentifier[i]);
			
			// game object can be null because explore may remove some game
			// object before they are iterated
			if (gameObject == null)
				continue;
			// check to see if the game object is not destructible
			if (!gameObject.destructible())
				continue;
			
			// checked to see if the game object is timed and needs to explore
			if (gameObject.destructionTimed())
			{
				gameObject.decrementNumberOfStepsUntilDestruction();
				if (gameObject.numberOfStepsUntilDestruction() == 0)
				{
					destroyGameObject(gameObject);
				}
			}
		}
	}
	
	/**
	 * Destroys the provided game object.
	 * 
	 * @param gameObject The game object to destroy.
	 * @throws CommunicationException Thrown if an error occurs while communicating with the clients.
	 */
	private void destroyGameObject(IGameObject gameObject) throws CommunicationException
	{
		switch (gameObject.destructionAction())
		{
			case EXPLODE:
				// increase the number of owned bombs to he bomb owner
				gameObject.owner().incrementBomb();
				
				// temporary record the row and column of the game object(bomb)
				int row = gameObject.row();
				int column = gameObject.column();
				
				// remove the game object from board
				board.removeGameObject(gameObject);
				
				// remove the game object from hash map
				gameObjects.remove(gameObject.identifier());
				
				// send the game object destruction to all clients
				serverCommunicator.sendMessages(new GameObjectDestroyedMessage(gameObject.identifier()));
				
				// create a new explosion game object
				IGameObject exploreGameObject = gameObjectFactory.createExplosion(gameObject.owner());
				
				// create a new explosion game object on the board
				board.moveGameObjectToSpace(row, column, exploreGameObject);
				
				// create a new explosion game object in the hash map
				gameObjects.put(exploreGameObject.identifier(), exploreGameObject);
				
				// send message to all clients
				serverCommunicator.sendMessages(new GameObjectCreatedMessage(
						exploreGameObject.identifier(), GameObjectType.EXPLOSION, row, column));
				
				// if the next space has a breakable game object, destroy it
				if (!board.canMoveToSpace(row, column, gameObject))
				{
					destroyGameObject(gameObject);
				}
				
				// explore in 4 directions
				explode(row, column, Direction.UP, gameObject.owner());
				explode(row, column, Direction.DOWN, gameObject.owner());
				explode(row, column, Direction.LEFT, gameObject.owner());
				explode(row, column, Direction.RIGHT, gameObject.owner());
				break;
			

			case RESPAWN:

				board.removeGameObject(gameObject);
				serverCommunicator.sendMessages(new GameObjectDestroyedMessage(gameObject.identifier()));
				break;
			
			case DISAPPEAR:
				// remove the game object from board
				board.removeGameObject(gameObject);
				
				// remove the game object from hash map
				gameObjects.remove(gameObject.identifier());
				
				// send message to clients
				serverCommunicator.sendMessages((new GameObjectDestroyedMessage(gameObject.identifier())));
				break;		}
	}
	
	/**
	 * a help method to explore a bomb in 4 directions
	 * 
	 * @param row row of the game object(bomb)
	 * @param column column of the game object(bomb
	 * @param direction direction of the explosion should go
	 * @param owner the owner of the explosion
	 * @throws CommunicationException handle the communication exception
	 */
	private void explode(int row, int column, Direction direction, IGameObject owner)
			throws CommunicationException
	{
		
		final int EXPLOSION_LENGTH = 3;
		
		int rowChange = 0;
		int columnChange = 0;
		
		switch (direction)
		{
			case UP:
				rowChange = -1;
				break;
			case DOWN:
				rowChange = 1;
				break;
			case LEFT:
				columnChange = -1;
				break;
			case RIGHT:
				columnChange = 1;
				break;
		}
		
		for (int i = 0; i < EXPLOSION_LENGTH; i++)
		{
			row += rowChange;
			column += columnChange;
			
			// if the board contains an indestructible object, stop creating the explosion
			for (IGameObject gameObject : board.gameObjectsAtSpace(row, column))
				if (!gameObject.destructible())
					return;
			
			// create a new explosion game object
			IGameObject explosionGameObject = gameObjectFactory.createExplosion(owner);
			
			// make sure the explosion can be placed at the row and column
			if (!board.canMoveToSpace(row, column, explosionGameObject))
				return;
			
			board.moveGameObjectToSpace(row, column, explosionGameObject);
			
			// create a new explosion game object in the hash map
			gameObjects.put(explosionGameObject.identifier(), explosionGameObject);
			
			// send message to all clients
			serverCommunicator.sendMessages(new GameObjectCreatedMessage(
					explosionGameObject.identifier(), GameObjectType.EXPLOSION, row, column));
			
			// if the space contains a solid object, stop
			for (IGameObject gameObject : board.gameObjectsAtSpace(row, column))
				if (gameObject.solid())
					return;
		}
	}
	
	/**
	 * a helper method to iterate each space on board, and destroy the the objects which is reached by
	 * explosion
	 * 
	 * @throws CommunicationException handle the communication exception in detroyGameObject method
	 */
	private void checkCollision() throws CommunicationException
	{
		for (int row = 1; row < 17; row++)
		{
			for (int column = 1; column < 23; column++)
			{
				
				// then test if it contains explosion.
				boolean explode = false;
				IGameObject explosionOwner = null;
				
				for (IGameObject gameObject : board.gameObjectsAtSpace(row, column))
				{
					if (gameObject.type() == GameObjectType.EXPLOSION)
					{
						explode = true;
						explosionOwner = gameObject.owner();
						break;
					}
				}
				
				if (!explode)
					continue;
				
				// if it contains explosion, then destroy every destructible game object in this space
				for (IGameObject gameObject : board.gameObjectsAtSpace(row, column))
				{
					if (gameObject.destructible() && gameObject.type() != GameObjectType.EXPLOSION)
					{
						destroyGameObject(gameObject);
						
						// set the score
						if (gameObject.type() == GameObjectType.PLAYER)
						{
							if (gameObject.identifier() == explosionOwner.identifier())
								explosionOwner.decrementScore();
							else
								explosionOwner.incrementScore();
						}
						
						// send the score updated message
						IMessage message = new ScoreUpdatedMessage(explosionOwner.identifier(), explosionOwner.score());
						serverCommunicator.sendMessages(message);
					}
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int numberOfRemainingSteps()
	{
		return remainingSteps;
	}
	
	/**
	 * Decrements the time until the game objects respawn and spawn the gmae objects if the time has run out.
	 * @throws CommunicationException throws if a communication error occurs.
	 */
	public void checkRespawn() throws CommunicationException{
		
		// decrement the time until respawn
		for(IGameObject gameObject:gameObjects.values()){
			if(gameObject.destructionAction() == DestructionAction.RESPAWN && !gameObject.onBoard()) {
				gameObject.decrementNumberOfStepUntilRespawn();
				
				if (gameObject.numberOfStepUntilRespawn() <= 0)
					spawnPlayer(gameObject);
				
				// send a message to the client if the game object was readded to the board
				if (gameObject.onBoard())
				{
					IMessage message = new GameObjectCreatedMessage(gameObject.identifier(), gameObject.type(), gameObject.row(), gameObject.column());
					serverCommunicator.sendMessages(message);
				}
			}
		}
	}
}
