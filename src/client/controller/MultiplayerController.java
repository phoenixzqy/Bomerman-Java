package client.controller;

import shared.controller.*;
import shared.model.communication.*;

/**
 * Implements IMultiplayerController.
 */
public class MultiplayerController extends Controller implements
		IMultiplayerController
{
	// the navigator
	private final INavigator navigator;

	// the controller factory
	private final IControllerFactory controllerFactory;

	// the server address
	private String serverAddress;

	// the error message
	private String errorMessage;

	/**
	 * Constructor.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param controllerFactory
	 *            The controllerFactory.
	 * @throws NullPointerException
	 *             Thrown if navigator or controllerFactory is null.
	 */
	public MultiplayerController(INavigator navigator,
			IControllerFactory controllerFactory)
	{
		if (navigator == null || controllerFactory == null)
			throw new NullPointerException();

		this.navigator = navigator;
		this.controllerFactory = controllerFactory;

		// initialize server address and error message to empty strings
		serverAddress = "";
		errorMessage = "";
	}

	/**
	 * {@inheritDoc}
	 */
	public String serverAddress()
	{
		return serverAddress;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setServerAddress(String serverAddress)
	{
		this.serverAddress = serverAddress;
		propertyDidChange("serverAddress");
	}

	/**
	 * {@inheritDoc}
	 */
	public String errorMessage()
	{
		return errorMessage;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
		propertyDidChange("errorMessage");
	}

	/**
	 * {@inheritDoc}
	 */
	public void join()
	{
		ICommunicator communicator = null;
		try
		{
			// join server as a player
			communicator = new Communicator(new MessageFactory(),
					serverAddress);
			if (communicator.connected())
			{
				// migrate to game lobby
				IGameLobbyController gameLobbyController = controllerFactory
						.createGameLobbyController(navigator, communicator);
				navigator.push(gameLobbyController);
			} else
			{
				setErrorMessage("Could not connect to host.");
			}
		} catch (CommunicationException e)
		{
			setErrorMessage(e.getMessage());
		}
		
	}


	/**
	 * {@inheritDoc}
	 */
	public void mainMenu()
	{
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
}
