package org.wallentines.midnightessentials.common.module;

import org.wallentines.midnightcore.api.MidnightCoreAPI;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightessentials.api.module.blockcommand.BlockCommandRegistry;
import org.wallentines.midnightessentials.api.module.blockcommand.BlockCommandModule;
import org.wallentines.midnightessentials.api.MidnightEssentialsAPI;
import org.wallentines.midnightlib.config.ConfigRegistry;
import org.wallentines.midnightlib.config.ConfigSection;
import org.wallentines.midnightlib.config.FileConfig;
import org.wallentines.midnightlib.math.Vec3i;
import org.wallentines.midnightlib.registry.Identifier;

import java.util.HashMap;

public abstract class AbstractBlockCommandModule implements BlockCommandModule {

    public static final Identifier ID = new Identifier("midnightessentials", "block_commands");
    protected static final ConfigSection DEFAULT_CONFIG = new ConfigSection().with("register_command", true).with("file_name", "block_commands");

    protected HashMap<String, BlockCommandRegistry> registries = new HashMap<>();

    private String fileName;
    private FileConfig config;

    @Override
    public boolean initialize(ConfigSection configuration, MidnightCoreAPI api) {

        if(configuration.getBoolean("register_command")) {
            registerCommands();
        }

        fileName = configuration.getString("file_name");

        ConfigRegistry.INSTANCE.registerSerializer(BlockCommandRegistry.BlockCommand.class, BlockCommandRegistry.BlockCommand.SERIALIZER);

        registerListeners();
        return true;
    }

    @Override
    public BlockCommandRegistry createRegistry(String id) {
        BlockCommandRegistry out = new BlockCommandRegistry();
        registries.put(id, out);

        return out;
    }

    @Override
    public void reloadDefaultRegistry() {

        if(config == null) {
            config = FileConfig.findOrCreate(fileName, MidnightEssentialsAPI.getInstance().getDataFolder());
        } else {
            config.reload();
        }

        for(String s : config.getRoot().getKeys()) {

            BlockCommandRegistry reg = createRegistry(s);
            reg.setActiveWorld(Identifier.parseOrDefault(s, "minecraft"));
            reg.loadFromConfig(config.getRoot().getSection(s));
        }
    }

    @Override
    public boolean execute(Vec3i loc, BlockCommandRegistry.InteractionType type, MPlayer player) {
        for(BlockCommandRegistry reg : registries.values()) {

            if(reg.getActiveWorld().equals(player.getLocation().getWorldId())) return reg.executeCommands(player, type, loc);
        }
        return false;
    }

    @Override
    public void unloadRegistry(String id) {
        registries.remove(id);
    }

    protected abstract void registerCommands();
    protected abstract void registerListeners();

}
