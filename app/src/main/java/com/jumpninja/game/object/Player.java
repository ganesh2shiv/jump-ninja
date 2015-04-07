package com.jumpninja.game.object;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jumpninja.game.manager.ResourcesManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class Player extends AnimatedSprite {
    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    private Body body;

    private boolean canRun = false;

    private int footContacts = 0;

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld) {
        super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
        createPhysics(camera, physicsWorld);
        camera.setChaseEntity(this);
    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

        body.setUserData("player");
        body.setFixedRotation(true);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);

                if (getY() <= 0) {
                    onDie();
                }

                if (canRun) {
                    body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));
                }
            }
        });
    }

    public void setRunning() {
        canRun = true;

        final long[] PLAYER_ANIMATE = new long[]{50, 50, 50, 50, 50, 50, 50, 50, 50};

        animate(PLAYER_ANIMATE, 0, 8, true);
    }

    public void jump() {

        final long[] PLAYER_ANIMATE = new long[]{50, 50, 50, 50, 50, 50, 50, 50, 50};

        animate(PLAYER_ANIMATE, 9, 17, false);

        if (footContacts < 1) {
            return;
        }
        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
    }

    public void increaseFootContacts() {
        footContacts++;
    }

    public void decreaseFootContacts() {
        footContacts--;
    }

    public void stopRunning() {
        canRun = false;
        body.setLinearVelocity(0, 0);
    }

    public abstract void onDie();
}