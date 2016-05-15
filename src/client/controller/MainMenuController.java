package client.controller;

import shared.controller.*;

/**
 * Implements the IMainMenuController
 */
public class MainMenuController extends Controller implements
		IMainMenuController
{
	// the navigator
	private final INavigator navigator;

	// the controller factory
	private final IControllerFactory controllerFactory;

	/**
	 * MainMenuController constructor.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param controllerFactory
	 *            The controllerFactory.
	 * @throws NullPointerException
	 *             Thrown if navigator or controllerFactory is null.
	 */
	public MainMenuController(INavigator navigator,
			IControllerFactory controllerFactory)
	{
		if (navigator == null || controllerFactory == null)
			throw new NullPointerException();

		this.navigator = navigator;
		this.controllerFactory = controllerFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public void exit()
	{
		System.exit(0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void multiplayer()
	{
		// navigate to multiplayer screen
		IMultiplayerController multiplayerController = controllerFactory
				.createMultiplayerController(navigator);
		navigator.push(multiplayerController);
	}

	/**
	 * {@inheritDoc}
	 */
	public void credits()
	{
		// navigate to credits screen
		ICreditsController creditsController = controllerFactory
				.createCreditsController(navigator);
		navigator.push(creditsController);
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
