package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public final class SnowParticles {
    private final float[] decayRates;
    private final float[] radii;
    private final float[] phases;
    private final float[] angularVelocities;

    public SnowParticles(int nParticles, float radius, float lifetime) {
        decayRates = new float[nParticles];
        radii = new float[nParticles];
        phases = new float[nParticles];
        angularVelocities = new float[nParticles];

        Random rng = new Random();
        for (int i = 0; i < nParticles; ++i) {
            decayRates[i] = lifetime / rng.nextFloat();
            radii[i] = (float)Math.exp(rng.nextGaussian() + Math.log(radius));
            phases[i] = 2.0f * (float)Math.PI * rng.nextFloat();
            angularVelocities[i] = 1.0f / radii[i] + (float)Math.exp(rng.nextGaussian());
        }
    }

    public void render(ShapeRenderer renderer, double time) {
        for (int i = 0; i < decayRates.length; ++i) {
            float alpha = (float)Math.exp(-time / decayRates[i]);
            renderer.setColor(1.0f, 1.0f, 1.0f, alpha);
            float angle = phases[i] + decayRates[i] * angularVelocities[i] * alpha;
            float radius = radii[i];
            float x = radius * (float)Math.cos(angle);
            float y = radius * (float)Math.sin(angle);
            renderer.point(x, y, 0.0f);
        }
    }
}
