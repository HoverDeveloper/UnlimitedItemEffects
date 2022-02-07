package net.hover.unlimiteditemeffects.config;

import net.hover.corelib_s.Config;
import net.hover.corelib_s.SPUtils;
import net.hover.unlimiteditemeffects.InventoryUtil;
import net.hover.unlimiteditemeffects.data.BoundedPotionData;
import net.hover.unlimiteditemeffects.data.UIEKeys;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataConfig extends Config {

    public PlayerDataConfig(UUID playerUUID) {
        super(playerUUID.toString() + ".yml", SPUtils.configFolderName + File.separator + "player_data");
    }

    public void saveData(Player player){

        File file = new File("config" + File.separator + SPUtils.configFolderName + File.separator + "player_data");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config.setValue(null);

        Inventory inv = player.getInventory();
        for(Inventory slot : inv.slots()){
            if(slot.peek().isPresent()){
                ItemStack stack = slot.peek().get();
                Optional<String> optBoundedPotionEffects = stack.get(UIEKeys.BOUND_POTION_EFFECTS);
                if(optBoundedPotionEffects.isPresent()){
                    int index = slot.getInventoryProperty(SlotIndex.class).get().getValue();
                    // Old serialization uses semicolon. New serialization uses @@.
                    String boundedPotionEffects = optBoundedPotionEffects.get().replace(";", "@@");
                    config.getNode(String.valueOf(index)).setValue(boundedPotionEffects);
                }
            }
        }
        saveConfig();

    }

    public void boundPotionEffectsToItems(Player player){

        Inventory inv = player.getInventory();
        inv.getInventoryProperty(SlotIndex.class);
        Map<Object, ? extends ConfigurationNode> slotKeysSection = config.getChildrenMap();

        for(Object o : slotKeysSection.keySet()){

            int slot = Integer.parseInt((String)o);

            // Old serialization uses semicolon. New serialization uses @@.
            String potionEffectsBounded = config.getNode(String.valueOf(slot)).getString();
            if(potionEffectsBounded == null) return;
            potionEffectsBounded = potionEffectsBounded.replace(";", "@@");

            ItemStack itemAtSlot = InventoryUtil.getItemAtIndex(inv, slot);
            if(itemAtSlot != null){
                itemAtSlot.offer(itemAtSlot.getOrCreate(BoundedPotionData.class).get());
                itemAtSlot.offer(UIEKeys.BOUND_POTION_EFFECTS, potionEffectsBounded);
                InventoryUtil.setItemAtIndex(inv, itemAtSlot, slot);
            }

        }

    }

}
