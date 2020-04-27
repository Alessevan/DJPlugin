package fr.bakaaless.DJPlugin.utils;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.entities.animations.DiscoBall;
import fr.bakaaless.DJPlugin.entities.animations.IAnimations;
import fr.bakaaless.DJPlugin.entities.animations.Rainbow;
import fr.bakaaless.DJPlugin.entities.animations.choregraphies.*;
import fr.bakaaless.DJPlugin.plugin.DjPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Consumer;

public class InventoryUtils implements Listener {

    public static List<Material> disks;

    private final String title;
    private final int rows;
    private final Map<Integer, ItemStack> items;
    private final Map<ItemStack, Consumer<InventoryClickEvent>> actions;
    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)
    private boolean cancelled;
    private Optional<Inventory> inventory;

    static {
        DjPlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(DjPlugin.getInstance(), () -> {
            disks = new ArrayList<>();
            for (Material material : Material.values()) {
                if(material.toString().contains("MUSIC_DISC_"))
                    disks.add(material);
            }
        });
    }

    private InventoryUtils(final String title, final int rows){
        this.title = title;
        this.rows = rows;
        this.items = new HashMap<>();
        this.actions = new HashMap<>();
        this.cancelled = false;
        this.inventory = Optional.empty();
        DjPlugin.getPlugin(DjPlugin.class).getServer().getPluginManager().registerEvents(this, DjPlugin.getInstance());
    }

    public static InventoryUtils create(final String title, final int rows){
        return new InventoryUtils(title, rows);
    }

    public InventoryUtils setItem(final Integer slot, final ItemStack itemStack){
        if(this.items.containsKey(slot)){
            this.actions.remove(this.items.get(slot));
            this.items.remove(slot);
        }
        this.items.put(slot, itemStack);
        return this;
    }

    public InventoryUtils setItem(final Integer slot, final ItemStack itemStack, final Consumer<InventoryClickEvent> consumer){
        if(this.items.containsKey(slot)){
            this.actions.remove(this.items.get(slot));
            this.items.remove(slot);
        }
        this.items.put(slot, itemStack);
        if(!this.actions.containsKey(itemStack)) this.actions.put(itemStack, consumer);
        return this;
    }

    public InventoryUtils drawDj(){
        final ItemStack blackGlass = new ItemUtils().ItemStack(Material.BLACK_STAINED_GLASS_PANE, Optional.of("§f"), Optional.empty(), Optional.of(1));
        for(int slot = 0; slot < 45; slot++){
            this.setItem(slot, blackGlass, inventoryClickEvent -> inventoryClickEvent.getWhoClicked().closeInventory());
        }
        for(int slot = 0; slot < 45; slot++) {
            final ItemStack redGlass = new ItemUtils().ItemStack(Material.RED_STAINED_GLASS_PANE, Optional.of("§f"), Optional.empty(), Optional.of(1));
            if(slot <= 1 || slot >= 7 && slot <= 9 || slot == 17 || slot == 27 || slot >= 35 && slot <= 37 || slot >= 43){
                this.setItem(slot, redGlass, inventoryClickEvent -> inventoryClickEvent.getWhoClicked().closeInventory());
            }
        }
        for(int slot = 10; slot < 36; slot++) {
            if(slot != 17 && slot != 18 && slot != 26 && slot != 27 && slot != 35){
                this.setItem(slot, new ItemStack(Material.AIR));
            }
        }
        this.setCancelled(true);
        return this;
    }

    public static void drawMusic(final Player player, final int page){
        final InventoryUtils inventoryUtils = InventoryUtils.create("&3&lDJ Station &8&l» &7Musique &8&l» &7Page " + page, 5)
                .drawDj();
        if(page <= 1) inventoryUtils.setItem(38, new ItemUtils().SkullLocal("§6§lPage précédente", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ=="), inventoryClickEvent -> DjEntity.DjMenu(player));
        else inventoryUtils.setItem(38, new ItemUtils().SkullLocal("§6§lPage précédente", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ=="), inventoryClickEvent -> InventoryUtils.drawMusic(player, page - 1));
        int index = (page - 1) * 21;
        for(int slot = 10; slot < 36; slot++) {
            if(slot != 17 && slot != 18 && slot != 26 && slot != 27 && slot != 35){
                if(DjPlugin.getInstance().getSongs().size() <= index) break;
                final ItemStack itemStack = new ItemStack(disks.get(new Random().nextInt(disks.size())));
                final ItemMeta itemMeta = itemStack.getItemMeta();
                assert itemMeta != null;
                final String title = DjPlugin.getInstance().getSongs().get(index).getTitle();
                itemMeta.setDisplayName("§f" + DjPlugin.getInstance().getSongs().get(index).getTitle());
                itemStack.setItemMeta(itemMeta);
                inventoryUtils.setItem(slot, itemStack, inventoryClickEvent -> {
                    player.closeInventory();
                    if(DjPlugin.getInstance().getDjEntity(player).isPresent()) {
                        DjPlugin.getInstance().getDjEntity(player).get().startSound(title);
                    }
                });
                index++;
            }
        }
        if(index < DjPlugin.getInstance().getSongs().size())
            inventoryUtils.setItem(42, new ItemUtils().SkullLocal("§6§lPage suivante", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJmOTEwYzQ3ZGEwNDJlNGFhMjhhZjZjYzgxY2Y0OGFjNmNhZjM3ZGFiMzVmODhkYjk5M2FjY2I5ZGZlNTE2In19fQ=="), inventoryClickEvent -> InventoryUtils.drawMusic(player, page + 1));
        DjPlugin.getInstance().getDjEntity(player).ifPresent(djEntity -> {
            if(djEntity.getSongPlayer() != null){
                inventoryUtils.setItem(40, new ItemUtils().SkullLocal("§c§lCouper la musique", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYxMzlmZDFjNTY1NGU1NmU5ZTRlMmM4YmU3ZWIyYmQ1YjQ5OWQ2MzM2MTY2NjNmZWVlOTliNzQzNTJhZDY0In19fQ=="), inventoryClickEvent -> djEntity.stopSound());
            }
        });
        inventoryUtils.open(player);
    }

    public static void drawAnimations(final Player player){
        final InventoryUtils inventoryUtils = InventoryUtils.create("&3&lDJ Station &8&l» &7Animations", 5)
                .drawDj()
                .setItem(38, new ItemUtils().SkullLocal("§6§lPage précédente", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ=="), inventoryClickEvent -> DjEntity.DjMenu(player));
        DjPlugin.getInstance().getDjEntity(player).ifPresent(djEntity -> {
            final boolean hasRainbow = djEntity.getAnimationsProgress().parallelStream().anyMatch(iAnimations -> iAnimations.getAnimationType().equals(Animations.RAINBOW));
            final boolean hasDiscoBall = djEntity.getAnimationsProgress().parallelStream().anyMatch(iAnimations -> iAnimations.getAnimationType().equals(Animations.DISCOBALL));
            inventoryUtils.setItem(20, new ItemUtils().ItemStack(Material.BOOK, Optional.of("§4A§cr§6c§7-§ee§an§7-§2C§9i§1e§5l"), Optional.empty(), Optional.of(1), hasRainbow), inventoryClickEvent -> {
                player.closeInventory();
                if (hasRainbow) {
                    final Iterator<IAnimations> it = djEntity.getAnimationsProgress().parallelStream().filter(IAnimations -> IAnimations.getClass() == Rainbow.class).iterator();
                    while (it.hasNext()) {
                        final IAnimations iAnimations = it.next();
                        iAnimations.stop();
                        djEntity.getAnimationsProgress().remove(iAnimations);
                    }
                } else {
                    djEntity.addAnimations(new Rainbow(djEntity));
                }
            });
            inventoryUtils.setItem(22, new ItemUtils().ItemStack(Material.BEACON, Optional.of("§6Boule DISCO"), Optional.empty(), Optional.of(1), hasDiscoBall), inventoryClickEvent -> {
                player.closeInventory();
                if (hasDiscoBall) {
                    final Iterator<IAnimations> it = djEntity.getAnimationsProgress().parallelStream().filter(IAnimations -> IAnimations.getClass() == DiscoBall.class).iterator();
                    while (it.hasNext()) {
                        final IAnimations iAnimations = it.next();
                        iAnimations.stop();
                        djEntity.getAnimationsProgress().remove(iAnimations);
                    }
                } else {
                    djEntity.addAnimations(new DiscoBall(djEntity));
                }
            });
            inventoryUtils.setItem(22, new ItemUtils().ItemStack(Material.ARMOR_STAND, Optional.of("§6Chorégraphies"), Optional.empty(), Optional.of(1)), inventoryClickEvent -> {
                InventoryUtils.drawChoreography(player);
            });
            if (djEntity.getDancer1().isPresent() && djEntity.getDancer2().isPresent())
                inventoryUtils.setItem(24, new ItemUtils().ItemStack(Material.SPAWNER, Optional.of("§7Danseurs"), Optional.empty(), Optional.of(1)), inventoryClickEvent -> InventoryUtils.drawDancers(player));
            if (djEntity.isAnimated())
                inventoryUtils.setItem(40, new ItemUtils().ItemStack(Material.BARRIER, Optional.of("§c§lEnlever les animations"), Optional.empty(), Optional.empty()), inventoryClickEvent -> {
                    player.closeInventory();
                    djEntity.stopAnimate();
                });
        });
        inventoryUtils.open(player);
    }

    public static void drawDancers(final Player player){
        final InventoryUtils inventoryUtils = InventoryUtils.create("&3&lDJ Station &8&l» &7Animations &8&l» &7Danseurs", 5)
                .drawDj()
                .setItem(38, new ItemUtils().SkullLocal("§6§lPage précédente", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ=="), inventoryClickEvent -> InventoryUtils.drawAnimations(player));
        DjPlugin.getInstance().getDjEntity(player).ifPresent(djEntity -> {
            int slot = 0;
            if(djEntity.getDancer1().get().getWorld().getDifficulty().ordinal() >= 1 && djEntity.getDancer2().get().getWorld().getDifficulty().ordinal() >= 1){
                inventoryUtils.setItem(10 + slot, new ItemUtils().ItemStack(Material.BLAZE_SPAWN_EGG, Optional.of("§7Blaze"), Optional.empty(), Optional.empty()), inventoryClickEvent -> {
                    player.closeInventory();
                    djEntity.setEntities(EntityType.BLAZE);
                });
                slot++;
            }
            inventoryUtils.setItem(10 + slot, new ItemUtils().ItemStack(Material.SHEEP_SPAWN_EGG, Optional.of("§7Mouton"), Optional.empty(), Optional.empty()), inventoryClickEvent -> {
                    player.closeInventory();
                djEntity.setEntities(EntityType.SHEEP);
            });
            slot++;
            if (djEntity.hasDancers())
                inventoryUtils.setItem(40, new ItemUtils().ItemStack(Material.BARRIER, Optional.of("§c§lEnlever les danseurs"), Optional.empty(), Optional.empty()), inventoryClickEvent -> {
                    player.closeInventory();
                    djEntity.removeEntity();
                });
        });
        inventoryUtils.open(player);
    }

    public static void drawChoreography(final Player player) {
        final InventoryUtils inventoryUtils = InventoryUtils.create("&3&lDJ Station &8&l» &7Animations &8&l» &7Chorégraphie", 5)
                .drawDj()
                .setItem(38, new ItemUtils().SkullLocal("§6§lPage précédente", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ=="), inventoryClickEvent -> InventoryUtils.drawAnimations(player));
        DjPlugin.getInstance().getDjEntity(player).ifPresent(djEntity -> {
            final boolean hasCrazy = djEntity.getAnimationsProgress().parallelStream().anyMatch(iAnimations -> iAnimations.getAnimationType().equals(Animations.CRAZY));
            inventoryUtils.setItem(10, new ItemUtils().SkullLocal("§6Fou", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFiZWNhY2VjODU4YTE5ODQ4ODYwYjI1MjJjMzI5MzZhZGYxNTg4YzFlZjQyMzc5NDAzZmVmODNkYTRiYiJ9fX0="), inventoryClickEvent -> {
                player.closeInventory();
                if (hasCrazy) {
                    final Iterator<IAnimations> it = djEntity.getAnimationsProgress().parallelStream().filter(IAnimations -> IAnimations.getClass() == Crazy.class).iterator();
                    while (it.hasNext()) {
                        final IAnimations iAnimations = it.next();
                        iAnimations.stop();
                        djEntity.getAnimationsProgress().remove(iAnimations);
                    }
                } else
                    djEntity.addAnimations(new Crazy(djEntity));
            });
            final boolean hasHandsUp = djEntity.getAnimationsProgress().parallelStream().anyMatch(iAnimations -> iAnimations.getAnimationType().equals(Animations.HANDSUP));
            inventoryUtils.setItem(11, new ItemUtils().SkullLocal("§6Mains en l'air", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzYzNmYyNzI0YWE2YWE0ZGU3YWM0NmMxOWYzYzg0NWZiMTQ4NDdhNTE4YzhmN2UwM2Q3OTJjODJlZmZiMSJ9fX0="), inventoryClickEvent -> {
                player.closeInventory();
                if (hasHandsUp) {
                    final Iterator<IAnimations> it = djEntity.getAnimationsProgress().parallelStream().filter(IAnimations -> IAnimations.getClass() == HandsUp.class).iterator();
                    while (it.hasNext()) {
                        final IAnimations iAnimations = it.next();
                        iAnimations.stop();
                        djEntity.getAnimationsProgress().remove(iAnimations);
                    }
                } else
                    djEntity.addAnimations(new HandsUp(djEntity));
            });
            final boolean hasJump = djEntity.getAnimationsProgress().parallelStream().anyMatch(iAnimations -> iAnimations.getAnimationType().equals(Animations.JUMP));
            inventoryUtils.setItem(12, new ItemUtils().SkullLocal("§6Sauts", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2JhYWJlNzI0ZWFlNTljNWQxM2Y0NDJjN2RjNWQyYjFjNmI3MGMyZjgzMzY0YTQ4OGNlNTk3M2FlODBiNGMzIn19fQ=="), inventoryClickEvent -> {
                player.closeInventory();
                if (hasJump) {
                    final Iterator<IAnimations> it = djEntity.getAnimationsProgress().parallelStream().filter(IAnimations -> IAnimations.getClass() == Jump.class).iterator();
                    while (it.hasNext()) {
                        final IAnimations iAnimations = it.next();
                        iAnimations.stop();
                        djEntity.getAnimationsProgress().remove(iAnimations);
                    }
                } else
                    djEntity.addAnimations(new Jump(djEntity));
            });
            final boolean hasScratch = djEntity.getAnimationsProgress().parallelStream().anyMatch(iAnimations -> iAnimations.getAnimationType().equals(Animations.SCRATCH));
            inventoryUtils.setItem(13, new ItemUtils().SkullLocal("§6Scratch", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDRjOGNkMzFiYWM2NTdhM2YyNmI1MmUyNjJjODNlYjgzMzhkNTY2NTFmZDIyYzE2MzM1NjVmM2RiYmM0NTc3NyJ9fX0="), inventoryClickEvent -> {
                player.closeInventory();
                if (hasJump) {
                    final Iterator<IAnimations> it = djEntity.getAnimationsProgress().parallelStream().filter(IAnimations -> IAnimations.getClass() == Scratch.class).iterator();
                    while (it.hasNext()) {
                        final IAnimations iAnimations = it.next();
                        iAnimations.stop();
                        djEntity.getAnimationsProgress().remove(iAnimations);
                    }
                } else
                    djEntity.addAnimations(new Scratch(djEntity));
            });
            final boolean hasSky = djEntity.getAnimationsProgress().parallelStream().anyMatch(iAnimations -> iAnimations.getAnimationType().equals(Animations.SKY));
            inventoryUtils.setItem(14, new ItemUtils().SkullLocal("§6Mains dans le ciel", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZmNDlhODY0NWRkNjhiNjhmMGJhZmEyMjY1YmFkOTMyZjc1MWM1ODRlNGY3MzFmZDcxNzNhNWU1YjI2ZDMifX19"), inventoryClickEvent -> {
                player.closeInventory();
                if (hasSky) {
                    final Iterator<IAnimations> it = djEntity.getAnimationsProgress().parallelStream().filter(IAnimations -> IAnimations.getClass() == Sky.class).iterator();
                    while (it.hasNext()) {
                        final IAnimations iAnimations = it.next();
                        iAnimations.stop();
                        djEntity.getAnimationsProgress().remove(iAnimations);
                    }
                } else
                    djEntity.addAnimations(new Sky(djEntity));
            });
            final boolean hasSlowDance = djEntity.getAnimationsProgress().parallelStream().anyMatch(iAnimations -> iAnimations.getAnimationType().equals(Animations.SlOWDANCE));
            inventoryUtils.setItem(15, new ItemUtils().SkullLocal("§6Slow", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzNiNGYzYjNjZWY2ODE3ZTcxZmViMGRhNDc5NDI1ZWI5ZDc5ZjFlZmQ1YWViNWFkN2M5ZTI2MTZiMTI0Y2U4NCJ9fX0="), inventoryClickEvent -> {
                player.closeInventory();
                if (hasSlowDance) {
                    final Iterator<IAnimations> it = djEntity.getAnimationsProgress().parallelStream().filter(IAnimations -> IAnimations.getClass() == SlowDance.class).iterator();
                    while (it.hasNext()) {
                        final IAnimations iAnimations = it.next();
                        iAnimations.stop();
                        djEntity.getAnimationsProgress().remove(iAnimations);
                    }
                } else
                    djEntity.addAnimations(new SlowDance(djEntity));
            });
            if (djEntity.hasChoreography())
                inventoryUtils.setItem(40, new ItemUtils().ItemStack(Material.BARRIER, Optional.of("§c§lEnlever la chorégraphie"), Optional.empty(), Optional.empty()), inventoryClickEvent -> {
                    player.closeInventory();
                    djEntity.stopChoreography();
                });
        });
        inventoryUtils.open(player);
    }

    public void open(final Player player) {
        final Inventory inventory = DjPlugin.getPlugin(DjPlugin.class).getServer().createInventory(null, 9 * rows, ChatColor.translateAlternateColorCodes('&', title));
        for (final Integer slot : this.items.keySet()) {
            inventory.setItem(slot, this.items.get(slot));
        }
        this.inventory = Optional.of(inventory);
        player.openInventory(inventory);
    }

    @EventHandler
    public void inventoryAction(final InventoryClickEvent e){
        e.setCancelled(this.isCancelled());
        if(e.getCurrentItem() == null) return;
        if(!this.inventory.equals(Optional.of(e.getView().getTopInventory())))
            return;
        if(this.items.containsKey(e.getSlot())){
            final ItemStack itemStack = this.items.get(e.getSlot());
            if(this.actions.containsKey(itemStack)){
                this.actions.get(itemStack).accept(e);
            }
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent e){
        if(e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',  title))){
            InventoryClickEvent.getHandlerList().unregister(this);
            InventoryCloseEvent.getHandlerList().unregister(this);
        }
    }
}
