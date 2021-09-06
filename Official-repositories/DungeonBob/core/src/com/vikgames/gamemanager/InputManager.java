/*
* Clase para gestionar las entradas al sistema
* */
package com.vikgames.gamemanager;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class InputManager extends InputAdapter {

    //Mueve a nuestro actor tres unidades dependiendo del boton que presiones
    OrthographicCamera camera;
    static Vector3 temp = new Vector3();

    public InputManager(OrthographicCamera camera){
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {

        //Si preionas la tecla izquierda del teclado
        if(keycode == Input.Keys.LEFT)
        {
            GameManager.bob.setLeftPressed(true);
        }
        else if(keycode == Input.Keys.RIGHT) //Si presionas la derecha
        {
            GameManager.bob.setRightPressed(true);
        }
        else if(keycode == Input.Keys.SPACE){//Si presionas saltar
            GameManager.bob.jump();
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if(keycode == Input.Keys.LEFT)
        {
            GameManager.bob.setLeftPressed(false);
        }
        else if(keycode == Input.Keys.RIGHT)
        {
            GameManager.bob.setRightPressed(false);
        }

        return false;
    }

    boolean isLeftPaddleTouched(float touchX, float touchY){
        if((touchX >= GameManager.leftPaddleSprite.getX()) && touchX
                <= (GameManager.leftPaddleSprite.getX() + GameManager.
                leftPaddleSprite.getWidth()) && (touchY >= GameManager.
                leftPaddleSprite.getY()) && touchY <= (GameManager.
                leftPaddleSprite.getY() + GameManager.leftPaddleSprite.
                getHeight())){
            return true;
        }
        return false;
    }

    boolean isRightPaddleTouched(float touchX, float touchY){
        if((touchX >= GameManager.rightPaddleSprite.getX()) &&
                touchX <= (GameManager.rightPaddleSprite.getX()
                        +GameManager.rightPaddleSprite.getWidth()) && (touchY
                >=GameManager.rightPaddleSprite.getY()) && touchY<=
                (GameManager.rightPaddleSprite.getY()+GameManager.
                        rightPaddleSprite.getHeight()) ){
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        temp.set(screenX, screenY, 0);
        camera.unproject(temp);

        float touchX = temp.x;
        float touchY = temp.y;

        if (isLeftPaddleTouched(touchX, touchY)){
            GameManager.bob.setLeftPaddleTouched(true);
        }
        else if (isRightPaddleTouched(touchX, touchY)){
            GameManager.bob.setRightPaddleTouched(true);
        }

        return  false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(temp);

        float touchX = temp.x;
        float touchY = temp.y;

        if (isLeftPaddleTouched(touchX, touchY)){
            GameManager.bob.setLeftPaddleTouched(false);
        }
        else if (isRightPaddleTouched(touchX, touchY)){
            GameManager.bob.setRightPaddleTouched(false);
        }

        return  false;
    }
}
