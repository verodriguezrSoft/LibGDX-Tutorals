/*
* Clase para la gestión de mapas
* */

package com.vikgames.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.vikgames.GameConstans;
import com.vikgames.gameobjects.Enemy;
import com.vikgames.gameobjects.Zombie;

import java.util.Iterator;

import static com.badlogic.gdx.maps.tiled.TiledMapTileLayer.*;


public class MapUtils {

    public static TiledMap map;

    public static void initialize(TiledMap map){
        MapUtils.map = map;
    }

    private static Pool<Rectangle> rectanglePool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    private static Array<Rectangle> tiles = new Array<Rectangle>();

    public static Array<Rectangle> getTiles (int startX, int startY, int endX, int endY, String layerName) {

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);

        rectanglePool.freeAll(tiles);

        tiles.clear();

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {

                Cell cell = layer.getCell(x, y);

                if (cell != null) {

                    Rectangle rect = rectanglePool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
        return tiles;
    } //fin del metodo


    public static Array<Rectangle> getHorizNeighbourTiles(Vector2 velocity, Sprite sprite, String layerName){
        int startX, startY, endX, endY;

        if (velocity.x > 0) {
            startX = endX = (int)(sprite.getX() + sprite.getWidth() + velocity.x);
        }

        else {
            startX = endX = (int)(sprite.getX() + velocity.x);
        }
        startY = (int)(sprite.getY());
        endY = (int)(sprite.getY() + sprite.getHeight());


        return getTiles(startX, startY, endX, endY,layerName);
    }//fin del metodo


    public static  Array<Rectangle> getVertNeighbourTiles(Vector2 velocity,Sprite sprite,String layerName){
        int startX, startY, endX, endY;

        if (velocity.y > 0) {
            startY = endY = (int)(sprite.getY() + sprite.getHeight() );
        }
        else {
            startY = endY = (int)(sprite.getY() + velocity.y);
        }
        startX = (int)(sprite.getX());
        endX = (int)(sprite.getX() + sprite.getWidth());

        return getTiles(startX, startY, endX, endY,layerName);
    }//fin del metodo*/

    public static void spawnEnemies(Array<Enemy> enemies, float width, float height, TextureAtlas texturePack){
        Iterator<MapObject> mapObjectIterator = map.getLayers().get("Enemies").getObjects().iterator();
        float unitScale= GameConstans.unitScale;    while(mapObjectIterator.hasNext()){
             MapObject mapObject = mapObjectIterator.next();
             if(mapObject.getName().equals("zombie")){
            Rectangle rectangle = ((RectangleMapObject)
                    mapObject).getRectangle();
                     Zombie zombie = new Zombie(width, height, texturePack.
                             findRegion(GameConstans.zombie_image),rectangle.x*
                             unitScale,rectangle.y*unitScale);
                     enemies.add(zombie);
             }
        }
    }


        }//Fin de la clase
