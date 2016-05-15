package server.view;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import server.controller.IGameHostingController;
import server.controller.IMainMenuController;
import shared.controller.IController;
import shared.core.ICommand;
import shared.view.IMenuViewBuilder;
import shared.view.IViewFactory;
import shared.view.MenuViewBuilder;

/**
 * Tests ViewFactory.
 */
public class ViewFactoryTest extends shared.view.ViewTestHelper
{
	// a menu view builder
	private IMenuViewBuilder menuViewBuilder;
	
	// a test view factory
	private IViewFactory viewFactory;
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		/*
		 * Note: the menu view builder is not being mocked because it doens't have any dependencies and
		 * it would be too much work to mock it.
		 */
		menuViewBuilder = new MenuViewBuilder();
		viewFactory = new ViewFactory(menuViewBuilder);
	}
	
	/**
	 * Ensures the constructor works properly when provided with valid arguments.
	 */
	@Test
	public void testConstructor()
	{
		new ViewFactory(menuViewBuilder);
	}
	
	/**
	 * Ensures the constructor throws an exception if the provided menu view builder is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorNullMenuViewBuilderThrowsException()
	{
		new ViewFactory(null);
	}
	
	/**
	 * Ensures createView throws a NullPointerException when provided with a null controller.
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateViewNullController()
	{
		viewFactory.createView(null);
	}
	
	/**
	 * Ensures createView throws a IllegalArgumentException when provided with an unknown controller.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateViewUnknownControllerThrowsException()
	{
		IController mockController = mock(IController.class);
		viewFactory.createView(mockController);
	}
	
	/**
	 * Ensures createView returns a game hosting view when provided with a game hosting controller.
	 * @throws InvocationTargetException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testCreateViewGameHostingController() throws InterruptedException, InvocationTargetException
	{
		final IGameHostingController mockController = mock(IGameHostingController.class);
		final JPanel view = viewFactory.createView(mockController);
		
		// capture the command passed in to the controller
		ArgumentCaptor<ICommand> commandCaptor = ArgumentCaptor.forClass(ICommand.class);
		verify(mockController).bind(eq("running"), commandCaptor.capture());
		final ICommand runningCommand = commandCaptor.getValue();
		
		// check that everything is set up properly when the game is initially not running
		checkLabelExists(view, "Host a Game");
		checkLabelExists(view, "Accepting connections from clients.");
		
		checkButtonExistsAndClickOnIt(view, "Start Game");
		verify(mockController).startOrStopGame();
		verify(mockController, never()).mainMenu();
		reset(mockController);

		checkButtonExistsAndClickOnIt(view, "Main Menu");
		verify(mockController, never()).startOrStopGame();
		verify(mockController).mainMenu();
		reset(mockController);
		
		// start the game
		when(mockController.running()).thenReturn(true);
		runningCommand.execute();

	}
	
	/**
	 * Ensures createView returns a main menu view when provided with a main menu controller.
	 */
	@Test
	public void testCreateViewMainMenuController()
	{
		IMainMenuController mockController = mock(IMainMenuController.class);
		JPanel view = viewFactory.createView(mockController);
		
		checkLabelExists(view, "Bomberman");
		
		// check the buttons do what they're supposed to do
		checkButtonExistsAndClickOnIt(view, "Host a Game");
		verify(mockController).startListening();
		verify(mockController, never()).exit();
		reset(mockController);

		checkButtonExistsAndClickOnIt(view, "Exit");
		verify(mockController).exit();
		verify(mockController, never()).startListening();
	}
}
