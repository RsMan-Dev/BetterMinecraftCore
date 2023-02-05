package fr.rsman.betterMinecraftCore.enums

enum class EDropSourceType(val key: String) {
    ENTITY("entity"),
    BLOCK("block"),
    ;

    companion object {
        val keys: List<String>
            get() = values().toList().map { it.key }

        fun fromKey(key: String) : EDropSourceType? = values().find { it.key == key }
    }
}