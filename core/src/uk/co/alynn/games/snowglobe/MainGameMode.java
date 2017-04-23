package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.security.acl.Owner;

public class MainGameMode extends AbstractGameMode {
    private ShapeRenderer renderer;
    private HexGrid<Tile> tiles;

    private int selectedSlice = -100, selectedColumn = -100;

    @Override
    public void preActivate() {
        renderer = new ShapeRenderer();
        tiles = new HexGrid<Tile>();
        initGrid();
    }

    private void initGrid() {
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                Tile tile = new Tile();
                if (i == 2 && j == 4) {
                    tile.owner = Ownership.RED;
                }
                tiles.set(i, j, tile);
            }
        }
    }

    @Override
    public void postDeactivate() {
        renderer.dispose();
        renderer = null;
        tiles = null;
    }

    @Override
    public void render() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (HexGrid.Entry<Tile> entry : tiles) {
            switch (entry.value.owner) {
                case RED:
                    renderer.setColor(1.0f, 0.1f, 0.1f, 1.0f);
                    break;
                case BLUE:
                    renderer.setColor(0.0f, 0.3f, 1.0f, 1.0f);
                    break;
                case NEUTRAL:
                    renderer.setColor(0.7f, 0.7f, 0.7f, 1.0f);
                    break;
            }
            if (entry.slice == selectedSlice && entry.column == selectedColumn) {
                renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
            drawHex(entry.slice, entry.column);
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

    @Override
    public void click(float x, float y) {
        // hacky unmunge
        x /= 100.0f;
        x -= 1.0f;
        y /= 100.0f;
        y -= 1.0f;
        selectedColumn = HexGrid.locToColumn(x, y);
        selectedSlice = HexGrid.locToSlice(x, y);
        System.err.println("SELECT " + selectedSlice + "/" + selectedColumn + " from " + x + ", " + y);
    }
}
