package riku32.captcha.player;

import riku32.captcha.Captcha;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;

import static riku32.captcha.Captcha.Config.*;

public class CaptchaPlayer {
    private Player player;
    private String captcha;

    private ItemStack[] contents;
    private ItemStack[] armour;
    private int tries;
    private long lastTime;

    public CaptchaPlayer(Player player, String captcha, Captcha Captcha) {
        this.player = player;
        this.captcha = captcha;

        contents = player.getInventory().getContents();
        armour = player.getInventory().getArmorContents();

        lastTime = System.currentTimeMillis();
        tries = 0;

        if (timeLimitEnabled.equals("true")) {
            player.getServer().getScheduler().scheduleSyncDelayedTask(Captcha, () -> {
                if (Captcha.getPlayerManager().getPlayer(player) != null) {
                    player.getPlayer().kickPlayer(prefix + " " + captchaFailMessage);
                }
            }, captchaTimeLimit * 20);
        }
    }

    public BufferedImage render() {

        BufferedImage image = new BufferedImage(130, 130, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // Set white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Title
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.setColor(Color.BLACK);
        g.drawString("Type this in chat", (int) ((image.getWidth() - g.getFontMetrics().getStringBounds("Type this in chat", g).getWidth()) / 2), 30);

        g.setFont(new Font("Arial", Font.BOLD, 10));
        String sTries = "Tries Left: ";
        g.setColor(Color.BLACK);
        g.drawString(sTries, (int) ((image.getWidth() - g.getFontMetrics().getStringBounds(sTries, g).getWidth()) / 2), 45);
        g.setColor((captchaTries - tries) == 1 ? Color.RED : Color.GREEN);
        g.drawString(String.valueOf((captchaTries - tries)), (int) (((image.getWidth() - g.getFontMetrics().getStringBounds(sTries, g).getWidth()) / 2) + g.getFontMetrics().getStringBounds(sTries, g).getWidth() + 2), 45);

        if (timeLimitEnabled.equals("true")) {
            String sTime = "Time Left: ";
            g.setColor(Color.BLACK);
            g.drawString(sTime, (int) ((image.getWidth() - g.getFontMetrics().getStringBounds(sTime, g).getWidth()) / 2), 55);
            g.setColor((captchaTimeLimit * 1000) - (System.currentTimeMillis() - lastTime) == 1000 ? Color.RED : Color.GREEN);
            g.drawString(new SimpleDateFormat("ss").format((captchaTimeLimit * 1000) - (System.currentTimeMillis() - lastTime)) + " sec", (int) (((image.getWidth() - g.getFontMetrics().getStringBounds(sTime, g).getWidth()) / 2) + g.getFontMetrics().getStringBounds(sTime, g).getWidth() + 2), 55);
        }

        // Actual captcha
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.BLACK);
        g.drawString(captcha, (int) ((image.getWidth() - g.getFontMetrics().getStringBounds(captcha, g).getWidth()) / 2), 105);

        return image;
    }

    public void resetInventory() {
        getPlayer().getInventory().setContents(contents);
        getPlayer().getInventory().setArmorContents(armour);
        getPlayer().updateInventory();
    }

    public CaptchaPlayer cleanPlayer() {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        getPlayer().updateInventory();
        tries = 0;
        return this;
    }

    public String getCaptcha() {
        return captcha;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

}
