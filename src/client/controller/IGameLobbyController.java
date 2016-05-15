package client.controller;

import shared.model.communication.ICommunicator;

/**
 * Controls the game lobby screen.
 */
public interface IGameLobbyController extends IController
{
	/**
	 * Disconnects the application from the game server and returns te
	 * application to the main menu.
	 */
	public void cancel();

	/**
	 * Returns the number of players currently connected to the game. This is a
	 * bindable property.
	 * 
	 * @return The number of players currently connected to the game.
	 */
	public int numberOfPlayers();

	/**
	 * Sets the number of players currently connected to the game.
	 * 
	 * @param players
	 *            The number of players currently connected to the game.
	 */
	public void setNumberOfPlayers(int players);

	/**
	 * Returns the ICommunicator for this IGameLobbyController.
	 * 
	 * @return The ICommunicator for this IGameLobbyController.
	 */
	public ICommunicator communicator();
}
