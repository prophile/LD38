package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainGameMode extends AbstractGameMode {
    private ShapeRenderer renderer;

    @Override
    public void preActivate() {
        renderer = new ShapeRenderer();
    }

    @Override
    public void postDeactivate() {
        renderer.dispose();
        renderer = null;
    }

    @Override
    public void render() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = -1; i < 10; ++i) {
            for (int j = -5; j < 5; ++j) {
                drawHex(j, i);
            }
        }
        renderer.end();
    }

    private void drawHex(int slice, int column) {
        double x = HexGrid.hexToX(slice, column);
        double y = HexGrid.hexToY(slice, column);
        //renderer.circle(((float)x + 1.0f) * 100.0f, ((float)y + 1.0f) * 100.0f, 40.0f);
        // hack
        x = 100.0 * (x + 1.0);
        y = 100.0 * (y + 1.0);

        float width = 100.0f;
        float halfWidth = width / 2.0f;

        float fx = (float)x;
        float fy = (float)y;

        float hpxo = halfWidth * 0.5f;
        float hpyo = halfWidth * 0.8660254037844386f;

        renderer.triangle(fx - halfWidth, fy, fx - hpxo, fy + hpyo, fx, fy);
        renderer.triangle(fx - hpxo, fy + hpyo, fx + hpxo, fy + hpyo, fx, fy);
        renderer.triangle(fx + hpxo, fy + hpyo, fx + halfWidth, fy, fx, fy);
        renderer.triangle(fx + halfWidth, fy, fx + hpxo, fy - hpyo, fx, fy);
        renderer.triangle(fx + hpxo, fy - hpyo, fx - hpxo, fy - hpyo, fx, fy);
        renderer.triangle(fx - hpxo, fy - hpyo, fx - halfWidth, fy, fx, fy);
    }
}
