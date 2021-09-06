/*
* Clase de arranque
* */

package com.vikgames;

import com.badlogic.gdx.Game;


public class MainGame extends Game {

	@Override
	public void create() {
		//Accediendo a la pantalla principal
		setScreen(new GameScreen(this));
	}
}
