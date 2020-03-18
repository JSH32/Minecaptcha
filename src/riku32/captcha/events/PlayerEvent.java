package riku32.captcha.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import riku32.captcha.Captcha;
import riku32.captcha.player.CaptchaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Random;

import static riku32.captcha.Captcha.Config.*;

public class PlayerEvent implements Listener {
    private Captcha Captcha;

    public PlayerEvent(Captcha Captcha) {
        this.Captcha = Captcha;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (player.hasPermission(permission)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Captcha, () -> sendPlayerToServer(player), 15);
            return;
        }

        CaptchaPlayer captchaPlayer = new CaptchaPlayer
                (
                        player, genCaptcha(), Captcha
                ).cleanPlayer();

        ItemStack itemStack = new ItemStack(Material.MAP);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Captcha");
        itemMeta.setLore(Collections.singletonList("Open the map to see the captcha."));
        itemStack.setItemMeta(itemMeta);

        if (mapInOffHand.equals("true")) {
            captchaPlayer.getPlayer().getInventory().setItemInOffHand(itemStack);
        } else {
            captchaPlayer.getPlayer().getInventory().setItemInMainHand(itemStack);
        }

        Captcha.getPlayerManager().addPlayer(captchaPlayer);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeave(PlayerQuitEvent event) {

        CaptchaPlayer captchaPlayer = Captcha.getPlayerManager().getPlayer(event.getPlayer());

        if (captchaPlayer == null) {
            return;
        }

        captchaPlayer.resetInventory();
        Captcha.getPlayerManager().removePlayer(captchaPlayer);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {

        CaptchaPlayer player = Captcha.getPlayerManager().getPlayer(event.getPlayer());

        if (player != null) {

            // captcha was not right
            if (!event.getMessage().equals(player.getCaptcha())) {
                if (player.getTries() >= (captchaTries - 1)) { // kicking the player because he's out of tries
                    Bukkit.getScheduler().runTask(Captcha, () -> player.getPlayer().kickPlayer(prefix + " " + captchaFailMessage));
                } else { // telling the player to try again
                    player.setTries(player.getTries() + 1);
                    player.getPlayer().sendMessage(prefix + " " + captchaRetryMessage.replace("{CURRENT}", String.valueOf(player.getTries())).replace("{MAX}", String.valueOf(captchaTries)));
                }
            } else { // captcha success
                player.getPlayer().sendMessage(prefix + " " + captchaSuccessMessage);
                player.resetInventory();
                Captcha.getPlayerManager().removePlayer(player);
                sendPlayerToServer(player.getPlayer());
            }

            event.setCancelled(true);
        }
    }

    private String genCaptcha() {
        String charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder random = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            random.append(charset.charAt(new Random().nextInt(charset.length() - 1)));
        }
        return random.toString();
    }

    private void sendPlayerToServer(Player player) {
        if (successServer != null && !successServer.isEmpty()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(successServer);
            player.sendPluginMessage(Captcha, "BungeeCord", out.toByteArray());
        }
    }

}
