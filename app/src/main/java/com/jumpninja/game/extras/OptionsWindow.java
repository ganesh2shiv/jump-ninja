package com.jumpninja.game.extras;

import com.jumpninja.game.manager.ResourcesManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class OptionsWindow extends Sprite{

	public OptionsWindow(VertexBufferObjectManager pSpriteVertexBufferObject)
	{
		super(0, 0, 650, 400, ResourcesManager.getInstance().options_window_region, pSpriteVertexBufferObject);
	}
	
	public void display(Scene scene, Camera camera)
	{
		// Disable camera chase entity
		camera.setChaseEntity(null);

		// Attach our level complete panel in the middle of camera
		setPosition(camera.getCenterX(), camera.getCenterY());
		scene.attachChild(this);
	}

}
