package me.rsman.BetterMinecraftCore.Managers.Command.Lang;

import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;

public enum MessageKeys implements MessageKeyProvider {
    //general
    BMC_HELP,
    TOO_FEW_ARGUMENTS,
    INCORRECT_VALUE,
    INCORRECT_NUMBER,
    INCORRECT_NAME,
    INCORRECT_ATTRIBUTE,
    INCORRECT_ENCHANT,
    COMING_SOON,
    COMING_SOON_CONF_POSSIBLE,
    NEED_HOLD_ITEM,
    INVENTORY_FULL,

    //items
    ITEM_ATTRIBUTE_SET,
    ITEM_ATTRIBUTE_REMOVE,
    ITEM_ENCHANTMENT_SET,
    ITEM_ENCHANTMENT_REMOVE,
    ITEM_NAME_SET,
    ITEM_MATERIAL_DATA_SET,
    ITEM_REV_SET,
    ITEM_GIVEN,
    ITEM_SAVED,
    ITEM_NEVER_SAVED,
    ITEM_DELETED,
    ITEM_LORE_SET,
    ITEM_UNBREAKABLE_SET,
    ITEM_RENAMABLE_SET,

    //player
    PLAYER_BASE_ATTRIBUTE_SET,
    PLAYER_ATTRIBUTE,

    //crafts
    INVALID_NAME_KEY_PAIR,
    INVALID_RESULT,
    NEED_TO_FORCE,
    NEED_RESULT
    ;

    private static final String PREFIX = "bmc";

    private final MessageKey chatKey = MessageKey.of(PREFIX + ".command.result." + this.name().toLowerCase());

    @Override
    public MessageKey getMessageKey() {
        return chatKey;
    }
}
