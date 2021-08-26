package com.oop.platformer.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.oop.platformer.Constants;
import com.oop.platformer.GameClass;
import com.oop.platformer.util.Assets;

import static java.lang.Math.min;

public class Player extends GameObject {


    private final float damagePeriod;
    public boolean shooting;
    private int jumpCounter;
    private State currentState;
    private State previousState;
    private float stateTimer;
    private boolean runningRight;
    //player Lives
    private int lives;
    //player Score
    private int score;
    //player dead or not
    private boolean win;
    private boolean dead;
    private boolean isDamaged;
    private float damageTimer;
    private Array<Vector2> playerCheckpoints;
    private int currentCheckPointIndex;
    private float xRespawn, yRespawn;
    private float currentTime;
    private float deathTime;
    private float winTime;
    public Player(World world, Vector2 position, Array<Vector2> playerCheckpoints) {
        super(world, position);

        jumpCounter = 0;
        xRespawn = this.spritePosition.x;
        yRespawn = this.spritePosition.y;
        this.playerCheckpoints = playerCheckpoints;
        currentTime = 0;

        deathTime = 0;
        winTime = 0;

        lives = Constants.LIVES;
        score = Constants.SCORE;
        win = false;
        dead = false;
        shooting = false;

        currentState = State.Standing;
        previousState = State.Standing;
        stateTimer = 0;
        runningRight = true;

        damagePeriod = 0.15f;

        isDamaged = false;

        currentCheckPointIndex = -1;

        setBounds(0, 0, 32 / GameClass.PPM, 32 / GameClass.PPM);


        setRegion(Assets.instance.playerAssets.idleAnimation.getKeyFrame(stateTimer, true));
    }

    @Override
    public void define() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spritePosition);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);


        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();

        shape.setRadius(13 / GameClass.PPM);
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void update(float deltaTime) {

        currentTime += deltaTime;

        if (isDamaged)
            damageTimer += deltaTime;
        if (damageTimer >= damagePeriod) {
            damageTimer = 0;
            isDamaged = false;
        }


        if (win && winTime == 0)
            winTime = currentTime;

        checkPlayerPosition();
        this.spritePosition = body.getPosition();
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(deltaTime));

        if (currentCheckPointIndex + 1 < playerCheckpoints.size) {
            if (body.getPosition().x >= playerCheckpoints.get(currentCheckPointIndex + 1).x / GameClass.PPM) {
                xRespawn = playerCheckpoints.get(currentCheckPointIndex + 1).x / GameClass.PPM;
                yRespawn = playerCheckpoints.get(currentCheckPointIndex + 1).y / GameClass.PPM;
                currentCheckPointIndex++;
            }
        }
    }

    private TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;

        switch (currentState) {
            case Jumping:
                region = Assets.instance.playerAssets.jumpingAnimation;
                break;
            case Running:
                region = Assets.instance.playerAssets.runAnimation.getKeyFrame(stateTimer, true);
                break;
            case Shooting:
            case Jumping_Shooting:
                region = Assets.instance.playerAssets.shootAnimation.getKeyFrame(stateTimer, true);
                break;
            case Falling:
                region = Assets.instance.playerAssets.fallingAnimation.getKeyFrame(stateTimer, true);
                break;
            case Dead:
                region = Assets.instance.playerAssets.deathAnimation.getKeyFrame(stateTimer, false);
                break;
            case Standing:
            case Win:
            default:
                region = Assets.instance.playerAssets.idleAnimation.getKeyFrame(stateTimer, true);
                break;
        }

        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        if (currentState == previousState)
            stateTimer += deltaTime;
        else
            stateTimer = 0;

        previousState = currentState;

        return region;
    }

    private State getState() {
        if (dead)
            return State.Dead;
        else if (win)
            return State.Win;
        if (body.getLinearVelocity().y < 0 || (body.getLinearVelocity().y < 0 && previousState == State.Jumping))
            return State.Falling;
        else if (body.getLinearVelocity().y > 0 && Gdx.input.isKeyPressed(Input.Keys.F))
            return State.Jumping_Shooting;
        else if (body.getLinearVelocity().x != 0)
            return State.Running;
        else if (body.getLinearVelocity().y > 0)
            return State.Jumping;
        else if (shooting)
            return State.Shooting;
        else
            return State.Standing;
    }

    public void handleInput() {

        float verticalSpeed = body.getLinearVelocity().y;

        //ignores player input while the player is taking damage
        if (!isDamaged) {
            if (verticalSpeed == 0) {
                jumpCounter = 0;
            }
            if (jumpCounter == 0 && verticalSpeed < 0) {
                jumpCounter = 2;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && jumpCounter != 2) {
                body.setLinearVelocity(body.getLinearVelocity().x, 2.5f);
                jumpCounter++;
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                body.setLinearVelocity(-1.8f, body.getLinearVelocity().y);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                body.setLinearVelocity(1.8f, body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
            }
        }
    }

    public void hitPlayer() {

        if (lives == 0) {
            //player is dead
            dead = true;
            if (deathTime == 0)
                deathTime = currentTime;
            body.setLinearVelocity(0, 0);
        } else {
            //player is hit
            Assets.instance.audio.playerHit.play();
            bouncePlayer();
            lives--;
        }
    }

    private void bouncePlayer() {
        isDamaged = true;
        if (body.getLinearVelocity().x >= 0) {
            if (body.getLinearVelocity().y >= 0)
                body.setLinearVelocity((body.getLinearVelocity().x + 2) / 2 * -1, (body.getLinearVelocity().y + 2f));
            else
                body.setLinearVelocity((body.getLinearVelocity().x + 2) / 2 * -1, min((body.getLinearVelocity().y * -1), 4));
        } else {
            if (body.getLinearVelocity().y >= 0)
                body.setLinearVelocity((body.getLinearVelocity().x - 2) / 2 * -1, (body.getLinearVelocity().y + 2f));
            else
                body.setLinearVelocity((body.getLinearVelocity().x - 2) / 2 * -1, min((body.getLinearVelocity().y * -1), 4));
        }
    }

    private void checkPlayerPosition() {
        if (!dead && this.spritePosition.y < -5f) {
            hitPlayer();
            if (!dead)
                respawnPlayer();
        }
    }

    //Returns lives remaining for the player
    public int getLives() {
        return lives;
    }

    //returns player Current score
    public int getScore() {
        return score;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isWin() {
        return win;
    }

    public void increaseScore() {
        score += 100;
    }

    private void respawnPlayer() {
        body.setTransform(new Vector2(xRespawn, yRespawn), 0);
        if (!isRunningRight())
            runningRight = true;
    }

    public boolean isRunningRight() {
        return runningRight;
    }

    public boolean wonLevel() {
        return win && currentTime - winTime >= 5;
    }

    public void setWin() {
        win = true;
        winTime = currentTime;
    }

    public boolean lostLevel() {
        return dead && currentTime - deathTime >= 5;
    }

    public enum State {Falling, Jumping, Standing, Running, Shooting, Jumping_Shooting, Dead, Win}

}