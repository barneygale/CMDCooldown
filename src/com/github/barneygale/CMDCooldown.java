package com.github.barneygale;

import java.io.File;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

public class CMDCooldown extends JavaPlugin {
    private CMDCooldownListener listener = new CMDCooldownListener(this);
    
    private HashMap<String, Integer> cache;    //player,command --> expiry time
    private HashMap<String, Integer> commands; //command --> cooldown length
    
    public void onEnable() {
        // get config
        File config_file = new File(getDataFolder(), "config.yml");
        if (!config_file.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        
        // get commands
        cache = new HashMap<String, Integer>();
        commands = new HashMap<String, Integer>();
        for (String s : getConfig().getStringList("commands")) {
            try {
                String[] p = s.split(",");
                commands.put(p[0], Integer.parseInt(p[1]));
            }
            catch (Exception e) {
                getLogger().info("invalid config: "+s);
            }
        }
        
        //register listener
        getServer().getPluginManager().registerEvents(listener, this);
    }
    private int getTime() {
        return (int) (System.currentTimeMillis() / 1000L);
    }
    private int getCooldown(String command) {
        for(String k : commands.keySet()) {
            if (command.startsWith(k)) {
                return commands.get(k).intValue();
            }
        }
        return 0;
    }
    public int getWait(String player, String command) {
        int cooldown = getCooldown(command);
        if (cooldown == 0) return 0;
        
        String key = player + ',' + command;
        Integer t = cache.get(key);
        if (t != null) {
            int diff = t - getTime();
            if (diff > 0) {
                return diff;
            } else {
                cache.remove(key);
            }
        }
        cache.put(key, getTime() + cooldown);
        return 0;
    }
    
}
