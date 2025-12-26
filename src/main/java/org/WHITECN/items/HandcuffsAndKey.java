package org.WHITECN.items;

import java.util.ArrayList;
import java.util.List;

import org.WHITECN.anendrod;
import org.WHITECN.utils.keyGen;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;

public class HandcuffsAndKey implements Listener{
    public static final String handCuffsName = ChatColor.LIGHT_PURPLE + "手铐♥";
    public static final String keyItemName = ChatColor.GRAY + "钥匙";
    private static final double RANGE = 1.5;
    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack main = event.getMainHandItem();
        ItemStack off  = event.getOffHandItem();
        
        if (main == null || off == null) return;
        if (!main.hasItemMeta() || !off.hasItemMeta()) return;

        /* 名字过滤：主手必须是手铐，副手必须是钥匙 */
        ItemMeta mainMeta = main.getItemMeta();
        ItemMeta offMeta   = off.getItemMeta();
        if (!handCuffsName.equals(mainMeta.getDisplayName()) ||
            !keyItemName.equals(offMeta.getDisplayName())) return;

        List<Integer> cuffsCode = keyGen.getKey(mainMeta);
        List<Integer> keyCode = keyGen.getKey(offMeta);

        /* 规则 1：俩都没数据 → 生成新钥匙并同时写入 */
        if (cuffsCode.isEmpty() && keyCode.isEmpty()) {
            List<Integer> newCode = keyGen.generateKey();
            keyGen.setKey(mainMeta, newCode);
            keyGen.setKey(offMeta, newCode);
            mainMeta.addEnchant(org.bukkit.enchantments.Enchantment.BINDING_CURSE, 1, true);
            mainMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            int[] arr = newCode.stream().mapToInt(Integer::intValue).toArray();
            String keyPattern = keyGen.getKeyShape(newCode);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.LIGHT_PURPLE + "这是一个钥匙，可以解锁也可以上锁");
            lore.add(ChatColor.GREEN + "目前绑定钥匙：" + ChatColor.WHITE + keyPattern);
            offMeta.setLore(lore);
            mainMeta.getPersistentDataContainer().set(new NamespacedKey(anendrod.getInstance(),"code"),PersistentDataType.INTEGER_ARRAY,arr);
            offMeta.getPersistentDataContainer().set(new NamespacedKey(anendrod.getInstance(),"code"),PersistentDataType.INTEGER_ARRAY,arr);
            player.sendMessage(ChatColor.GREEN + "手铐与钥匙已绑定！");
            return;
        }

        /* 规则 2：钥匙有数据，手铐没有 → 把手铐写成钥匙的序列 */
        if (!keyCode.isEmpty() && cuffsCode.isEmpty()) {
            // 把 keyCode 写进手铐
            mainMeta.getPersistentDataContainer().set(
                    new NamespacedKey(anendrod.getInstance(), "code"),
                    PersistentDataType.INTEGER_ARRAY,
                    keyCode.stream().mapToInt(Integer::intValue).toArray());
            mainMeta.addEnchant(org.bukkit.enchantments.Enchantment.BINDING_CURSE, 1, true);
            mainMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            main.setItemMeta(mainMeta);
            player.sendMessage(ChatColor.GREEN + "手铐已绑定到当前钥匙！");
            return;
        }
        
        player.sendMessage(ChatColor.RED + "这个手铐已经绑定过钥匙了！");
    }
    @EventHandler
    public void onHandCuffs_toEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (!(event.getRightClicked() instanceof Player)) return;

        Player user  = event.getPlayer();
        Player target = (Player) event.getRightClicked();
        ItemStack mainHand = user.getInventory().getItemInMainHand();
        if (!mainHand.hasItemMeta()) return;
        ItemMeta meta = mainHand.getItemMeta();
        if (!handCuffsName.equals(meta.getDisplayName())) return;
        if(target.getEquipment().getChestplate() != null) return;

        ItemStack cuffsCopy = mainHand.clone();
        cuffsCopy.setAmount(1);
        target.getInventory().setChestplate(cuffsCopy);
        
        mainHand.setAmount(mainHand.getAmount() - 1);
        user.sendMessage(ChatColor.GREEN + "已给 " + target.getName() + " 戴上手铐！");
        target.sendMessage(ChatColor.RED + "你被戴上了手铐！挖掘速度和触碰距离受限！");
    }
    @EventHandler
    public void onUnlock(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof Player)) return;

        Player unlocker = event.getPlayer();
        Player target   = (Player) event.getRightClicked();

        ItemStack key = unlocker.getInventory().getItemInMainHand();
        if (!key.hasItemMeta()) return;
        ItemMeta keyMeta = key.getItemMeta();
        if (!keyItemName.equals(keyMeta.getDisplayName())) return;

        ItemStack cuffs = target.getInventory().getChestplate();
        if (cuffs == null || !cuffs.hasItemMeta()) return;
        ItemMeta cuffsMeta = cuffs.getItemMeta();
        if (!handCuffsName.equals(cuffsMeta.getDisplayName())) return;

        List<Integer> cuffsCode = keyGen.getKey(cuffsMeta);
        
        if (cuffsCode.isEmpty()) {
            target.getInventory().setChestplate(null);
            target.getWorld().dropItemNaturally(target.getLocation(), cuffs);
            return;
        }
        
        List<Integer> keyCode = keyGen.getKey(keyMeta);
        if (keyCode.isEmpty() || !keyCode.equals(cuffsCode)) {
            return;
        }
        
        target.getInventory().setChestplate(null);
        target.getWorld().dropItemNaturally(target.getLocation(), cuffs);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
        if (event.getPlayer().getLocation().distance(event.getClickedBlock().getLocation().add(0.5, 0.5, 0.5)) > RANGE) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer().getLocation().distance(event.getRightClicked().getLocation()) > RANGE) {
            event.setCancelled(true);
        }
    }
}