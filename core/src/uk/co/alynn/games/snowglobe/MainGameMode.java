package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Random;

public class MainGameMode extends AbstractGameMode {
    private ShapeRenderer renderer;
    private HexGrid<Tile> tiles;

    private int selectedSlice = -100, selectedColumn = -100;
    private OrthographicCamera orthographicCamera;
    private final double initialFlakeRate = 10.0;
    private final double flakeRateHalfLife = 10.0;
    private double time = 0.0;
    private final Random rng = new Random();

    @Override
    protected Viewport createViewport() {
        orthographicCamera = new OrthographicCamera(10.24f, 6.4f);
        Viewport fixedPort = new FitViewport(orthographicCamera.viewportWidth, orthographicCamera.viewportHeight, orthographicCamera);
        System.err.print("BEES");
        return fixedPort;
    }

    @Override
    public void preActivate() {
        renderer = new ShapeRenderer();
        tiles = new HexGrid<Tile>();
        initGrid();
    }

    private void initGrid() {
        for (int i = -30; i < 30; ++i) {
            for (int j = -30; j < 30; ++j) {
                if (HexGrid.distance(i, j, 0, 0) >= 4)
                    continue;
                Tile tile = new Tile();
                if (i == 2 && j == 1) {
                    tile.owner = Ownership.RED;
                }
                if (i == -2 && j == -1) {
                    tile.owner = Ownership.BLUE;
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
        orthographicCamera.update();
        renderer.setProjectionMatrix(orthographicCamera.combined);
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
                    if (entry.value.value < 1) {
                        renderer.setColor(0.5f, 0.5f, 0.5f, 1.0f);
                    } else if (entry.value.value < 3) {
                        renderer.setColor(0.6f, 0.6f, 0.6f, 1.0f);
                    } else if (entry.value.value < 5) {
                        renderer.setColor(0.7f, 0.7f, 0.7f, 1.0f);
                    } else if (entry.value.value < 5) {
                        renderer.setColor(0.8f, 0.8f, 0.8f, 1.0f);
                    } else if (entry.value.value < 8) {
                        renderer.setColor(0.9f, 0.9f, 0.9f, 1.0f);
                    } else {
                        renderer.setColor(1, 1, 1, 1.0f);
                    }
                    break;
            }
            if (entry.slice == selectedSlice && entry.column == selectedColumn) {
                renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
            drawHex(entry.slice, entry.column);
        }
        renderer.end();
    }

    @Override
    public GameMode tick(double dt) {
        time += dt;
        double currentFlakeRate = initialFlakeRate * Math.pow(2.0, -(time / flakeRateHalfLife));
        int numFlakes = Utils.randomPoisson(currentFlakeRate * dt, rng);
        ArrayList<Tile> workingTiles = new ArrayList<Tile>();
        for (HexGrid.Entry<Tile> entry : tiles) {
            workingTiles.add(entry.value);
        }
        for (int i = 0; i < numFlakes; ++i) {
            // pick a tile at random
            int tileIndex = rng.nextInt(workingTiles.size());
            workingTiles.get(tileIndex).value += 1;
            System.out.println(workingTiles.get(tileIndex).value);
        }

        return this;
    }

    private void drawHex(int slice, int column) {
        double x = HexGrid.hexToX(slice, column);
        double y = HexGrid.hexToY(slice, column);

        float width = 1.0f;
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
        selectedColumn = HexGrid.locToColumn(x, y);
        selectedSlice = HexGrid.locToSlice(x, y);
        System.err.println("SELECT " + selectedSlice + "/" + selectedColumn + " from " + x + ", " + y);
    }

    @Override
    public boolean usesCenteredCamera() {
        return false;
    }
}
