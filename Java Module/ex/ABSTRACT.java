package com.quadx.dungeons.abilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public abstract class Ability {
    protected Texture icon;
    protected String name="default";
    final ArrayList<String> output=new ArrayList<>();
    int[] upCost={0,2,4,4,4};
    static boolean enabled=false;
    static float cooldown=0;
    static float timeCounter=0;
    int level=0;

    Ability(){
    }
    Texture loadIcon(String s){icon= new Texture(Gdx.files.internal(s));return icon;}
    public  Texture getIcon(){return icon;}

    public abstract void onActivate();
    public abstract int getMod();
    public abstract void l1();
    public abstract void l2();
    public abstract void l3();
    public abstract void l4();
    public abstract void l5();

    public static void updateTimeCounter(){timeCounter+= Gdx.graphics.getDeltaTime();}

    public static boolean isEnabled(){return enabled;}
    public abstract String getName();

    public void upgrade(){
        if(level<6) {
            try {
                if (player.getAbilityPoints() >= upCost[level]) {
                    player.setAbilityPoints(-upCost[level]);
                    level++;
                    switch (level) {
                        case 2: {
                            l2();
                            break;
                        }
                        case 3: {
                            l3();
                            break;
                        }
                        case 4: {
                            l4();
                            break;
                        }
                        case 5: {
                            l5();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public int getLevel() {
        return level;
    }
    public abstract ArrayList<String> details();
}
