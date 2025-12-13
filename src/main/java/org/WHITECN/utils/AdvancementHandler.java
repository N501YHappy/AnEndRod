package org.WHITECN.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class AdvancementHandler {
    public static void onUsed1kTimes(Player player){
        TextComponent message = new TextComponent();
        TextComponent playerName = new TextComponent(player.getName());
        TextComponent advancementText = new TextComponent("[要...要坏掉了喵T^T]");
        playerName.setInsertion(player.getName());
        advancementText.setColor(ChatColor.GREEN);
        message.addExtra(playerName);
        message.addExtra("取得了进度");
        message.addExtra(advancementText);
        player.spigot().sendMessage(message);
        player.playSound(player, Sound.UI_TOAST_IN,100.0f, 1.0f);
    }

    public static void onUsed10kTimes(Player player){
        TextComponent message = new TextComponent();
        TextComponent playerName = new TextComponent(player.getName());
        TextComponent advancementText = new TextComponent("[已...已经坏掉了T^T]");
        playerName.setInsertion(player.getName());
        advancementText.setColor(ChatColor.DARK_PURPLE);
        message.addExtra(playerName);
        message.addExtra("完成了挑战");
        message.addExtra(advancementText);
        player.spigot().sendMessage(message);
        player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE,80.0f, 1.0f);
    }

    public static void advancementTest(Player player){
        try {
            if (Integer.parseInt(tagUtils.getTag(player, "rodUsed")) == 1000) {
                onUsed1kTimes(player);
            }
            if (Integer.parseInt(tagUtils.getTag(player, "rodUsed")) == 10000) {
                onUsed10kTimes(player);
            }
        }catch (ClassCastException e){
            player.sendMessage("\n§c§l末地烛插件发生了内部错误喵 错误类型：玩家标签数据类型错误 请报告腐竹喵\n");
        }
    }
}
