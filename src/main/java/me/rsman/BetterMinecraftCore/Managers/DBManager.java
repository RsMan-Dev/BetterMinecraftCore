package me.rsman.BetterMinecraftCore.Managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.table.TableUtils;
import me.rsman.BetterMinecraftCore.db.models.PlayerBaseAttr;

import java.sql.DriverManager;
import java.sql.SQLException;


public final class DBManager {
    public static JdbcPooledConnectionSource connectionSource;

    public static Dao<PlayerBaseAttr, String> playerBaseAttrDao;

    public static boolean initConnection(){
        String host = (String)ConfigManager.getKey("db/config", "host", "String", "localhost");
        String port = (String)ConfigManager.getKey("db/config", "port", "String", "3306");
        String database = (String)ConfigManager.getKey("db/config", "database", "String", "fs");
        String user = (String)ConfigManager.getKey("db/config", "user", "String", "root");
        String password = (String)ConfigManager.getKey("db/config", "password", "String", "");

        try{
            Logger.setGlobalLogLevel(Log.Level.ERROR);
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://"+host+":"+port+"/"+database, user, password);
            TableUtils.createTableIfNotExists(connectionSource, PlayerBaseAttr.class);
            playerBaseAttrDao = DaoManager.createDao(connectionSource, PlayerBaseAttr.class);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
}
