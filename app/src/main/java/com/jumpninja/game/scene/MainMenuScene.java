package com.jumpninja.game.scene;

import com.jumpninja.game.base.BaseScene;
import com.jumpninja.game.manager.ResourcesManager;
import com.jumpninja.game.manager.SceneManager;
import com.jumpninja.game.manager.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private MenuScene mMenuScene;
    private MenuScene mOptionsMenuScene;

    private final int MENU_PLAY = 0;
    private final int MENU_OPTIONS = 1;
    private final int MENU_HIGH_SCORE = 2;
    private final int MENU_MUSIC = 3;

    //---------------------------------------------
    // METHODS FROM SUPERCLASS
    //---------------------------------------------

    @Override
    public void createScene() {
        createMenuBackground();
        createMenuScene();
        createOptionsMenuScene();
        ResourcesManager.getInstance().playMenuAudio();
    }

    @Override
    public void onBackKeyPressed() {
        if (mMenuScene.hasChildScene()) {
            createMenuBackground();
            mOptionsMenuScene.back();
        } else {
            System.exit(0);
        }
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_MENU;
    }


    @Override
    public void disposeScene() {
        // TODO Auto-generated method stub
        ResourcesManager.getInstance().pauseMenuAudio();
    }

    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case MENU_PLAY:
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_OPTIONS:
                createSubMenuBackground();
                pMenuScene.setChildSceneModal(mOptionsMenuScene);
                return true;
            case MENU_HIGH_SCORE:
                SceneManager.getInstance().loadScoreOptionScene();
                return true;
            case MENU_MUSIC:
                SceneManager.getInstance().loadMusicOptionScene();
                return true;
            default:
                return false;
        }
    }

    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    private void createMenuBackground() {
        attachChild(new Sprite(400, 240, resourcesManager.menu_bg_region, vbom) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        });
    }

    private void createSubMenuBackground() {
        attachChild(new Sprite(400, 240, resourcesManager.sub_menu_bg_region, vbom) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        });
    }

    private void createMenuScene() {
        mMenuScene = new MenuScene(camera);
        mMenuScene.setPosition(0, 0);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.10f, 1);
        final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.10f, 1);
        mMenuScene.addMenuItem(playMenuItem);
        mMenuScene.addMenuItem(optionsMenuItem);

        mMenuScene.buildAnimations();
        mMenuScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 60);
        optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 80);

        mMenuScene.setOnMenuItemClickListener(this);

        setChildScene(mMenuScene, false, true, true);
    }

    private void createOptionsMenuScene() {

        mOptionsMenuScene = new MenuScene(camera);

        final IMenuItem scoreMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_HIGH_SCORE, resourcesManager.high_score_region, vbom), 1.10f, 1);
        final IMenuItem musicMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_MUSIC, resourcesManager.music_region, vbom), 1.10f, 1);
        mOptionsMenuScene.addMenuItem(scoreMenuItem);
        mOptionsMenuScene.addMenuItem(musicMenuItem);

        mOptionsMenuScene.buildAnimations();
        mOptionsMenuScene.setBackgroundEnabled(false);
        scoreMenuItem.setPosition(scoreMenuItem.getX(), scoreMenuItem.getY() - 60);
        musicMenuItem.setPosition(musicMenuItem.getX(), musicMenuItem.getY() - 80);

        mOptionsMenuScene.setOnMenuItemClickListener(this);
    }

}