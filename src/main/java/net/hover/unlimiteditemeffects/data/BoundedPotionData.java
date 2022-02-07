package net.hover.unlimiteditemeffects.data;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class BoundedPotionData extends AbstractSingleData<String, BoundedPotionData, BoundedPotionData.Immutable> {

    protected BoundedPotionData(String boundedPotionEffects) {
        super(UIEKeys.BOUND_POTION_EFFECTS, boundedPotionEffects);
    }

    @Override
    protected Value<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(UIEKeys.BOUND_POTION_EFFECTS, getValue());
    }

    @Override
    public Optional<BoundedPotionData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<BoundedPotionData> data_ = dataHolder.get(BoundedPotionData.class);
        if (data_.isPresent()) {
            BoundedPotionData data = data_.get();
            BoundedPotionData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<BoundedPotionData> from(DataContainer container) {
        return Optional.of(this);
    }

    @Override
    public BoundedPotionData copy() {
        return new BoundedPotionData(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public static class Immutable extends AbstractImmutableSingleData<String, Immutable, BoundedPotionData> {

        Immutable(String boundedPotionEffects) {
            super(UIEKeys.BOUND_POTION_EFFECTS, boundedPotionEffects);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(UIEKeys.BOUND_POTION_EFFECTS, getValue()).asImmutable();
        }

        @Override
        public BoundedPotionData asMutable() {
            return new BoundedPotionData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 1;
        }

    }

    public static class Builder extends AbstractDataBuilder<BoundedPotionData> implements DataManipulatorBuilder<BoundedPotionData, Immutable> {

        public Builder() {
            super(BoundedPotionData.class, 1);
        }

        @Override
        public BoundedPotionData create() {
            return new BoundedPotionData("");
        }

        @Override
        public Optional<BoundedPotionData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<BoundedPotionData> buildContent(DataView container) throws InvalidDataException {
            return Optional.of(create());
        }

    }

}
