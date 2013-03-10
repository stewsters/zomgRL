package com.stewsters.zomgrl.item

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog

class Equipment {
    Entity owner

    Slot slot
    boolean isEquiped = false

    public Equipment(Map params){
        slot = params?.slot
    }

    public void toggleEquip(Entity holder){
        if (isEquiped)
            dequip()
        else
            equip(holder)
    }


    void equip(Entity holder){

        Equipment oldEquipment = holder.inventory.getEquippedInSlot(slot)
        if (oldEquipment)
            oldEquipment.dequip()

        isEquiped=true
        MessageLog.send("Equiped ${owner.name}")
    }

    void dequip() {
        if (!isEquiped){
            return
        }else{
            isEquiped=false
            MessageLog.send("Dequipped ${owner.name} from ${slot.name}")
        }

    }
}
