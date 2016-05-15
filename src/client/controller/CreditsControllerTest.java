package client.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.controller.INavigator;

/**
 * Tests CreditsController.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { CreditsController.class })
public class CreditsControllerTest 
{
	// a mock navigator for testing
	private INavigator mockNavigator;
	
	// a mock controller factory
	private IControllerFactory mockControllerFactory;
	
	// a test credits controller
	private ICreditsController creditsController;

	/**
	 * Private helper method which flushes EventQueue (which SwingUtilities uses).
	 * @throws Exception Thrown by SwingUtilities.
	 */
	private void flushEventQueue() throws Exception
	{
		/*
		 * This is the only way to really get EventQueue to flush.  Simply force all previous commands
		 * to run first with invokeAndWait.
		 */
		SwingUtilities.invokeAndWait(new Runnable()
		{
			public void run()
			{
				// do nothing
			}
		});
	}
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		mockNavigator = mock(INavigator.class);
		mockControllerFactory = mock(IControllerFactory.class);
		creditsController = new CreditsController(mockNavigator, mockControllerFactory);
	}
	
	/**
	 * Ensures the constructor throws a null pointer exception when provided with a null navigator.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorNavigatorNull()
	{
		new CreditsController(null, mockControllerFactory);
	}
	
	/**
	 * Ensures the navigator method returns the navigator passed in to the constructor.
	 */
	@Test
	public void testNavigator()
	{
		assertEquals(mockNavigator, creditsController.navigator());
	}
	
	/**
	 * Ensures the constructor throws a null pointer exception when provided with a null controller
	 * factory.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorControllerFactoryNull()
	{
		new CreditsController(mockNavigator, null);
	}
	
	/**
	 * Ensures the navigator method returns the controller factory passed in to the constructor.
	 */
	@Test
	public void testControllerFactory()
	{
		assertEquals(mockControllerFactory, creditsController.controllerFactory());
	}
	
	/**
	 * Ensures the main menu method navigates to the main menu screen.
	 */
	@Test
	public void testMultiplayerNavigates()
	{
		creditsController.mainMenu();
		verify(mockNavigator, times(1)).pop();
	}
	
	/**
	 * Ensures the readme method doesn't open the readme file if the desktop is not supported.
	 * @throws Exception Thrown from SwingUtilities.invokeAndWait().
	 */
	@Test
	public void testReadmeMethodDoesntDoAnythingIfDesktopNotSupported() throws Exception
	{
		// mock the desktop class
		PowerMockito.mockStatic(Desktop.class);
		when(Desktop.isDesktopSupported()).thenReturn(false);

		creditsController.readme();
		flushEventQueue();
		
		// make sure isDesktopSupported was called
		PowerMockito.verifyStatic();
		Desktop.isDesktopSupported();
		
		// make sure getDesktop was called
		PowerMockito.verifyStatic(never());
		Desktop.getDesktop();
	}
	
	/**
	 * Ensures the readme method doesn't do anything if the edit command is not supported.
	 * @throws Exception Thrown from SwingUtilities.invokeAndWait().
	 */
	@Test
	public void testReadmeMethodDoesntDoAnythingIfEditNotSupported() throws Exception
	{
		// mock the desktop class
		PowerMockito.mockStatic(Desktop.class);
		when(Desktop.isDesktopSupported()).thenReturn(true);
		final Desktop mockDesktop = mock(Desktop.class);
		when(mockDesktop.isSupported(Desktop.Action.EDIT)).thenReturn(false);
		when(Desktop.getDesktop()).thenReturn(mockDesktop);
		
		creditsController.readme();
		flushEventQueue();
				
		// make sure isDesktopSupported was called
		PowerMockito.verifyStatic();
		Desktop.isDesktopSupported();

		// make sure getDesktop was called
		PowerMockito.verifyStatic();
		Desktop.getDesktop();
				
		// make sure isSupported method was called
		verify(mockDesktop).isSupported(Desktop.Action.EDIT);
				
		// make sure the edit method was not called
		try
		{
			verify(mockDesktop, never()).edit(any(File.class));
		}
		catch (IOException exception)
		{
			// do nothing
		}
	}
	
	/**
	 * Ensures the readme method opens the README.txt file. 
	 * @throws Exception Thrown from SwingUtilities.invokeAndWait().
	 */
	@Test
	public void testReadmeMethodOpensReadmeFile() throws Exception
	{
		// mock the desktop class
		PowerMockito.mockStatic(Desktop.class);
		when(Desktop.isDesktopSupported()).thenReturn(true);
		final Desktop mockDesktop = mock(Desktop.class);
		when(mockDesktop.isSupported(Desktop.Action.EDIT)).thenReturn(true);
		when(Desktop.getDesktop()).thenReturn(mockDesktop);
		
		creditsController.readme();
		flushEventQueue();
				
		// make sure isDesktopSupported was called
		PowerMockito.verifyStatic();
		Desktop.isDesktopSupported();

		// make sure getDesktop was called
		PowerMockito.verifyStatic();
		Desktop.getDesktop();
				
		// make sure isSupported method was called
		verify(mockDesktop).isSupported(Desktop.Action.EDIT);
				
		// make sure the edit method was not called
		try
		{
			verify(mockDesktop).edit(any(File.class));
		}
		catch (IOException exception)
		{
			// do nothing
		}
	}
}
