package com.vikgames.gamemanager;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.vikgames.GameConstans;
import com.vikgames.GameScreen;
import com.vikgames.gameobjects.Bob;
import com.vikgames.gameobjects.Enemy;
import com.vikgames.gameobjects.Zombie;
import com.vikgames.utils.MapUtils;



public class GameManager {

    //Todo lo relacioando con Bob que es nuestro personaje principal
    static Bob bob; //Instancian de bob
    public static Zombie zombie;
    static TextureRegion bobSpriteSheet; //Textura para la imagen de bob
    public static Sprite backgroundSprite; // fondo spprite
    public static Texture backgroundTexture; // Teture image for the background

    //Para gestionar las texturas del botones para Android
    static TextureRegion leftPaddleTexture;
    static TextureRegion rightPaddleTextura;
    public static final float PADDLE_REZISE_FACTOR = 700f; //Factor de conversion dimensionado
    public static final float PADDLE_ALPHA = 0.25f; //Opacidad de la textura
    public static final float PADDLE_HORIZ_POSITION_FACTOR = 0.02f;
    public static final float PADDLE_VERT_POSITION_FACTOR =0.01f;

    //Para gestionar los contoles para Ordenador
    static Sprite leftPaddleSprite;
    static Sprite rightPaddleSprite;

    //Para gestionar las texturas de una manera más eficiente
    static AssetManager assetManager;
    static TextureAtlas texturePack;

    //Para cargar nuestro mapa
    public static TiledMap map;
    public static OrthogonalTiledMapRenderer renderer;

    //ancho y largo del mapa
    public static int mapWidth;
    public static int mapHeight;

    //Instanciar clase Zombie
    public static Array<Enemy> enemies;

    //Metodo que inicia todo
    public static void initialize(float width, float height){
        //Instanciando el objeto de tipo AssetManager
        assetManager = new AssetManager();
        loadAssset(); //Para cargar lo assets o recursos de neceserias para nuestro proyecto, como Texturas, efectos de sonidos, musica

        //Cargamos el mapa
        map = assetManager.get(GameConstans.level1);
        setMapDimensions();

        //Instanciamos nuestra camara
        renderer = new OrthogonalTiledMapRenderer(map, GameConstans.unitScale);
        GameScreen.camera.setToOrtho(false, 15 , 13);
        GameScreen.camera.update();
        renderer.setView(GameScreen.camera);

        //Cargamos nuestro texture packer
        texturePack = assetManager.get(GameConstans.texturePack);


        //Instancia de bob
        bob = new Bob();
        bobSpriteSheet = texturePack.findRegion(GameConstans.bobSpriteSheet); //Cargado la Textura de bob
        bob.initalize(width, height, bobSpriteSheet); //Inicializamos sus variables

        enemies = new Array<Enemy>();
        MapUtils.spawnEnemies(enemies,width,height,texturePack);

        //Cargando la Textura del fondo
        backgroundTexture = assetManager.get(GameConstans.backGroundImage);
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(width, height);

        //Iniciamos los botones que se mestra en pantalla para celualr
        initializeLeftPaddle(width, height);
        initalizeRidghtPaddle(width, height);

        //Inicializamos el mapa
        MapUtils.initialize(map);
    }

    //Para dibujar nuestro actor bob en pantalla
    public static void renderBackground(SpriteBatch batch)
    {
        backgroundSprite.draw(batch);
    }


    //Metodo que actulizara 60 veces por segundo (frames por segundo)
    public static void renderGame(SpriteBatch batch){

        batch.setProjectionMatrix(GameScreen.camera.combined);

        //dibujamos el fondo en pantalla
        //backgroundSprite.draw(batch);
        bob.update();
        //dibujamos a bob
        bob.render(batch);
        zombie.update();
        zombie.render(batch);
        GameScreen.camera.position.x = bob.bobSprite.getX();

        //Para que la camara siga a nustro actor
        if(!(GameScreen.camera.position.x - GameScreen.camera.viewportWidth/2 > 0))
        {
            GameScreen.camera.position.x = GameScreen.camera.viewportWidth/2;
        }
        else if (((GameScreen.camera.position.x + GameScreen.camera.viewportWidth/2) >= mapWidth))
        {
            GameScreen.camera.position.x = mapWidth - GameScreen.camera.viewportWidth/2;
        }
        renderer.setView(GameScreen.camera);
        GameScreen.camera.update();

        batch.setProjectionMatrix(GameScreen.hudCamera.combined);

        leftPaddleSprite.draw(batch);
        rightPaddleSprite.draw(batch);
    }

    //Para vaciar nuestra tarjeta grafica
    public static void dispose() {
        //disposeamos el background
        assetManager.unload(GameConstans.backGroundImage);
        assetManager.clear();

        //leftPaddleTexture.dispose();
        //rightPaddleTextura.dispose();
    }

    //Para incializar los botones en pantalla en este caso el izquierdo
    public static void initializeLeftPaddle(float width, float height){
        //Cargamos la textura Left
        leftPaddleTexture = texturePack.findRegion(GameConstans.leftPaddleImage);
        leftPaddleSprite = new Sprite(leftPaddleTexture);
        leftPaddleSprite.setSize(leftPaddleSprite.getWidth() * (width/PADDLE_REZISE_FACTOR), leftPaddleSprite.getHeight() * width/PADDLE_REZISE_FACTOR);
        leftPaddleSprite.setPosition(width * PADDLE_HORIZ_POSITION_FACTOR, height * PADDLE_VERT_POSITION_FACTOR);
        leftPaddleSprite.setAlpha(PADDLE_ALPHA);
    }

    //Para incializar los botones en pantalla en este caso el izquierdo
    public static void initalizeRidghtPaddle(float width, float height){
        rightPaddleTextura = texturePack.findRegion(GameConstans.rightPaddleImage);
        rightPaddleSprite = new Sprite(rightPaddleTextura);
        rightPaddleSprite.setSize(rightPaddleSprite.getWidth() * width/PADDLE_REZISE_FACTOR,rightPaddleSprite.getHeight() * width/PADDLE_REZISE_FACTOR);
        rightPaddleSprite.setPosition(leftPaddleSprite.getX() + leftPaddleSprite.getWidth() + width * PADDLE_HORIZ_POSITION_FACTOR, height * PADDLE_VERT_POSITION_FACTOR);
        rightPaddleSprite.setAlpha(PADDLE_ALPHA);
    }

    //Cargando nuestros assets
    public static void loadAssset(){

        assetManager.load(GameConstans.backGroundImage, Texture.class);
        assetManager.load(GameConstans.texturePack, TextureAtlas.class);
        assetManager.setLoader(TiledMap.class, new TmxMapLoader( new InternalFileHandleResolver()));
        assetManager.load(GameConstans.level1, TiledMap.class);
        assetManager.finishLoading();
    }

    // Para dimensionar nuestro mapa
    static void setMapDimensions(){
        MapProperties properties = map.getProperties();
        mapHeight = Integer.parseInt(properties.get("height").toString());
        mapWidth = Integer.parseInt(properties.get("width").toString());
    }
}
