package client.controller;

import java.util.*;
import shared.controller.*;
import shared.model.communication.*;

/**
 * Implementation of IGameLobbyController.
 */
public class GameLobbyController extends Controller implements
		IGameLobbyController
{
	// the navigator
	private final INavigator navigator;

	// the controller factory
	private final IControllerFactory controllerFactory;

	// the communicator
	private final ICommunicator communicator;

	// number of connected players
	private int numberOfPlayers;

	// game object identifier for this client
	private int gameObjectIdentifier;

	// game object identifier for this client is set
	private boolean gameObjectIdentifierSet;

	// delay for checking for game start messages
	private final static int timerPeriod = 100;

	// Periodically checks for game start message
	private Timer checkForMessagesFromServerTimer;
	
	/**
	 * TimerTask that checks for messages from the host.
	 * This is protected for the sake of testing and should not be
	 * used outside of GameLobbyController.
	 */
	protected class CheckForMessagesFromHostTimerTask extends TimerTask {

		@Override
		public void run()
		{
			checkForMessagesFromHost();
		}
		
	}

	/**
	 * GameLobbyController constructor.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param controllerFactory
	 *            The controllerFactory.
	 * @param communicator
	 *            The communicator.
	 * @throws NullPointerException
	 *             Thrown if navigator, controllerFactory or communicator is
	 *             null.
	 * @throws IllegalArgumentException
	 *             Thrown if the provided communicator is not connected to a
	 *             server.
	 */
	public GameLobbyController(INavigator navigator,
			IControllerFactory controllerFactory, ICommunicator communicator)
	{
		if (navigator == null || controllerFactory == null
				|| communicator == null)
			throw new NullPointerException();

		if (!communicator.connected())
			throw new IllegalArgumentException();

		this.navigator = navigator;
		this.controllerFactory = controllerFactory;
		this.communicator = communicator;
		numberOfPlayers = 0;
		gameObjectIdentifierSet = false;

		// starts timer listening for messages from the server
		checkForMessagesFromServerTimer = new Timer();
		checkForMessagesFromServerTimer.schedule(new CheckForMessagesFromHostTimerTask(), 0, timerPeriod);
	}

	/**
	 * {@inheritDoc}
	 */
	public void cancel()
	{
		try
		{
			// disconnect from host 
			communicator.disconnect();
			
		} catch (CommunicationException e)
		{
			// do nothing here, behavior same for non-error
		}

		// stop the timer checking for message from server.
		checkForMessagesFromServerTimer.cancel();
		
		// navigate to MultiplayerMenu
		navigator.pop();
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
	public ICommunicator communicator()
	{
		return communicator;
	}

	/**
	 * {@inheritDoc}
	 */
	public IControllerFactory controllerFactory()
	{
		return controllerFactory;
	}

	private void checkForMessagesFromHost()
	{
		
		try
		{
			// get a single message from the host, as to avoid prematurely receiving
			// messages intended for the GameController
			IMessage[] messages = communicator.receivedMessages(1);
			if (messages.length == 0)
			{
				return;
			}

			IMessage message = messages[0];
			
			if (message instanceof GameMessage)
			{
				// message has info about game status and number of connected players
				GameMessage gameMessage = (GameMessage) message;
				switch (gameMessage.action())
				{
				case START:
					// throw exception if client has not yet received their game object identifier
					if (!gameObjectIdentifierSet)
					{
						throw new IllegalStateException(
								"Player's game object identifier not set before game is started.");
					}
					
					// sets the number of players in the game during as of the start
					setNumberOfPlayers(gameMessage.numberOfPlayers());
					
					// navigate to game view
					IGameController gameController = controllerFactory
							.createGameController(navigator, communicator,
									gameMessage.numberOfPlayers(), gameObjectIdentifier);
					navigator.replaceTop(gameController);
					
					// cancel this timer
					checkForMessagesFromServerTimer.cancel();
					
					// starts the game
					gameController.start();
					return;
				case WAITING:
					// update number of players
					setNumberOfPlayers(gameMessage.numberOfPlayers());
					break;
				case STOP:
					// this state is invalid, game has not yet been started
					throw new IllegalStateException(
							"Game cannot be stopped before it has been started.");
				}
			} else if (message instanceof PlayerGameObjectIdentifierMessage)
			{
				// message has information about the player's game object identifier
				
				// invalid state, a player can have only one game object identifier
				if (gameObjectIdentifierSet)
				{
					throw new IllegalStateException(
							"Player Object Identifier message sent twice.");
				}
				
				// set the game object identifier for this player
				PlayerGameObjectIdentifierMessage playerGameObjectIdentifierMessage = 
						(PlayerGameObjectIdentifierMessage) message;
				gameObjectIdentifier = playerGameObjectIdentifierMessage
						.gameObjectIdentifier();
				gameObjectIdentifierSet = true;
			}
		} catch (CommunicationException e)
		{
			// error occurred when receiving messages from server, navigate to connection failure view
			IConnectionFailureController connectionFailureController = controllerFactory
					.createConnectionFailureController(navigator, e
							.getMessage());
			navigator.push(connectionFailureController);
			checkForMessagesFromServerTimer.cancel();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int numberOfPlayers()
	{
		return numberOfPlayers;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNumberOfPlayers(int players)
	{
		this.numberOfPlayers = players;
		propertyDidChange("numberOfPlayers");
	}

}
