/*
* Pantalla principal donde se carga lo que necesita el juego
* */

package com.vikgames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vikgames.gamemanager.GameManager;
import com.vikgames.gamemanager.InputManager;



public class GameScreen implements Screen {

    MainGame game;
    SpriteBatch batch; //SpriteBatch para dibujar
    public  static  OrthographicCamera camera, hudCamera;



    //Método constructor
    public GameScreen(MainGame mainGame) {
        this.game = game;

        //Obtenemos el tamaño de la ventana
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();

        //Configuramos la ventana para las dimensiones de la ventana
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false); //centrar la camara falso

        //Para seguir a nuestro personaje
        hudCamera = new OrthographicCamera(width, height);
        hudCamera.setToOrtho(false);

        //Va mostrar todos nuestros recursos en pantalla
        batch = new SpriteBatch();

        //Inicializar el juego;
        GameManager.initialize(width, height);

        Gdx.input.setInputProcessor(new InputManager(hudCamera));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //Para limpiar targeta grafica
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Se actualiza la camara
        batch.setProjectionMatrix(camera.combined);

        batch.setProjectionMatrix(hudCamera.combined);

        //Iniciar el fondo
        batch.begin();
        GameManager.renderBackground(batch);
        batch.end();

        //Para actualizar nuestro actores
        GameManager.renderer.render();

        //renderiza los gameObjects
        batch.begin();
        GameManager.renderGame(batch);
        batch.end();


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        GameManager.dispose();

    }

}
