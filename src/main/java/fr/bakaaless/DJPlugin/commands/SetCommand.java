package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.InventoryUtils;
import fr.bakaaless.DJPlugin.utils.ItemUtils;
import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Optional;

class SetCommand {

    @Getter
    private final Executor executor;

    SetCommand(final Executor executor){
        this.executor = executor;
    }

    void handle(final Player player){
        if(!player.hasPermission("djstation.set")){
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(player);
            return;
        }
        if(!this.getExecutor().getEditPlayerDj().containsKey(player)){
            Message.create("&c&lDJ Station &4&l» &cVous devez d'abord éditer une station.")
                    .sendMessage(player);
            return;
        }
        final DjEntity djEntity = this.getExecutor().getMain().getExecutor().getEditPlayerDj().get(player);
        InventoryUtils.create("&3&lDJ Station &8&l» &7Éditeur", 3)
                .setItem(10, new ItemUtils().ItemStack(Material.ARMOR_STAND, Optional.of("§7DJ"), Optional.empty(), Optional.empty()), InventoryClickEvent -> {
                    player.closeInventory();
                    final Optional<ArmorStand> dj = djEntity.getDj();
                    dj.ifPresent(armorStand -> {
                        armorStand.remove();
                        djEntity.setDjEntity(Optional.empty());
                    });
                    djEntity.newDj(player);
                })
                .setItem(12, new ItemUtils().ItemStack(Material.JUKEBOX, Optional.of("§7JukeBox"), Optional.empty(), Optional.empty()), InventoryClickEvent -> {
                    player.closeInventory();
                    final Optional<Location> jukebox = djEntity.getJukebox();
                    jukebox.ifPresent(jukeBox -> {
                        jukeBox.getBlock().setType(Material.AIR);
                        djEntity.setJukebox(Optional.empty());
                    });
                    final Location location = player.getLocation();
                    player.teleport(location.clone().add(0, 1, 0));
                    location.getBlock().setType(Material.JUKEBOX);
                    djEntity.setJukebox(Optional.of(location));
                    Message.create("&3&lDJ Station &8&l» &7Le jukebox a été placé.")
                            .sendMessage(player);
                })
                .setItem(14, new ItemUtils().SkullLocal("§7Tête", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ1MWVlMTgxMDIwYjYyMzdiNDIzMTE4OWQ1Y2Q5Nzk0YWRhMTU3ZGY1NzE5NjQ3ZGM5YmJiMjgwMTk0NWY0OCJ9fX0="), InventoryClickEvent -> {
                    player.closeInventory();
                    final Optional<ArmorStand> head = djEntity.getHead();
                    head.ifPresent(location -> {
                        head.get().remove();
                        djEntity.setHead(Optional.empty());
                    });
                    djEntity.newHead(player);
                })
                .setItem(16, new ItemUtils().ItemStack(Material.RED_WOOL, Optional.of("§7Danseurs"), Optional.empty(), Optional.empty()), InventoryClickEvent -> {
                    InventoryUtils.create("&3&lDJ Station &8&l» &7Éditeur &8&l» &7Danseurs", 3)
                            .setItem(11, new ItemUtils().ItemStack(Material.SLIME_BLOCK, Optional.of("§7Danseur 1"), Optional.empty(), Optional.empty()), InventoryClickEvent2 -> {
                                player.closeInventory();
                                djEntity.setDancer1(Optional.of(player.getLocation()));
                                Message.create("&3&lDJ Station &8&l» &7Le premier danseur a été placé.")
                                        .sendMessage(player);
                            })
                            .setItem(15, new ItemUtils().ItemStack(Material.HONEY_BLOCK, Optional.of("§7Danseur 2"), Optional.empty(), Optional.empty()), InventoryClickEvent2 -> {
                                player.closeInventory();
                                djEntity.setDancer2(Optional.of(player.getLocation()));
                                Message.create("&3&lDJ Station &8&l» &7Le second danseur a été placé.")
                                        .sendMessage(player);
                                player.closeInventory();
                            })
                            .open(player);
                })
                .open(player);
    }
}
