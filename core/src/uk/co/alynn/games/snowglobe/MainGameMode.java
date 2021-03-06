package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameMode extends AbstractGameMode {
    public static final double AI_THINKING_TIME = 2.5;
    private ShapeRenderer renderer;
    private SpriteBatch batch;
    private HexGrid<Tile> tiles;
    private SnowParticles particles;
    private EraseParticles erasicles;

    private int selectedSlice = -100, selectedColumn = -100;
    private OrthographicCamera orthographicCamera;
    private final int initialValue = 8;
    private final int combatCost = 2;
    private final int gridSize = 4;
    private final double initialFlakeRate = 10.0;
    private final double flakeRateHalfLife = 10.0;
    private final double initialEraseRate = 0.1;
    private final double eraseRateHalfLife = -30.0;
    private int allTimeHighFlakes = 0;
    private double time = 0.0;
    private double gameEndTimer = 3.0;
    private final Random rng = new Random();

    private Ownership turn = Ownership.RED;
    private double blueThinkingTime = 0.0;

    @Override
    protected Viewport createViewport() {
        orthographicCamera = new OrthographicCamera(16f, 10f);
        Viewport fixedPort = new FitViewport(orthographicCamera.viewportWidth, orthographicCamera.viewportHeight, orthographicCamera);
        System.err.print("BEES");
        return fixedPort;
    }

    @Override
    public void preActivate() {
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        tiles = new HexGrid<Tile>();
        initGrid();
        particles = new SnowParticles(10000, 8.0f, 2.0f);
        erasicles = new EraseParticles(30, 5.0f, 4.0f);
    }

    private void initGrid() {
        for (int i = -30; i < 30; ++i) {
            for (int j = -30; j < 30; ++j) {
                if (HexGrid.distance(i, j, 0, 0) >= gridSize)
                    continue;
                Tile tile = new Tile();
                if (i == 2 && j == 1) {
                    tile.owner = Ownership.RED;
                    tile.value = initialValue;
                }
                if (i == -2 && j == -1) {
                    tile.owner = Ownership.BLUE;
                    tile.value = initialValue;
                }
                tiles.set(i, j, tile);
            }
        }
    }

    @Override
    public void postDeactivate() {
        renderer.dispose();
        renderer = null;
        batch.dispose();
        batch = null;
        tiles = null;
    }

    private final Matrix4 IDENTITY4 = new Matrix4();

    @Override
    public void render() {
        orthographicCamera.update();
        renderer.setProjectionMatrix(orthographicCamera.combined);
        batch.setProjectionMatrix(orthographicCamera.combined);
        batch.setTransformMatrix(IDENTITY4);

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
            drawHex(entry.slice, entry.column);
        }
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glLineWidth(3.0f);
        if (hasSelection()) {
            drawHexOutline(selectedSlice, selectedColumn);
        }
        renderer.end();

        batch.begin();
        drawText(3.0f, -2.5f, "Top: " + allTimeHighFlakes + "\n" + (turn == Ownership.RED ? "red" : "blue") + " to move", true);
        for (HexGrid.Entry<Tile> entry : tiles) {
            drawText((float)HexGrid.hexToX(entry.slice, entry.column), (float)HexGrid.hexToY(entry.slice, entry.column) - 0.18f, "" + entry.value.value, true);
        }
        batch.end();

        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        renderer.begin(ShapeRenderer.ShapeType.Point);
        particles.render(renderer, time);
        renderer.end();

        batch.setTransformMatrix(IDENTITY4);
        batch.begin();
        erasicles.render(batch, time);
        batch.end();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void drawText(float x, float y, String text, boolean center) {
        Matrix4 trans = new Matrix4();
        trans.translate(x, y, 0.0f);
        trans.scale(0.01f, 0.01f, 1.0f);
        batch.setTransformMatrix(trans);
        batch.setShader(Overlord.s_instance.fontShader);
        BitmapFont fnt = Overlord.s_instance.assetManager.get("bitstream.fnt", BitmapFont.class);
        fnt.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        fnt.draw(batch, text, 0f, 0f, 0.0f, center ? Align.center : Align.left, true);
        batch.setShader(null);
    }

    @Override
    public GameMode tick(double dt) {
        // generate and distribute erased tiles and new flakes
        time += dt;
        double currentEraseRate = initialEraseRate * Math.pow(2.0, -(time/ eraseRateHalfLife));
        int numErase = Utils.randomPoisson(currentEraseRate * dt, rng);
        for (int i = 0; i < numErase; i++) {
            // determine tile radius where tile is to be erased,
            // with preference for larger radii
            int radius = getCubeRand();
            // get all tiles at this radius
            ArrayList<HexGrid.Entry<Tile>> radiusTiles = new ArrayList<HexGrid.Entry<Tile>>();
            for (HexGrid.Entry<Tile> entry : tiles) {
                if (HexGrid.distance(0, 0, entry.slice, entry.column) == radius) {
                    radiusTiles.add(entry);
                }
            }
            if (!radiusTiles.isEmpty()) {
                int eraseIdx = rng.nextInt(radiusTiles.size());
                tiles.set(radiusTiles.get(eraseIdx).slice, radiusTiles.get(eraseIdx).column, null);
                System.err.println("erasing slice " + radiusTiles.get(eraseIdx).slice + " column " + radiusTiles.get(eraseIdx).column);
            }
        }

        if (tiles.size() <= 1) {
            gameEndTimer -= dt;
            if (gameEndTimer <= 0.0) {
                return new SingleTextureGameMode("am_end.png", new SingleTextureGameMode.Receiver() {
                    @Override
                    public GameMode receive() {
                        return new MenuGameMode();
                    }
                });
            } else {
                return this;
            }
        }

        double currentFlakeRate = initialFlakeRate * Math.pow(2.0, -(time / flakeRateHalfLife));
        int numFlakes = Utils.randomPoisson(currentFlakeRate * dt, rng);
        ArrayList<Tile> workingTiles = new ArrayList<Tile>();
        int redFlakes = 0;
        for (HexGrid.Entry<Tile> entry : tiles) {
            workingTiles.add(entry.value);

            // While we're iterating through anyway, sum up player tiles
            if (entry.value.owner == Ownership.RED) {
                redFlakes += entry.value.value;
            }
        }

        if (redFlakes > allTimeHighFlakes) {
            allTimeHighFlakes = redFlakes;
        }

        for (int i = 0; i < numFlakes; ++i) {
            // pick a tile at random
            int tileIndex = rng.nextInt(workingTiles.size());
            workingTiles.get(tileIndex).value += 1;
            System.out.println(workingTiles.get(tileIndex).value);
        }

        blueThinkingTime -= dt;
        if (blueThinkingTime < 0.0 && turn == Ownership.BLUE) {
            doAITurn(Ownership.BLUE);
        }

        return this;
    }

    private int getCubeRand() {
        // return random number between 1 and gridSize
        // with a preference for values closer to the limits
        int randMax = 0;
        int result = gridSize - 1;
        for (int i = 0; i < gridSize; i++) {
            randMax += i * i;
        }
        double r = Math.random();
        r = r * randMax;
        while (r > 0) {
            r -= result * result;
            result -= 1;
        }
        return result + 1;
    }

    private void drawHex(int slice, int column) {
        emitHex(slice, column, new CoordReceiver() {
            @Override
            public void receive(float x1, float y1, float x2, float y2, float xc, float yc) {
                renderer.triangle(x1, y1, x2, y2, xc, yc);
            }
        });
    }

    private void drawHexOutline(int slice, int column) {
        emitHex(slice, column, new CoordReceiver() {
            @Override
            public void receive(float x1, float y1, float x2, float y2, float xc, float yc) {
                renderer.line(x1, y1, x2, y2);
            }
        });
    }

    private void emitHex(int slice, int column, CoordReceiver cr) {
        double x = HexGrid.hexToX(slice, column);
        double y = HexGrid.hexToY(slice, column);

        float width = 1.14f;
        float halfWidth = width / 2.0f;

        float fx = (float)x;
        float fy = (float)y;

        float hpxo = halfWidth * 0.5f;
        float hpyo = halfWidth * 0.8660254037844386f;

        cr.receive(fx - halfWidth, fy, fx - hpxo, fy + hpyo, fx, fy);
        cr.receive(fx - hpxo, fy + hpyo, fx + hpxo, fy + hpyo, fx, fy);
        cr.receive(fx + hpxo, fy + hpyo, fx + halfWidth, fy, fx, fy);
        cr.receive(fx + halfWidth, fy, fx + hpxo, fy - hpyo, fx, fy);
        cr.receive(fx + hpxo, fy - hpyo, fx - hpxo, fy - hpyo, fx, fy);
        cr.receive(fx - hpxo, fy - hpyo, fx - halfWidth, fy, fx, fy);
    }

    private void nope() {
        if (turn == Ownership.RED) {
            SFX.MEEPMERP.play();
        }
    }

    @Override
    public void click(float x, float y) {
        int targetColumn = HexGrid.locToColumn(x, y);
        int targetSlice = HexGrid.locToSlice(x, y);

        System.err.println("Has sel: " + hasSelection());
        System.err.println("inbound: " + !isOutsideWorld(targetSlice, targetColumn));

        if (isOutsideWorld(targetSlice, targetColumn) || !hasSelection()) {
            // select or deselect
            if (hasSelection()) {
                SFX.UNSELECT.play();
            } else {
                SFX.SELECT.play();
            }
            selectedSlice = targetSlice;
            selectedColumn = targetColumn;
        } else {
            // action
            if (HexGrid.isAdjacent(selectedSlice, selectedColumn, targetSlice, targetColumn)) {
                moveAction(selectedSlice, selectedColumn, targetSlice, targetColumn);
            } else {
                nope();
            }
            selectedColumn = -1000;
            selectedSlice = -1000;
        }
        System.err.println("SELECT " + targetColumn + "/" + targetSlice + " from " + x + ", " + y);
    }

    private void moveAction(int selectedSlice, int selectedColumn, int targetSlice, int targetColumn) {
        System.err.println("MOVE " + selectedColumn + "/" + selectedColumn + " => " + targetSlice + "/" + targetColumn);

        Tile fromCell = tiles.get(selectedSlice, selectedColumn);
        Tile toCell = tiles.get(targetSlice, targetColumn);
        if (fromCell.owner != turn) {
            nope();
            return;
        }

        if (toCell.owner == fromCell.owner) {
            if (fromCell.value > 0) {
                ++toCell.value;
                --fromCell.value;
                swapTurn();
                SFX.TRANSFER.play();
            }
            return;
        } else if (toCell.owner == Ownership.NEUTRAL) {
            if (toCell.value >= fromCell.value) {
                nope();
                return;
            }

            int tmpValue = toCell.value;
            toCell.value = fromCell.value;
            fromCell.value = tmpValue;

            toCell.owner = fromCell.owner;
            SFX.CAPTURE.play();
            swapTurn();
        } else {
            if (fromCell.value < toCell.value + combatCost) {
                nope();
                return;
            }

            fromCell.value -= combatCost;

            int tmpValue = toCell.value;
            toCell.value = fromCell.value;
            fromCell.value = tmpValue;

            toCell.owner = fromCell.owner;
            SFX.CAPTURE.play();
            swapTurn();
        }
    }

    private void waitAction() {
        SFX.SKIP.play();
        swapTurn();
    }

    private void swapTurn() {
        if (turn == Ownership.RED) {
            turn = Ownership.BLUE;
            blueThinkingTime = Math.abs(rng.nextGaussian() * AI_THINKING_TIME);
        } else {
            turn = Ownership.RED;
        }
    }

    private void doAITurn(Ownership player) {
        // really smart AI: pick a blue cell at random, then a target at random
        List<HexGrid.Entry<Tile>> candidateSources = new ArrayList<HexGrid.Entry<Tile>>();
        for (HexGrid.Entry<Tile> entry : tiles) {
            if (entry.value.owner == player) {
                for (int i = 0; i < entry.value.value + 1; ++i) {
                    candidateSources.add(entry);
                }
            }
        }

        boolean isPreparedToTransfer = rng.nextDouble() < 0.3;

        if (candidateSources.isEmpty()) {
            waitAction();
            return;
        }

        if (candidateSources.size() < 4) {
            if (rng.nextDouble() < 0.01) {
                waitAction();
                return;
            }
        }

        int index = rng.nextInt(candidateSources.size());
        HexGrid.Entry<Tile> source = candidateSources.get(index);

        List<HexGrid.Entry<Tile>> candidateTargets = new ArrayList<HexGrid.Entry<Tile>>();
        for (HexGrid.Entry<Tile> entry : tiles) {
            if ((entry.value.owner != player || isPreparedToTransfer) && entry.value.value <= source.value.value && HexGrid.isAdjacent(source.slice, source.column, entry.slice, entry.column)) {
                candidateTargets.add(entry);
            }
        }

        if (candidateTargets.isEmpty()) {
            if (rng.nextDouble() < 0.05) {
                waitAction();
            }
            return;
        }

        index = rng.nextInt(candidateTargets.size());
        HexGrid.Entry<Tile> target = candidateTargets.get(index);

        moveAction(source.slice, source.column, target.slice, target.column);
    }

    private boolean hasSelection() {
        return !isOutsideWorld(selectedSlice, selectedColumn);
    }

    private boolean isOutsideWorld(int slice, int column) {
        return tiles.get(slice, column) == null;
    }

    @Override
    public boolean usesCenteredCamera() {
        return false;
    }

    private interface CoordReceiver {
        public void receive(float x1, float y1, float x2, float y2, float xc, float yc);
    }
}
