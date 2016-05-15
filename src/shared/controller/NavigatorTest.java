package shared.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import shared.view.IViewFactory;
import shared.view.IViewStack;

/**
 * 
 */
public class NavigatorTest
{
	// a mock view stack
	private IViewStack mockViewStack;
	
	// a mock view factory
	private IViewFactory mockViewFactory;
	
	// a test controller
	private IController mockController;
	
	// a test view
	private JPanel mockView;
	
	// a test navigator
	private INavigator navigator;
	
	/**
	 * Sets up the tests
	 */
	@Before
	public void setUp()
	{
		// set up the mocks
		mockViewStack = mock(IViewStack.class);
		mockViewFactory = mock(IViewFactory.class);
		mockController = mock(IController.class);
		mockView = mock(JPanel.class);
		
		// stub the createView method
		when(mockViewFactory.createView(any(IController.class))).thenReturn(mockView);
		
		// create a navigator for testing
		navigator = new Navigator(mockViewStack, mockViewFactory);
	}
	
	/**
	 * A simple test that couldn't fail.
	 */
	@Test
	public void sanityCheck()
	{
		new Navigator(mockViewStack, mockViewFactory);
	}
	
	/**
	 * Ensures the constructor throws an exception when the view stack argument is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorNullViewStackThrowsException()
	{
		new Navigator(null, mockViewFactory);
	}
	
	/**
	 * Ensures the constructor throws an exception when the view factory argument is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorNullViewFactoryThrowsException()
	{
		new Navigator(mockViewStack, null);
	}
	
	/**
	 * Ensures the push method throws an exception if the provided class is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testPushMethodNullControllerInterfaceThrowsException()
	{
		navigator.push(null);
	}
	
	/**
	 * Ensures the push method creates a view, creates a controller and pushes the view onto the view
	 * stack.
	 */
	@Test
	public void testPush()
	{
		navigator.push(mockController);
		verify(mockViewFactory, times(1)).createView(mockController);
		verify(mockViewStack, times(1)).push(mockView);
	}
	
	/**
	 * Ensures the pop method throws an exception when no controller interfaces have been pushed.
	 */
	@Test(expected=IllegalStateException.class)
	public void testPopNoControllerInterfacesPushed()
	{
		/*
		 * Stub ViewStack so it throws an IllegalStateException when popped with no views on the stack.
		 */
		doThrow(new IllegalStateException()).when(mockViewStack).pop();
		navigator.pop();
	}
	
	/**
	 * Ensures the pop method pops the top view off of the view stack.
	 */
	@Test
	public void testPop()
	{
		navigator.push(mockController);
		navigator.pop();
		verify(mockViewStack, times(1)).pop();
	}

	/**
	 * Ensures replaceTop throws a NullPointerException if the provided controller interface is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testReplaceTopNullControllerInterface()
	{
		navigator.push(null);
		navigator.replaceTop(null);
	}
	
	/**
	 * Ensures the replaceTop method throws an IllegalStateException if there are no controllers
	 * currently in the navigator.
	 */
	@Test(expected=IllegalStateException.class)
	public void testReplaceTopNoControllerOnStack()
	{
		/*
		 * Stub ViewStack so it throws an IllegalStateException when popped with no views on the stack.
		 */
		doThrow(new IllegalStateException()).when(mockViewStack).pop();
		navigator.replaceTop(mockController);
	}
	
	/**
	 * Ensures the replaceTop method creates a new controller, creates a new view, removes the old
	 * view from the view stack and pushes the new view onto the view stack.
	 */
	@Test
	public void testReplaceTop()
	{
		// get the navigator into a valid state
		navigator.push(mockController);
		
		// reset all of the mocks
		reset(mockViewFactory);
		reset(mockViewStack);
		
		// redo the stubs
		when(mockViewFactory.createView(any(IController.class))).thenReturn(mockView);
		
		// perform the replaceTop method
		navigator.replaceTop(mockController);
		
		// verify all of the mock methods were called correctly
		verify(mockViewFactory).createView(mockController);
		verify(mockViewStack).pop();
		verify(mockViewStack).push(mockView);
	}
}