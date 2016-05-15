package server.controller;

import shared.controller.Controller;
import shared.controller.INavigator;
import shared.model.communication.CommunicationException;
import shared.model.communication.IServerCommunicator;
import shared.model.communication.MessageFactory;
import shared.model.communication.ServerCommunicator;

/**
 * Implements the IMainMenuControllerInterface.
 */
public class MainMenuController extends Controller implements IMainMenuController 
{
	// the navigator
	private final INavigator navigator;
	
	// the controller factory
	private final IControllerFactory controllerFactory;
	
	// The error message for this controller
	private String errorMessage;
	
	/**
	 * Constructor.
	 * @param navigator The navigator.
	 * @param controllerFactory The controller factory.
	 * @throws NullPointerException Thrown if navigator or controllerFactory is null.
	 */
	public MainMenuController(INavigator navigator, IControllerFactory controllerFactory)
	{
		// check the arguments
		if (navigator == null || controllerFactory == null)
			throw new NullPointerException();
		
		this.navigator = navigator;
		this.controllerFactory = controllerFactory;
		errorMessage = "";
	}
	
	/**
	 * Makes the application start listening.
	 */
	public void startListening()
	{
		// attempt to create a server communicator
		try
		{
			IServerCommunicator serverCommunicator = new ServerCommunicator(new MessageFactory());
			
			// if successful, navigate to the game hosting screen
			IGameHostingController controller = controllerFactory.createGameHostingController(navigator, 
					serverCommunicator);
			navigator.push(controller);
		}
		catch (CommunicationException exception)
		{
			
			setErrorMessage(exception.getMessage());
		}
	}
	
	/**
	 * Exits the application.
	 */
	public void exit()
	{
		System.exit(0);
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
	@Override
	public String errorMessage()
	{
		return errorMessage;
	}
	
	/**
	 * Sets the error message to the given value.
	 * @param errorMessage The error message to be set.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		propertyDidChange("errorMessage");
	}
}