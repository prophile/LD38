package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public final class EraseParticles {
    private final float[] decayRates;
    private final float[] radii;
    private final float[] phases;
    private final float[] angularVelocities;

    public EraseParticles(int nParticles, float radius, float lifetime) {
        decayRates = new float[nParticles];
        radii = new float[nParticles];
        phases = new float[nParticles];
        angularVelocities = new float[nParticles];

        Random rng = new Random();
        for (int i = 0; i < nParticles; ++i) {
            decayRates[i] = lifetime / rng.nextFloat();
            radii[i] = (float)Math.exp(rng.nextGaussian() + Math.log(radius));
            phases[i] = 2.0f * (float)Math.PI * rng.nextFloat();
            angularVelocities[i] = 3f / radii[i] + (float)Math.exp(rng.nextGaussian());
        }
    }

    public void render(SpriteBatch spriteBatch, double time) {
        for (int i = 0; i < decayRates.length; ++i) {
            float alpha = (float) Math.exp(-time / decayRates[i]);
            float angle = phases[i] + decayRates[i] * angularVelocities[i] * alpha;
            float radius = radii[i];
            float x = radius * (float) Math.cos(angle);
            float y = radius * (float) Math.sin(angle);
            float rot = (float)Math.toDegrees(radius + alpha);


            spriteBatch.draw(Overlord.s_instance.assetManager.get("eraser.png", Texture.class), x, y, 0.1f, 0.1f, 0.3f, 0.3f, 2, 2, rot, 0, 0, 256, 256, false, false
            );

        }
    }
}
