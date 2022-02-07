package net.hover.unlimiteditemeffects;

import net.hover.unlimiteditemeffects.data.UIEKeys;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PotionApplier extends BaseRunnable{

    public PotionApplier(UIEPlugin plugin) {
        super(plugin);
    }

    @Override
    public void accept(Task task) {

        for(Player player : Sponge.getServer().getOnlinePlayers()){
            List<ItemStack> checkedItems = compileCheckedItems(player);
            for(ItemStack item : checkedItems){
                Optional<String> optBoundPotionEffects = item.get(UIEKeys.BOUND_POTION_EFFECTS);
                if(optBoundPotionEffects.isPresent()){
                    String boundPotionEffects = optBoundPotionEffects.get();
                    String newSerialization = boundPotionEffects.replace(";", "@@");
                    item.offer(UIEKeys.BOUND_POTION_EFFECTS, newSerialization);
                    applyBoundPotionEffects(player, newSerialization);
                }
            }
        }

    }

    private void applyBoundPotionEffects(Player player, String boundPotionEffects){

        List<PotionEffect> effects = new ArrayList<>();
        // The string will look like this initially: minecraft:speed@@1 minecraft:strength@@2 minecraft:jump_boost@@3
        String[] split = boundPotionEffects.split(" ");
        for(String s : split){

            String[] split2 = s.split("@@");
            String potionId = split2[0];
            Optional<PotionEffectType> type = Sponge.getRegistry().getType(PotionEffectType.class, potionId);
            Logger logger = plugin.getLogger();
            if(!type.isPresent()){
                logger.info("=========");
                logger.info("UIE ERROR: ");
                logger.info("Entire Line: " + boundPotionEffects);
                logger.info("Piece of Line: " + s);
                logger.error("Invalid ID: " + potionId);
                logger.info("=========");
                continue;
            }

            int potionAmp = Integer.parseInt(split2[1]);
            PotionEffect effect = PotionEffect.builder()
                .particles(true)
                .potionType(type.get())
                .duration(12)
                .amplifier(potionAmp)
                .build();
            effects.add(effect);

        }

        Optional<List<PotionEffect>> optCurrentEffects = player.get(Keys.POTION_EFFECTS);
        optCurrentEffects.ifPresent(effects::addAll);

        player.offer(Keys.POTION_EFFECTS, effects);

    }

    private List<ItemStack> compileCheckedItems(Player player){

        List<ItemStack> checkedItems = new ArrayList<>();
        List<Optional<ItemStack>> optCheckedItems = new ArrayList<>();

        optCheckedItems.add(player.getItemInHand(HandTypes.MAIN_HAND));
        optCheckedItems.add(player.getHelmet());
        optCheckedItems.add(player.getLeggings());
        optCheckedItems.add(player.getBoots());
        optCheckedItems.add(player.getChestplate());
        optCheckedItems.add(player.getItemInHand(HandTypes.OFF_HAND));

        for(Optional<ItemStack> opt : optCheckedItems){
            if(opt.isPresent() && opt.get().getType() != ItemTypes.AIR){
                checkedItems.add(opt.get());
            }
        }

        return checkedItems;

    }

}
