package server.controller;

import java.util.Timer;
import java.util.TimerTask;

import server.model.Game;
import server.model.GameObjectFactory;
import shared.controller.Controller;
import shared.controller.INavigator;
import shared.model.communication.CommunicationException;
import shared.model.communication.HelloMessage;
import shared.model.communication.GameMessage;
import shared.model.communication.GameTimeMessage;
import shared.model.communication.IServerCommunicator;
import shared.model.communication.MessageFactory;
import shared.model.communication.ServerCommunicator;

/**
 * Implements the IGameHostingController interface.
 */
public class GameHostingController extends Controller implements
		IGameHostingController
{
	// the navigator
	private final INavigator navigator;

	// the controller factory
	private final IControllerFactory controllerFactory;

	// whether or not the game is running
	private boolean running;

	// the error message for the controller
	private String errorMessage;

	// the server communicator
	private IServerCommunicator serverCommunicator;

	// game
	private Game game;

	// the time between game steps in milliseconds
	private static final int stepPeriod = 1000 / 10;

	// the timer for the game
	private Timer gameTimer;

	// connected players
	private int connectedPlayers = 0;

	// TimerTask to step through the game or send WAITING GameMessages to
	// connected clients.
	protected class GameStepTimerTask extends TimerTask
	{
		/**
		 * {@inheritDoc}
		 */
		public void run()
		{
			try
			{
				if (running && game.numberOfRemainingSteps() > 0)
				{
					GameTimeMessage gameTimeMessage = new GameTimeMessage(
							(int) ((stepPeriod / 1000.0) * game
									.numberOfRemainingSteps()));
					// send the time message to clients
					serverCommunicator.sendMessages(gameTimeMessage);
					// run one game step at one time
					game.step();
				} else if (running && game.numberOfRemainingSteps() == 0)
				{
					//stop the game
					startOrStopGame();
				} else if (!running)
				{
					// reset the connected players number
					if (connectedPlayers != serverCommunicator
							.numberOfConnectedCommunicators())
					{

						setConnectedPlayer(serverCommunicator
								.numberOfConnectedCommunicators());
						//send player information message to clients
						serverCommunicator.sendMessages(new GameMessage(
								GameMessage.Action.WAITING, connectedPlayers));

					} else
					{
						HelloMessage message = new HelloMessage();
						serverCommunicator.sendMessages(message);
					}
				}
			} catch (CommunicationException e)
			{
				setErrorMessage(e.getMessage());
			}
		}
	}

	/**
	 * GameHostingController constructor.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param serverCommunicator
	 *            The server communicator.
	 * @param controllerFactory
	 *            The controller factory.
	 * @throws NullPointerException
	 *             Thrown if navigator, controllerFactory or serverCommunicator
	 *             is null.
	 */
	public GameHostingController(INavigator navigator,
			IControllerFactory controllerFactory,
			IServerCommunicator serverCommunicator)
	{
		if (navigator == null || controllerFactory == null
				|| serverCommunicator == null)
			throw new NullPointerException();

		this.navigator = navigator;
		this.controllerFactory = controllerFactory;
		this.serverCommunicator = serverCommunicator;
		
		// initialize the message with empty String
		errorMessage = "";
		
		// initialize the timer and run it
		gameTimer = new Timer();
		gameTimer.schedule(new GameStepTimerTask(), 0, stepPeriod);
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
	public void startOrStopGame()
	{
		if (running)
		{

			try
			{
				// set running status to false
				setRunning(false);
				//send the waiting to start game message to clients
				setErrorMessage("Waiting to Start Game");
				game = null;

				GameMessage gameStopMessage = new GameMessage(
						GameMessage.Action.STOP, connectedPlayers);
				serverCommunicator.sendMessages(gameStopMessage);

				// Restart the server communicator
				setConnectedPlayer(0);
				serverCommunicator = new ServerCommunicator(
						new MessageFactory());
			} catch (CommunicationException exception)
			{
				setErrorMessage(exception.getMessage());
			}
		} else
		{
			if (serverCommunicator.numberOfConnectedCommunicators() > 0)
			{
				// stop listening from server
				setErrorMessage("Game In Progress");
				serverCommunicator.stopListening();

				try
				{
					game = new Game(serverCommunicator, new GameObjectFactory());
				} catch (CommunicationException e)
				{
					setErrorMessage(e.getMessage());
					return;
				}

				setRunning(true);
			} else {
				setErrorMessage("Game cannot be started with 0 connected players.");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mainMenu()
	{
		if (running)
		{
			startOrStopGame();
		}
		gameTimer.cancel();
		try
		{
			serverCommunicator.disconnect();
		} catch (CommunicationException e)
		{
			// cannot set error message on MainMenuController here
		}
		navigator.pop();
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
	public boolean running()
	{
		return running;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRunning(boolean running)
	{
		this.running = running;
		propertyDidChange("running");
	}

	/**
	 * {@inheritDoc}
	 */
	public IServerCommunicator serverCommunicator()
	{
		return serverCommunicator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String errorMessage()
	{
		return errorMessage;
	}

	/**
	 * Sets the error message to the given value.
	 * 
	 * @param errorMessage The error message to be set.
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
		propertyDidChange("errorMessage");
	}

	/**
	 * {@inheritDoc}
	 */
	public int connectedPlayers()
	{
		return connectedPlayers;
	}

	/**
	 * A method to set the the connectedPlayers number
	 * 
	 * @param numbOfPlayers
	 */
	public void setConnectedPlayer(int numbOfPlayers)
	{
		connectedPlayers = numbOfPlayers;
		propertyDidChange("connectedPlayers");
	}
}
