package net.hover.unlimiteditemeffects.command;

import net.hover.corelib_s.CommandSpecable;
import net.hover.unlimiteditemeffects.UIEPlugin;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Set;

public class ListCommand implements CommandSpecable {

    private UIEPlugin plugin;

    public ListCommand(UIEPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public CommandSpec base() {
        return CommandSpec.builder()
            .permission("unlimiteditemeffects.list")
            .executor(this)
            .build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Set<String> itemNames = plugin.getItems().keySet();
        src.sendMessage(Text.of("Items:"));
        for(String name : itemNames){
            src.sendMessage(Text.of("- " + name));
        }
        return CommandResult.success();
    }

}
