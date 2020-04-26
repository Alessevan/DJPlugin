package fr.bakaaless.DJPlugin.entities.animations;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Animations;
import fr.bakaaless.DJPlugin.utils.Colors;
import fr.bakaaless.DJPlugin.utils.VectorUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiscoBall extends IAnimations {

    private Location location;

    private int particles;

    private int radius;

    private Double ground;

    public DiscoBall(final DjEntity djEntity) {
        super(djEntity);
        this.animationType = Animations.DISCOBALL;
    }

    @Override
    public void start() {
        super.start();
        this.particles = 20;
        this.radius = 4;
        Vector nv = this.getDjEntity().getDj().get().getLocation().getDirection().normalize();
        nv.setX(nv.getX() * 6.5D);
        nv.setY(0);
        nv.setZ(nv.getZ() * 6.5D);
        this.location = this.getDjEntity().getDj().get().getLocation().clone().add(nv).getBlock().getLocation().add(0.5, 0, 0.5);
        this.ground = this.getGround(this.location);
        this.location.setY(this.ground + 6);
        this.location.getBlock().setType(Material.BEACON);
    }

    @Override
    public void progress() {
        if (this.step % 5 == 0) {
            final List<Location> locationList = new ArrayList<>();
            for (double theta = 0; theta < 2 * Math.PI; theta += Math.PI / 10) {
                final double x = this.radius * Math.cos(theta);
                for (double thetaz = 0; thetaz < 2 * Math.PI; thetaz += Math.PI / 10) {
                    final double z = this.radius * Math.sin(thetaz);
                    locationList.add(this.location.clone().add(x, this.ground - this.location.getY(), z));
                }
            }
            final Location start = this.location.clone();
            final Location end = locationList.get(new Random().nextInt(locationList.size())).clone();
            final Vector vector = VectorUtils.from(start, end);
            final int[] rgb = Colors.getRGBFromValue(Colors.values()[new Random().nextInt(Colors.values().length)].getValue());
            for (int progress = 1; progress <= this.particles; progress++) {
                this.location.getWorld().spawnParticle(Particle.REDSTONE, this.location.clone().add(progress * vector.getX() / this.particles, progress * vector.getY() / this.particles, progress * vector.getZ() / this.particles), 1, new Particle.DustOptions(Color.fromRGB(rgb[0], rgb[1], rgb[2]), 1));
            }
        }
        super.progress();
    }

    @Override
    public void stop() {
        super.stop();
        this.location.getBlock().setType(Material.AIR);
    }

    private Double getGround(final Location start) {
        final Location location = start.clone();
        if (!location.getBlock().getType().equals(Material.AIR)) {
            while (!location.getBlock().getType().equals(Material.AIR)) {
                location.add(0D, 1D, 0D);
            }
            location.add(0D, -1D, 0D);
        } else {
            while (location.getBlock().getType().equals(Material.AIR)) {
                location.add(0D, -1D, 0D);
            }
        }
        return location.getY();
    }
}
