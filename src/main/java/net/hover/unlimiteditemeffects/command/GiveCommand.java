package net.hover.unlimiteditemeffects.command;

import net.hover.corelib_s.CommandSpecable;
import net.hover.corelib_s.helpers.ColorHelper;
import net.hover.unlimiteditemeffects.UIEPlugin;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Map;
import java.util.Optional;

public class GiveCommand implements CommandSpecable {

    private UIEPlugin plugin;

    public GiveCommand(UIEPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public CommandSpec base() {
        return CommandSpec.builder()
            .permission("unlimiteditemeffects.give")
            .arguments(
                GenericArguments.optionalWeak(GenericArguments.player(Text.of("target_player"))),
                GenericArguments.string(Text.of("item_name"))
            )
            .executor(this)
            .build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {

        Player itemTaker;
        if(args.hasAny("target_player")){
            Optional<Player> optTargetPlayer = args.getOne(Text.of("target_player"));
            if(!optTargetPlayer.isPresent()){
                src.sendMessage(Text.of("This is not a valid player!"));
                return CommandResult.empty();
            }
            itemTaker = optTargetPlayer.get();
        }else if(src instanceof Player){
            itemTaker = (Player) src;
        }else{
            src.sendMessage(ColorHelper.colorText("&cTry using this command: &6/uie give <player name> <item name>"));
            return CommandResult.empty();
        }

        if(!args.hasAny("item_name")){
            src.sendMessage(Text.of("You forgot to input a item name!"));
            return CommandResult.empty();
        }

        String itemName = args.<String>getOne(Text.of("item_name")).get();
        Map<String, ItemStack> items = plugin.getItems();
        if(!items.containsKey(itemName)){
            src.sendMessage(Text.of("This is not a valid item name! Do /uie list to see all the items!"));
            return CommandResult.empty();
        }

        ItemStack item = items.get(itemName);
        itemTaker.getInventory().offer(item.copy());
        itemTaker.sendMessage(Text.of("You have been given an item!"));

        return CommandResult.success();

    }

}
