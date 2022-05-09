package me.rsman.BetterMinecraftCore.interfaces

enum class CoreSourceFrom{
    ItemsAdder,
    MythicMobs,
    Vanilla
}

data class ItemDropPattern(val id: String, val patternId: String, val minimum: Int, val maximum: Int, val chance: Double, val sourceFrom: CoreSourceFrom){
    companion object{
        fun parsePattern(input: String): ItemDropPattern?{
            try{
                val splitted = input.split(" ")
                if(splitted.size > 3) return null
                var itemId: String
                var patternId: String
                var sourceFrom: CoreSourceFrom
                with(splitted[0]){
                    patternId = toString()
                    when{
                        startsWith("m.") -> {
                            itemId = substring(2)
                            sourceFrom = CoreSourceFrom.Vanilla
                        }
                        startsWith("ia.") -> {
                            itemId = substring(3)
                            sourceFrom = CoreSourceFrom.ItemsAdder
                        }
                        startsWith("mm.") -> {
                            itemId = substring(3)
                            sourceFrom = CoreSourceFrom.MythicMobs
                        }
                        else -> return null
                    }
                }
                var minimum: Int? = null
                var maximum: Int? = null
                if(splitted.size > 1){
                    val numRangeSplitted = splitted[1].split("-")
                    if(numRangeSplitted.size <= 2){
                        minimum = numRangeSplitted[0].toInt()
                        maximum = if(numRangeSplitted.size == 2){
                            numRangeSplitted[1].toInt()
                        } else {
                            minimum
                        }
                    }
                }
                var chance: Double? = null
                if(splitted.size > 2){
                    chance = splitted[2].toDouble()
                }
                return ItemDropPattern(itemId, patternId, minimum?:1, maximum?:1, chance?:1.0, sourceFrom)
            }catch(e:Exception){
                return null
            }
        }
    }
}
