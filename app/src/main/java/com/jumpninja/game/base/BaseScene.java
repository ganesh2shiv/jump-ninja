package com.jumpninja.game.base;

import android.app.Activity;

import com.jumpninja.game.manager.ResourcesManager;
import com.jumpninja.game.manager.SceneManager.SceneType;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class BaseScene extends Scene
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	protected final Engine engine;
	protected final Activity activity;
	protected final ResourcesManager resourcesManager;
	protected final VertexBufferObjectManager vbom;
	protected final BoundCamera camera;

	//---------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------

	protected BaseScene()
	{
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
		this.activity = resourcesManager.activity;
		this.vbom = resourcesManager.vbom;
		this.camera = resourcesManager.camera;
		createScene();
	}

	//---------------------------------------------
	// ABSTRACTION
	//---------------------------------------------

	protected abstract void createScene();

	public abstract void onBackKeyPressed();

	public abstract SceneType getSceneType();

	public abstract void disposeScene();
}