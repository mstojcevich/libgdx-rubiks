package cubesolve;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

/**
 * Single piece of the rubik's cube
 */
public interface Cubelet {

    /**
     * Draw the cubelet into the MeshBuilder
     *
     * @param builder Meshbuilder to draw onto
     * @param x       X position to start the cubelet
     * @param y       Y position to start the cubelet
     * @param z       Z position to start the cubelet
     * @param depth   Width/Height/Depth of the cubelet in gl units
     */
    void drawMeshes(MeshBuilder builder, float x, float y, float z, float depth);

    /**
     * Rotate the cubelet tall-wise counter-clockwise
     */
    void rotateTallCCW();

    /**
     * Rotate the cubelet tall-wise clockwise
     */
    void rotateTallCW();

    /**
     * Rotate the cubelet wide-wise counter-clockwise
     */
    void rotateWideCCW();

    /**
     * Rotate the cubelet wide-wise clockwise
     */
    void rotateWideCW();

    /**
     * Rotate the cubelet depth-wise counter-clockwise
     */
    void rotateDepthCCW();

    /**
     * Rotate the cubelet depth-wise clockwise
     */
    void rotateDepthCW();

    /**
     * Set the render mask for the sides of the cubelet
     * @param top Whether to draw the top
     * @param bottom Whether to draw the bottom
     * @param west Whether to draw the west
     * @param east Whether to draw the east
     * @param north Whether to draw the north
     * @param south Whether to draw the south
     */
    void setMask(boolean top, boolean bottom, boolean west, boolean east, boolean north, boolean south);

    /**
     * Get the color on a side of the cubelet
     * @param side Side to get the color of
     * @return Color of the specified side
     */
    PlainCubelet.CubeletColor getColor(PlainCubelet.CubeletSide side);
}
