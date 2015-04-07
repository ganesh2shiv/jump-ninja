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

public class ScoreOptionScene extends BaseScene{

	private Text firstHighScore;
	private Text secondHighScore;
	private Text thirdHighScore;


	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		createBackground();
		OptionsWindow optionsWindow = new OptionsWindow(vbom);
		optionsWindow.display(ScoreOptionScene.this, camera);

		createHUD();

	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		SceneManager.getInstance().loadMenuFromScoreOptionScene();
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_SCORE_OPTION;
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
		HUD highScoreHUD = new HUD();

		Text highScoreTitle = new Text(0, 0, resourcesManager.font, "High Scores", new TextOptions(HorizontalAlign.RIGHT), vbom);
		highScoreTitle.setText("High Scores");
		highScoreTitle.setAnchorCenter(0, 0);
		highScoreTitle.setPosition(285, 355);

		firstHighScore = new Text(0, 0, resourcesManager.font, "0123456789. ", new TextOptions(HorizontalAlign.RIGHT), vbom);
		firstHighScore.setText("1. " + Utils.getPreferenceInt(activity, PreferenceKeys.FirstHighScore, 0));
		firstHighScore.setAnchorCenter(0, 0);
		firstHighScore.setPosition(335, 275);

		secondHighScore = new Text(0, 0, resourcesManager.font, "0123456789. ", new TextOptions(HorizontalAlign.RIGHT), vbom);
		secondHighScore.setText("2. " + Utils.getPreferenceInt(activity, PreferenceKeys.SecondHighScore, 0));
		secondHighScore.setAnchorCenter(0, 0);
		secondHighScore.setPosition(335, 200);

		thirdHighScore = new Text(0, 0, resourcesManager.font, "0123456789. ", new TextOptions(HorizontalAlign.RIGHT), vbom);
		thirdHighScore.setText("3. " + Utils.getPreferenceInt(activity, PreferenceKeys.ThirdHighScore, 0));
		thirdHighScore.setAnchorCenter(0, 0);
		thirdHighScore.setPosition(335, 125);

		Text clearAllScore = new Text(0, 0, resourcesManager.font, "clear all", new TextOptions(HorizontalAlign.RIGHT), vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
										 float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					Utils.addPreferenceInt(activity, PreferenceKeys.FirstHighScore, 0);
					Utils.addPreferenceInt(activity, PreferenceKeys.SecondHighScore, 0);
					Utils.addPreferenceInt(activity, PreferenceKeys.ThirdHighScore, 0);
					Utils.addPreferenceInt(activity, PreferenceKeys.CurrentLevel, 0);

					firstHighScore.setText("1. 0");
					secondHighScore.setText("2. 0");
					thirdHighScore.setText("3. 0");
				}
				return false;
			}
		};

		clearAllScore.setText("clear all");
		clearAllScore.setAnchorCenter(0, 0);
		clearAllScore.setPosition(330, 55);

		highScoreHUD.registerTouchArea(clearAllScore);
		highScoreHUD.setTouchAreaBindingOnActionDownEnabled(true);
		highScoreHUD.setTouchAreaBindingOnActionMoveEnabled(true);

		highScoreHUD.attachChild(highScoreTitle);
		highScoreHUD.attachChild(firstHighScore);
		highScoreHUD.attachChild(secondHighScore);
		highScoreHUD.attachChild(thirdHighScore);
		highScoreHUD.attachChild(clearAllScore);

		camera.setHUD(highScoreHUD);
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
