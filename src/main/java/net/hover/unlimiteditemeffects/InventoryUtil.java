package net.hover.unlimiteditemeffects;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryUtil {

    public static ItemStack getItemAtIndex(Inventory inv, int index){
        for(Inventory slot : inv.slots()){
            SlotIndex slotIndex = slot.getInventoryProperty(SlotIndex.class).orElse(null);
            if(slotIndex != null && slotIndex.getValue() == index){
                Optional<ItemStack> slotStackOpt = slot.peek();
                if(slotStackOpt.isPresent()){
                    return slotStackOpt.get();
                }
            }
        }
        return null;
    }

    public static void setItemAtIndex(Inventory inv, ItemStack stack, int index){
        for(Inventory slot : inv.slots()){
            SlotIndex slotIndex = slot.getInventoryProperty(SlotIndex.class).orElse(null);
            if(slotIndex != null && slotIndex.getValue() == index){
                Optional<ItemStack> slotStackOpt = slot.peek();
                if(slotStackOpt.isPresent()){
                    slot.set(stack);
                    return;
                }
            }
        }
    }

    public static int getIndexItem(Inventory inv, ItemStack stack){
        for(Inventory slot : inv.slots()){
            SlotIndex slotIndex = slot.getInventoryProperty(SlotIndex.class).orElse(null);
            if(slotIndex != null){
                Optional<ItemStack> slotStackOpt = slot.peek();
                if(slotStackOpt.isPresent()){
                    if(areSimilarItems(stack, slotStackOpt.get())){
                        return slotIndex.getValue();
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Checks for item equality via display name, lore, and item type.
     */
    public static boolean areSimilarItems(ItemStack stack1, ItemStack stack2){

        if(stack1.getType() != stack2.getType()){
            return false;
        }

        Text displayName1 = stack1.getOrElse(Keys.DISPLAY_NAME, Text.of(""));
        Text displayName2 = stack2.getOrElse(Keys.DISPLAY_NAME, Text.of(""));
        if(!displayName1.toPlain().equalsIgnoreCase(displayName2.toPlain())){
            return false;
        }

        List<Text> lore1 = stack1.getOrElse(Keys.ITEM_LORE, new ArrayList<>());
        List<Text> lore2 = stack2.getOrElse(Keys.ITEM_LORE, new ArrayList<>());
        if(lore1.size() != lore2.size()){
            return false;
        }

        for (int i = 0; i < lore1.size(); i++) {
            Text lore1Current = lore1.get(i);
            Text lore2Current = lore2.get(i);
            if(!lore1Current.toPlain().equalsIgnoreCase(lore2Current.toPlain())){
                return false;
            }
        }

        return true;

    }

}
