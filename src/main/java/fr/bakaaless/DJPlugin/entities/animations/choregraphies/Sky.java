package fr.bakaaless.DJPlugin.entities.animations.choregraphies;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class Sky extends Choreography {

    public Sky(final DjEntity djEntity) {
        super(djEntity);
        this.animationType = Animations.SKY;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void progress() {
        super.progress(2);
        final ArmorStand armorStand = this.getDjEntity().getDj().get();
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(-90.0), Math.toRadians(this.timer + 50), Math.toRadians(this.timer + 50)));
        armorStand.setLeftArmPose(new EulerAngle(Math.toRadians(-90 + this.timer), Math.toRadians(this.timer + 50), Math.toRadians(this.timer + 120)));
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(this.timer), 0.0, Math.toRadians(10.0)));
    }

    @Override
    public void stop() {
        super.stop();
    }
}
