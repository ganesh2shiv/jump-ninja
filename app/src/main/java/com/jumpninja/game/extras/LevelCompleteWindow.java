package com.jumpninja.game.extras;

import com.jumpninja.game.manager.ResourcesManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class LevelCompleteWindow extends Sprite
{
	private TiledSprite star1;
	private TiledSprite star2;
	private TiledSprite star3;
	private TiledSprite star4;
	private TiledSprite star5;

	public enum StarsCount
	{
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE
	}

	public LevelCompleteWindow(VertexBufferObjectManager pSpriteVertexBufferObject)
	{
		super(0, 0, 650, 400, ResourcesManager.getInstance().complete_window_region, pSpriteVertexBufferObject);
		attachStars(pSpriteVertexBufferObject);
	}

	private void attachStars(VertexBufferObjectManager pSpriteVertexBufferObject)
	{
		star1 = new TiledSprite(85, 120, ResourcesManager.getInstance().complete_stars_region, pSpriteVertexBufferObject);
		star2 = new TiledSprite(205, 120, ResourcesManager.getInstance().complete_stars_region, pSpriteVertexBufferObject);
		star3 = new TiledSprite(325, 120, ResourcesManager.getInstance().complete_stars_region, pSpriteVertexBufferObject);
		star4 = new TiledSprite(445, 120, ResourcesManager.getInstance().complete_stars_region, pSpriteVertexBufferObject);
		star5 = new TiledSprite(565, 120, ResourcesManager.getInstance().complete_stars_region, pSpriteVertexBufferObject);

		attachChild(star1);
		attachChild(star2);
		attachChild(star3);
		attachChild(star4);
		attachChild(star5);
	}

	/**
	 * Change star's tile index, depends on stars count.
	 * @param starsCount
	 */
	public void display(StarsCount starsCount, Scene scene, Camera camera)
	{
		// Change stars tile index, based on stars count (1-3)
		switch (starsCount)
		{
		case ONE:
			star1.setCurrentTileIndex(0);
			star2.setCurrentTileIndex(1);
			star3.setCurrentTileIndex(1);
			star4.setCurrentTileIndex(1);
			star5.setCurrentTileIndex(1);
			break;
		case TWO:
			star1.setCurrentTileIndex(0);
			star2.setCurrentTileIndex(0);
			star3.setCurrentTileIndex(1);
			star4.setCurrentTileIndex(1);
			star5.setCurrentTileIndex(1);
			break;
		case THREE:
			star1.setCurrentTileIndex(0);
			star2.setCurrentTileIndex(0);
			star3.setCurrentTileIndex(0);
			star4.setCurrentTileIndex(1);
			star5.setCurrentTileIndex(1);
			break;
		case FOUR:
			star1.setCurrentTileIndex(0);
			star2.setCurrentTileIndex(0);
			star3.setCurrentTileIndex(0);
			star4.setCurrentTileIndex(0);
			star5.setCurrentTileIndex(1);
			break;
		case FIVE:
			star1.setCurrentTileIndex(0);
			star2.setCurrentTileIndex(0);
			star3.setCurrentTileIndex(0);
			star4.setCurrentTileIndex(0);
			star5.setCurrentTileIndex(0);
			break;
		}

		// Hide HUD
		camera.getHUD().setVisible(true);

		// Disable camera chase entity
		camera.setChaseEntity(null);

		// Attach our level complete panel in the middle of camera
		setPosition(camera.getCenterX(), camera.getCenterY());
		scene.attachChild(this);
	}
}