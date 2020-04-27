package fr.bakaaless.DJPlugin.entities.animations.choregraphies;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class Jump extends Choreography {

    public Jump(final DjEntity djEntity) {
        super(djEntity);
        this.animationType = Animations.JUMP;
    }

    @Override
    public void start() {
        super.start();
        this.getDjEntity().getDj().get().setGravity(true);
    }

    @Override
    public void progress() {
        super.progress(2);
        final ArmorStand armorStand = this.getDjEntity().getDj().get();
        armorStand.setLeftArmPose(new EulerAngle(Math.toRadians(-60.0), Math.toRadians(-this.timer), 0.0));
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(-(this.timer + 140)), 0.0, 0.0));
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(this.timer), 0.0, Math.toRadians(10.0)));
    }

    @Override
    public void stop() {
        super.stop();
        this.getDjEntity().getDj().get().setGravity(false);
        this.getDjEntity().getDj().get().getLocation().setY(this.getDjEntity().getDj().get().getLocation().getBlockY() - this.getDjEntity().getDj().get().getLocation().getY());
    }
}
