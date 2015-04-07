package com.jumpninja.game.manager;

import com.jumpninja.game.base.BaseScene;
import com.jumpninja.game.scene.GameScene;
import com.jumpninja.game.scene.LoadingScene;
import com.jumpninja.game.scene.MainMenuScene;
import com.jumpninja.game.scene.MusicOptionScene;
import com.jumpninja.game.scene.ScoreOptionScene;
import com.jumpninja.game.scene.SplashScene;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class SceneManager
{
	//---------------------------------------------
	// SCENES
	//---------------------------------------------

	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene loadingScene;
	private BaseScene musicOptionScene;
	private BaseScene scoreOptionScene;

	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	private static final SceneManager INSTANCE = new SceneManager();

	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private BaseScene currentScene;

	private final Engine engine = ResourcesManager.getInstance().engine;

	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_MUSIC_OPTION,
		SCENE_SCORE_OPTION,
		SCENE_GAME,
		SCENE_LOADING,
	}

	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	private void setScene(BaseScene scene)
	{
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	public void setScene(SceneType sceneType)
	{
		switch (sceneType)
		{
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_MUSIC_OPTION:
			setScene(musicOptionScene);
			break;
		case SCENE_SCORE_OPTION:
			setScene(scoreOptionScene);
			break;
		case SCENE_GAME:
			setScene(gameScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		}
	}

	public void createMenuScene()
	{
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		SceneManager.getInstance().setScene(menuScene);
		disposeSplashScene();
	}

	public void loadMenuFromGameScene(final Engine mEngine)
	{
		setScene(loadingScene);
		gameScene.disposeScene();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback()
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadMenuTextures();
				ResourcesManager.getInstance().playMenuAudio();
				setScene(menuScene);
			}
		}));
	}

	public void loadMenuFromMusicOptionScene()
	{
		musicOptionScene.disposeScene();
		ResourcesManager.getInstance().unloadOptionTextures();

		ResourcesManager.getInstance().loadMenuTextures();
		ResourcesManager.getInstance().playMenuAudio();
		setScene(menuScene);
	}

	public void loadMenuFromScoreOptionScene()
	{
		scoreOptionScene.disposeScene();
		ResourcesManager.getInstance().unloadOptionTextures();

		ResourcesManager.getInstance().loadMenuTextures();
		ResourcesManager.getInstance().playMenuAudio();
		setScene(menuScene);
	}

	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	private void disposeSplashScene()
	{
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}

	public void loadGameScene(final Engine mEngine)
	{
		setScene(loadingScene);
		menuScene.disposeScene();
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadGameResources();
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
	}

	public void loadMusicOptionScene()
	{
		menuScene.disposeScene();
		ResourcesManager.getInstance().unloadMenuTextures();
		ResourcesManager.getInstance().loadOptionsResources();
		musicOptionScene = new MusicOptionScene();
		setScene(musicOptionScene);
	}

	public void loadScoreOptionScene()
	{
		menuScene.disposeScene();
		ResourcesManager.getInstance().unloadMenuTextures();
		ResourcesManager.getInstance().loadOptionsResources();
		scoreOptionScene = new ScoreOptionScene();
		setScene(scoreOptionScene);
	}

	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------

	public static SceneManager getInstance()
	{
		return INSTANCE;
	}

	public SceneType getCurrentSceneType()
	{
		return currentSceneType;
	}

	public BaseScene getCurrentScene()
	{
		return currentScene;
	}
}