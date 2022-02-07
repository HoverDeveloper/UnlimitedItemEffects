package net.hover.unlimiteditemeffects;

import net.hover.corelib_s.Config;
import net.hover.corelib_s.SPUtils;
import net.hover.corelib_s.exception.InvalidRegistryID;
import net.hover.corelib_s.helpers.ItemStackHelper;
import net.hover.unlimiteditemeffects.data.BoundedPotionData;
import net.hover.unlimiteditemeffects.data.UIEKeys;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.*;

public class BaseConfig extends Config {

    public BaseConfig() {
        super("config.yml", SPUtils.configFolderName);
    }

    public Map<String, ItemStack> getItems(){

        Map<String, ItemStack> items = new HashMap<>();
        Map<Object, ? extends ConfigurationNode> itemsSection = config.getNode("Items").getChildrenMap();
        if(!itemsSection.isEmpty()){
            for(Object key : itemsSection.keySet()){
                String itemKey = (String) key;
                ItemStack item = createItem(itemKey);
                if(item != null){
                    items.put(itemKey, item);
                }
            }
        }

        return items;

    }

    private ItemStack createItem(String itemKey){

        ConfigurationNode itemNode = config.getNode("Items", itemKey);
        String itemID = itemNode.getNode("Item ID").getString("");
        Text displayName = UIEPlugin.colorText(itemNode.getNode("Display Name").getString(""));
        List<String> lore = getStringList(itemNode.getNode("Lore"));
        List<String> strPotionEffects = getStringList(itemNode.getNode("Potion Effects"));
        List<String> enchantmentsStrList = getStringList(itemNode.getNode("Enchantments"));

        String potionEffectsLine = "";
        for(String s : strPotionEffects){
            potionEffectsLine += s + " ";
        }

        List<Text> loreText = new ArrayList<>();
        for(String s : lore){
            loreText.add(UIEPlugin.colorText(s));
        }

        List<Enchantment> enchantments = new ArrayList<>();
        for(String enchLine : enchantmentsStrList){

            String[] enchLineSplit = enchLine.split(";");
            String enchantmentTypeStr = enchLineSplit[0];
            int level = Integer.parseInt(enchLineSplit[1]);

            Optional<EnchantmentType> optItemType = Sponge.getRegistry().getType(EnchantmentType.class, enchantmentTypeStr);
            if(!optItemType.isPresent()){
                try {
                    throw new InvalidRegistryID(EnchantmentType.class, enchantmentTypeStr);
                } catch (InvalidRegistryID invalidRegistryID) {
                    invalidRegistryID.printStackTrace();
                    continue;
                }
            }

            EnchantmentType enchantmentType = optItemType.get();
            Enchantment enchantment = Enchantment.of(enchantmentType, level);
            enchantments.add(enchantment);

        }

        ItemStack stack;
        try{
            stack = ItemStackHelper.getStack(itemID);
        }catch(InvalidRegistryID e){
            e.printStackTrace();
            return null;
        }

        stack.offer(Keys.DISPLAY_NAME, displayName);
        stack.offer(Keys.ITEM_LORE, loreText);
        stack.offer(stack.getOrCreate(BoundedPotionData.class).get());
        stack.offer(UIEKeys.BOUND_POTION_EFFECTS, potionEffectsLine);

        if(stack.getType() == ItemTypes.ENCHANTED_BOOK){
            stack.offer(Keys.STORED_ENCHANTMENTS, enchantments);
        }else{
            stack.offer(Keys.ITEM_ENCHANTMENTS, enchantments);
        }

        boolean shouldHideEnchantments = itemNode.getNode("Should Hide Enchantments").getBoolean();
        boolean shouldHideToolip = itemNode.getNode("Should Hide Tooltip").getBoolean();

        stack.offer(Keys.HIDE_ENCHANTMENTS, shouldHideEnchantments);
        stack.offer(Keys.HIDE_ATTRIBUTES, shouldHideToolip);
        stack.offer(Keys.HIDE_MISCELLANEOUS, shouldHideToolip);

        return stack;

    }

}
