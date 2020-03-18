package riku32.captcha.events;

import riku32.captcha.Captcha;
import riku32.captcha.player.CaptchaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.List;

public class MapEvent implements Listener {
    private Captcha Captcha;

    public MapEvent(Captcha Captcha) {
        this.Captcha = Captcha;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMapInitilize(MapInitializeEvent event) {

        MapView map = event.getMap();
        List<MapRenderer> old = map.getRenderers();

        map.setScale(MapView.Scale.NORMAL);
        map.getRenderers().forEach(map::removeRenderer);

        map.addRenderer(new MapRenderer() {
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                CaptchaPlayer p = Captcha.getPlayerManager().getPlayer(player);
                if (p == null) {
                    old.forEach(map::addRenderer);
                } else {
                    mapCanvas.drawImage(0, 0, p.render());
                }
            }
        });
    }
}
