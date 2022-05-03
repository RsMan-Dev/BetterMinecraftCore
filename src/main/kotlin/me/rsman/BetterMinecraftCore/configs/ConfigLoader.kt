package me.rsman.BetterMinecraftCore.configs

import me.rsman.BetterMinecraftCore.BetterMinecraftCore
import me.rsman.BetterMinecraftCore.BetterMinecraftCore.Companion.instance
import me.rsman.BetterMinecraftCore.configs.containers.*
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.introspector.Property
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.nodes.NodeTuple
import org.yaml.snakeyaml.nodes.Tag
import java.io.*
import java.lang.Exception
import java.nio.charset.StandardCharsets

object ConfigLoader {
    @JvmStatic
    fun getFile(configFile: String): File {
        var file = File(instance.dataFolder, "$configFile.yml")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            instance.saveResource("$configFile.yml", false)
            file = File(instance.dataFolder, "$configFile.yml")
        }
        return file
    }

    fun <T> loadConfig(configFile: String, tClass: Class<T>?): T? {
        return try {
            val yaml = Yaml(CustomClassLoaderConstructor(ConfigLoader::class.java.classLoader), representer)
            val file = getFile(configFile)
            val fileInputStream = FileInputStream(file)
            yaml.loadAs(fileInputStream, tClass)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun <T> saveConfig(configFile: String, obj: T) {
        try {
            val options = DumperOptions()
            options.splitLines = false
            options.indent = 2
            options.isPrettyFlow = true
            // Fix below - additional configuration
            options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK

            val yaml = Yaml(CustomClassLoaderConstructor(ConfigLoader::class.java.classLoader), representer, options)
            val file = getFile(configFile)
            val fileWriter = BufferedWriter(OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8))
            yaml.dump(obj, fileWriter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // if value of property is null, ignore it.
    val representer: Representer
        get() {
            val representer: Representer = object : Representer() {
                override fun representJavaBeanProperty(javaBean: Any?, property: Property?, propertyValue: Any?, customTag: Tag?): NodeTuple {
                    // if value of property is null, ignore it.
                    return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag)
                }
            }
            representer.propertyUtils.isSkipMissingProperties = true
            return representer
        }

    fun init() {
        GlobalConfigContainer.load()
        BmcItemContainer.load()
        AttributeLangContainer.load()
        EnchantLangContainer.load()
        BmcCraftContainer.load()
        MessagesLangContainer.load()
    }
}