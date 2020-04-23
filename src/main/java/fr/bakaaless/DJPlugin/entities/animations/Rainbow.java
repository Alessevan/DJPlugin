package fr.bakaaless.DJPlugin.entities.animations;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;
import fr.bakaaless.DJPlugin.utils.Colors;
import fr.bakaaless.DJPlugin.utils.VectorUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class Rainbow extends IAnimations {

    double twoPI;
    double radius;
    int particles;

    public Rainbow(final DjEntity djEntity) {
        this.djEntity = djEntity;
        this.animationType = Animations.RAINBOW;
    }


    @Override
    public void start() {
        this.twoPI = Math.PI;
        this.radius = 4D;
        this.particles = 12;
    }

    @Override
    public void progress() {
        Vector nv = this.getDjEntity().getDj().get().getLocation().getDirection().normalize();
        nv.setY(0);
        Vector ya = VectorUtils.perp(nv, new Vector(0, 1, 0)).normalize();
        Vector xa = ya.getCrossProduct(nv).normalize();
        nv.multiply(-1);
        for (int c = 0; c < 8; c++) {
            for (double theta = 0; theta < particles; theta += twoPI / particles) {
                double angle = twoPI * theta / particles;
                double ax = Math.cos(angle) * (radius - c / 5f);
                double az = Math.sin(angle) * (radius - c / 5f);
                double zb = 0;
                double xi = xa.getX() * ax + ya.getX() * az + nv.getX() * zb;
                double yi = xa.getY() * ax + ya.getY() * az + nv.getY() * zb;
                double zi = xa.getZ() * ax + ya.getZ() * az + nv.getZ() * zb;
                final Location location = this.getDjEntity().getDj().get().getLocation().clone().add(new Vector(xi, yi, zi));
                final int[] rgb = Colors.getRGBFromValue(c);
                location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(Color.fromRGB(rgb[0], rgb[1], rgb[2]), 1));
            }
        }
    }
}
