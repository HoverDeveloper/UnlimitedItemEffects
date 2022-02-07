package net.hover.unlimiteditemeffects;

import com.google.inject.Inject;
import net.hover.corelib_s.CoreLib_S;
import net.hover.corelib_s.SPUtils;
import net.hover.unlimiteditemeffects.command.UIECommand;
import net.hover.unlimiteditemeffects.config.PlayerDataConfig;
import net.hover.unlimiteditemeffects.data.BoundedPotionData;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Map;
import java.util.Optional;

@Plugin(
        id = "unlimiteditemeffects",
        name = "UnlimitedItemEffects",
        version = "1.0-SNAPSHOT"
)
public class UIEPlugin {

    @Inject
    public Logger logger;

    @Inject
    PluginContainer container;

    private static UIEPlugin instance;

    private BaseConfig config;
    private Map<String, ItemStack> items;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        instance = this;
        Optional<PluginContainer> optContainer = Sponge.getPluginManager().getPlugin("unlimiteditemeffects");
        if(optContainer.isPresent()){
            CoreLib_S.setInstance(optContainer.get());
        }else{
            throw new IllegalStateException("Can't hook CoreLib!");
        }

        SPUtils.validatePluginFolder();
        SPUtils.validateFiles(SPUtils.configFolderName, "config.yml");
        SPUtils.validateFolders(SPUtils.configFolderName, "player_data");
        SPUtils.registerListeners(new PlayerWatcher(this));
        this.config = new BaseConfig();
        this.items = config.getItems();

        Sponge.getCommandManager().register(this, new UIECommand(this).base(), "unlimiteditemeffects", "unlimitedie", "uie", "uitem", "uitemeffecs", "uieffects");
        PotionApplier applier = new PotionApplier(this);
        Sponge.getScheduler().createTaskBuilder().name("UIE Potion Applier").delayTicks(5).intervalTicks(10).execute(applier).submit(this);

    }

    @Listener
    public void preInit(GamePreInitializationEvent e){

        DataRegistration.builder()
            .dataName("Bounded Potion Data")
            .manipulatorId("bounded_potion_data")
            .dataClass(BoundedPotionData.class)
            .immutableClass(BoundedPotionData.Immutable.class)
            .builder(new BoundedPotionData.Builder())
            .buildAndRegister(container);

    }

    @Listener
    public void onServerClose(GameStoppingServerEvent e){
        for(Player player : Sponge.getServer().getOnlinePlayers()){
            PlayerDataConfig pdc = new PlayerDataConfig(player.getUniqueId());
            pdc.saveData(player);
        }
    }

    public static UIEPlugin getInstance() {
        return instance;
    }

    public void reload(){
        this.config = new BaseConfig();
        this.items = config.getItems();
    }

    public BaseConfig getConfig() {
        return config;
    }

    public Map<String, ItemStack> getItems() {
        return items;
    }

    public Logger getLogger() {
        return logger;
    }

    public static Text colorText(String txt){
        return TextSerializers.FORMATTING_CODE.deserialize(txt);
    }

}
