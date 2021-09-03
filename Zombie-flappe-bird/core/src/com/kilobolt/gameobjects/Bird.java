package com.kilobolt.gameobjects;

import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

public class Bird {
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acceleration;

    private float rotation;
    private int width;
    private int height;

    public Bird(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 460);
    }

    public void update(float delta){
        velocity.add(acceleration.cpy().scl(delta));
        if (velocity.y > 200){
            velocity.y = 200;
        }
        position.add(velocity.cpy().scl(delta));
    }

    public void onClick(){
        velocity.y = -140;
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }

    public float getRotation() {
        return rotation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
