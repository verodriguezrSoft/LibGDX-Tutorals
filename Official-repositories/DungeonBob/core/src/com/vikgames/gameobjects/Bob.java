package com.vikgames.gameobjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.vikgames.GameConstans;
import com.vikgames.gamemanager.GameManager;
import com.vikgames.utils.MapUtils;

public class Bob {

    //Variables para mover a Bob continuamente
    boolean isLeftPressed; //Indica si la tecla izquierda es presionada
    boolean isRightPressed; //Indica si la tecla derecha es presionada
    private static final float maxVelocity = 0.1f; //Las unidades que Bob se movera en el eje x
    public Sprite bobSprite;  //Sprite para mostrar a Bob

    Animation walkAnimation; //Instancia de animacion
    static TextureRegion walkSheet;       //Sprite sheet
    TextureRegion currentFrame; //actual animacion frame
    float stateTime;            //tiempo transcurrido

    //private static int ANIMATION_FRAME_SIZE = 8; //Especifica la cantidad de imagenes
    private static float ANIMATION_TIME_PERIODO = 0.08f; //Especifica el tiempo entre dos frames consecutivos de la animación
    public static final float BOB_RESIZE_FACTOR = 700f;

    //Control de animacion
    boolean updateAnimationStateTime = false;

    enum Direction{LEFT, RIGHT};
    Direction direction = Direction.RIGHT;

    boolean isLeftPaddleTouched;
    boolean isRightPaddleTouched;

    private static final Vector2 gravity = new Vector2(0, -0.02f);

    Vector2 velocity; //Velocidad para nuestros actor
    private static final float damping = 0.03f;

    Rectangle bobRectagle;
    public static final float jumpVelocity = 0.35f;
    boolean isGrounded = false;

    public void render(SpriteBatch batch){
        bobSprite.setRegion(currentFrame); // set the bob sprite's texture to the current frame

        //Para que no camine de espaldas
        if(direction == Direction.LEFT){
            bobSprite.setFlip(true, false);
        }
        else{
            bobSprite.setFlip(false, false);
        }

        bobSprite.draw(batch);
    }



    //Para posicionar a Bob en la pantalla
    public void setPosition(float x, float y){
        bobSprite.setPosition(x, y);
    }

    //Para mover nuestro actor
    public void move(float x, float y)
    {
        setPosition(bobSprite.getX() + x, bobSprite.getY() + y );
    }

    public void setLeftPressed(boolean isPressed) {
        if(isRightPressed && isPressed){
            isRightPressed = false;
        }

        isLeftPressed = isPressed;
    }

    public void setRightPressed(boolean isPressed){
        if(isLeftPressed && isPressed) {

            isLeftPressed = false;
        }
        isRightPressed = isPressed;
    }

    //Metodo update
    public void update() {
        checkEnemies();
        updateAnimationStateTime = false;

        if (isLeftPressed)
        {
            updateAnimationStateTime = true;
            direction = Direction.LEFT;
            //move(-maxVelocity, 0);
            velocity.x =- maxVelocity;
        }
        else if (isRightPressed)
        {
            updateAnimationStateTime = true;
            direction = Direction.RIGHT;
            //move(maxVelocity, 0);
            velocity.x = maxVelocity;
        }

        if(isLeftPaddleTouched){
            updateAnimationStateTime = true;
            direction = Direction.LEFT;
            //move(-maxVelocity,0);
            velocity.x =- maxVelocity;
        }
        else if (isRightPaddleTouched){
            updateAnimationStateTime = true;
            direction = Direction.RIGHT;
            //move(maxVelocity, 0);
            velocity.x = maxVelocity;
        }


        if (direction == Direction.RIGHT && velocity.x <= 0.02f){
            velocity.x = 0.0f;
        }
        else if (direction == Direction.LEFT && velocity.x >= -0.02f){
            velocity.x = 0.0f;
        }

        if (velocity.x < 0){
            velocity.x += damping;
        }
        else if (velocity.x > 0){
            velocity.x -= damping;
        }

        if (velocity.x != 0){
            updateAnimationStateTime = true;
        }

        velocity.add(gravity);
        checkWallHit();
        checkCollectibleHit();
        move(velocity.x, velocity.y);


        if(updateAnimationStateTime){
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
        }
    }

    public void initalize(float width, float height, TextureRegion walkSheet)
    {
        velocity = new Vector2(0,0);
        bobRectagle = new Rectangle();


        this.walkSheet = walkSheet; //Guarda el sprite sheet
        TextureRegion [][] tmp = walkSheet.split(walkSheet.getRegionWidth()/GameConstans.ANIMATION_FRAME_SIZE, walkSheet.getRegionHeight());

        //Convierte el array 2D a 1D
        TextureRegion [] walkFrames = tmp[0];

        walkAnimation = new Animation(ANIMATION_TIME_PERIODO, walkFrames);

        bobSprite = new Sprite(); //Instanciamos un  nuevo Sprites
        bobSprite.setSize((walkSheet.getRegionWidth()/GameConstans.ANIMATION_FRAME_SIZE) * (width/BOB_RESIZE_FACTOR), walkSheet.getRegionHeight() * (width/BOB_RESIZE_FACTOR));
        bobSprite.setSize(bobSprite.getWidth() * GameConstans.unitScale, bobSprite.getHeight() * GameConstans.unitScale);
        setPosition(7,5);


        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
    }

    public void setLeftPaddleTouched(boolean isTouched)
    {
        if(isRightPaddleTouched && isTouched){
            isRightPaddleTouched = false;
        }
        isLeftPaddleTouched = isTouched;
    }

    public void setRightPaddleTouched(boolean isTouched)
    {
        if(isLeftPaddleTouched && isTouched){
            isLeftPaddleTouched = false;
        }
        isRightPaddleTouched = isTouched;
    }

    public void checkWallHit(){

        bobRectagle.set(bobSprite.getX(), bobSprite.getY(), bobSprite.getWidth(), bobSprite.getHeight());

        Array<Rectangle> tiles = MapUtils.getHorizNeighbourTiles(velocity, bobSprite, "Wall");

        for (Rectangle tile: tiles){
            if (bobRectagle.overlaps(tile)){
                velocity.x = 0;
                break;
            }
        }//Fin del for


        bobRectagle.x = bobSprite.getX();
        tiles = MapUtils.getVertNeighbourTiles(velocity, bobSprite, "Wall");

        bobRectagle.y += velocity.y;
        for (Rectangle tile: tiles){
            if (bobRectagle.overlaps(tile)){
                if (velocity.y > 0){
                    bobSprite.setY(tile.y -bobSprite.getHeight());
                }
                else if (velocity.y < 0){
                    bobSprite.setY(tile.y + tile.height);
                    isGrounded = true;
                }

                velocity.y = 0;
                break;
            }
        }

    }

    public void checkCollectibleHit(){
        //Obtenemos el rectangulo de Bob
        bobRectagle.set(bobSprite.getX(), bobSprite.getY(), bobSprite.getWidth(), bobSprite.getHeight());

        //Obtenemos las monedas del mapa
        Array<Rectangle> tiles = MapUtils.getHorizNeighbourTiles(velocity, bobSprite, "Collectibles");


        TiledMapTileLayer layer = (TiledMapTileLayer) GameManager.map.getLayers().get("Collectibles");

        for (Rectangle tile: tiles){
            if (bobRectagle.overlaps(tile)){
                layer.setCell((int) tile.x, (int) tile.y, null);
                break;
            }
        }//Fin for

        bobRectagle.x = bobSprite.getX();
        tiles = MapUtils.getVertNeighbourTiles(velocity, bobSprite, "Collectibles");

        bobRectagle.y += velocity.y;
        for (Rectangle tile: tiles){
            if (bobRectagle.overlaps(tile)){
                layer.setCell((int) tile.x, (int) tile.y, null);
            }
        }
    }//Fin del metodo

    public void checkEnemies (){
        bobRectagle.set(bobSprite.getX(), bobSprite.getY(),
                bobSprite.getWidth(), bobSprite.getHeight());
        }

    public void jump(){
        velocity.y = jumpVelocity;
        isGrounded = false;
    }

}
