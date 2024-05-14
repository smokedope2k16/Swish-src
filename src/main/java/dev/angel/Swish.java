package dev.angel;

import dev.angel.api.event.bus.SimpleBus;
import dev.angel.api.event.bus.api.EventBus;
import dev.angel.api.util.logging.Logger;
import dev.angel.impl.manager.*;
import org.apache.logging.log4j.Level;

import java.io.File;

// TODO: organize mixins
// TODO: rotation manager
public class Swish {
	public static final String NAME = "Swish";
	public static final String VERSION = "v0.0.1";
	public static final File DIRECTORY = new File(System.getProperty("user.home"), "swish");
	private static final EventBus EVENT_BUS = new SimpleBus();

	private static final CommandManager COMMAND_MANAGER = new CommandManager();
	private static final ModuleManager MODULE_MANAGER = new ModuleManager();
	private static final ConfigManager CONFIG_MANAGER = new ConfigManager();
	private static final PopManager POP_MANAGER = new PopManager();
	private static final TimerManager TIMER_MANAGER = new TimerManager();
	private static final FriendManager FRIEND_MANAGER = new FriendManager();
	private static final TpsManager TPS_MANAGER = new TpsManager();
	private static final SpeedManager SPEED_MANAGER = new SpeedManager();

	public static void init() {
		EVENT_BUS.subscribe(MODULE_MANAGER);
		EVENT_BUS.subscribe(COMMAND_MANAGER);
		EVENT_BUS.subscribe(POP_MANAGER);
		EVENT_BUS.subscribe(TPS_MANAGER);
		EVENT_BUS.subscribe(SPEED_MANAGER);

		CONFIG_MANAGER.init();
		FRIEND_MANAGER.init();

		if (!DIRECTORY.exists()) {
			Logger.getLogger().log(Level.INFO, String.format("%s client directory.", DIRECTORY.mkdir() ? "Created" : "Failed to create"));
		}

		Logger.getLogger().log(Level.INFO, String.format("%s - %s loaded successfully%n", NAME, VERSION));
	}

	public static ModuleManager getModuleManager() {
		return MODULE_MANAGER;
	}

	public static CommandManager getCommandManager() {
		return COMMAND_MANAGER;
	}

	public static PopManager getPopManager() {
		return POP_MANAGER;
	}

	public static ConfigManager getConfigManager() {
		return CONFIG_MANAGER;
	}

	public static FriendManager getFriendManager() {
		return FRIEND_MANAGER;
	}

	public static TpsManager getTpsManager() {
		return TPS_MANAGER;
	}

	public static SpeedManager getSpeedManager() {
		return SPEED_MANAGER;
	}

	public static EventBus getEventBus() {
		return EVENT_BUS;
	}

	public static TimerManager getTimerManager() {
		return TIMER_MANAGER;
	}
}
