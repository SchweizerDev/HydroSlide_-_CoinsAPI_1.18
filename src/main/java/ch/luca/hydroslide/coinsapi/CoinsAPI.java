package ch.luca.hydroslide.coinsapi;

import ch.luca.hydroslide.coinsapi.mysql.MySQL;
import ch.luca.hydroslide.coinsapi.mysql.repository.CoinsRepository;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CoinsAPI extends JavaPlugin {

    @Getter
    private static CoinsAPI instance;

    @Getter
    private MySQL coinsMySQL;

    @Getter
    private CoinsRepository coinsRepository;

    @Getter
    private static String prefix = "§bHydroSlide §8» §7";

    @Override
    public void onEnable() {
        instance = this;
        this.coinsMySQL = new MySQL("localhost", "bungeecord", "root", "password", 3306);
        this.coinsRepository = new CoinsRepository(this.coinsMySQL);
        Bukkit.getConsoleSender().sendMessage(CoinsAPI.getPrefix() + "CoinsAPI wurde §aaktiviert§7!");
        Bukkit.getConsoleSender().sendMessage(CoinsAPI.getPrefix() + "Author§8: §eLuca §7| §7Version§8: §a1.0");
        Bukkit.getConsoleSender().sendMessage(CoinsAPI.getPrefix() + "MySQL try to connect...");
        Bukkit.getConsoleSender().sendMessage(CoinsAPI.getPrefix() + "All data has been §asuccessfully §7loaded!");
    }

    @Override
    public void onDisable() {

    }
}
