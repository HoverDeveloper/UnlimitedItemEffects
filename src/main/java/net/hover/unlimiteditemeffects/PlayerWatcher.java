package net.hover.unlimiteditemeffects;

import net.hover.unlimiteditemeffects.config.PlayerDataConfig;
import net.hover.unlimiteditemeffects.data.UIEKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.AffectSlotEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class PlayerWatcher {

    private UIEPlugin plugin;

    public PlayerWatcher(UIEPlugin plugin){
        this.plugin = plugin;
    }

    @Listener
    public void onPlayerLeave(ClientConnectionEvent.Disconnect e){
        Player player = e.getTargetEntity();
        PlayerDataConfig config = new PlayerDataConfig(player.getUniqueId());
        config.saveData(player);
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join e){
        Player player = e.getTargetEntity();
        PlayerDataConfig config = new PlayerDataConfig(player.getUniqueId());
        config.boundPotionEffectsToItems(player);
    }

    /*
        Fixes the issue that occurs when you are in creative and open your inventory.
        The problem is that when you open your inventory in creative, Sponge copies your items in your inventory and re-applies them to your inventory.
        The problem was that my custom data didn't get re-applied.
     */
    @Listener
    public void onInvOpenUIEEffect(AffectSlotEvent e){

        Object sourceObj = e.getSource();
        if(!(sourceObj instanceof Player)){
            return;
        }

        Player player = (Player) sourceObj;
        for(Transaction<ItemStackSnapshot> t : e.getTransactions()){
            ItemStackSnapshot originalSnapshot = t.getOriginal();
            // Whether the key is set in the original snapshot
            if(originalSnapshot.supports(UIEKeys.BOUND_POTION_EFFECTS) && originalSnapshot.getOrNull(UIEKeys.BOUND_POTION_EFFECTS) != null){
                ItemStack originalStack = originalSnapshot.createStack();
                int indexItem = InventoryUtil.getIndexItem(player.getInventory(), originalStack);
                if(indexItem == -1){
                    continue;
                }
                Sponge.getScheduler().createTaskBuilder().name("reapplication of UIE items").delayTicks(3).execute( task -> {
                    InventoryUtil.setItemAtIndex(player.getInventory(), originalStack, indexItem);
                }).submit(plugin);
            }
        }

    }

}
