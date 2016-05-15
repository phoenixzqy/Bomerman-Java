package client.controller;

import shared.controller.*;

/**
 * Implementation of IConnectionFailureController.
 */
public class ConnectionFailureController extends Controller implements IConnectionFailureController
{
	// the navigator
	private final INavigator navigator;
	
	// the controller factory
	private final IControllerFactory controllerFactory;
	
	// the error message
	private final String errorMessage;
	
	/**
	 * ConnectionFailureController constructor.
	 * @param navigator The navigator
	 * @param controllerFactory The controllerFactory.
	 * @param errorMessage The error message for this IConnectionFailureController.
	 * @throws NullPointerException Thrown if navigator or controllerFactory is null.
	 */
	public ConnectionFailureController(INavigator navigator, IControllerFactory controllerFactory, 
		String errorMessage)
	{
		if (navigator == null || controllerFactory == null || errorMessage == null)
			throw new NullPointerException();
		
		this.navigator = navigator;
		this.controllerFactory = controllerFactory;
		this.errorMessage = errorMessage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void multiplayer()
	{
		// connection failure view is two views above multiplayer view
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
	public String errorMessage()
	{
		return errorMessage;
	}
}