package net.hover.unlimiteditemeffects.command;

import net.hover.corelib_s.CommandSpecable;
import net.hover.unlimiteditemeffects.UIEPlugin;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class ReloadCommand implements CommandSpecable {

    private UIEPlugin plugin;

    public ReloadCommand(UIEPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public CommandSpec base() {
        return CommandSpec.builder()
            .permission("unlimiteditemeffects.reload")
            .executor(this)
            .build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        plugin.reload();
        src.sendMessage(Text.of("You have reloaded the config!"));
        return CommandResult.empty();
    }

}
