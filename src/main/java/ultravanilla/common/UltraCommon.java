package ultravanilla.common;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.bukkit.plugin.java.JavaPlugin;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class UltraCommon extends JavaPlugin {
    private Path pluginFolder = Paths.get(getDataFolder().getAbsolutePath());

    public Options rocksdbOptions;
    public static RocksDB rocksdb;

    public static HikariDataSource ds;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        RocksDB.loadLibrary();

        rocksdbOptions = new Options();

        rocksdbOptions.setCreateIfMissing(true);

        try {
            rocksdb = RocksDB.open(rocksdbOptions, pluginFolder.resolve("database").toString());
        } catch (RocksDBException err) {
            throw new RuntimeException(err);
        }

        if (getConfig().getBoolean("database.enabled")) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(getConfig().getString("database.uri"));
            config.setUsername(getConfig().getString("database.username"));
            config.setPassword(getConfig().getString("database.password"));
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("characterEncoding", "utf8");
            config.addDataSourceProperty("useUnicode", "true");
            config.addDataSourceProperty("connectionInitSql", "SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci");

            ds = new HikariDataSource(config);
        }
    }

    @Override
    public void onDisable() {
        rocksdb.close();
        rocksdbOptions.close();
    }
}
