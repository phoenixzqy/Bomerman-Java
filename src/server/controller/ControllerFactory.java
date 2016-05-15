package server.controller;

import shared.controller.INavigator;
import shared.model.communication.IServerCommunicator;

/**
 * Implementation of IControllerFactory for the server.
 */
public class ControllerFactory implements IControllerFactory
{
	/**
	 * {@inheritDoc}
	 */
	public IMainMenuController createMainMenuController(INavigator navigator)
	{
		return new MainMenuController(navigator, this);
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameHostingController createGameHostingController(INavigator navigator, 
		IServerCommunicator serverCommunicator)
	{
		return new GameHostingController(navigator, this, serverCommunicator);
	}
}
