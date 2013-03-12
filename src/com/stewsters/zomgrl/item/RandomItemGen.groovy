package com.stewsters.zomgrl.item

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.map.LevelMap
import com.stewsters.zomgrl.sfx.ItemFunctions
import squidpony.squidcolor.SColor

class RandomItemGen {

    public static def spawnChance = ['AntiViral': 10,
            'Bandages': 20,
            'Beef Jerkey': 20,
            "24pk Monster": 10,
            "Pump Shotgun": 10,
            "AR-15": 10,
            "Beretta 9mm": 10,
            'Baseball Bat': 20,
            "Machete":10,
            "Winter Coat":10,
            'Hockey Mask':10
    ]

    private static String getChoice(Map choicesMap) {
        int totalChances = spawnChance.values().sum() //check that shit out, groovy ftw
        int dice = MathUtils.getIntInRange(0, totalChances)
        int runningTotal = 0
        for (def keyValue : choicesMap) {
            runningTotal += keyValue.value
            if (dice <= runningTotal)
                return keyValue.key
        }
        return null
    }



    public static Entity getRandomItem(LevelMap map, int x, int y) {
        createFromName(map, x, y, getChoice(spawnChance))
    }

    public static void createFromName(LevelMap map, int x, int y, String name) {
        switch (name) {
            case ('AntiViral'):
                new Entity(map: map, x: x, y: y,
                        ch: 'a', name: 'AntiViral', color: SColor.AZURE,
                        itemComponent: new Item(useFunction: ItemFunctions.antiviral)
                )
                break

            case ('Bandages'):
                new Entity(map: map, x: x, y: y,
                        ch: 'b', name: 'Bandages', color: SColor.AZURE,
                        itemComponent: new Item(useFunction: ItemFunctions.bandage)
                )
                break
            case ('Beef Jerkey'):
                new Entity(map: map, x: x, y: y,
                        ch: 's', name: 'Beef Jerkey', color: SColor.AZURE,
                        itemComponent: new Item(useFunction: ItemFunctions.eat)
                )
                break
            case ("24pk Monster"):
                new Entity(map: map, x: x, y: y,
                        ch: '?', name: "24pk Monster", color: SColor.GREEN,
                        itemComponent: new Item(useFunction: ItemFunctions.heartExplosion)
                )
                break
            case ("Pump Shotgun"):
                new Entity(map: map, x: x, y: y,
                        ch: 'w', name: "Pump Shotgun", color: SColor.ORANGE,
                        itemComponent: new Item(useFunction: ItemFunctions.gunPumpShotGun),
                        equipment: new Equipment(slot: Slot.rightHand)
                )
                break
            case ("AR-15"):
                new Entity(map: map, x: x, y: y,
                        ch: 'W', name: "AR-15", color: SColor.AMBER,
                        itemComponent: new Item(useFunction: ItemFunctions.gunAR15),
                        equipment: new Equipment(slot: Slot.rightHand)
                )
                break
            case ("Beretta 9mm"):
                new Entity(map: map, x: x, y: y,
                        ch: 'w', name: "Beretta 9mm", color: SColor.AMARANTH,
                        itemComponent: new Item(useFunction: ItemFunctions.gunBerreta),
                        equipment: new Equipment(slot: Slot.rightHand)
                )
                break
            case ("Baseball Bat"):
                new Entity(map: map, x: x, y: y,
                        ch: '/', name: 'Baseball Bat', color: SColor.GOLD,
                        equipment: new Equipment(slot: Slot.rightHand, bonusPower: 4)
                )
                break
            case ("Machete"):
                new Entity(map: map, x: x, y: y,
                        ch: '/', name: 'Machete', color: SColor.SILVER,
                        equipment: new Equipment(slot: Slot.rightHand, bonusPower:6)
                )
                break
            case('Winter Coat'):
                new Entity(map: map, x: x, y: y,
                        ch: 'c', name: 'Winter Coat', color: SColor.WHITE,
                        equipment: new Equipment(slot: Slot.chest, bonusDefense: 1)
                )
                break
            case('Hockey Mask'):
                new Entity(map: map, x: x, y: y,
                        ch: 'm', name: 'Hockey Mask', color: SColor.WHITE,
                        equipment: new Equipment(slot: Slot.head, bonusDefense: 1)
                )
                break
            default:
                new Entity(map: map, x: x, y: y,
                        ch: 'r', name: 'rock', color: SColor.GRAY,
                        equipment: new Equipment(slot: Slot.rightHand)
                )
        }


    }
}
