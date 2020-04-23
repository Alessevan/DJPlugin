package fr.bakaaless.DJPlugin.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class ItemUtils {

    public ItemStack ItemStack(final Material m, final Optional<String> displayNameOptional, final Optional<String> loreOptional, Optional<Integer> amountOptional) {
        return this.ItemStack(m, displayNameOptional, loreOptional, amountOptional, false);
    }

    public ItemStack ItemStack(final Material m, final Optional<String> displayNameOptional, final Optional<String> loreOptional, Optional<Integer> amountOptional, boolean b) {
        if (Optional.empty().equals(amountOptional)) amountOptional = Optional.of(1);
        final ItemStack itemStack = new ItemStack(m, amountOptional.get());
        final ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        displayNameOptional.ifPresent(itemMeta::setDisplayName);
        loreOptional.ifPresent(lore ->
                itemMeta.setLore(Arrays.asList(lore.split("\n")))
        );
        if (b) {
            itemStack.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setItemMeta(itemMeta);
        amountOptional.ifPresent(itemStack::setAmount);
        return itemStack;
    }

    public ItemStack skullPlayer(final Player player){
        final ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setOwningPlayer(player);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @SuppressWarnings("null")
    public ItemStack SkullLocal(final String displayName, String texture) {
        if (!texture.contains("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv")) {
            texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + texture;
        }
        final ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        final GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", texture));
        try {
            final Field declaredField = itemMeta.getClass().getDeclaredField("profile");
            declaredField.setAccessible(true);
            declaredField.set(itemMeta, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            final Throwable t = null;
            t.printStackTrace();
        }
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getColorArmor(final Material material, final Color color) {
        final ItemStack itemStack = new ItemStack(material, 1);
        if(!(itemStack.getItemMeta() instanceof LeatherArmorMeta)) return this.ItemStack(material, Optional.empty(), Optional.empty(), Optional.empty());
        final LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        itemMeta.setColor(color);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
