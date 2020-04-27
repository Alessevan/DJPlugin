package fr.bakaaless.DJPlugin.entities.animations.choregraphies;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;
import fr.bakaaless.DJPlugin.utils.MathUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class Crazy extends Choreography {

    public Crazy(final DjEntity djEntity) {
        super(djEntity);
        this.animationType = Animations.CRAZY;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void progress() {
        super.progress(1);
        final ArmorStand armorStand = this.getDjEntity().getDj().get();
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(-(60 + MathUtils.randomRange(0, 20))), Math.toRadians(this.timer + 50 + MathUtils.randomRange(0, 20)), Math.toRadians(-5 + MathUtils.randomRange(0, 20))));
        armorStand.setLeftArmPose(new EulerAngle(Math.toRadians(-(this.timer + 90)), Math.toRadians(this.timer - 50), Math.toRadians(MathUtils.randomRange(0, 20))));
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(this.timer), Math.toRadians(10 + MathUtils.randomRange(0, 20)), 0.0));
    }

    @Override
    public void stop() {
        super.stop();
    }

}
