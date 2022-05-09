package me.rsman.BetterMinecraftCore.Managers

import me.rsman.BetterMinecraftCore.models.PlayerBaseAttr
import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource
import com.j256.ormlite.dao.Dao
import me.rsman.BetterMinecraftCore.configs.containers.GlobalConfigContainer
import java.sql.DriverManager
import com.j256.ormlite.table.TableUtils
import com.j256.ormlite.dao.DaoManager
import java.sql.SQLException
import com.j256.ormlite.logger.Log
import com.j256.ormlite.logger.Logger
import com.mysql.cj.jdbc.Driver

object DBManager {
    var connectionSource: JdbcPooledConnectionSource? = null
    var playerBaseAttrDao: Dao<PlayerBaseAttr?, String?>? = null
    fun initConnection() {
        val host = GlobalConfigContainer.instance!!.db_host
        val port = GlobalConfigContainer.instance!!.db_port
        val database = GlobalConfigContainer.instance!!.db_database
        val user = GlobalConfigContainer.instance!!.db_user
        val password = GlobalConfigContainer.instance!!.db_password
        BetterMinecraftCore.instance.logger.info("§3Initializing DB...")
        try {
            Logger.setGlobalLogLevel(Log.Level.ERROR)
            DriverManager.registerDriver(Driver())
            connectionSource = JdbcPooledConnectionSource("jdbc:mysql://$host:$port/$database", user, password)
            TableUtils.createTableIfNotExists(connectionSource, PlayerBaseAttr::class.java)
            playerBaseAttrDao = DaoManager.createDao(connectionSource, PlayerBaseAttr::class.java)
            BetterMinecraftCore.instance.logger.info("§bDB connected!")
        } catch (throwables: SQLException) {
            BetterMinecraftCore.instance.logger.severe(throwables.toString())
            BetterMinecraftCore.instance.logger.warning("§4Database connection not initialized, please setup config for mariaDB database, disabling plugin")

            //let plugin finish instanciation to avoid null crash
            BetterMinecraftCore.instance.server.scheduler.runTaskLater(BetterMinecraftCore.instance, Runnable {
                BetterMinecraftCore.instance.server.pluginManager.disablePlugin(BetterMinecraftCore.instance)
             }, 0)
        }
    }
}