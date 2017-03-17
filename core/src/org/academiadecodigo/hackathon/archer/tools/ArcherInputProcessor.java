package org.academiadecodigo.hackathon.archer.tools;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class ArcherInputProcessor implements InputProcessor {

    public boolean wKey;
    public boolean aKey;
    public boolean sKey;
    public boolean dKey;
    public boolean upKey;
    public boolean downKey;
    public boolean leftKey;
    public boolean rightKey;
    private boolean enterKey;

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.W) {
            wKey = true;
        }
        if (keycode == Input.Keys.S) {
            sKey = true;
        }
        if (keycode == Input.Keys.A) {
            aKey = true;
        }
        if (keycode == Input.Keys.D) {
            dKey = true;
        }
//        if (keycode == Input.Keys.UP) {
//            upKey = true;
//        }
//        if (keycode == Input.Keys.DOWN) {
//            downKey = true;
//        }
//        if (keycode == Input.Keys.LEFT) {
//            leftKey = true;
//        }
//        if (keycode == Input.Keys.RIGHT) {
//            rightKey = true;
//        }

        return false;

    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.W) {
            wKey = false;
        }
        if (keycode == Input.Keys.S) {
            sKey = false;
        }
        if (keycode == Input.Keys.A) {
            aKey = false;
        }
        if (keycode == Input.Keys.D) {
            dKey = false;
        }
//        if (keycode == Input.Keys.UP) {
//            upKey = false;
//        }
//        if (keycode == Input.Keys.DOWN) {
//            downKey = false;
//        }
//        if (keycode == Input.Keys.LEFT) {
//            leftKey = false;
//        }
//        if (keycode == Input.Keys.RIGHT) {
//            rightKey = false;
//        }
        
        return false;
        
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
