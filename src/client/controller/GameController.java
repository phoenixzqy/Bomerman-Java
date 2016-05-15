package client.controller;

import java.util.*;
import shared.controller.*;
import shared.model.*;
import shared.model.communication.*;
import client.model.*;

/**
 * Implementation of IGameController.
 */
public class GameController extends Controller implements IGameController
{
	// the navigator
	private final INavigator navigator;

	// the controller factory
	private final IControllerFactory controllerFactory;

	// the communicator
	private final ICommunicator communicator;

	// the game timer
	private final Timer timer;

	// the time remaining, in seconds
	private int remainingTime;

	// the game objects
	private HashMap<Integer, IGameObject> gameObjects;

	// the player scores
	private Score[] scores;

	// the player game object id number
	private int playerGameObjectId;

	// list of player game object identifiers
	// get(index) should return the game object id of the player with a player
	// number of (index + 1)
	// this list should be sorted any time an element is added
	private List<Integer> playerGameObjectIdList;

	/**
	 * This is a TimerTask that checks for messages from the server and updates the local game state.
	 */
	protected class GameTimerTask extends TimerTask
	{
		public void run()
		{
			step();
		}
	}

	/**
	 * IGameController constructor.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param communicator
	 *            A communicator connected to a server.
	 * @param controllerFactory
	 *            The controllerFactory.
	 * @param numPlayers
	 *            The number of players in this game.
	 * @param gameObjectIdentifier
	 *            The game object identifier of the player on this client.
	 * @throws NullPointerException
	 *             Thrown if navigator controllerFactory, or communicator is
	 *             null.
	 * @throws IllegalArgumentException
	 *             Thrown if communicator is not connected to a server.
	 */
	public GameController(INavigator navigator,
			IControllerFactory controllerFactory, ICommunicator communicator,
			int numPlayers, int gameObjectIdentifier)
	{
		if (navigator == null || controllerFactory == null)
			throw new NullPointerException();

		if (!communicator.connected())
			throw new IllegalArgumentException();

		this.navigator = navigator;
		this.controllerFactory = controllerFactory;
		this.communicator = communicator;
		this.playerGameObjectId = gameObjectIdentifier;
		// max number of players is 4
		playerGameObjectIdList = new ArrayList<Integer>(4);
		remainingTime = 0;

		timer = new Timer();

		this.gameObjects = new HashMap<Integer, IGameObject>();
		scores = new Score[numPlayers];

		for (int i = 0; i < scores.length; i++)
		{
			// initialize scores to 0 with -1 as game object identifiers
			// identifier will be changed when the score is first updated
			scores[i] = new Score(-1, 0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void start()
	{

		// set up and start the timer
		TimerTask timerTask = new GameTimerTask();
		timer.schedule(timerTask, 0, 1000 / 60);
	}

	/**
	 * {@inheritDoc}
	 */
	public void quitGame()
	{
		// navigate to the MultiplayerMenu
		navigator.pop();
		navigator.pop();
		// stop receiving messages from the server
		timer.cancel();
	}

	/**
	 * {@inheritDoc}
	 */
	public INavigator navigator()
	{
		return navigator;
	}

	/**
	 * {@inheritDoc}
	 */
	public IControllerFactory controllerFactory()
	{
		return controllerFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public ICommunicator communicator()
	{
		return communicator;
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameObject[] gameObjects()
	{
		return gameObjects.values().toArray(new IGameObject[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGameObjects(IGameObject[] gameObjects)
	{
		if (gameObjects == null)
			throw new NullPointerException();

		// create a new map of game objects
		this.gameObjects = new HashMap<Integer, IGameObject>();
		for (IGameObject gameObject : gameObjects)
		{
			this.gameObjects.put(gameObject.identifier(), gameObject);
		}

		propertyDidChange("gameObjects");
	}

	/**
	 * {@inheritDoc}
	 */
	public Timer timer()
	{
		return timer;
	}

	/**
	 * {@inheritDoc}
	 */
	public void step()
	{
		boolean gameObjectsUpdatedFlag = false;

		try
		{
			for (IMessage message : communicator.receivedMessages())
			{
				// process all messages received from the host
				if (message instanceof GameObjectCreatedMessage)
				{
					// create the object and add it to the map
					GameObjectCreatedMessage createdMessage = (GameObjectCreatedMessage) message;
					IGameObject gameObject = new GameObject(createdMessage
							.gameObjectIdentifier(), createdMessage
							.gameObjectType(), createdMessage.row(),
							createdMessage.column());
					gameObjects.put(gameObject.identifier(), gameObject);

					// if gameObject is a player being created for the first
					// time, map gameObjectId to player number
					if (gameObject.gameObjectType() == GameObjectType.PLAYER
							&& !playerGameObjectIdList.contains(gameObject
									.identifier()))
					{
						playerGameObjectIdList.add(gameObject.identifier());
						
						// sort the playerGameObjectIdList
						Collections.sort(playerGameObjectIdList,
								new Comparator<Integer>()
								{
									public int compare(Integer lhs, Integer rhs)
									{
										// use natural ordering
										return lhs.compareTo(rhs);
									}

								});
					}

					gameObjectsUpdatedFlag = true;
				} else if (message instanceof GameObjectUpdatedMessage)
				{
					GameObjectUpdatedMessage updatedMessage = (GameObjectUpdatedMessage) message;

					// try to get the game object
					IGameObject gameObject = gameObjects.get(updatedMessage
							.gameObjectIdentifier());

					// game object updated message has to refer to a game object in this game
					if (gameObject == null)
						throw new IllegalStateException();

					// update the object
					gameObject.setRow(updatedMessage.row());
					gameObject.setColumn(updatedMessage.column());
					gameObjectsUpdatedFlag = true;
				} else if (message instanceof GameObjectDestroyedMessage)
				{
					GameObjectDestroyedMessage destroyedMessage = (GameObjectDestroyedMessage) message;

					// try to get the game object
					IGameObject gameObject = gameObjects.get(destroyedMessage
							.gameObjectIdentifier());

					// game object destroyed message has to refer to a game object in this game
					if (gameObject == null)
						throw new IllegalStateException();

					// remove the objects from the game objects
					gameObjects.remove(gameObject.identifier());
					gameObjectsUpdatedFlag = true;
				} else if (message instanceof ScoreUpdatedMessage)
				{
					ScoreUpdatedMessage scoreMessage = (ScoreUpdatedMessage) message;

					// get the player number for the score message
					int playerNumber = playerGameObjectIdList
							.indexOf(scoreMessage.gameObjectIdentifier()) + 1;

					// if player number > 4 or we don't have a player with that
					// game object identifier, throw ISE
					if (playerNumber > scores.length || playerNumber == 0)
					{
						throw new IllegalStateException();
					}

					// update corresponding score
					scores[playerNumber - 1] = new Score(scoreMessage
							.gameObjectIdentifier(), scoreMessage.score());
					propertyDidChange("scores");

				} else if (message instanceof GameMessage)
				{
					GameMessage gameMessage = (GameMessage) message;
					if (gameMessage.action() == GameMessage.Action.STOP)
					{
						// game has been stopped, navigate to game over view
						IGameOverController gameOverController = controllerFactory
								.createGameOverController(navigator, scores,
										communicator);
						navigator.replaceTop(gameOverController);
					} else
					{
						// game should never be started or waiting after it is
						// already started.
						throw new IllegalStateException();
					}
				} else if (message instanceof GameTimeMessage)
				{
					// update the game time remaining
					GameTimeMessage gameTimeMessage = (GameTimeMessage) message;
					setTime(gameTimeMessage.time());
				} else
				{
					// the message is unrecognized
					throw new IllegalStateException();
				}
			}
		} catch (CommunicationException exception)
		{
			// navigate to the connection failure screen
			IConnectionFailureController connectionFailureController = controllerFactory
					.createConnectionFailureController(navigator, exception
							.getMessage());
			navigator.replaceTop(connectionFailureController);
			timer.cancel();
		}

		// fire property change for gameObjects if they have been modified during this step
		// allows game view to update
		if (gameObjectsUpdatedFlag)
			propertyDidChange("gameObjects");

		gameObjectsUpdatedFlag = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyEventDidOccur(Key key, KeyAction action)
	{
		if (key == null || action == null)
			throw new NullPointerException();

		try
		{
			// send the key message for the player
			communicator.sendMessage(new KeyMessage(playerGameObjectId, key,
					action));
		} catch (CommunicationException exception)
		{
			// navigate to the connection failure screen
			IConnectionFailureController connectionFailureController = controllerFactory
					.createConnectionFailureController(navigator, exception
							.getMessage());
			navigator.push(connectionFailureController);
			timer.cancel();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IScore[] scores()
	{
		return scores;
	}

	/**
	 * {@inheritDoc}
	 */
	public int playerGameObjectId()
	{
		return playerGameObjectId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int time()
	{
		return remainingTime;
	}

	/**
	 * Sets the amount of time remaining in the game.
	 * 
	 * @param time
	 *            The amount of time remaining in the game.
	 */
	public void setTime(int time)
	{
		this.remainingTime = time;
		propertyDidChange("time");
	}
}
