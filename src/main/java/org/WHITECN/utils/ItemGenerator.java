package org.WHITECN.utils;

import net.md_5.bungee.api.ChatColor;
import org.WHITECN.anendrod;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemGenerator {
    public static ItemStack createRegularRod() {
        // 创建基础物品
        ItemStack rod = new ItemStack(Material.END_ROD);
        ItemMeta meta = rod.getItemMeta();

        // 设置显示名称
        meta.setDisplayName("§2普通末地烛");

        // 设置 Lore
        List<String> lore = new ArrayList<>();
        lore.add("§7没什么特别的 就是末地烛哦");
        lore.add("§7已使用 §e0 §7次");
        meta.setLore(lore);

        // 设置自定义NBT标签
        meta.getPersistentDataContainer().set(
                new NamespacedKey(anendrod.getInstance(), "useCount"),
                PersistentDataType.INTEGER, 0
        );

        meta.setCustomModelData(1);    //添加特殊数据，用于匹配材质包    

        // 应用修改
        rod.setItemMeta(meta);
        return rod;
    }
    public static ItemStack createSlimeRod() {
        // 创建基础物品
        ItemStack rod = new ItemStack(Material.END_ROD);
        ItemMeta meta = rod.getItemMeta();

        // 设置显示名称
        meta.setDisplayName("§a粘液§2末地烛");

        // 设置 Lore
        List<String> lore = new ArrayList<>();
        lore.add("§7一个黏糊糊的末地烛哦\n");
        lore.add("§7已使用 §e0 §7次");
        meta.setLore(lore);

        // 设置自定义NBT标签
        meta.getPersistentDataContainer().set(
                new NamespacedKey(anendrod.getInstance(), "useCount"),
                PersistentDataType.INTEGER, 0
        );

        meta.setCustomModelData(2);    //添加特殊数据，用于匹配材质包    

        // 应用修改
        rod.setItemMeta(meta);
        return rod;
    }
    public static ItemStack createRegularProRod() {
        // 创建基础物品
        ItemStack rod = new ItemStack(Material.END_ROD);
        ItemMeta meta = rod.getItemMeta();

        // 设置显示名称
        meta.setDisplayName("§bPro§2末地烛");
        //添加附魔效果
        meta.addEnchant(Enchantment.DURABILITY, 0, false);

        // 设置 Lore
        List<String> lore = new ArrayList<>();
        lore.add("§7普通末地烛的§bPro§7版");
        lore.add("§7已使用 §e0 §7次");
        meta.setLore(lore);

        // 设置自定义NBT标签
        meta.getPersistentDataContainer().set(
                new NamespacedKey(anendrod.getInstance(), "useCount"),
                PersistentDataType.INTEGER, 0
        );

        meta.setCustomModelData(3);    //添加特殊数据，用于匹配材质包    

        // 应用修改
        rod.setItemMeta(meta);
        return rod;
    }

    public static ItemStack createHandCuffs(){
        ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§d手铐♥");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.LIGHT_PURPLE + "这是一个手铐，可以限制"
                + ChatColor.YELLOW + "玩家" + ChatColor.LIGHT_PURPLE + "挖掘速度和触碰距离...");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(
                new NamespacedKey(anendrod.getInstance(),"code"),
                PersistentDataType.INTEGER_ARRAY, new int[] {0}
        );
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createKeyItem() {
        ItemStack item = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§7钥匙");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.LIGHT_PURPLE + "这是一个钥匙，可以解锁也可以上锁");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(
                new NamespacedKey(anendrod.getInstance(),"code"),
                PersistentDataType.INTEGER_ARRAY, new int[] {0}
        );
        item.setItemMeta(meta);
        return item;
    }
}
