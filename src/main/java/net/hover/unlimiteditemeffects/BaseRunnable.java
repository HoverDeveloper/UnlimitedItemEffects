package net.hover.unlimiteditemeffects;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.util.function.Consumer;

public abstract class BaseRunnable implements Consumer<Task> {

    protected UIEPlugin plugin;

    public BaseRunnable(UIEPlugin plugin){
        this.plugin = plugin;
    }

    public void cancel(){
        for(Task task : Sponge.getGame().getScheduler().getScheduledTasks(plugin)){
            if(task.getConsumer() == this){
                task.cancel();
            }
        }
    }

}