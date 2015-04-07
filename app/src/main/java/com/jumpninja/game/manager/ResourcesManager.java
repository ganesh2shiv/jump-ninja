package com.jumpninja.game.manager;

import android.graphics.Color;

import com.jumpninja.game.GameActivity;
import com.jumpninja.game.utils.PreferenceKeys;
import com.jumpninja.game.utils.Utils;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import java.io.IOException;

public class ResourcesManager
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;

	public Font font;

	//---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	//---------------------------------------------

	public ITextureRegion splash_region;
	public ITextureRegion menu_bg_region;
	public ITextureRegion sub_menu_bg_region;
	public ITextureRegion options_bg_region;

	public ITextureRegion game_bg_region;

	public ITextureRegion play_region;
	public ITextureRegion options_region;

	public ITextureRegion high_score_region;
	public ITextureRegion music_region;

	private Sound jumpSound;
	private Sound coinSound;
	private Music backgroundMusic;

	// Game Texture Regions
	public ITextureRegion platform1_region;
	public ITextureRegion platform2_region;
	public ITextureRegion platform3_region;
	public ITextureRegion coin_region;
	public ITiledTextureRegion player_region;

	private BitmapTextureAtlas splashTextureAtlas;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	private BuildableBitmapTextureAtlas OptionsTextureAtlas;

	// Level Complete Window
	public ITextureRegion complete_window_region;
	public ITextureRegion options_window_region;

	public ITiledTextureRegion complete_stars_region;

	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	public void loadMenuResources()
	{
		loadMenuGraphics();
		loadMenuFonts();
		loadMenuAudio();
	}

	public void loadGameResources()
	{
		loadGameGraphics();
		loadGameAudio();
	}

	public void loadOptionsResources()
	{
		loadOptionsGraphics();
	}

	private void loadMenuGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
		menu_bg_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "main_menu_bg.png");
		sub_menu_bg_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "sub_menu_bg.png");

		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
		options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");

		high_score_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "high_score.png");
		music_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "music.png");

		try 
		{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	private void loadMenuAudio()
	{
		MusicFactory.setAssetBasePath("mfx/");

		try {
			backgroundMusic = MusicFactory.createMusicFromAsset(engine
					.getMusicManager(), activity, "background_music.wav");
			backgroundMusic.setLooping(true);
			backgroundMusic.setVolume(0.2f);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pauseMenuAudio()
	{
		if(backgroundMusic != null && backgroundMusic.isPlaying() && Utils.getPreferenceBoolean(activity, PreferenceKeys.MenuMusicOn, true))
		{
			backgroundMusic.pause();
		}
	}

	public void playMenuAudio()
	{
		if(backgroundMusic != null && !backgroundMusic.isPlaying() && Utils.getPreferenceBoolean(activity, PreferenceKeys.MenuMusicOn, true))
		{
			backgroundMusic.play();
		}
	}

	private void loadMenuFonts()
	{
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	private void loadGameGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		BuildableBitmapTextureAtlas gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
		platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
		platform3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
		coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player_ninja.png", 3, 6);

		complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "level_completed_bg.png");
		complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);

		game_bg_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game_bg.png");

		try 
		{
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			gameTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	private void loadOptionsGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		OptionsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		options_bg_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(OptionsTextureAtlas, activity, "sub_menu_bg.png");

		options_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(OptionsTextureAtlas, activity, "optionsWindow.png");

		try 
		{
			this.OptionsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.OptionsTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}

	}

	private void loadGameAudio()
	{
		SoundFactory.setAssetBasePath("mfx/");
		try {
			jumpSound = SoundFactory.createSoundFromAsset(engine
					.getSoundManager(), activity, "breathe_jump.mp3");
			jumpSound.setVolume(0.7f);
			coinSound = SoundFactory.createSoundFromAsset(engine
					.getSoundManager(), activity, "coin_gain.aac");
			coinSound.setVolume(0.2f);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pauseShootAudio()
	{
		if(jumpSound.isLoaded() && Utils.getPreferenceBoolean(activity, PreferenceKeys.GameSoundOn, true))
		{
			jumpSound.pause();
		}
	}

	public void playShootAudio()
	{
		if(Utils.getPreferenceBoolean(activity, PreferenceKeys.GameSoundOn, true))
		{
			jumpSound.play();
		}
	}

	public void pauseCoinAudio()
	{
		if(coinSound.isLoaded() && Utils.getPreferenceBoolean(activity, PreferenceKeys.GameSoundOn, true))
		{
			coinSound.pause();
		}
	}

	public void playCoinAudio()
	{
		if(Utils.getPreferenceBoolean(activity, PreferenceKeys.GameSoundOn, true))
		{
			coinSound.play();
		}
	}

	public void loadSplashScreen()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();	
	}

	public void unloadSplashScreen()
	{
		splashTextureAtlas.unload();
		splash_region = null;
	}

	public void unloadMenuTextures()
	{
		menuTextureAtlas.unload();
	}

	public void unloadOptionTextures()
	{
		OptionsTextureAtlas.unload();
	}

	public void loadMenuTextures()
	{
		menuTextureAtlas.load();
	}


	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 *
	 * We use this method at beginning of game loading, to prepare Resources Manager properly,
	 * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
	 */

	public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------

	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}
}