package cubesolve;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

import java.util.Random;

public class CubeSolveInputProcessor extends CameraInputController {

    private final CubeSolve game;

    protected CubeSolveInputProcessor(CubeSolve game, Camera camera) {
        super(camera);
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean consumed = false;

        switch(keycode) {
            case Input.Keys.Q:
                game.getCube().rotateColumn(0);
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.W:
                game.getCube().rotateColumn(1);
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.E:
                game.getCube().rotateColumn(2);
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.A:
                game.getCube().rotateRow(2);
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.S:
                game.getCube().rotateRow(1);
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.D:
                game.getCube().rotateRow(0);
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.Z:
                game.getCube().rotateFace(2);
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.X:
                game.getCube().rotateFace(1);
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.C:
                game.getCube().rotateFace(0);
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.R:
                game.getCube().shuffle(new Random());
                game.updateSolved();
                consumed = true;
                break;
            case Input.Keys.T:
                game.getCube().reset();
                game.updateSolved();
                consumed = true;
                break;
        }

        return consumed || super.keyDown(keycode);
    }

}
