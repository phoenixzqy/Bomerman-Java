package server.controller;

import shared.model.communication.IServerCommunicator;

/**
 * Controls the game hosting screen.
 */
public interface IGameHostingController extends IController
{	
	/**
	 Determines whether the game is currently running or not.  This is a bindable property.
	 * @return True if the game is currently running and false otherwise.
	 */
	public boolean running();
	
	/**
	 * Sets the game's running status.  This is a bindable property.
	 * @param running The value to set.
	 */
	public void setRunning(boolean running);
	
	/**
	 * Starts the game if it is currently stopped and stops the game if it is currently started.
	 */
	public void startOrStopGame();
	
	/**
	 * Navigates the application to the main menu screen.
	 */
	public void mainMenu();
	
	/**
	 * Returns the server communicator.
	 * @return The server communicator.
	 */
	public IServerCommunicator serverCommunicator();
	
	/**
	 * Returns the error message for this IGameHostingController.  This is a bindable property.
	 * @return The error message for this IGameHostingController.
	 */
	public String errorMessage();
	
	/**
	 * Returns the number of connected player.
	 * @return the number of connected player.
	 */
	public int connectedPlayers();
}
