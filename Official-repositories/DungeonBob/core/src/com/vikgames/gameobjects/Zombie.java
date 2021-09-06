package com.vikgames.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vikgames.GameConstans;

public class Zombie extends Enemy {

    Enemy enemy;
    Sprite sprite_zombie;
    public static final Float RESIZE_FACTOR= 900F;



    @Override
    public void render(SpriteBatch batch) {
        sprite_zombie.draw(batch);
    }

    @Override
    public void update() {
        rectangle.set(sprite.getX(), sprite.getY(),
                sprite.getWidth(), sprite.getHeight());

    }

    public Zombie (float width,float height,TextureRegion
    zombieTexture, Float x, Float y){
        sprite = new Sprite(zombieTexture);
        sprite.setSize(sprite.getWidth()*(width/RESIZE_FACTOR),
                sprite.getHeight()*(width/RESIZE_FACTOR));
        sprite.setSize(sprite.getWidth()* GameConstans.
                unitScale,sprite.getHeight()*GameConstans.unitScale);
        sprite.setPosition(x,y);
        velocidadEnemigo = new Vector2 (0,0);
        rectangle= new Rectangle();

}}
