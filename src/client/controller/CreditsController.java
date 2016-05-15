package client.controller;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;

import shared.controller.Controller;
import shared.controller.INavigator;

/**
 * Implementation of ICreditController.
 */
public class CreditsController extends Controller implements ICreditsController
{
	// the navigator
	private final INavigator navigator;

	// the controller factory
	private final IControllerFactory controllerFactory;

	/**
	 * CreditController controller.
	 * 
	 * @param navigator
	 *            The navigator.
	 * @param controllerFactory
	 *            The controllerFactory.
	 * @throws NullPointerException
	 *             Thrown if navigator or controllerFactory is null.
	 */
	public CreditsController(INavigator navigator,
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
	public void mainMenu()
	{
		navigator.pop();
	}

	/**
	 * {@inheritDoc}
	 */
	public void readme()
	{

		// make sure this platform supports the desktop command
		if (!Desktop.isDesktopSupported())
		{
			return;
		}

		Desktop desktop = Desktop.getDesktop();

		// make sure this platform supports the edit command
		if (!desktop.isSupported(Action.EDIT))
		{
			return;
		}

		// edit the readme file
		try
		{
			desktop.edit(new File("README.txt"));
		} catch (IOException exception)
		{
			return;
		}
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
