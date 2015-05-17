package cubesolve;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;
import cubesolve.PlainCubelet.CubeletSide;

import java.util.Random;

/**
 * Rubik's cube containing multiple cubelets
 */
public class Cube implements Disposable {

    private final int size;
    private final Cubelet[][][] cubelets;
    private Model model;
    private ModelInstance modelInstance;
    private boolean disableAutoRerender;

    /**
     * Creates a Rubik's cube with the default configuration
     *
     * @param size Number of rows for the cube to have
     */
    public Cube(int size) {
        this.size = size;
        this.cubelets = new Cubelet[size][size][size];

        fillWithDefault();
        rerenderCube();
    }

    /**
     * Get the cubelet at the specified position
     *
     * @param x X position of the cubelet
     * @param y Y position of the cubelet
     * @param z Z position of the cubelet
     *
     * @return Cubelet at the position
     */
    public Cubelet getCubelet(int x, int y, int z) {
        return cubelets[x][y][z];
    }

    /**
     * Render the cube to the ModelBatch
     *
     * @param batch ModelBatch to render to
     * @param environment Environment to render with
     */
    public void render(ModelBatch batch, Environment environment) {
        batch.render(modelInstance, environment);
    }

    /**
     * @return Number of rows in the cube
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Calculates whether to cube is solved
     *
     * @return Whether all of the sides are of only one color
     */
    public boolean isSolved() {
        // Check top
        PlainCubelet.CubeletColor topColor = cubelets[0][size-1][0].getColor(CubeletSide.TOP);
        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {
                if(!cubelets[x][size-1][z].getColor(CubeletSide.TOP).equals(topColor))
                    return false;
            }
        }
        // Check bottom
        PlainCubelet.CubeletColor bottomColor = cubelets[0][0][0].getColor(CubeletSide.BOTTOM);
        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {
                if(!cubelets[x][0][z].getColor(CubeletSide.BOTTOM).equals(bottomColor))
                    return false;
            }
        }

        // Check south
        PlainCubelet.CubeletColor southColor = cubelets[0][0][0].getColor(CubeletSide.SOUTH);
        for(int x = 0; x < size; x++) {
            for(int y = 0; y < size; y++) {
                if(!cubelets[x][y][0].getColor(CubeletSide.SOUTH).equals(southColor))
                    return false;
            }
        }
        // Check north
        PlainCubelet.CubeletColor northColor = cubelets[0][0][size-1].getColor(CubeletSide.NORTH);
        for(int x = 0; x < size; x++) {
            for(int y = 0; y < size; y++) {
                if(!cubelets[x][y][size-1].getColor(CubeletSide.NORTH).equals(northColor))
                    return false;
            }
        }

        // Check west
        PlainCubelet.CubeletColor westColor = cubelets[0][0][0].getColor(CubeletSide.WEST);
        for(int z = 0; z < size; z++) {
            for(int y = 0; y < size; y++) {
                if(!cubelets[0][y][z].getColor(CubeletSide.WEST).equals(westColor))
                    return false;
            }
        }
        // Check east
        PlainCubelet.CubeletColor eastColor = cubelets[size-1][0][0].getColor(CubeletSide.EAST);
        for(int z = 0; z < size; z++) {
            for(int y = 0; y < size; y++) {
                if(!cubelets[size-1][y][z].getColor(CubeletSide.EAST).equals(eastColor))
                    return false;
            }
        }

        // None of the side checks failed
        return true;
    }

    /**
     * Rotate a column tall-wise counter-clockwise
     * @param row Row (x) to rotate
     */
    public void rotateColumn(int row) {
        for(int y = 0; y < size; y++) {
            for(int z = 0; z < size; z++) {
                cubelets[row][y][z].rotateTallCCW();
            }
        }

        if(!disableAutoRerender)
            rerenderCube();
    }

    /**
     * Rotate a row wide-wise counter-clockwise
     * @param row Row (y) to rotate
     */
    public void rotateRow(int row) {
        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {
                cubelets[x][row][z].rotateWideCCW();
            }
        }

        if(!disableAutoRerender)
            rerenderCube();
    }

    /**
     * Rotate a face depth-wise counter-clockwise
     * @param row Row (z) to rotate
     */
    public void rotateFace(int row) {
        for(int x = 0; x < size; x++) {
            for(int y = 0; y < size; y++) {
                cubelets[x][y][row].rotateDepthCCW();
            }
        }

        if(!disableAutoRerender)
            rerenderCube();
    }

    /**
     * Reset the cube to its default state
     */
    public void reset() {
        fillWithDefault();
        rerenderCube();
    }

    /**
     * Shuffle the cube
     */
    public void shuffle(Random rng) {
        disableAutoRerender = true;
        for(int iter = 0; iter < 100; iter++) {
            for(int row = 0; row < size; row++) {
                for(int times = 0; times < rng.nextInt(3); times++) {
                    rotateRow(row);
                }
            }
            for(int col = 0; col < size; col++) {
                for(int times = 0; times < rng.nextInt(3); times++) {
                    rotateColumn(col);
                }
            }
            for(int face = 0; face < size; face++) {
                for(int times = 0; times < rng.nextInt(3); times++) {
                    rotateFace(face);
                }
            }
        }
        disableAutoRerender = false;
        rerenderCube();
    }

    @Override
    public void dispose() {
        this.model.dispose();
    }

    /**
     * Rerender the cube with its current status
     */
    private void rerenderCube() {
        if(model != null)
            model.dispose();

        MeshBuilder builder = new MeshBuilder();
        Mesh[] meshes = new Mesh[cubelets.length*cubelets.length*cubelets.length*6];
        float startX, startY, startZ;
        startX = startY = startZ = -cubelets.length*3.2f/2f;
        int meshNum = 0;
        for (int xT = 0; xT < cubelets.length; xT++) {
            for (int yT = 0; yT < cubelets[0].length; yT++) {
                for (int zT = 0; zT < cubelets[0].length; zT++) {
                    Cubelet cblt = cubelets[xT][yT][zT];
                    if(cblt == null)continue;
                    Mesh[] cubeletMeshes = cblt.drawMeshes(builder, startX + xT * 3f, startY + yT * 3f, startZ + zT * 3f, 3);
                    for (Mesh cubeletMesh : cubeletMeshes) {
                        meshes[meshNum] = cubeletMesh;
                        meshNum++;
                    }
                }
            }
        }
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        int num = 0;
        for(Mesh mesh : meshes) {
            if(mesh == null)continue;
            modelBuilder.part("cubemesh" + num, mesh, GL20.GL_TRIANGLES, new Material(ColorAttribute.createSpecular(Color.WHITE)));
        }
        this.model = modelBuilder.end();
        this.modelInstance = new ModelInstance(model);
    }

    private void fillWithDefault() {
        for(int i = 0; i < cubelets.length; i++) {
            for(int j = 0; j < cubelets[0].length; j++) {
                for(int k = 0; k < cubelets[0][0].length; k++) {
                    // Don't add cubelets that are internal
                    if((i > 0 && i < cubelets.length - 1)
                            && (j > 0 && j < cubelets.length - 1)
                            && (k > 0 && k < cubelets.length - 1))continue;
                    cubelets[i][j][k] = new PlainCubelet(
                            PlainCubelet.CubeletColor.BLUE,
                            PlainCubelet.CubeletColor.RED,
                            PlainCubelet.CubeletColor.ORANGE,
                            PlainCubelet.CubeletColor.YELLOW,
                            PlainCubelet.CubeletColor.GREEN,
                            PlainCubelet.CubeletColor.WHITE);
                    cubelets[i][j][k].setMask(j == cubelets.length-1, j == 0, i == 0, i == cubelets.length-1,  k == cubelets.length - 1, k == 0);
                }
            }
        }
    }

}
