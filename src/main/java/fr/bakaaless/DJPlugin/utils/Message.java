package fr.bakaaless.DJPlugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private final List<String> lines;

    public Message(final String message){
        this.lines = new ArrayList<>();
        this.lines.add(message);
    }

    public static Message create(final String text){
        return new Message(text);
    }

    public Message append(final String text){
        final String oldMessage = this.lines.get(this.lines.size() - 1);
        this.lines.set(this.lines.size() - 1, oldMessage + text);
        return this;
    }

    public Message append(final int i){
        final String oldMessage = this.lines.get(this.lines.size() - 1);
        this.lines.set(this.lines.size() - 1, oldMessage + i);
        return this;
    }

    public Message append(final double d){
        final String oldMessage = this.lines.get(this.lines.size() - 1);
        this.lines.set(this.lines.size() - 1, oldMessage + d);
        return this;
    }

    public Message append(final int i, final String text){
        final String oldMessage = this.lines.get(i);
        this.lines.set(i, oldMessage + text);
        return this;
    }

    public Message addLine(final String text){
        this.lines.add(text);
        return this;
    }

    public void sendMessage(final CommandSender target) {
        for (final String string : this.lines) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
        }
    }

}
