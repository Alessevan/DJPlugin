package fr.bakaaless.DJPlugin.entities.animations.choregraphies;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class HandsUp extends Choreography {

    public HandsUp(final DjEntity djEntity) {
        super(djEntity);
        this.animationType = Animations.HANDSUP;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void progress() {
        super.progress(2);
        final ArmorStand armorStand = this.getDjEntity().getDj().get();
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(-60.0), Math.toRadians(this.timer + 50), -5.0));
        armorStand.setLeftArmPose(new EulerAngle(Math.toRadians(-(this.timer + 90)), Math.toRadians(this.timer - 50), 0.0));
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(this.timer), Math.toRadians(10.0), 0.0));
    }

    @Override
    public void stop() {
        super.stop();
    }

}
