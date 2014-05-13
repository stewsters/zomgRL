package com.stewsters.zomgrl.entity.components.item

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog
import squidpony.squidcolor.SColor

class Equipment {
    Entity owner

    Slot slot
    boolean isEquiped = false

    int bonusMaxHp
    int bonusMaxInfection
    int bonusMaxStamina
    int bonusPower
    int bonusDefense
    int bonusMarksman


    public Equipment(Map params) {
        slot = params?.slot

        bonusMaxHp = params?.bonusMaxHp ?: 0
        bonusMaxInfection = params?.bonusMaxInfection ?: 0
        bonusMaxStamina = params?.bonusMaxStamina ?: 0
        bonusPower = params?.bonusPower ?: 0
        bonusDefense = params?.bonusDefense ?: 0
        bonusMarksman = params?.bonusMarksman ?: 0
    }

    public void toggleEquip(Entity holder) {
        if (isEquiped)
            dequip(holder)
        else
            equip(holder)
    }


    void equip(Entity holder) {

        Equipment oldEquipment = holder.inventory.getEquippedInSlot(slot)
        if (oldEquipment)
            oldEquipment.dequip(holder)

        isEquiped = true
        MessageLog.send("Equiped ${owner.name}", SColor.WHITE, [holder])
    }

    void dequip(Entity holder) {
        if (isEquiped) {
            isEquiped = false
            MessageLog.send("Dequipped ${owner.name} from ${slot.name}", SColor.WHITE, [holder])
        }

    }
}
