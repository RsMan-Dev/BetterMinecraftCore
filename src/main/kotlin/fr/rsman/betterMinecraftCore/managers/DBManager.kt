package fr.rsman.betterMinecraftCore.managers

import fr.rsman.betterMinecraftCore.models.PlayerBaseAttr
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource
import com.j256.ormlite.dao.Dao
import fr.rsman.betterMinecraftCore.configs.containers.GlobalConfigContainer
import java.sql.DriverManager
import com.j256.ormlite.table.TableUtils
import com.j256.ormlite.dao.DaoManager
import java.sql.SQLException
import com.j256.ormlite.logger.Log
import com.j256.ormlite.logger.Logger
import com.mysql.cj.jdbc.Driver

object DBManager {
    private var connectionSource: JdbcPooledConnectionSource? = null
    var playerBaseAttrDao: Dao<PlayerBaseAttr?, String?>? = null
    fun initConnection() {
        val host = GlobalConfigContainer.instance!!.db_host
        val port = GlobalConfigContainer.instance!!.db_port
        val database = GlobalConfigContainer.instance!!.db_database
        val user = GlobalConfigContainer.instance!!.db_user
        val password = GlobalConfigContainer.instance!!.db_password
        fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§3Initializing DB...")
        try {
            Logger.setGlobalLogLevel(Log.Level.ERROR)
            DriverManager.registerDriver(Driver())
            connectionSource = JdbcPooledConnectionSource("jdbc:mysql://$host:$port/$database", user, password)
            TableUtils.createTableIfNotExists(connectionSource, PlayerBaseAttr::class.java)
            playerBaseAttrDao = DaoManager.createDao(connectionSource, PlayerBaseAttr::class.java)
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.info("§bDB connected!")
        } catch (throwables: SQLException) {
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.severe(throwables.toString())
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.logger.warning("§4Database connection not initialized, please setup config for mariaDB database, disabling plugin")

            //let plugin finish instanciation to avoid null crash
            fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.server.scheduler.runTaskLater(fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance, Runnable {
                fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance.server.pluginManager.disablePlugin(fr.rsman.betterMinecraftCore.BetterMinecraftCore.instance)
             }, 0)
        }
    }
}