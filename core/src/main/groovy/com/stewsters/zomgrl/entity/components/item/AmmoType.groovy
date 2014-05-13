package com.stewsters.zomgrl.entity.components.item

public enum AmmoType {
    rifle("5.56x45 mm"),
    pistol("9mm"),
    shotgun("12 gauge")


    String technicalName

    public AmmoType(String technicalName) {
        this.technicalName = technicalName
    }
}