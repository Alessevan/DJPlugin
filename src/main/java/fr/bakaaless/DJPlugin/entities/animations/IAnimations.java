package fr.bakaaless.DJPlugin.entities.animations;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;

public abstract class IAnimations {

    public DjEntity djEntity;

    public Animations animationType;

    public void start() {
    }

    public void progress() {
    }

    public void stop() {
    }

    public DjEntity getDjEntity() {
        return this.djEntity;
    }

    public Animations getAnimationType() {
        return this.animationType;
    }

}
