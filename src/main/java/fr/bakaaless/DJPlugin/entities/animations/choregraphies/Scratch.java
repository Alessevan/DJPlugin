package fr.bakaaless.DJPlugin.entities.animations.choregraphies;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class Scratch extends Choreography {

    public Scratch(final DjEntity djEntity) {
        super(djEntity);
        this.animationType = Animations.SCRATCH;
    }

    @Override
    public void start() {
        super.start();
        this.getDjEntity().getDj().get().setLeftArmPose(new EulerAngle(Math.toRadians(-60.0), 0.0, Math.toRadians(-30.0)));
    }

    @Override
    public void progress() {
        super.progress(2);
        final ArmorStand armorStand = this.getDjEntity().getDj().get();
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(-60.0), Math.toRadians(-this.timer), 0.0));
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(this.timer), 0.0, Math.toRadians(10.0)));
    }

    @Override
    public void stop() {
        super.stop();
    }
}
