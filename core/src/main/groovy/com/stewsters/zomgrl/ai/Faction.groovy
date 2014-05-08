package com.stewsters.zomgrl.ai;

/**
 * To be honest I have no Idea where this is going.  Ideally some kind of generic faction thing
 */
public enum Faction {

    human,
    zombie;

    public boolean likes(Faction other) {
        return (this == other)
    }

    public boolean hates(Faction other) {
        return (this != other)
    }

}
