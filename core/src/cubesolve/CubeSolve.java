package cubesolve;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

/**
 * Rubik's cube in libgdx
 */
public class CubeSolve implements ApplicationListener {

	private static String[] CONTROLS_INFO = new String[] {
			"---Column---",
			"Q - Rotate Left",
			"W - Rotate Middle",
			"E - Rotate Right",
			"",
			"--Row--",
			"A - Rotate Top",
			"S - Rotate Middle",
			"D - Rotate Bottom",
			"",
			"--Face--",
			"Z - Rotate Front",
			"X - Rotate Middle",
			"C - Rotate Back",
			"",
			"--Other--",
			"R - Randomize",
			"T - Reset"
	};

	private Environment environment;
	private OrthographicCamera hudCam;
	private PerspectiveCamera cam;
	private CameraInputController camController;
	private ModelBatch modelBatch;
	private BitmapFont fpsFont;
	private BitmapFontCache controlsCache, fpsCache;
	private SpriteBatch hudBatch;
	private Cube cube;

	private boolean solved = true;
    private long lastFpsUpdate;

	@Override
	public void create() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 0.8f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1, 1, 1));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1, -1, -1));

		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		cube = new Cube(3);

		hudCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudBatch = new SpriteBatch();
		hudBatch.setProjectionMatrix(hudCam.combined);

		fpsFont = new BitmapFont();

		if(Gdx.app.getType().equals(Application.ApplicationType.Desktop)
				|| Gdx.app.getType().equals(Application.ApplicationType.WebGL)) {
			controlsCache = new BitmapFontCache(fpsFont, true);
			createControlsCache();
		}
        fpsCache = new BitmapFontCache(fpsFont, true);
        updateFpsCache();

        camController = new CubeSolveInputProcessor(this, cam);
		Gdx.input.setInputProcessor(camController);

		Gdx.graphics.setContinuousRendering(true);
	}

	@Override
	public void render() {
		hudBatch.setProjectionMatrix(hudCam.combined);

		camController.update();
        updateFpsCache();

		Gdx.gl.glClearColor(0.2f,
                solved ? 0.2f + (1 + (float) Math.sin((System.currentTimeMillis() % 6282) / 200.0f)) * 0.05f : 0.2f,
                0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		cube.render(modelBatch, environment);
		modelBatch.end();

		hudBatch.begin();
        fpsCache.draw(hudBatch);
		if(Gdx.app.getType().equals(Application.ApplicationType.Desktop)
				|| Gdx.app.getType().equals(Application.ApplicationType.WebGL)) {
			controlsCache.draw(hudBatch);
		}
		hudBatch.end();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		cube.dispose();
	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update();

		hudCam.setToOrtho(false, width, height);
		hudCam.update();
		hudBatch.setProjectionMatrix(hudCam.combined);

		if(Gdx.app.getType().equals(Application.ApplicationType.Desktop)
				|| Gdx.app.getType().equals(Application.ApplicationType.WebGL)) {
			createControlsCache();
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	/**
	 * @return The cube being rendered
	 */
	public Cube getCube() {
		return cube;
	}

	/**
	 * Check whether the cube is solved.
	 * Should be called every time the cube changes.
	 */
	public void updateSolved() {
		this.solved = cube.isSolved();
        if(solved) {
            Gdx.graphics.setTitle("CubeSolve - You Win");
        } else {
            Gdx.graphics.setTitle("CubeSolve");
        }
	}

	private void createControlsCache() {
		// The controls only matter for desktop since it's the only platform with a keyboard
		if(Gdx.app.getType().equals(Application.ApplicationType.Desktop)
				|| Gdx.app.getType().equals(Application.ApplicationType.WebGL)) {
			controlsCache.clear();
			float y = fpsFont.getLineHeight();
			for (int i = CONTROLS_INFO.length - 1; i >= 0; i--) { // Backwards because Y is upside down
				String s = CONTROLS_INFO[i];

				controlsCache.addText(s, 4, y);
				y += fpsFont.getLineHeight();
			}
		}
	}

    private void updateFpsCache() {
        if(System.currentTimeMillis() - lastFpsUpdate > 1000l) {
            fpsCache.setText("FPS: " + Gdx.graphics.getFramesPerSecond(), 4, Gdx.graphics.getHeight() - 4);
            lastFpsUpdate = System.currentTimeMillis();
        }
    }

}