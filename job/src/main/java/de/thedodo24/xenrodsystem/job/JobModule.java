package de.thedodo24.xenrodsystem.job;

import de.thedodo24.xenrodsystem.common.module.Module;
import de.thedodo24.xenrodsystem.common.module.ModuleManager;
import de.thedodo24.xenrodsystem.common.module.ModuleSettings;
import de.thedodo24.xenrodsystem.job.commands.JobCommand;
import de.thedodo24.xenrodsystem.job.commands.QuestCommand;
import de.thedodo24.xenrodsystem.job.listener.InventoryListener;
import de.thedodo24.xenrodsystem.job.listener.PlayerListener;
import de.thedodo24.xenrodsystem.job.listener.WorldListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

@ModuleSettings
@Getter
public class JobModule extends Module {

    @Getter
    private static JobModule instance;


    public JobModule(ModuleSettings settings, ModuleManager manager, JavaPlugin plugin) {
        super(settings, manager, plugin);
        instance = this;
    }



    @Override
    public void onEnable() {
        new JobCommand();
        new QuestCommand();
        startScheduler();
        registerListener(new InventoryListener(), new WorldListener(), new PlayerListener());
    }

    private void startScheduler() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance.getPlugin(), () -> {
            List<UUID> overtimed = PlayerListener.getKillCooldown().keySet().stream().filter(u -> PlayerListener.getKillCooldown().get(u) <= System.currentTimeMillis()).toList();
            overtimed.forEach(uuid -> {
                PlayerListener.getKillCooldown().remove(uuid);
                PlayerListener.getKillMap().remove(uuid);
            });
        }, 20, 20);
    }
}
