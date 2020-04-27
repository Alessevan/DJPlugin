package fr.bakaaless.DJPlugin.entities.animations.choregraphies;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.entities.animations.IAnimations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public abstract class Choreography extends IAnimations {

    protected boolean loop;
    protected int timer;

    public Choreography(DjEntity djEntity) {
        super(djEntity);
    }

    @Override
    public void start() {
        super.start();
        this.loop = false;
        this.timer = 0;
    }

    public void progress(final int progress) {
        super.progress();
        if (!this.loop) {
            this.timer += progress;
            if (this.timer >= 20) {
                this.loop = true;
            }
        } else {
            this.timer -= progress;
            if (this.timer <= 5) {
                this.loop = false;
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        final ArmorStand armorStand = this.getDjEntity().getDj().get();
        armorStand.setLeftArmPose(new EulerAngle(0D, 0D, 0D));
        armorStand.setRightArmPose(new EulerAngle(0D, 0D, 0D));
        armorStand.setHeadPose(new EulerAngle(0D, 0D, 0D));
    }
}
