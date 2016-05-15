package shared.view;

import javax.swing.JPanel;

/**
 * Displays the main application frame.  Mains a stack of views for the application.
 */
public interface IViewStack 
{
	/**
	 * Pushes the provided view onto this IViewStack.
	 * @param view The view to push onto this IViewStack.
	 */
	public void push(JPanel view);
	
	/**
	 * Pops the top view off of this IViewStack and displays the view under it.  If no view
	 * exists under the current view, this IViewStack will display nothing.
	 * @throws IllegalStateException Thrown if there is currently no view on this IViewStack.
	 */
	public void pop();
}
