package com.jumpninja.game.scene;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jumpninja.game.base.BaseScene;
import com.jumpninja.game.extras.LevelCompleteWindow;
import com.jumpninja.game.extras.LevelCompleteWindow.StarsCount;
import com.jumpninja.game.manager.ResourcesManager;
import com.jumpninja.game.manager.SceneManager;
import com.jumpninja.game.manager.SceneManager.SceneType;
import com.jumpninja.game.object.Player;
import com.jumpninja.game.utils.PreferenceKeys;
import com.jumpninja.game.utils.Utils;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import java.io.IOException;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private final static String TAG = "GameScene";
    private int level;
    private int score = 0;

    private HUD gameHUD;

    private PhysicsWorld physicsWorld;
    private LevelCompleteWindow levelCompleteWindow;

    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";

    private Player player;

    private Text gameOverText;
    private Text gameMenuText;
    private Text gameReplayText;
    private Text nextLevelText;
    private Text levelText;
    private Text scoreText;
    private Text pauseText;

    private boolean gameOverDisplayed = false;

    private boolean gamePaused = false;
    private boolean shouldJump = false;

    private boolean firstTouch = false;

    @Override
    public void createScene() {
        level = Utils.getPreferenceInt(activity, PreferenceKeys.CurrentLevel, 0) + 1;
        createBackground();
        createHUD();
        createPhysics();
        Log.d(TAG, "Value of level:" + level);
        Log.d(TAG, "Value of preference:" + Utils.getPreferenceInt(activity, PreferenceKeys.CurrentLevel, 0));

        loadLevel(Utils.getPreferenceInt(activity, PreferenceKeys.CurrentLevel, 0));
        createGameOverText();

        levelCompleteWindow = new LevelCompleteWindow(vbom);

        setOnSceneTouchListener(this);
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuFromGameScene(engine);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
        camera.setHUD(null);
        camera.setChaseEntity(null); //TODO
        camera.setCenter(400, 240);

        // TODO code responsible for disposing scene
        // removing all game scene objects.
    }

    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown() && !gameOverDisplayed) {
            if (!firstTouch && !gamePaused) {
                player.setRunning();
                firstTouch = true;
            } else if (!gamePaused && !shouldJump) {
                player.jump();
                ResourcesManager.getInstance().playShootAudio();
            }
            shouldJump = false;
        }
        return false;
    }

    private void loadLevel(int levelID) {
        final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL) {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
                final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

                camera.setBounds(0, 0, width, height); // here we set camera bounds
                camera.setBoundsEnabled(true);

                return GameScene.this;
            }
        });

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY) {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

                final Sprite levelObject;

                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1)) {
                    levelObject = new Sprite(x, y, resourcesManager.platform1_region, vbom);
                    PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2)) {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform2");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3)) {
                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform3");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN)) {
                    levelObject = new Sprite(x, y, resourcesManager.coin_region, vbom) {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) {
                            super.onManagedUpdate(pSecondsElapsed);

                            if (player.collidesWith(this)) {
                                ResourcesManager.getInstance().playCoinAudio();

                                addToScore(10);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
                    player = new Player(x, y, vbom, camera, physicsWorld) {
                        @Override
                        public void onDie() {
                            if (!gameOverDisplayed) {
                                compareScores();
                                player.stopRunning();
                                player.stopAnimation();
                                displayGameOverText();
                            }
                        }
                    };
                    levelObject = player;
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE)) {
                    levelObject = new Sprite(x, y, resourcesManager.complete_stars_region, vbom) {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) {
                            super.onManagedUpdate(pSecondsElapsed);

                            if (player.collidesWith(this)) {
                                compareScores();
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                                GameScene.this.unregisterUpdateHandler(physicsWorld);
                                player.stopAnimation();
                                player.stopRunning();

                                gameOverDisplayed = true;
                                if (score > 0 && score <= 34) {
                                    levelCompleteWindow.display(StarsCount.ONE, GameScene.this, camera);
                                } else if (score > 34 && score <= 68) {
                                    levelCompleteWindow.display(StarsCount.TWO, GameScene.this, camera);
                                } else if (score > 68 && score <= 102) {
                                    levelCompleteWindow.display(StarsCount.THREE, GameScene.this, camera);
                                } else if (score > 102 && score <= 136) {
                                    levelCompleteWindow.display(StarsCount.FOUR, GameScene.this, camera);
                                } else if (score > 136 && score <= 170) {
                                    levelCompleteWindow.display(StarsCount.FIVE, GameScene.this, camera);
                                }

                                createNextLevelHUD();
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                } else {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);

                return levelObject;
            }
        });

        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
    }

    private void createNextLevelHUD() {
        gameHUD.detachChild(pauseText);
        gameHUD.detachChild(levelText);
        scoreText.setPosition(325, 410);
        gameHUD.registerTouchArea(nextLevelText);
        gameHUD.attachChild(nextLevelText);
    }

    private void createGameOverText() {
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
        gameOverText.setText("Game Over!");

        gameMenuText = new Text(0, 0, resourcesManager.font, "Menu", vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                                         float pTouchAreaLocalX, float pTouchAreaLocalY) {
                // TODO Auto-generated method stub
                if (pSceneTouchEvent.isActionDown()) {
                    camera.setCenter(400, 240);
                    SceneManager.getInstance().loadMenuFromGameScene(engine);
                }
                return false;
            }
        };
        gameMenuText.setText("Menu");

        gameReplayText = new Text(0, 0, resourcesManager.font, "replay", vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                                         float pTouchAreaLocalX, float pTouchAreaLocalY) {
                // TODO Auto-generated method stub
                if (pSceneTouchEvent.isActionDown()) {
                    camera.setCenter(400, 240);
                    SceneManager.getInstance().loadGameScene(engine);
                }
                return false;
            }
        };
        gameReplayText.setText("replay");

    }

    private void displayGameOverText() {
        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
        gameMenuText.setPosition(camera.getCenterX(), camera.getCenterY() * 0.15f);
        gameReplayText.setPosition(camera.getCenterX(), camera.getCenterY() * 1.85f);

        gameHUD.detachChild(pauseText);
        attachChild(gameOverText);
        attachChild(gameReplayText);
        attachChild(gameMenuText);

        GameScene.this.registerTouchArea(gameReplayText);
        GameScene.this.registerTouchArea(gameMenuText);

        gameOverDisplayed = true;
    }

    private void createHUD() {
        gameHUD = new HUD();

        scoreText = new Text(0, 0, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.RIGHT), vbom);
        scoreText.setText("Score: 0");
        scoreText.setAnchorCenter(0, 0);
        scoreText.setPosition(20, 410);

        levelText = new Text(0, 0, resourcesManager.font, "level: 0123456789", new TextOptions(HorizontalAlign.RIGHT), vbom);
        levelText.setText("level: 0" + level);
        levelText.setAnchorCenter(0, 0);
        levelText.setPosition(625, 410);

        pauseText = new Text(0, 0, resourcesManager.font, "pauserm", new TextOptions(HorizontalAlign.RIGHT), vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                                         float pTouchAreaLocalX, float pTouchAreaLocalY) {
                // TODO Auto-generated method stub
                if (pSceneTouchEvent.isActionDown() && !gameOverDisplayed) {
                    if (!gamePaused) {
                        GameScene.this.unregisterUpdateHandler(physicsWorld);

                        player.stopAnimation();
                        player.stopRunning();

                        pauseText.setText("resume");
                        gamePaused = true;
                        shouldJump = false;
                        Log.e("GameScene", "gamePaused = true");
                    } else {
                        GameScene.this.registerUpdateHandler(physicsWorld);
                        player.setRunning();
                        pauseText.setText("pause");
                        gamePaused = false;
                        shouldJump = true;
                        Log.e("GameScene", "gamePaused = false");
                    }
                }
                return false;
            }
        };
        pauseText.setText("pause");
        pauseText.setAnchorCenter(0, 0);
        pauseText.setPosition(355, 415);

        nextLevelText = new Text(0, 0, resourcesManager.font, "Next level", vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                                         float pTouchAreaLocalX, float pTouchAreaLocalY) {
                // TODO Auto-generated method stub
                if (pSceneTouchEvent.isActionDown()) {
                    if (level < 3) {
                        Utils.addPreferenceInt(activity, PreferenceKeys.CurrentLevel, level);
                    }
                    gameHUD.detachChild(scoreText);
                    gameHUD.detachChild(nextLevelText);
                    camera.setCenter(400, 240);
                    SceneManager.getInstance().loadGameScene(engine);
                }
                return false;
            }
        };
        nextLevelText.setText("Next level");
        nextLevelText.setAnchorCenter(0, 0);
        nextLevelText.setPosition(camera.getCenterX() - 80, camera.getCenterY() - 30);

        gameHUD.registerTouchArea(pauseText);
        gameHUD.setTouchAreaBindingOnActionDownEnabled(true); //fire event when finger touches the screen
        gameHUD.setTouchAreaBindingOnActionMoveEnabled(true);
        gameHUD.attachChild(scoreText);
        gameHUD.attachChild(levelText);
        gameHUD.attachChild(pauseText);

        camera.setHUD(gameHUD);
    }

    private void createBackground() {
        //		setBackground(new Background(Color.BLUE));
        ParallaxBackground background = new ParallaxBackground(0, 0, 0);
        background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(400, 240, resourcesManager.game_bg_region, vbom)));
        GameScene.this.setBackground(background);
    }

    private void addToScore(int i) {
        score += i;
        scoreText.setText("Score: " + score);
    }

    private void createPhysics() {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
        physicsWorld.setContactListener(contactListener());
        registerUpdateHandler(physicsWorld);
    }

    // ---------------------------------------------
    // INTERNAL CLASSES
    // ---------------------------------------------

    private ContactListener contactListener() {
        ContactListener contactListener = new ContactListener() {
            public void beginContact(Contact contact) {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) {
                    if (x2.getBody().getUserData().equals("player")) {
                        player.stopAnimation();
                        player.increaseFootContacts();
                    }

                    if (x1.getBody().getUserData().equals("platform1") && x2.getBody().getUserData().equals("player")) {
                        if (player != null) {
                            player.setRunning();
                        }
                    } else if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("player")) {
                        engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {
                            public void onTimePassed(final TimerHandler pTimerHandler) {
                                if (player != null) {
                                    player.setRunning();
                                }
                                pTimerHandler.reset();
                                engine.unregisterUpdateHandler(pTimerHandler);
                                x1.getBody().setType(BodyType.DynamicBody);
                            }
                        }));
                    } else if (x1.getBody().getUserData().equals("platform3") && x2.getBody().getUserData().equals("player")) {
                        if (player != null) {
                            player.setRunning();
                        }
                        x1.getBody().setType(BodyType.DynamicBody);
                    }
                }
            }

            public void endContact(Contact contact) {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) {
                    if (x2.getBody().getUserData().equals("player")) {
                        player.stopAnimation();
                        player.decreaseFootContacts();
                    }
                }
            }

            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
        return contactListener;
    }

    private void compareScores() {
        int firstHighScore = Utils.getPreferenceInt(activity, PreferenceKeys.FirstHighScore, 0);
        int secondHighScore = Utils.getPreferenceInt(activity, PreferenceKeys.SecondHighScore, 0);
        int thirdHighScore = Utils.getPreferenceInt(activity, PreferenceKeys.ThirdHighScore, 0);

        if (score > firstHighScore) {
            Utils.addPreferenceInt(activity, PreferenceKeys.SecondHighScore, firstHighScore);
            Utils.addPreferenceInt(activity, PreferenceKeys.ThirdHighScore, secondHighScore);
            Utils.addPreferenceInt(activity, PreferenceKeys.FirstHighScore, score);
        }
        if (score > secondHighScore && score < firstHighScore) {
            Utils.addPreferenceInt(activity, PreferenceKeys.ThirdHighScore, secondHighScore);
            Utils.addPreferenceInt(activity, PreferenceKeys.SecondHighScore, score);
        }

        if (score > thirdHighScore && score < secondHighScore) {
            Utils.addPreferenceInt(activity, PreferenceKeys.ThirdHighScore, score);
        }
    }
}