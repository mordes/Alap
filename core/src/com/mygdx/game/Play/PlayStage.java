package com.mygdx.game.Play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GlobalClasses.Assets;
import com.mygdx.game.MyBaseClasses.MyStage;
import com.mygdx.game.MyBaseClasses.OneSpriteAnimatedActor;
import com.mygdx.game.MyBaseClasses.OneSpriteStaticActor;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;

/**
 * Created by tuskeb on 2016. 09. 30..
 */
public class PlayStage extends MyStage {
    private TextButton textButton;

    private KolbaszTolto kolbaszTolto;
    private Husok husok;
    private float husTime = 0, gyilkosTime = 0, nonhusTime = 0;

    private float elapsedTime;
    private int speed = 1;

    private static int dbPotyogas;

    private static ArrayList<OneSpriteStaticActor> esodolgok;
    private static OneSpriteStaticActor palinkaKijelzoActor;
    public static int palinkaSzint, jelenlegiPalinkaSzint;

    public PlayStage(Viewport viewport, Batch batch, MyGdxGame game) {
        super(viewport, batch, game);
    }


    public void init() {
        addBackEventStackListener();

        esodolgok = new ArrayList<OneSpriteStaticActor>();

        //pálinka
        palinkaSzint = 15;
        palinkaKijelzoActor = new OneSpriteStaticActor(new PalinkaKijelzo().getTexture());
        addActor(palinkaKijelzoActor);
        palinkaKijelzoActor.setSize(50,200);
        palinkaKijelzoActor.setPosition(getViewport().getWorldWidth() - palinkaKijelzoActor.getWidth(),0);
        jelenlegiPalinkaSzint = palinkaSzint;
        //pálinka



        kolbaszTolto = new KolbaszTolto();
        addActor(kolbaszTolto);

        //potyogoDolgok.add(husok);

        palinkaszint();

        }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
        husTime += delta;
        nonhusTime += delta;
        gyilkosTime += delta;

        pozicionalas();
        esik();
        utkozik();

        //pálinkaszint váltása
        palinkaSzint--;
        palinkaszint();
        //pálinkaszint váltása

    }

    private void utkozik() {
        for (int i = 0; i < esodolgok.size(); i++) {
            OneSpriteStaticActor a = esodolgok.get(i);
            if(a.getY()+a.getHeight() < 0){
                a.remove();
                esodolgok.remove(i);
                i--;
            }else{
                if(kolbaszTolto.getY()+kolbaszTolto.getHeight() > a.getY() && kolbaszTolto.getY()+kolbaszTolto.getHeight() < 100+a.getY() && a.getX()+a.getWidth()/2 > kolbaszTolto.getX() && a.getX()-a.getWidth()/2 < kolbaszTolto.getX()+kolbaszTolto.getWidth()){
                    if(a instanceof Husok){
                        System.out.println("hus");
                    }else if( a instanceof NemHusok){
                        System.out.println("nem hus");
                    }else if( a instanceof DaraloGyilkos){
                        System.out.println("dgy");
                    }

                    a.remove();
                    esodolgok.remove(i);
                    i--;
                }
            }
        }
    }

    private void palinkaszint() {
        if (palinkaKijelzoActor != null && jelenlegiPalinkaSzint != palinkaSzint) {
            palinkaKijelzoActor.remove();
            palinkaKijelzoActor = new OneSpriteStaticActor(new PalinkaKijelzo().getTexture());
            addActor(palinkaKijelzoActor);
            palinkaKijelzoActor.setSize(50,200);
            palinkaKijelzoActor.setPosition(getViewport().getWorldWidth() - palinkaKijelzoActor.getWidth(),0);
            jelenlegiPalinkaSzint = palinkaSzint;
            }
        }
    private void esik(){
        if(elapsedTime > 50){
            elapsedTime = 0;
            speed++;
        }
        if(husTime > 2){
            husTime = 0;
            int ez = vel(1,5);
            Texture t = null;
            if(ez == 1) t = Assets.manager.get(Assets.HUS1);
            else if(ez == 2) t = Assets.manager.get(Assets.HUS2);
            else if(ez == 3) t = Assets.manager.get(Assets.HUS3);
            else if(ez == 4) t = Assets.manager.get(Assets.HUS4);
            else if(ez == 5) t = Assets.manager.get(Assets.HUS5);

            Husok husok = new Husok(t);
            husok.setPosition(vel(0,getViewport().getWorldWidth()-husok.getWidth()),getViewport().getWorldHeight());
            addActor(husok);
            esodolgok.add(husok);
            husok.setSpeed(speed);
        }
        if(nonhusTime > 5){
            nonhusTime = 0;
            NemHusok nemHusok = new NemHusok(vel(0,1) == 0 ? Assets.manager.get(Assets.RONT1) : Assets.manager.get(Assets.RONT2));
            nemHusok.setPosition(vel(0,getViewport().getWorldWidth()-nemHusok.getWidth()),getViewport().getWorldHeight());
            addActor(nemHusok);
            esodolgok.add(nemHusok);
            nemHusok.setSpeed(speed+1);
        }
        if(gyilkosTime > 11){
            gyilkosTime = 0;
            int ez = vel(1,3);
            Texture t = null;
            if(ez == 1) t = Assets.manager.get(Assets.GYILKOS1);
            else if(ez == 2) t = Assets.manager.get(Assets.GYILKOS2);
            else if(ez == 3) t = Assets.manager.get(Assets.GYILKOS3);

            DaraloGyilkos daraloGyilkos = new DaraloGyilkos(t);
            daraloGyilkos.setPosition(vel(0,getViewport().getWorldWidth()-daraloGyilkos.getWidth()),getViewport().getWorldHeight());
            addActor(daraloGyilkos);
            esodolgok.add(daraloGyilkos);
            daraloGyilkos.setSpeed(speed+2);
        }
    }

    private int vel(int a, int b){
        return (int)(Math.random()*(b-a+1)+a);
    }

    private float vel(float a, float b){
        return (float)(Math.random()*(b-a+1)+a);
    }

    private void pozicionalas(){
        if(kolbaszTolto.getX() >=0 && kolbaszTolto.getX()+kolbaszTolto.getWidth() <= ((ExtendViewport)getViewport()).getMinWorldWidth())
            kolbaszTolto.setPosition(kolbaszTolto.getX()-(Gdx.input.getAccelerometerX()*2), kolbaszTolto.getY());
        if(kolbaszTolto.getX() < 0)
            kolbaszTolto.setPosition(0, kolbaszTolto.getY());
        if(kolbaszTolto.getX() + kolbaszTolto.getWidth() > ((ExtendViewport)getViewport()).getMinWorldWidth())
            kolbaszTolto.setPosition(((ExtendViewport)getViewport()).getMinWorldWidth() - kolbaszTolto.getWidth(), kolbaszTolto.getY());
    }


    @Override
    public void dispose() {
        super.dispose();
    }
}
