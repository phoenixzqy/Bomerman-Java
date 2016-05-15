package client.controller;

import shared.controller.INavigator;
import shared.model.Score;
import shared.model.communication.ICommunicator;

/**
 * Factory for client controllers.
 */
public interface IControllerFactory
{
	/**
	 * Creates a new instance of IConnectionFailureController.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param errorMessage
	 *            The error message.
	 * @throws NullPointerException
	 *             Thrown if navigator or errorMessage is null.
	 * @return A new instance of IConnectionFailureController.
	 */
	public IConnectionFailureController createConnectionFailureController(
			INavigator navigator, String errorMessage);

	/**
	 * Creates a new instance of ICreditsController.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @throws NullPointerException
	 *             Thrown if navigator is null.
	 * @return A new instance of ICreditsController.
	 */
	public ICreditsController createCreditsController(INavigator navigator);

	/**
	 * Creates a new instance of IGameController.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param communicator
	 *            A communicator already connected to a server.
	 * @param players
	 *            The number of players in the game.
	 * @param gameObjectIdentifier
	 *            The game object identifier of the player on this client.
	 * @throws NullPointerException
	 *             Thrown if navigator or communicator is null.
	 * @throws IllegalArgumentException
	 *             Thrown if communicator is not connected to a server.
	 * @return A new instance of IGameController.
	 */
	public IGameController createGameController(INavigator navigator,
			ICommunicator communicator, int players, int gameObjectIdentifier);

	/**
	 * Creates a new instance of IGameLobbyController.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param communicator
	 *            A connected communicator.
	 * @throws NullPointerException
	 *             Thrown if navigator is null.
	 * @throws IllegalArgumentException
	 *             Thrown if the provided communicator is not not connected to a
	 *             server.
	 * @return A new instance of IGameLobbyController.
	 */
	public IGameLobbyController createGameLobbyController(INavigator navigator,
			ICommunicator communicator);

	/**
	 * Creates a new instance of IGameOverController.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param scores
	 *            The scores from the game.
	 * @param lastGameCommunicator
	 *            The ICommunicator used for the last game.
	 * @throws NullPointerException
	 *             Thrown if navigator is null.
	 * @return A new instance of IGameOverController.
	 */
	public IGameOverController createGameOverController(INavigator navigator,
			Score[] scores, ICommunicator lastGameCommunicator);

	/**
	 * Creates a new instance of IMainMenuController.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @throws NullPointerException
	 *             Thrown if navigator is null.
	 * @return A new instance of IMainMenuController.
	 */
	public IMainMenuController createMainMenuController(INavigator navigator);

	/**
	 * Creates a new instance of IMultiplayerController.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @throws NullPointerException
	 *             Thrown if navigator is null.
	 * @return A new instance of IMultiplayerController.
	 */
	public IMultiplayerController createMultiplayerController(
			INavigator navigator);
}