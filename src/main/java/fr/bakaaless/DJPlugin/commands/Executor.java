package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.plugin.DjPlugin;
import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class Executor implements CommandExecutor, TabCompleter {

    @Getter
    private final DjPlugin main;

    @Getter
    private final Map<Player, DjEntity> editPlayerDj;

    public Executor() {
        this.main = DjPlugin.getInstance();
        this.editPlayerDj = new HashMap<>();
        this.getMain().getCommand("djstation").setExecutor(this);
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] args) {
        if(!(commandSender instanceof Player)){
            new ReloadCommand(this).handle(commandSender);
            return true;
        }
        final Player player = (Player) commandSender;
        if(args.length == 0){
            Message.create("&c&lDJ Station &4&l» &cTrop peu d'arguments.")
                    .sendMessage(player);
            return true;
        }
        else if(args[0].equalsIgnoreCase("create")){
            new CreateCommand(this).handle(player);
        }
        else if(args[0].equalsIgnoreCase("delete")){
            new DeleteCommand(this).handle(player);
        }
        else if(args[0].equalsIgnoreCase("edit")){
            if(args.length <= 1){
                Message.create("&c&lDJ Station &4&l» &cTrop peu d'arguments : &4/djstation edit <id>")
                        .sendMessage(player);
                return true;
            }
            new EditCommand(this).handle(player, args[1]);
        }
        else if(args[0].equalsIgnoreCase("kick")){
            if(args.length <= 1){
                Message.create("&c&lDJ Station &4&l» &cTrop peu d'arguments : &4/djstation kick <id>")
                        .sendMessage(player);
                return true;
            }
            new KickCommand(this).handle(player, args[1]);
        }
        else if(args[0].equalsIgnoreCase("leave")){
            new LeaveCommand(this).handle(player);
        }
        else if(args[0].equalsIgnoreCase("list")){
            new ListCommand(this).handle(player, (args.length <= 1 ? 1 : (StringUtils.isNumeric(args[1]) ? Integer.parseInt(args[1]) : 1)));
        }
        else if(args[0].equalsIgnoreCase("reload")){
            new ReloadCommand(this).handle(player);
        }
        else if(args[0].equalsIgnoreCase("save")){
            new SaveCommand(this).handle(player);
        }
        else if(args[0].equalsIgnoreCase("set")){
            new SetCommand(this).handle(player);
        }
        else {
            Message.create("&c&lDJ Station &4&l» &cCommande inconnue.")
                    .sendMessage(player);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        final String[] completions0 = { "create", "delete", "edit", "kick", "leave", "list", "reload", "save", "set" };
        if(args.length == 1){
            final List<String> completions = new ArrayList<>();
            for(final String string : completions0){
                if(string.toLowerCase().startsWith(args[0].toLowerCase()) && commandSender.hasPermission("djstation." + string.toLowerCase().replace("leave", "use"))) completions.add(string);
            }
            return completions;
        }
        else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("edit") && commandSender.hasPermission("djstation.edit")) {
                final List<String> djEntities = new ArrayList<>();
                for(final DjEntity djEntity : this.getMain().getDjEntities()){
                    djEntities.add(String.valueOf(djEntity.getId()));
                }
                return djEntities;
            }
            else if(args[0].equalsIgnoreCase("kick") && commandSender.hasPermission("djstation.kick")) {
                final List<String> djEntities = new ArrayList<>();
                for(final DjEntity djEntity : this.getMain().getDjEntities()){
                    if(djEntity.getPlayer().isPresent())
                        djEntities.add(String.valueOf(djEntity.getId()));
                }
                return djEntities;
            }
            else if(args[0].equalsIgnoreCase("list") && commandSender.hasPermission("djstation.list")){
                final List<String> pages = new ArrayList<>();
                for(int i = 1; i <= (int) Math.ceil(this.getMain().getDjEntities().size() / 9.0); i++){
                    pages.add(String.valueOf(i));
                }
                return pages;
            }
        }
        return null;
    }
}
