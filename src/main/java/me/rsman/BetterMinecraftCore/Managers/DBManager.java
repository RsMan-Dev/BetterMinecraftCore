package me.rsman.BetterMinecraftCore.Managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.rsman.BetterMinecraftCore.Entities.PlayerBaseAttr;

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
            connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://"+host+":"+port+"/"+database, user, password, new MysqlDatabaseType());
            TableUtils.createTableIfNotExists(connectionSource, PlayerBaseAttr.class);
            playerBaseAttrDao = DaoManager.createDao(connectionSource, PlayerBaseAttr.class);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }


    /*
    private static MysqlDataSource dataSource;

    public static boolean initConnection(){
        String host = (String)ConfigManager.getKey("db/config", "host", "String", "localhost");
        String port = (String)ConfigManager.getKey("db/config", "port", "String", "3306");
        String database = (String)ConfigManager.getKey("db/config", "database", "String", "fs");
        String user = (String)ConfigManager.getKey("db/config", "user", "String", "root");
        String password = (String)ConfigManager.getKey("db/config", "password", "String", "");

        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(host);
        dataSource.setPortNumber(Integer.parseInt(port));
        dataSource.setDatabaseName(database);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        try{
            dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        DBManager.dataSource = dataSource;
        return DBManager.initTables();
    }

    public static Connection getConnection(){
        try {
            return DBManager.dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static boolean initTables(){
        Connection conn = DBManager.getConnection();

        try {
            PreparedStatement base_stat_table = conn.prepareStatement(
            "CREATE TABLE IF NOT EXISTS player_base_stats" +
                "(" +
                "    uuid  CHAR(36) NOT NULL," +
                "    health BIGINT DEFAULT 100 NOT NULL," +
                "    damage BIGINT DEFAULT 0 NOT NULL," +
                "    defense BIGINT DEFAULT 0 NOT NULL," +
                "    strength BIGINT DEFAULT 0 NOT NULL," +
                "    speed BIGINT DEFAULT 0 NOT NULL," +
                "    critchance BIGINT DEFAULT 0 NOT NULL," +
                "    critdamage BIGINT DEFAULT 0 NOT NULL," +
                "    attackspeed BIGINT DEFAULT 0 NOT NULL," +
                "    intelligence BIGINT DEFAULT 0 NOT NULL," +
                "    mana BIGINT DEFAULT 100 NOT NULL," +
                "    PRIMARY KEY (uuid)" +
                ");"
            );
            base_stat_table.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }*/
}
