package net.hover.unlimiteditemeffects.data;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public class UIEKeys {

    private UIEKeys(){}
    static void dummy(){}

    public static final Key<Value<String>> BOUND_POTION_EFFECTS;

    static {
        BOUND_POTION_EFFECTS = Key.builder()
            .type(TypeTokens.STRING_VALUE_TOKEN)
            .id("uie:bound_potion_effects")
            .name("Bound Potion Effects")
            .query(DataQuery.of('.', "bound.potion.effects"))
            .build();
    }

}
