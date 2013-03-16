zomgRL
======

Zombie Game Rogue Like


I'm starting a zombie based roguelike.  You will start in a small city and have to fight through the zombie hordes to reach an extraction point.
 This is my first roguelike, and is being done for the 7DRL Challenge 2013.

My initial plans include:
+ a randomly generated city
+ common weapons and items for salvage
+ zombies that wander around the town, and can hear noise over a distance
+ the world starts off with civilians in it, which quickly get eaten.

My tools:
+ Java7
+ Groovy 2.1.1
+ SquidLib 1.95 ( https://github.com/SquidPony/SquidLib)
+ Intellij


If you want to test the non-complete version, download it here:
https://github.com/stewsters/zomgRL/blob/master/build/libs/zomgRL-0.9.jar?raw=true

and run

java -jar zomgRL-0.9.jar

(or double click on it in Windows)

To compile from source you will need Java 7 and to install groovy and gradle.  I recommend using http://gvmtool.net/ for that.
Once you are set up, it will be something like:

git clone git://github.com/stewsters/zomgRL.git

cd zomgRL

gradle run


