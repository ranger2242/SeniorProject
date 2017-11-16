package com.quadx.dungeons.tools.controllers;

import com.badlogic.gdx.controllers.PovDirection;

import java.util.ArrayList;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings({"DefaultFileTemplate", "unused"})
public class Xbox360Pad
{
    /*
     * It seems there are different versions of gamepads with different ID Strings.
     * Therefore its IMO a better bet to check for:
     * if (contB.getName().toLowerCase().contains("xbox") &&
                   contB.getName().contains("360"))
     *
     * Controller (Gamepad for Xbox 360)
       Controller (XBOX 360 For Windows)
       Controller (Xbox 360 Wireless Receiver for Windows)
       Controller (Xbox wireless receiver for windows)
       XBOX 360 For Windows (Controller)
       Xbox 360 Wireless Receiver
       Xbox Receiver for Windows (Wireless Controller)
       Xbox wireless receiver for windows (Controller)
     */
    //public static final String ID = "XBOX 360 For Windows (Controller)";

    public static ArrayList<String> bnames=new ArrayList<>();
    public static ArrayList<String> dnames=new ArrayList<>();

    public static boolean win10=false;
    public static void addNames(){
        String os=System.getProperty("os.name");
        if(!os.equals("Windows 10")){
            AXIS_RIGHT_TRIGGER=4;
            AXIS_LEFT_TRIGGER=4;
            win10=false;
        }else{
            win10=true;
        }
        bnames.add("A");
        bnames.add("B");
        bnames.add("X");
        bnames.add("Y");
        bnames.add("LB");
        bnames.add("RB");
        bnames.add("BACK");
        bnames.add("START");
        bnames.add("L3");
        bnames.add("R3");

        dnames.add("D UP");
        dnames.add("D DOWN");
        dnames.add("D RIGHT");
        dnames.add("D LEFT");
    }
    public static final int BUTTON_A = 0;
    public static final int BUTTON_B = 1;
    public static final int BUTTON_X = 2;
    public static final int BUTTON_Y = 3;
    public static final int BUTTON_LB = 4;
    public static final int BUTTON_RB = 5;
    public static final int BUTTON_BACK = 6;
    public static final int BUTTON_START = 7;
    public static final int BUTTON_L3 = 8;
    public static final int BUTTON_R3 = 9;
    public static final PovDirection    BUTTON_DPAD_UP = PovDirection.north;
    public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
    public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
    public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
    public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
    public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
    public static int AXIS_LEFT_TRIGGER = 4; //value 0 to 1f
    public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
    public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
    public static int AXIS_RIGHT_TRIGGER = 5; //value 0 to -1f

    public static float lxStick=0;
    public static float rxStick=0;
    public static float lyStick=0;
    public static float ryStick=0;
    static float rt =0;
    static float lt =0;
    public static boolean rbv=false;
    public static boolean lbv=false;
    public static boolean dup=false;
    public static boolean ddown=false;
    public static boolean dleft=false;
    public static boolean dright=false;

    static float tr=.3f;

    public static String getName(int a){
        String s=""+(char)(a+65);

        return s;
    }

    public static boolean getLT(){
        return lt >tr; //minimum trigger value
    }
    public static boolean getRT(){
        if(win10)
        return rt >tr;
        else{
            return rt<-tr;
        }
    }
    public static boolean getLUp(){
        return lyStick<-tr;
    }
    public static boolean getLDown(){
        return lyStick>tr;
    }
    public static boolean getLRight(){
        return lxStick>tr;
    }
    public static boolean getLLeft(){
        return lxStick<-tr;
    }
    public static boolean getRUp(){
        return ryStick<-tr;
    }
    public static boolean getRDown(){
        return ryStick>tr;
    }
    public static boolean getRRRight(){
        return rxStick>tr;
    }
    public static boolean getRLeft(){
        return rxStick<-tr;
    }

    public static void updatePOV(PovDirection p){
        if(p==BUTTON_DPAD_DOWN){
            ddown=true;
        }else{ddown=false;}
        if(p==BUTTON_DPAD_UP){
            dup=true;
        }else{dup=false;}
        if(p==BUTTON_DPAD_RIGHT){
            dright=true;
        }else{dright=false;}
        if(p==BUTTON_DPAD_LEFT){
            dleft=true;
        }else{dleft=false;}
        if(p==PovDirection.northEast){
            dright=true;
            dup=true;
        }
        if(p==PovDirection.northWest){
            dup=true;
            dleft=true;
        }
        if(p==PovDirection.southEast){
            ddown=true;
            dright=true;
        }
        if(p==PovDirection.southWest){
            ddown=true;
            dleft=true;
        }
    }
    public static void updateSticks(int code,  float  v){
        if(code==Xbox360Pad.AXIS_LEFT_X) {
            lxStick=v;
        }
        if(code==Xbox360Pad.AXIS_LEFT_Y) {
            lyStick=v;
        }
        if(code==Xbox360Pad.AXIS_RIGHT_X) {
            rxStick=v;
        }
        if(code==Xbox360Pad.AXIS_RIGHT_Y) {
            ryStick=v;
        }
        if(code==Xbox360Pad.AXIS_LEFT_TRIGGER){
            lt=v;
        }
        if(code==Xbox360Pad.AXIS_RIGHT_TRIGGER){
            rt =v;
        }
    }
}