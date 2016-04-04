package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import thepaperpilot.order.Util.Constants;

public class ParticleEffectSystem extends EntitySystem {

    private ParticleEffect player;
    private ParticleEffect enemy;
    private Batch batch;
    private float time;

    public ParticleEffectSystem(Batch batch) {
        super(9);
        this.batch = batch;
    }

    public void addedToEngine (Engine engine) {
        player = new ParticleEffect();
        player.load(Gdx.files.internal("fighter.p"), Gdx.files.internal(""));
        player.setPosition(30, 0);
        player.getEmitters().first().getTint().setColors(new float[]{.12f, .69f, .03f});

        enemy = new ParticleEffect();
        enemy.load(Gdx.files.internal("fighter.p"), Gdx.files.internal(""));
        enemy.setPosition(Constants.WORLD_WIDTH - 250, 0);
        enemy.getEmitters().first().getTint().setColors(new float[]{.72f, .1f, .03f});
    }

    public void update (float deltaTime) {
        PuzzleSystem puzzle = getEngine().getSystem(PuzzleSystem.class);

        time += deltaTime;
        while (time > Constants.PARTICLE_FREQUENCY) {
            if (puzzle.turn == puzzle.player) {
                player.getEmitters().first().addParticle();
            } else if (puzzle.turn == puzzle.enemy) {
                enemy.getEmitters().first().addParticle();
            }
            time -= Constants.PARTICLE_FREQUENCY;
        }

        batch.begin();
        player.draw(batch, deltaTime);
        enemy.draw(batch, deltaTime);
        batch.end();
    }
}
