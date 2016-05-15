package server.controller;

/**
 * A controller interface specific to the server.
 */
public interface IController extends shared.controller.IController
{
	/**
	 * Returns the controller factory for this IController.
	 * @return The controller factory for this IController.
	 */
	public IControllerFactory controllerFactory();
}
