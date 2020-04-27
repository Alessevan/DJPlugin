package fr.bakaaless.DJPlugin.entities.animations.choregraphies;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class SlowDance extends Choreography {

    public SlowDance(final DjEntity djEntity) {
        super(djEntity);
        this.animationType = Animations.SKY;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void progress() {
        super.progress(1);
        final ArmorStand armorStand = this.getDjEntity().getDj().get();
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(this.timer + 10), 0.0, 0.0));
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(-90.0), Math.toRadians(this.timer + 20), 0.0));
        armorStand.setLeftArmPose(new EulerAngle(Math.toRadians(-90.0), Math.toRadians(this.timer - 20), 0.0));
    }

    @Override
    public void stop() {
        super.stop();
    }
}
