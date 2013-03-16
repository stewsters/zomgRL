package com.stewsters.zomgrl.input;

import com.stewsters.zomgrl.entity.Entity;
import com.stewsters.zomgrl.main.HelloDungeon;

import javax.swing.event.MouseInputListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import static java.awt.event.KeyEvent.*;
import static squidpony.squidgrid.util.Direction.*;

/**
 * A simple input listener.
 */
public class CharacterInputListener implements MouseInputListener, KeyListener {


    HelloDungeon context;
    Entity player;

    public CharacterInputListener(HelloDungeon context, Entity player) {
        this.context = context;
        this.player = player;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //nothing special happens
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //nothing special happens
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        dragged = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //nothing special happens
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //http://forums.codeguru.com/showthread.php?495419-KeyListener-doesn-t-work-on-Windows-OS
        handleKey(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void handleKey(KeyEvent e) {
        int code = e.getExtendedKeyCode();
        boolean shift = e.isShiftDown();
        switch (code) {
            //movement
            case VK_H:
            case VK_LEFT:
            case VK_NUMPAD4:
                context.move(LEFT, shift);
                break;
            case VK_L:
            case VK_RIGHT:
            case VK_NUMPAD6:
                context.move(RIGHT, shift);
                break;
            case VK_K:
            case VK_UP:
            case VK_NUMPAD8:
                context.move(UP, shift);
                break;
            case VK_J:
            case VK_DOWN:
            case VK_NUMPAD2:
                context.move(DOWN, shift);
                break;
            case VK_B:
            case VK_NUMPAD1:
                context.move(DOWN_LEFT, shift);
                break;
            case VK_N:
            case VK_NUMPAD3:
                context.move(DOWN_RIGHT, shift);
                break;
            case VK_PERIOD:
            case VK_NUMPAD5:
                context.standStill(); // waste time
                break;
            case VK_Y:
            case VK_NUMPAD7:
                context.move(UP_LEFT, shift);
                break;
            case VK_U:
            case VK_NUMPAD9:
                context.move(UP_RIGHT, shift);
                break;
            case VK_G:
                context.grab(); //pick up item
                break;
            case VK_D:
                context.drop(); //release item
                break;
            case VK_F:
                context.fire(); //use a bow or spell
                break;
//            case VK_E:
//                context.inspect(); //use a bow or spell
//                break;
//            case VK_R:
//                context.reload();
//                break;
            case VK_1:
                context.useItem(1);
                break;
            case VK_2:
                context.useItem(2);
                break;
            case VK_3:
                context.useItem(3);
                break;
            case VK_4:
                context.useItem(4);
                break;
            case VK_5:
                context.useItem(5);
                break;
            case VK_6:
                context.useItem(6);
                break;
            case VK_7:
                context.useItem(7);
                break;
            case VK_8:
                context.useItem(8);
                break;
//            case VK_9:
//                context.useItem(9);
//                break;
//            case VK_0:
//                context.useItem(0);
//                break;
            default:
                //NONE;
                break;
        }
    }


}