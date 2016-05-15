package client.view;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import java.awt.Component;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.controller.IController;
import shared.model.IScore;
import shared.view.IMenuViewBuilder;
import shared.view.IViewFactory;
import shared.view.MenuViewBuilder;
import shared.view.ViewTestHelper;
import client.controller.IConnectionFailureController;
import client.controller.ICreditsController;
import client.controller.IGameController;
import client.controller.IGameLobbyController;
import client.controller.IGameOverController;
import client.controller.IMainMenuController;
import client.controller.IMultiplayerController;

/**
 * Tests ViewFactory.
 */
@RunWith(PowerMockRunner.class)
public class ViewFactoryTest
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
		 * Note: the menu view builder is not being mocked because it doens't
		 * have any dependencies and it would be too much work to mock it.
		 */
		menuViewBuilder = new MenuViewBuilder();
		viewFactory = new ViewFactory(menuViewBuilder);
	}

	/**
	 * Ensures the constructor works properly when provided with valid
	 * arguments.
	 */
	@Test
	public void testConstructor()
	{
		new ViewFactory(menuViewBuilder);
	}

	/**
	 * Ensures the constructor throws an exception if the provided menu view
	 * builder is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNullMenuViewBuilderThrowsException()
	{
		new ViewFactory(null);
	}

	/**
	 * Ensures createView throws a NullPointerException when provided with a
	 * null controller.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateViewNullController()
	{
		viewFactory.createView(null);
	}

	/**
	 * Ensures createView throws a IllegalArgumentException when provided with
	 * an unknown controller.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateViewUnknownControllerThrowsException()
	{
		IController mockController = mock(IController.class);
		viewFactory.createView(mockController);
	}

	/**
	 * Ensures createView returns a connection failure view when provided with a
	 * connection failure controller.
	 */
	@Test
	public void testCreateViewConnectionFailureController()
	{
		String testErrorMessage = "Test Error Message";

		IConnectionFailureController mockController = mock(IConnectionFailureController.class);
		when(mockController.errorMessage()).thenReturn(testErrorMessage);

		JPanel view = viewFactory.createView(mockController);

		ViewTestHelper.checkLabelExists(view, "Connection Failure");

		ViewTestHelper.checkLabelExists(view, testErrorMessage);

		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Multiplayer");
		verify(mockController).multiplayer();
		reset(mockController);
	}

	/**
	 * Ensures createView return a credits view when provided with a credits
	 * controller.
	 */
	@Test
	public void testCreateViewCreditsController()
	{
		ICreditsController mockController = mock(ICreditsController.class);
		JPanel view = viewFactory.createView(mockController);

		ViewTestHelper.checkLabelExists(view, "Credits");

		ViewTestHelper.checkLabelExists(view, "Team: E1");

		// make sure a project description is present.
		ViewTestHelper.checkLabelExists(view,
				"Team Member: Schropp,Landon   Zhao,Qiyu");
		ViewTestHelper.checkLabelExists(view,
				"                             Brouwer,Daniel   Tan,Sen ");

		// check the buttons do what they're supposed to do
		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Open README");
		verify(mockController).readme();
		verify(mockController, never()).mainMenu();
		reset(mockController);

		// check the buttons do what they're supposed to do
		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Main Menu");
		verify(mockController, never()).readme();
		verify(mockController).mainMenu();
	}

	/**
	 * Ensures createView return a game view when provided with a game
	 * controller.
	 */
	@Test
	public void testCreateViewGameViewController()
	{
		IGameController mockController = mock(IGameController.class);
		IScore[] scores = new IScore[4];
		for (int i = 0; i < scores.length; i++)
		{
			scores[i] = mock(IScore.class);
			when(scores[i].getScore()).thenReturn(0);
		}
		when(mockController.scores()).thenReturn(scores);
		JPanel view = viewFactory.createView(mockController);

		Component[] viewComponents = view.getComponents();
		boolean foundGameView = false;
		for (Component viewComponent : viewComponents)
		{
			if (viewComponent instanceof GameView)
			{
				foundGameView = true;
				break;
			}
		}
		assertTrue(foundGameView);
	}

	/**
	 * Ensures createView returns a game view with key events bound to the
	 * specified game controller.
	 * @throws Exception Thrown by PowerMockito.
	 */
	@Test
	public void testCreateViewGameViewControllerKeyEventsBound() throws Exception
	{
		IGameController mockController = mock(IGameController.class);
		IScore[] scores = new IScore[4];
		for (int i = 0; i < scores.length; i++)
		{
			scores[i] = mock(IScore.class);
			when(scores[i].getScore()).thenReturn(0);
		}
		when(mockController.scores()).thenReturn(scores);
		KeyBinder mockKeyBinder = mock(KeyBinder.class);
		PowerMockito.whenNew(KeyBinder.class).withArguments(anyObject(), anyObject()).thenReturn(mockKeyBinder);
		viewFactory.createView(mockController);
		PowerMockito.verifyNew(KeyBinder.class);

	}

	/**
	 * Ensures createView return a game lobby view when provided with a game
	 * lobby controller.
	 */
	@Test
	public void testCreateViewGameLobbyController()
	{

		IGameLobbyController mockController = mock(IGameLobbyController.class);
		JPanel view = viewFactory.createView(mockController);

		ViewTestHelper.checkLabelExists(view, "Game Lobby");

		ViewTestHelper.checkLabelExists(view, "Waiting for the game to start.");

		// check for number of connected players
		ViewTestHelper.checkLabelExists(view, "Connected Players: 0");

		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Cancel");
		verify(mockController).cancel();
	}

	/**
	 * Ensures createView return a game over view when provided with a game over
	 * controller.
	 */
	@Test
	public void testCreateViewGameOverController()
	{
		IGameOverController mockController = mock(IGameOverController.class);
		IScore[] scores = new IScore[4];
		for (int i = 0; i < scores.length; i++)
		{
			scores[i] = mock(IScore.class);
			when(scores[i].getScore()).thenReturn(i);
		}
		when(mockController.scores()).thenReturn(scores);
		JPanel view = viewFactory.createView(mockController);

		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Rematch");
		verify(mockController).rematch();
		verify(mockController, never()).mainMenu();
		reset(mockController);

		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Main Menu");
		verify(mockController, never()).rematch();
		verify(mockController).mainMenu();
	}

	/**
	 * Ensures createView return a main menu view when provided with a main menu
	 * controller.
	 */
	@Test
	public void testCreateViewMainMenuController()
	{
		IMainMenuController mockController = mock(IMainMenuController.class);
		JPanel view = viewFactory.createView(mockController);

		// check the buttons do what they're supposed to do
		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Multiplayer");
		verify(mockController).multiplayer();
		verify(mockController, never()).credits();
		verify(mockController, never()).exit();
		reset(mockController);

		// check the buttons do what they're supposed to do
		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Credits");
		verify(mockController, never()).multiplayer();
		verify(mockController).credits();
		verify(mockController, never()).exit();
		reset(mockController);

		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Exit");
		verify(mockController, never()).multiplayer();
		verify(mockController, never()).credits();
		verify(mockController).exit();
	}

	/**
	 * Ensures createView return a multiplayer view when provided with a
	 * multiplayer controller.
	 * 
	 * @throws Exception
	 *             Thrown by PowerMockito.
	 */
	@Test
	public void testCreateViewMultiplayerController() throws Exception
	{
		IMultiplayerController mockController = mock(IMultiplayerController.class);
		JPanel view = viewFactory.createView(mockController);

		// test for server address label
		ViewTestHelper.checkLabelExists(view, "Server Address:");

		// test for error label
		ViewTestHelper.checkLabelExists(view, "Waiting to Join Game");

		// test for address text field
		ViewTestHelper.checkTextFieldExists(view);

		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Join");
		verify(mockController).join();
		verify(mockController, never()).mainMenu();
		reset(mockController);

		// check the buttons do what they're supposed to do
		ViewTestHelper.checkButtonExistsAndClickOnIt(view, "Main Menu");
		verify(mockController, never()).join();
		verify(mockController).mainMenu();
	}
}
