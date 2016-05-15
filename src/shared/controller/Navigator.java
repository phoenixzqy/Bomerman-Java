package shared.controller;

import javax.swing.JPanel;

import shared.view.IViewFactory;
import shared.view.IViewStack;

/**
 * Implements the INavigator interface.
 */
public class Navigator implements INavigator
{
	// the view stack
	IViewStack viewStack;
	
	// view factory
	IViewFactory viewFactory;
	
	/**
	 * Constructor for Navigator
	 * @param viewStack The view stack.
	 * @param viewFactory The view factory.
	 * @throws NullPointerException Thrown if viewStack, controllerFactory or viewFactory is null.
	 */
	public Navigator(IViewStack viewStack, IViewFactory viewFactory)
	{
		if (viewStack == null || viewFactory == null)
			throw new NullPointerException();
		
		this.viewStack = viewStack;
		this.viewFactory = viewFactory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void push(IController controller)
	{
		if (controller == null)
			throw new NullPointerException();
		
		// create the controller and view
		JPanel view = viewFactory.createView(controller);
		
		viewStack.push(view);
	}

	public void replaceTop(IController controller)
	{
		/*
		 * This method cannot simply call pop and then push because it should verify the parameters
		 * before altering its state.
		 */
		if (controller == null)
			throw new NullPointerException();
		
		// create the controller and view
		JPanel view = viewFactory.createView(controller);

		// pop the top and push the new view
		viewStack.pop();
		viewStack.push(view);
	}

	/**
	 * {@inheritDoc}
	 */
	public void pop()
	{
		viewStack.pop();
	}
}
