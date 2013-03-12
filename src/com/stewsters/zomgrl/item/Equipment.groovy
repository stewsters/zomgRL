package com.stewsters.zomgrl.item

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog

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
            dequip()
        else
            equip(holder)
    }


    void equip(Entity holder) {

        Equipment oldEquipment = holder.inventory.getEquippedInSlot(slot)
        if (oldEquipment)
            oldEquipment.dequip()

        isEquiped = true
        MessageLog.send("Equiped ${owner.name}")
    }

    void dequip() {
        if (!isEquiped) {
            return
        } else {
            isEquiped = false
            MessageLog.send("Dequipped ${owner.name} from ${slot.name}")
        }

    }
}
