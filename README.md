zomgRL
======

Zombie Groovy Rogue Like


I'm starting a zombie based roguelike.  You will start in a small city and have to exterminate the zombie hordes.
This is my first roguelike, and is being done for the 7DRL Challenge 2013.

My initial plans include:
+ A randomly generated city
+ Weapons and items that can be collected and used
+ Zombies that wander around the town, and can hear noise over a distance
+ The world starts off with civilians in it, which quickly get eaten.

My tools:
+ Java7
+ Groovy 2.3.0
+ SquidLib 1.95 ( https://github.com/SquidPony/SquidLib)
+ Intellij

Download
========
You can get the app at end of the 7drl here:

https://github.com/stewsters/zomgRL/blob/master/build/libs/zomgRL-1.0.jar?raw=true

You will need to have a java 7 jre installed and run

```bash
java -jar zomgRL-1.0.jar
```

(or double click on it in Windows)


Controls
========
```
Movement: numpad, vi keys, or arrow keys(if you don't like diagonal movement)

Melee: Bump into enemies

g: Pick up

f: fire gun (you will need to find one first)

d: drop the last item you picked up

1-8: use/equip item in that slot (use ammo to add them to your stockpile)

shift: move to sprint or get an additional attack.  It consumes stamina.
```

Compile + Run
=============
To compile from source you will need Java 7 and to install groovy and gradle.  I recommend using http://gvmtool.net/ for that.
Once you are set up, it will be something like:

```bash
git clone git://github.com/stewsters/zomgRL.git

git submodule init
git submodule update

cd zomgRL

gradle run
```
