package client.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import shared.controller.INavigator;

/**
 * Tests for ConnectionFailureController.
 */
public class ConnectionFailureControllerTest 
{
	// a mock navigator for testing
	private INavigator mockNavigator;
	
	// a mock controller factory
	private IControllerFactory mockControllerFactory;
	
	// a test connection failure controller
	private IConnectionFailureController connectionFailureController;
	
	// a test error message
	private String testErrorMessage;
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		mockNavigator = mock(INavigator.class);
		mockControllerFactory = mock(IControllerFactory.class);
		testErrorMessage = "Test error message.";
		connectionFailureController = new ConnectionFailureController(mockNavigator, 
			mockControllerFactory, testErrorMessage);
	}
	
	/**
	 * Ensures the constructor throws a null pointer exception when provided with a null navigator.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorNavigatorNull()
	{
		new ConnectionFailureController(null, mockControllerFactory, testErrorMessage);
	}
	
	/**
	 * Ensures the navigator method returns the navigator passed in to the constructor.
	 */
	@Test
	public void testNavigator()
	{
		assertEquals(mockNavigator, connectionFailureController.navigator());
	}
	
	/**
	 * Ensures the constructor throws a null pointer exception when provided with a null controller
	 * factory.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorControllerFactoryNull()
	{
		new ConnectionFailureController(mockNavigator, null, testErrorMessage);
	}
	
	/**
	 * Ensures the navigator method returns the controller factory passed in to the constructor.
	 */
	@Test
	public void testControllerFactory()
	{
		assertEquals(mockControllerFactory, connectionFailureController.controllerFactory());
	}
	
	/**
	 * Ensures the constructor throws a null pointer exception when provided with a null error
	 * message.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorErrorMessageNull()
	{
		new ConnectionFailureController(mockNavigator, mockControllerFactory, null);
	}
	
	/**
	 * Ensures the errorMessage method returns the error message passed into the constructor.
	 */
	@Test
	public void testErrorMessage()
	{
		assertEquals(testErrorMessage, connectionFailureController.errorMessage());
	}
	
	/**
	 * Ensures the multiplayer method navigates to the multiplayer screen.
	 */
	@Test
	public void testMultiplayerNavigates()
	{
		connectionFailureController.multiplayer();
		verify(mockNavigator, times(2)).pop();
	}
}
