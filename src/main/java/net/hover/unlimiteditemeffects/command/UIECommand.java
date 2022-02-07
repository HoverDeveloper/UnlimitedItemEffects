package net.hover.unlimiteditemeffects.command;

import net.hover.corelib_s.CommandSpecable;
import net.hover.unlimiteditemeffects.UIEPlugin;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class UIECommand implements CommandSpecable {

    private UIEPlugin plugin;

    public UIECommand(UIEPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public CommandSpec base() {
        return CommandSpec.builder()
            .permission("unlimiteditemeffects.admin")
            .child(new ReloadCommand(plugin).base(), "reload")
            .child(new ListCommand(plugin).base(), "list")
            .child(new GiveCommand(plugin).base(), "give")
            .executor(this)
            .build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        src.sendMessage(Text.of("UnlimitedItemEffects Commands: "));
        src.sendMessage(Text.of("/uie reload"));
        src.sendMessage(Text.of("/uie list"));
        src.sendMessage(Text.of("/uie give <player name> <item name>"));
        src.sendMessage(Text.of("/uie give <item name>"));
        return CommandResult.success();
    }

}
