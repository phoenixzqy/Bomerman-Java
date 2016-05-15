package shared.view;

import javax.swing.JPanel;

import shared.controller.IController;

/**
 * Factory for views.
 */
public interface IViewFactory
{
	/**
	 * Creates a new view which corresponds to the provided controller.
	 * @param controller The controller controller used to create the view.
	 * @return Returns a newly created view corresponding to the provided controller.
	 * @throws IllegalArgumentException Thrown if this IViewFactory does not know how to create a view
	 * using the provided controller.
	 */
	public JPanel createView(IController controller);
}
