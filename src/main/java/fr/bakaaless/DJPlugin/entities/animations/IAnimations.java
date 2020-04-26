package fr.bakaaless.DJPlugin.entities.animations;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;

public abstract class IAnimations {

    public DjEntity djEntity;

    public Animations animationType;

    protected int step;

    public IAnimations(final DjEntity djEntity) {
        this.djEntity = djEntity;
    }

    public void start() {
        this.step = 0;
    }

    public void progress() {
        this.step++;
    }

    public void stop() {
        this.step = 0;
    }

    public DjEntity getDjEntity() {
        return this.djEntity;
    }

    public Animations getAnimationType() {
        return this.animationType;
    }

}
