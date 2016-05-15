package client.controller;

/**
 * A controller interface specific to the client application.
 */
public interface IController extends shared.controller.IController
{
	/**
	 * Returns the controller factory for this IController.
	 * @return The controller factory for this IController.
	 */
	public IControllerFactory controllerFactory();
}
