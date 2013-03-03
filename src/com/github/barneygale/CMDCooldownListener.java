package com.github.barneygale;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CMDCooldownListener implements Listener {
	private final CMDCooldown plugin;
    CMDCooldownListener(CMDCooldown instance) {
        plugin = instance;
    }

	@EventHandler(priority = EventPriority.LOW)
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        
        Player player = event.getPlayer();
        if (player.hasPermission("cmdcooldown.override")) return;
        
        int wait = plugin.getWait(player.getDisplayName(), event.getMessage());
        if (wait > 0) {
        	player.sendMessage(ChatColor.RED + "[CMDCooldown] Please wait " + wait + " seconds!");
        	event.setCancelled(true);
        }
    }
}
