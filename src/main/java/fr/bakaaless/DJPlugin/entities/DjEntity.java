package fr.bakaaless.DJPlugin.entities;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import fr.bakaaless.DJPlugin.plugin.DjPlugin;
import fr.bakaaless.DJPlugin.utils.*;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.*;

public class DjEntity {

    @Getter
    private final int id;
    @Getter
    private DjPlugin main;

    @Getter
    private Optional<ArmorStand> dj;

    @Getter
    private Optional<ArmorStand> head;

    @Getter
    private Optional<Location> jukebox;

    @Getter
    private Optional<Location> dancer1;

    @Getter
    private Optional<Location> dancer2;

    @Getter
    private Optional<Player> player;
    @Getter
    private Entity[] entities;

    @Getter
    private PositionSongPlayer songPlayer;

    @Getter
    private List<Animations> animations;

    private int task;

    public DjEntity(final int id) {
        this.id = id;
        this.dj = Optional.empty();
        this.head = Optional.empty();
        this.jukebox = Optional.empty();
        this.dancer1 = Optional.empty();
        this.dancer2 = Optional.empty();
        this.setup();
    }

    public DjEntity(final int id, final Optional<ArmorStand> djEntity, final Optional<ArmorStand> head, final Optional<Location> jukebox, final Optional<Location> dancer1, final Optional<Location> dancer2) {
        this.id = id;
        this.dj = djEntity;
        this.head = head;
        this.jukebox = jukebox;
        this.dancer1 = dancer1;
        this.dancer2 = dancer2;
        this.setup();
    }

    private void setup() {
        this.main = DjPlugin.getInstance();
        this.player = Optional.empty();
        this.entities = new Entity[2];
        this.songPlayer = null;
        this.animations = new ArrayList<>();
        this.task = -1;
    }


    public void setDjEntity(final Optional<ArmorStand> dj) {
        this.dj = dj;
    }

    public void setHead(final Optional<ArmorStand> head) {
        this.head = head;
    }

    public void setJukebox(final Optional<Location> jukebox) {
        this.jukebox = jukebox;
    }

    public void setDancer1(final Optional<Location> dancer1) {
        this.dancer1 = dancer1;
    }

    public void setDancer2(final Optional<Location> dancer2) {
        this.dancer2 = dancer2;
    }

    public void setPlayer(final Player player) {
        this.player = Optional.of(player);
        this.getHead().ifPresent(armorStand -> {
            armorStand.getEquipment().setHelmet(new ItemUtils().skullPlayer(player));
            armorStand.setCustomName("§5§l" + player.getName());
            armorStand.setCustomNameVisible(true);
        });
        this.getDj().ifPresent(armorStand -> {
            armorStand.getEquipment().setHelmet(new ItemUtils().SkullLocal("", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjlhZDE2NmQ3OTNhZjQzZGE1MjJjMzgxZTI4ZTc4OTY2YjYzMjAzMmU1M2QyMGJkOGEzYmFhNGMwNzZhZThhIn19fQ=="));
            armorStand.getEquipment().setChestplate(new ItemUtils().getColorArmor(Material.LEATHER_CHESTPLATE, Color.BLACK));
            armorStand.getEquipment().setLeggings(new ItemUtils().getColorArmor(Material.LEATHER_LEGGINGS, Color.BLACK));
            armorStand.getEquipment().setBoots(new ItemUtils().getColorArmor(Material.LEATHER_BOOTS, Color.BLACK));
            armorStand.setCustomNameVisible(false);
        });
        this.start();
    }

    public void removePlayer() {
        this.getPlayer().ifPresent(player -> {
            this.player = Optional.empty();
            this.stop();
        });
    }

    public void newDj(final Player player){
        newDj(player.getLocation());
        Message.create("&3&lDJ Station &8&l» &7Le DJ a été placé.")
                .sendMessage(player);
    }

    public void newDj(final Location location){
        final ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.getEquipment().setHelmet(new ItemUtils().SkullLocal("", "ZjRlYTJkNmY5MzlmZWZlZmY1ZDEyMmU2M2RkMjZmYThhNDI3ZGY5MGIyOTI4YmMxZmE4OWE4MjUyYTdlIn19fQ=="));
        armorStand.getEquipment().setChestplate(new ItemUtils().getColorArmor(Material.LEATHER_CHESTPLATE, Color.SILVER));
        armorStand.getEquipment().setLeggings(new ItemUtils().getColorArmor(Material.LEATHER_LEGGINGS, Color.SILVER));
        armorStand.getEquipment().setBoots(new ItemUtils().getColorArmor(Material.LEATHER_BOOTS, Color.SILVER));
        armorStand.setArms(true);
        armorStand.setCollidable(false);
        armorStand.setCanPickupItems(false);
        armorStand.setBasePlate(false);
        armorStand.setSilent(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setCustomName("§a§lClique pour devenir §2§lDJ");
        armorStand.setCustomNameVisible(true);
        this.setDjEntity(Optional.of(armorStand));
    }

    public void newHead(final Player player){
        newHead(player.getLocation().clone().add(0, -1, 0));
        Message.create("&3&lDJ Station &8&l» &7La tête a été placée.")
                .sendMessage(player);
    }

    public void newHead(final Location location){
        final ArmorStand armorStand = location.getWorld().spawn(location.clone(), ArmorStand.class);
        armorStand.getEquipment().setHelmet(new ItemUtils().SkullLocal("", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzlmMjE5NGUxNTE1MWUyNTMxMGVlNTU2YTNmZDQ5YTNjMjRjYWZkMjEyNDcwODUzZTI2MWQyODBiYjBkN2RkMCJ9fX0="));
        armorStand.setCollidable(false);
        armorStand.setCanPickupItems(false);
        armorStand.setBasePlate(false);
        armorStand.setSilent(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(false);
        this.setHead(Optional.of(armorStand));
    }

    public void save(){
        final FileManager fileManager = this.getMain().getFileManager();
        if(this.getDj().isPresent()) fileManager.setLine("data", "instances." + this.getId() + ".dj", this.getDj().get().getUniqueId().toString());
        if(this.getHead().isPresent()) fileManager.setLine("data", "instances." + this.getId() + ".head", this.getHead().get().getUniqueId().toString());
        if(this.getJukebox().isPresent()) fileManager.setLine("data", "instances." + this.getId() + ".jukebox", this.getJukebox().get());
        if(this.getDancer1().isPresent()) fileManager.setLine("data", "instances." + this.getId() + ".dancer1", this.getDancer1().get());
        if(this.getDancer2().isPresent()) fileManager.setLine("data", "instances." + this.getId() + ".dancer2", this.getDancer2().get());
    }

    public void destroy(){
        final FileManager fileManager = this.getMain().getFileManager();
        fileManager.setLine("data", "instances." + this.getId(), null);
        if(this.getDj().isPresent()) this.getDj().get().remove();
        if(this.getHead().isPresent()) this.getHead().get().remove();
        if(this.getJukebox().isPresent()) this.getJukebox().get().getBlock().setType(Material.AIR);
        if(this.getDancer1().isPresent()) {
            for(final Entity entity : this.getDancer1().get().getWorld().getNearbyEntities(this.getDancer1().get(), 1, 1, 1)) {
                if(entity.getLocation().getBlock().getLocation().equals(this.getDancer1().get().getBlock().getLocation())){
                    entity.remove();
                }
            }
        }
        if(this.getDancer2().isPresent()) {
            for (final Entity entity : this.getDancer2().get().getWorld().getNearbyEntities(this.getDancer2().get(), 1, 1, 1)) {
                if (entity.getLocation().getBlock().getLocation().equals(this.getDancer2().get().getBlock().getLocation())) {
                    entity.remove();
                }
            }
        }
    }

    public void startSound(final String title){
        this.stopSound();
        final Optional<Song> songOptional = this.getMain().getSongs().parallelStream().filter(song -> song.getTitle().toLowerCase().equals(title.toLowerCase())).findFirst();
        if(!songOptional.isPresent()) return;
        this.songPlayer = new PositionSongPlayer(songOptional.get());
        if(this.getJukebox().isPresent())this.songPlayer.setTargetLocation(this.getJukebox().get());
        else this.songPlayer.setTargetLocation(this.getDj().get().getLocation());
        this.songPlayer.setDistance(32);
        this.songPlayer.setPlaying(true);
    }

    public void stopSound(){
        if(this.songPlayer == null) return;
        this.songPlayer.setDistance(0);
        this.songPlayer.setPlaying(false);
        this.songPlayer.destroy();
        this.songPlayer = null;
    }

    public void start(){
        final Map<Integer, Integer> dancers = new HashMap<>();
        dancers.put(0, 1);
        dancers.put(1, 1);
        this.getAnimations().add(Animations.RAINBOW);
        this.task = this.getMain().getServer().getScheduler().scheduleSyncRepeatingTask(this.getMain(), () -> {
            this.getHead().ifPresent(armorStand -> {
                final Location headLoc = armorStand.getLocation().clone();
                headLoc.setYaw((armorStand.getLocation().getYaw() + 3f) % 360);
                armorStand.teleport(headLoc);
            });
            if (this.songPlayer != null) {
                for (final Player player : this.getMain().getServer().getOnlinePlayers()) {
                    this.songPlayer.removePlayer(player);
                    if (this.songPlayer.isInRange(player))
                        this.songPlayer.addPlayer(player);
                }
            }
            for (int i = 0; i < 2; i++) {
                if (this.entities.length <= i) break;
                if (this.entities[i] != null) {
                    if (this.entities[i] instanceof Blaze) {
                        this.entities[i].teleport(this.entities[i].getLocation().clone().add(0D, 0.04 * dancers.get(i), 0D));
                        if ((this.entities[i].getLocation().getY() - (i == 0 ? this.getDancer1().get().getY() : this.getDancer2().get().getY()) >= 2) || (this.entities[i].getLocation().getY() <= (i == 0 ? this.getDancer1().get().getY() : this.getDancer2().get().getY()) + 0.01)) {
                            dancers.replace(i, dancers.get(i) * (-1));
                        }
                    } else {
                        final Location location = this.entities[i].getLocation().clone();
                        location.setPitch(location.getPitch() + 2f * dancers.get(i));
                        this.entities[i].teleport(location);
                        if ((this.entities[i].getLocation().getPitch() >= 60f) || (this.entities[i].getLocation().getPitch() <= -60f)) {
                            dancers.replace(i, dancers.get(i) * (-1));
                        }
                    }
                }
            }
            for (final Animations animations : this.getAnimations()) {
                if (animations.equals(Animations.RAINBOW)) {
                    final double twoPI = Math.PI;
                    final double radius = 4D;
                    final int particles = 12;
                    Vector nv = this.getDj().get().getLocation().getDirection().normalize();
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
                            final Location location = this.getDj().get().getLocation().clone().add(new Vector(xi, yi, zi));
                            final int[] rgb = Colors.getRGBFromValue(c);
                            location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(Color.fromRGB(rgb[0], rgb[1], rgb[2]), 1));
                        }
                    }
                }
            }
        }, 0L, 1L);
    }

    public boolean isStopped(){
        return this.task == -1;
    }

    public boolean isAnimated(){
        return this.hasDancers();
    }

    public boolean hasDancers(){
        return this.entities[0] != null && this.entities[1] != null;
    }

    public void stopAnimate(){
        this.removeEntity();
        this.getAnimations().clear();
    }

    public void removeEntity(){
        for(int i = 0; i < 2; i++) {
            if(this.entities.length <= i) break;
            if (this.entities[i] != null) {
                this.entities[i].remove();
                this.entities[i] = null;
            }
        }
    }

    public void setEntities(final EntityType entityType){
        this.removeEntity();
        for(int i = 0; i < 2; i++) {
            final Location location = (i == 0 ? this.getDancer1().get() : this.getDancer2().get());
            final Entity entity = location.getWorld().spawn(location, entityType.getEntityClass());
            entity.setInvulnerable(true);
            entity.setGlowing(true);
            entity.setSilent(true);
            if(entity instanceof LivingEntity) ((LivingEntity) entity).setAI(false);
            if(entity instanceof Sheep){
                ((Sheep) entity).setSheared(false);
                ((Sheep) entity).setColor(DyeColor.values()[new Random().nextInt(DyeColor.values().length)]);
            }
            this.entities[i] = entity;
        }
    }

    public void stop(){
        if(this.isStopped()) return;
        this.getMain().getServer().getScheduler().cancelTask(this.task);
        this.getHead().ifPresent(armorStand -> {
            final Location location = armorStand.getLocation();
            armorStand.remove();
            this.newHead(location);
        });
        this.getDj().ifPresent(armorStand -> {
            final Location location = armorStand.getLocation();
            armorStand.remove();
            this.newDj(location);
        });
        this.stopSound();
        this.stopAnimate();
        this.task = -1;
    }

    public static boolean isDj(final Player player, final ArmorStand armorStand){
        final DjPlugin main = DjPlugin.getInstance();
        final Optional<DjEntity> headEntityOptional = main.getDjEntityByHead(armorStand);
        if(headEntityOptional.isPresent())
            return true;
        final Optional<DjEntity> djEntityOptional = main.getDjEntity(armorStand);
        if(djEntityOptional.isPresent()){
            if(djEntityOptional.get().getPlayer().isPresent()){
                if(djEntityOptional.get().getPlayer().get().equals(player)){
                    DjEntity.DjMenu(player);
                    return true;
                }
                Message.create("&c&lDJ Station &4&l» &cCette station est déjà occupée.")
                        .sendMessage(player);
                return true;
            }
            if(!player.hasPermission("djstation.use")){
                Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission de faire cela.")
                        .sendMessage(player);
                return true;
            }
            if(main.getExecutor().getEditPlayerDj().containsValue(djEntityOptional.get())){
                Message.create("&c&lDJ Station &4&l» &cQuelqu'un modifie cette station vous ne pouvez pas l'utiliser.")
                        .sendMessage(player);
                return true;
            }
            djEntityOptional.get().setPlayer(player);
            DjEntity.DjMenu(player);
            Message.create("&3&lDJ Station &8&l» &7Vous contrôlez maintenant cette station.")
                    .sendMessage(player);
            return true;
        }
        return false;
    }

    public static void DjMenu(final Player player){
        InventoryUtils.create("&3&lDJ Station &8&l»", 5)
                .drawDj()
                .setItem(20, new ItemUtils().ItemStack(Material.END_CRYSTAL, Optional.of("§7Animations"), Optional.empty(), Optional.of(1)), inventoryClickEvent ->
                        InventoryUtils.drawAnimations(player)
                )
                .setItem(24, new ItemUtils().ItemStack(Material.JUKEBOX, Optional.of("§7Musique"), Optional.empty(), Optional.of(1)), inventoryClickEvent ->
                        InventoryUtils.drawMusic(player, 1)
                )
                .setItem(40, new ItemUtils().ItemStack(Material.BARRIER, Optional.of("§4§lQuitter"), Optional.empty(), Optional.of(1)), inventoryClickEvent -> {
                    player.closeInventory();
                    DjPlugin.getInstance().getDjEntity(player).ifPresent(djEntity -> player.performCommand("djstation leave"));
                })
                .open(player);
    }

}
