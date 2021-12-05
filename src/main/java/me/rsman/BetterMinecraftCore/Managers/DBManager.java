package me.rsman.BetterMinecraftCore.Managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.table.TableUtils;
import me.rsman.BetterMinecraftCore.BetterMinecraftCore;
import me.rsman.BetterMinecraftCore.configs.containers.GlobalConfigContainer;
import me.rsman.BetterMinecraftCore.db.models.PlayerBaseAttr;

import java.sql.DriverManager;
import java.sql.SQLException;


public final class DBManager {
    public static JdbcPooledConnectionSource connectionSource;

    public static Dao<PlayerBaseAttr, String> playerBaseAttrDao;

    public static void initConnection(){
        String host = GlobalConfigContainer.getInstance().getDb_host();
        String port = GlobalConfigContainer.getInstance().getDb_port();
        String database = GlobalConfigContainer.getInstance().getDb_database();
        String user = GlobalConfigContainer.getInstance().getDb_user();
        String password = GlobalConfigContainer.getInstance().getDb_password();

        BetterMinecraftCore.getInstance().getLogger().info("§3Initializing DB...");
        try{
            Logger.setGlobalLogLevel(Log.Level.ERROR);
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://"+host+":"+port+"/"+database, user, password);
            TableUtils.createTableIfNotExists(connectionSource, PlayerBaseAttr.class);
            playerBaseAttrDao = DaoManager.createDao(connectionSource, PlayerBaseAttr.class);

            BetterMinecraftCore.getInstance().getLogger().info("§bDB connected!");
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            BetterMinecraftCore.getInstance().getLogger().warning("§4Database connection not initialized, please setup config for mariaDB database, disabling plugin");
            BetterMinecraftCore.getInstance().getServer().getPluginManager().disablePlugin(BetterMinecraftCore.getInstance());
        }
    }
}
