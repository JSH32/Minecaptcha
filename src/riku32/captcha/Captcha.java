package riku32.captcha;

import riku32.captcha.configman.Configman;
import riku32.captcha.configman.data.ConfigValue;
import riku32.captcha.events.MapEvent;
import riku32.captcha.events.PlayerEvent;
import riku32.captcha.player.CaptchaPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.ChatColor.*;

public class Captcha extends JavaPlugin {
    private CaptchaPlayerManager playerManager = new CaptchaPlayerManager();

    @Override
    public void onEnable() {
        new Configman(this)
                .register(new Config())
                .load();

        // registering events
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerEvent(this), this);
        pluginManager.registerEvents(new MapEvent(this), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    public CaptchaPlayerManager getPlayerManager() {
        return playerManager;
    }

    public static class Config {

        @ConfigValue("prefix")
        public static String prefix = "[" + GREEN + "Captcha" + RESET + "]";

        public static String permission = "captcha.bypass";

        @ConfigValue("tries")
        public static int captchaTries = 3;

        @ConfigValue("map_in_offhand")
        public static String mapInOffHand = "false";

        @ConfigValue("time_limit_enabled")
        public static String timeLimitEnabled = "false";

        @ConfigValue("time_limit")
        public static int captchaTimeLimit = 10;

        @ConfigValue("success_server")
        public static String successServer = "";

        @ConfigValue("messages.success")
        public static String captchaSuccessMessage = "Captcha " + GREEN + "solved!";

        @ConfigValue("messages.retry")
        public static String captchaRetryMessage = "Captcha " + YELLOW + "failed, " + RESET + "please try again. ({CURRENT}/{MAX})";

        @ConfigValue("messages.fail")
        public static String captchaFailMessage = "Captcha " + RED + "failed!";
    }
}
