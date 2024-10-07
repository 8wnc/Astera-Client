package net.team.astera;

import net.team.astera.impl.manager.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsteraClient implements ModInitializer, ClientModInitializer {
    public static final String NAME = "AsteraClient";
    public static final String VERSION = "b1.0.9-92au7db+extern";

    public static float TIMER = 1f;

    public static final Logger LOGGER = LogManager.getLogger("AsteraClient");
    public static ServerManager serverManager;
    public static ColorManager colorManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static HoleManager holeManager;
    public static EventManager eventManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;
    public static PlayerManager playerManager;
    public static AsyncManager asyncManager;
    public static CombatManager combatManager;
    public static TotemPopManager totemPopManager;


    @Override public void onInitialize() {
        eventManager = new EventManager();
        serverManager = new ServerManager();
        rotationManager = new RotationManager();
        positionManager = new PositionManager();
        friendManager = new FriendManager();
        colorManager = new ColorManager();
        commandManager = new CommandManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        holeManager = new HoleManager();
        playerManager = new PlayerManager();
        asyncManager = new AsyncManager();
        combatManager = new CombatManager();
        totemPopManager = new TotemPopManager();
    }


    @Override public void onInitializeClient() {
        eventManager.init();
        moduleManager.init();

        configManager = new ConfigManager();
        configManager.load();
        colorManager.init();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
    }
}
