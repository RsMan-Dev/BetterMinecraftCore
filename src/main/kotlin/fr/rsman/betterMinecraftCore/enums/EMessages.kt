package fr.rsman.betterMinecraftCore.enums

import java.util.ArrayList

enum class EMessages(var key: String){
    RENAME_NOT_ALLOWED("rename_not_allowed");

    companion object {
        val enumKeys: List<String>
            get() {
                val keys: MutableList<String> = ArrayList()
                for (ee in EEnchants.values()) {
                    keys.add(ee.name)
                }
                return keys
            }
    }
}