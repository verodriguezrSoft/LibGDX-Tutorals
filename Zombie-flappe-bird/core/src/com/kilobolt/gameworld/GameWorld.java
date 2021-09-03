package com.kilobolt.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.kilobolt.gameobjects.Bird;


public class GameWorld {
    private Rectangle rectangle = new Rectangle();
    private Bird bird;

    public GameWorld() {
    }

    public void update(float delta){
        Gdx.app.log("GameWorld", "update");
        rectangle.x++;
        if (rectangle.x > 137){
            rectangle.x = 0;
        }
    }

    public Rectangle getRect(){
        return rectangle;
    }
}
