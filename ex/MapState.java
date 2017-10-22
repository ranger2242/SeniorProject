package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.attacks.Flame;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.State;
import com.quadx.dungeons.tools.ShapeRendererExt;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.controllers.Controllers;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.shapes.Circle;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.states.mapstate.MapStateRender.renderLayers;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.dtScrollAtt;


/**
 * Created by Brent on 6/26/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapState extends State implements ControllerListener {
    static final boolean debug=true;
    public static boolean noLerp=false;
    public static boolean pause=false;

    static ShapeRendererExt shapeR = new ShapeRendererExt();
    static final ArrayList<Cell> hitList=new ArrayList<>();
    public static boolean showStats=true;

    public static Texture statPopup;
    public static GridManager gm;
    static ParticleEffect effect;
    public static final Random rn = new Random();
    public static boolean inGame=false;
    static boolean effectLoaded = false;
    static final String DIVIDER= "_________________________";
    public static int cellW=30;
    public static Vector2 cell = new Vector2(cellW,cellW*(2f/3f));
    public static Vector2 warp=new Vector2();
    public static Vector2 shop=new Vector2();
    public static float dtStatPopup=0;
    public static float viewX;
    public static float viewY;
    static int altNumPressed=1;
    public static Circle circle = new Circle(new Vector2(cell.x*GridManager.res/2,cell.y*GridManager.res/2),200);


    public MapState(GameStateManager gsm) {
        super(gsm);
        new HUD();
        StatManager.reset();
        Controllers.initControllers(this);
        player.initPlayer();
        gm = new GridManager();
        cam.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        //debug();
    }
    public void debug() {
        Tests.timeKill();
        player.attackList.clear();
        player.attackList.add(new Flame());
        //Tests.goldTest();
        //Tests.testEquipmentRates();
        //Tests.giveItems(50);
        //Tests.testsMonsterStats();
    }
    public void handleInput() {
    }
    public void update(float dt) {
        MapStateUpdater.buttonHandler();
        MapStateUpdater.updateVariables(dt);
        GridManager.loadDrawList();
        Inventory.compareItemToEquipment();
        player.update(dt);
        if(MapStateUpdater.dtCollision>Game.frame/2) {
            if(!player.jumping)
            MapStateUpdater.collisionHandler();
        }
        //MapStateUpdater.moveMonsters();
        player.checkIfDead(gsm);
        HUD.create();
    }
    public void render(SpriteBatch sb) {

        renderLayers(sb);
    }
    public void dispose() {
    }

    public static void openCrate(Item item) {
        if (item.isGold()) {
            Gold g = (Gold) item;
            player.addGold(g);
        } else {
            if (item != null) {
                if (item.isEquip) {
                    item.loadIcon(item.getType());
                } else if (item.getIcon() == null)
                    item.loadIcon(item.getName());

                if (!item.isGold()) {
                    boolean a = false;
                    if (player.isInvEmpty()) {
                        a = true;
                    }
                    player.addItemToInventory(item);
                    if (a) Inventory.pos = 0;
                } else {
                    player.lastItem = new Gold();
                }
            }
        }
        HUD.setLootPopup(item.getIcon());
        StatManager.totalItems++;
    }

    public static void scrollAttacks(boolean right){
        if (dtScrollAtt > .3) {
            if (right) {
                if (Inventory.pos < player.invList.size() - 1)
                    Inventory.pos++;
                else Inventory.pos = 0;
                Inventory.dtInvSwitch = 0;
            } else {
                if (Inventory.pos > 0)
                    Inventory.pos--;
                else Inventory.pos = player.invList.size() - 1;
                Inventory.dtInvSwitch = 0;
            }
        }
    }
    //-----------------------------------------------------------------------------------------
    //Controller Interface
    public void connected(Controller controller) {

    }
    public void disconnected(Controller controller) {

    }
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        Xbox360Pad.updateSticks(axisCode,value);
        return false;

   }
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        Xbox360Pad.updatePOV(value);
        return false;
    }
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
