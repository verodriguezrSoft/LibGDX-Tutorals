package com.vikgames.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy {
    Sprite sprite;
    Vector2 velocidadEnemigo;
    Rectangle rectangle;
    public abstract void render(SpriteBatch batch);
    public abstract void update ();



}
