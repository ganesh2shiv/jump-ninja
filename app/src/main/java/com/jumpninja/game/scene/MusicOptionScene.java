package com.jumpninja.game.scene;

import com.jumpninja.game.base.BaseScene;
import com.jumpninja.game.extras.OptionsWindow;
import com.jumpninja.game.manager.SceneManager;
import com.jumpninja.game.manager.SceneManager.SceneType;
import com.jumpninja.game.utils.PreferenceKeys;
import com.jumpninja.game.utils.Utils;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.align.HorizontalAlign;

public class MusicOptionScene extends BaseScene{

	private Text menuMusic_OnOff;
	private Text gameSound_OnOff;


	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		createBackground();
		OptionsWindow optionsWindow = new OptionsWindow(vbom);
		optionsWindow.display(MusicOptionScene.this, camera);

		createHUD();
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		SceneManager.getInstance().loadMenuFromMusicOptionScene();
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_MUSIC_OPTION;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		camera.setHUD(null);
		camera.setChaseEntity(null); //TODO
		camera.setCenter(400, 240);
	}

	private void createHUD()
	{
		HUD musicOptionHUD = new HUD();

		Text menuMusicText = new Text(0, 0, resourcesManager.font, "Menu music:", new TextOptions(HorizontalAlign.RIGHT), vbom);
		menuMusicText.setText("Menu music :");
		menuMusicText.setAnchorCenter(0, 0);
		menuMusicText.setPosition(200, 270);

		menuMusic_OnOff= new Text(0, 0, resourcesManager.font, "On Off", new TextOptions(HorizontalAlign.RIGHT), vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if(pSceneTouchEvent.isActionDown())
				{
					if(!Utils.getPreferenceBoolean(activity, PreferenceKeys.MenuMusicOn, true))
					{
						menuMusic_OnOff.setText("On");
						Utils.addPreferenceBoolean(activity, PreferenceKeys.MenuMusicOn, true);
					}
					else
					{
						menuMusic_OnOff.setText("Off");
						Utils.addPreferenceBoolean(activity, PreferenceKeys.MenuMusicOn, false);
					}
				}
				return false;
			}
		};

		if(Utils.getPreferenceBoolean(activity, PreferenceKeys.MenuMusicOn, true))
		{
			menuMusic_OnOff.setText("On");
		}
		else
		{
			menuMusic_OnOff.setText("Off");
		}
		menuMusic_OnOff.setAnchorCenter(0, 0);
		menuMusic_OnOff.setPosition(535, 270);

		Text gameSoundText = new Text(0, 0, resourcesManager.font, "Game sound:", new TextOptions(HorizontalAlign.RIGHT), vbom);
		gameSoundText.setText("Game sound :");
		gameSoundText.setAnchorCenter(0, 0);
		gameSoundText.setPosition(200, 150);

		gameSound_OnOff= new Text(0, 0, resourcesManager.font, "On Off", new TextOptions(HorizontalAlign.RIGHT), vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if(pSceneTouchEvent.isActionDown())
				{
					if(!Utils.getPreferenceBoolean(activity, PreferenceKeys.GameSoundOn, true))
					{
						gameSound_OnOff.setText("On");
						Utils.addPreferenceBoolean(activity, PreferenceKeys.GameSoundOn, true);
					}
					else
					{
						gameSound_OnOff.setText("Off");
						Utils.addPreferenceBoolean(activity, PreferenceKeys.GameSoundOn, false);
					}
				}
				return false;
			}
		};

		if(Utils.getPreferenceBoolean(activity, PreferenceKeys.GameSoundOn, true))
		{
			gameSound_OnOff.setText("On");
		}
		else
		{
			gameSound_OnOff.setText("Off");
		}

		gameSound_OnOff.setAnchorCenter(0, 0);
		gameSound_OnOff.setPosition(535, 150);

		musicOptionHUD.registerTouchArea(menuMusic_OnOff);
		musicOptionHUD.registerTouchArea(gameSound_OnOff);
		musicOptionHUD.setTouchAreaBindingOnActionDownEnabled(true);
		musicOptionHUD.setTouchAreaBindingOnActionMoveEnabled(true);

		musicOptionHUD.attachChild(menuMusicText);
		musicOptionHUD.attachChild(menuMusic_OnOff);
		musicOptionHUD.attachChild(gameSoundText);
		musicOptionHUD.attachChild(gameSound_OnOff);

		camera.setHUD(musicOptionHUD);

	}

	private void createBackground()
	{
		//		setBackground(new Background(Color.BLUE));
		attachChild(new Sprite(400, 240, resourcesManager.options_bg_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}


}
