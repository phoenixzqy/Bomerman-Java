package client.controller;

import shared.controller.*;
import shared.model.IScore;
import shared.model.communication.*;

/**
 * Implementation of IGameOverController
 */
public class GameOverController extends Controller implements
		IGameOverController
{
	// the navigator
	private final INavigator navigator;

	// the controller factory
	private final IControllerFactory controllerFactory;

	// the scores from the game
	private final IScore[] scores;

	// the address of the host for the previous game
	private String hostAddress;

	/**
	 * Constructor.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param scores
	 *            The scores as a result of the game.
	 * @param controllerFactory
	 *            The controllerFactory.
	 * @param lastGameCommunicator
	 *            The communicator used for the last game.
	 * @throws NullPointerException
	 *             Thrown if navigator or controllerFactory is null.
	 */
	public GameOverController(INavigator navigator,
			IControllerFactory controllerFactory, IScore[] scores,
			ICommunicator lastGameCommunicator)
	{
		if (navigator == null || controllerFactory == null || scores == null
				|| lastGameCommunicator == null)
			throw new NullPointerException();

		this.navigator = navigator;
		this.controllerFactory = controllerFactory;
		this.scores = scores;
		// maintain address of last host for rematch
		this.hostAddress = lastGameCommunicator.connectedAddress();
	}

	/**
	 * {@inheritDoc}
	 */
	public void rematch()
	{

		try
		{
			Communicator communicator = new Communicator(new MessageFactory(),
					hostAddress);
			if (communicator.connected())
			{
				// if the rematch is successful, navigate to the game lobby
				// screen
				IGameLobbyController gameLobbyController = controllerFactory
						.createGameLobbyController(navigator, communicator);
				navigator.push(gameLobbyController);

			} else
			{
				// client could not connect
				IMultiplayerController multiplayerController = controllerFactory
						.createMultiplayerController(navigator);
				navigator.push(multiplayerController);
				multiplayerController
						.setErrorMessage("Could not connect to host.  Host is not allowing additional connections.");
			}
		} catch (CommunicationException e)
		{
			// if the rematch is not successful, navigate to the multiplayer
			// screen
			IMultiplayerController multiplayerController = controllerFactory
					.createMultiplayerController(navigator);
			navigator.push(multiplayerController);
			multiplayerController.setErrorMessage(e.getMessage());

		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void mainMenu()
	{
		// pop twice to navigate to the main menu
		navigator.pop();
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
	public IControllerFactory controllerFactory()
	{
		return controllerFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public IScore[] scores()
	{
		return scores;
	}
}
