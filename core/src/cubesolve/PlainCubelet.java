package cubesolve;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Cubelet surrounded on 4 sides with the same color
 */
public class PlainCubelet implements Cubelet {

    private CubeletColor topColor, bottomColor, westColor, eastColor, northColor, southColor;
    private boolean topMask, bottomMask, westMask, eastMask, northMask, southMask;

    /**
     * Create a plain cubelet with the specified colors
     * If there is no color on a side, use CubeletColor.NONE
     */
    public PlainCubelet(CubeletColor topColor, CubeletColor bottomColor,
                        CubeletColor westColor, CubeletColor eastColor,
                        CubeletColor northColor, CubeletColor southColor) {
        this.topColor = topColor; this.bottomColor = bottomColor;
        this.northColor = northColor; this.southColor = southColor;
        this.westColor = westColor; this.eastColor = eastColor;

        this.topMask = this.bottomMask = this.westMask = this.eastMask = this.northMask = this.southMask = true;
    }

    @Override
    public Mesh[] drawMeshes(MeshBuilder builder, float x, float y, float z, float depth) {
        List<Mesh> meshes = new ArrayList<Mesh>();
        if(topMask) {
            builder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);
            builder.setColor(topColor.getGdxColor());
            builder.rect(x, y + depth, z + depth,
                    x + depth, y + depth, z + depth,
                    x + depth, y + depth, z,
                    x, y + depth, z,
                    0, 1, 0);
            meshes.add(builder.end());
        }
        if(bottomMask) {
            builder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);
            builder.setColor(bottomColor.getGdxColor());
            builder.rect(x, y, z,
                    x + depth, y, z,
                    x + depth, y, z + depth,
                    x, y, z + depth,
                    0, -1, 0);
            meshes.add(builder.end());
        }

        if(southMask) {
            builder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);
            builder.setColor(southColor.getGdxColor());
            builder.rect(x, y + depth, z,
                    x + depth, y + depth, z,
                    x + depth, y, z,
                    x, y, z,
                    0, 0, -1);
            meshes.add(builder.end());
        }
        if(northMask) {
            builder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);
            builder.setColor(northColor.getGdxColor());
            builder.rect(x, y, z + depth,
                    x + depth, y, z + depth,
                    x + depth, y + depth, z + depth,
                    x, y + depth, z + depth,
                    0, 0, 1);
            meshes.add(builder.end());
        }

        if(westMask) {
            builder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);
            builder.setColor(westColor.getGdxColor());
            builder.rect(x, y, z + depth,
                    x, y + depth, z + depth,
                    x, y + depth, z,
                    x, y, z,
                    -1, 0, 0);
            meshes.add(builder.end());
        }
        if(eastMask) {
            builder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);
            builder.setColor(eastColor.getGdxColor());
            builder.rect(x + depth, y, z,
                    x + depth, y + depth, z,
                    x + depth, y + depth, z + depth,
                    x + depth, y, z + depth,
                    1, 0, 0);
            meshes.add(builder.end());
        }

        return meshes.toArray(new Mesh[meshes.size()]);
    }

    @Override
    public void rotateTallCCW() {
        // Temp store the old values so we can overwrite without losing what it was
        final CubeletColor nc = northColor;
        final CubeletColor sc = southColor;
        final CubeletColor tc = topColor;
        final CubeletColor bc = bottomColor;
        final boolean nm = northMask;
        final boolean sm = southMask;
        final boolean tm = topMask;
        final boolean bm = bottomMask;

        // Rotate colors
        northColor = tc;
        bottomColor = nc;
        southColor = bc;
        topColor = sc;
    }

    @Override
    public void rotateTallCW() {
        // Temp store the old values so we can overwrite without losing what it was
        final CubeletColor nc = northColor;
        final CubeletColor sc = southColor;
        final CubeletColor tc = topColor;
        final CubeletColor bc = bottomColor;

        // Rotate colors
        topColor = nc;
        northColor = bc;
        bottomColor = sc;
        southColor = tc;
    }

    @Override
    public void rotateWideCCW() {
        // Temp store the old values so we can overwrite without losing what it was
        final CubeletColor nc = northColor;
        final CubeletColor sc = southColor;
        final CubeletColor wc = westColor;
        final CubeletColor ec = eastColor;

        // Rotate colors
        northColor = ec;
        westColor = nc;
        southColor = wc;
        eastColor = sc;
    }

    @Override
    public void rotateWideCW() {
        // Temp store the old values so we can overwrite without losing what it was
        final CubeletColor nc = northColor;
        final CubeletColor sc = southColor;
        final CubeletColor wc = westColor;
        final CubeletColor ec = eastColor;

        // Rotate colors
        eastColor = nc;
        northColor = wc;
        westColor = sc;
        southColor = ec;
    }

    @Override
    public void rotateDepthCCW() {
        // Temp store the old values so we can overwrite without losing what it was
        final CubeletColor tc = topColor;
        final CubeletColor bc = bottomColor;
        final CubeletColor wc = westColor;
        final CubeletColor ec = eastColor;

        // Rotate colors
        topColor = ec;
        westColor = tc;
        bottomColor = wc;
        eastColor = bc;
    }

    @Override
    public void rotateDepthCW() {
        // Temp store the old values so we can overwrite without losing what it was
        final CubeletColor tc = topColor;
        final CubeletColor bc = bottomColor;
        final CubeletColor wc = westColor;
        final CubeletColor ec = eastColor;

        // Rotate colors
        eastColor = tc;
        topColor = wc;
        westColor = bc;
        bottomColor = ec;
    }

    @Override
    public void setMask(boolean top, boolean bottom, boolean west, boolean east, boolean north, boolean south) {
        this.topMask = top; this.bottomMask = bottom;
        this.westMask = west; this.eastMask = east;
        this.northMask = north; this.southMask = south;
    }

    @Override
    public CubeletColor getColor(CubeletSide side) {
        switch(side) {
            case TOP:
                return topColor;
            case BOTTOM:
                return bottomColor;
            case WEST:
                return westColor;
            case EAST:
                return eastColor;
            case NORTH:
                return northColor;
            case SOUTH:
                return southColor;
            default:
                return null;
        }
    }

    public enum CubeletColor {
        GREEN(0, 1, 0), YELLOW(0.8f, 0.8f, 0), RED(1, 0, 0), BLUE(0, 0, 1), WHITE(1, 1, 1), ORANGE(0.8f, 0.3f, 0);

        private final Color gdxColor;

        /**
         * Create a cubelet with the specified RGB color
         *
         * @param red   Intensity, between 0.0 and 1.0, inclusive, of the red channel
         * @param green Intensity, between 0.0 and 1.0, inclusive, of the green channel
         * @param blue  Intensity, between 0.0 and 1.0, inclusive, of the blue channel
         */
        CubeletColor(float red, float green, float blue) {
            this.gdxColor = new Color(red, green, blue, 1 /*Opaque*/);
        }

        /**
         * @return The GDX color representing the cubelet color
         */
        public Color getGdxColor() {
            return gdxColor;
        }
    }

    /**
     * Side of a cubelet
     */
    public enum CubeletSide {
        TOP, BOTTOM, WEST, EAST, NORTH, SOUTH
    }
}