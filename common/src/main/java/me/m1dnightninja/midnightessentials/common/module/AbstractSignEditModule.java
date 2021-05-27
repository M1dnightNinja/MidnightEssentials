package me.m1dnightninja.midnightessentials.common.module;

import me.m1dnightninja.midnightcore.api.config.ConfigSection;
import me.m1dnightninja.midnightcore.api.module.IModule;
import me.m1dnightninja.midnightcore.api.registry.MIdentifier;

public abstract class AbstractSignEditModule implements IModule {

    private static final MIdentifier ID = MIdentifier.create("midnightessentials","editable_signs");

    @Override
    public boolean initialize(ConfigSection configuration) {

        registerListeners();
        return true;
    }

    @Override
    public MIdentifier getId() {

        return ID;
    }

    @Override
    public ConfigSection getDefaultConfig() {

        return new ConfigSection();
    }

    protected abstract void registerListeners();


}
