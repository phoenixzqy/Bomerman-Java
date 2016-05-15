package client.controller;

import shared.controller.INavigator;
import shared.model.Score;
import shared.model.communication.ICommunicator;

/**
 * Implements IControllerFactory.
 */
public class ControllerFactory implements IControllerFactory
{
	/**
	 * {@inheritDoc}
	 */
	public IConnectionFailureController createConnectionFailureController(
			INavigator navigator, String errorMessage)
	{
		return new ConnectionFailureController(navigator, this, errorMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	public ICreditsController createCreditsController(INavigator navigator)
	{
		return new CreditsController(navigator, this);
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameController createGameController(INavigator navigator,
			ICommunicator communicator, int players, int gameObjectIdentifier)
	{
		return new GameController(navigator, this, communicator, players,
				gameObjectIdentifier);
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameLobbyController createGameLobbyController(INavigator navigator,
			ICommunicator communicator)
	{
		return new GameLobbyController(navigator, this, communicator);
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameOverController createGameOverController(INavigator navigator,
			Score[] scores, ICommunicator lastGameCommunicator)
	{
		return new GameOverController(navigator, this, scores,
				lastGameCommunicator);
	}

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
	public IMultiplayerController createMultiplayerController(
			INavigator navigator)
	{
		return new MultiplayerController(navigator, this);
	}

}