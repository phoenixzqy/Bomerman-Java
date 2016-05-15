package server.controller;

import shared.controller.INavigator;
import shared.model.communication.IServerCommunicator;

/**
 * Factory for server controllers.
 */
public interface IControllerFactory
{
	/**
	 * Creates a new instance of IMainMenuCommunicator.
	 * @param navigator The navigator.
	 * @throws NullPointerException Thrown if the navigator is null.
	 * @return A new instance of IMainMenuCommunicator.
	 */
	public IMainMenuController createMainMenuController(INavigator navigator);
	
	/**
	 * Creates a new instance of IGameHostingController.
	 * @param navigator The navigator.
	 * @param serverCommunicator The server communicator.
	 * @throws NullPointerException Thrown if the navigator or serverCommunicator is null.
	 * @return A new instance of IGameHostingController.
	 */
	public IGameHostingController createGameHostingController(INavigator navigator, 
			IServerCommunicator serverCommunicator);
}
