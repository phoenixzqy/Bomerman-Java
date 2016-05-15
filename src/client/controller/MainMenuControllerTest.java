package client.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.controller.INavigator;

/**
 * Tests MainMenuController.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { MainMenuController.class })
public class MainMenuControllerTest 
{
	// a mock navigator
	private INavigator mockNavigator;
	
	// a mock controller factory
	private IControllerFactory mockControllerFactory;
	
	// the test controller
	private IMainMenuController mainMenuController;
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		mockNavigator = mock(INavigator.class);
		mockControllerFactory = mock(IControllerFactory.class);
		mainMenuController = new MainMenuController(mockNavigator, mockControllerFactory);
	}
	
	/**
	 * Ensures the constructor throws a null pointer exception when provided with a null navigator.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorNavigatorNull()
	{
		new MainMenuController(null, mockControllerFactory);
	}
	
	/**
	 * Ensures the navigator method returns the navigator passed in to the constructor.
	 */
	@Test
	public void testNavigator()
	{
		assertEquals(mockNavigator, mainMenuController.navigator());
	}
	
	/**
	 * Ensures the constructor throws a null pointer exception when provided with a null controller
	 * factory.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorControllerFactoryNull()
	{
		new MainMenuController(mockNavigator, null);
	}
	
	/**
	 * Ensures the navigator method returns the controller factory passed in to the constructor.
	 */
	@Test
	public void testControllerFactory()
	{
		assertEquals(mockControllerFactory, mainMenuController.controllerFactory());
	}
	
	/**
	 * Ensures the credits method navigates to the credits screen.
	 */
	@Test
	public void testCreditsNavigatesToCredits()
	{
		ICreditsController mockCreditsController = mock(ICreditsController.class);
		stub(mockControllerFactory.createCreditsController(any(INavigator.class)))
			.toReturn(mockCreditsController);

		mainMenuController.credits();
		
		verify(mockControllerFactory).createCreditsController(mockNavigator);
		verify(mockNavigator, times(1)).push(mockCreditsController);
	}
	
	/**
	 * Ensures the multiplayer command navigates to the multiplayer screen.
	 */
	@Test
	public void testMultiplayerCommandNavigatesToMultiplayer()
	{
		IMultiplayerController mockMultiplayerController = mock(IMultiplayerController.class);
		stub(mockControllerFactory.createMultiplayerController(any(INavigator.class)))
			.toReturn(mockMultiplayerController);
		
		mainMenuController.multiplayer();

		verify(mockControllerFactory).createMultiplayerController(mockNavigator);
		verify(mockNavigator, times(1)).push(mockMultiplayerController);
	}
	
	/**
	 * Ensures the exit command exits the application.
	 */
	@Test
	public void testExitCommand()
	{
		/*
		 * Instructions for how to do this insanely difficult test are from:
		 * http://loongest.com/powermock/mock-static-void-method/
		 */
		
		// mock the system class
		PowerMockito.mockStatic(System.class);
		
		// run the test code
		mainMenuController.exit();
		
		// make sure System.exit(0) was called
		PowerMockito.verifyStatic(times(1));
		System.exit(0);
	}
}
