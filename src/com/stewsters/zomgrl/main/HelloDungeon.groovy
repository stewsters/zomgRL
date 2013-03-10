package com.stewsters.zomgrl.main

import com.stewsters.zomgrl.ai.Faction
import com.stewsters.zomgrl.ai.LocalPlayer
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.entity.Fighter
import com.stewsters.zomgrl.game.Game
import com.stewsters.zomgrl.game.GameState
import com.stewsters.zomgrl.graphic.MessageLog
import com.stewsters.zomgrl.graphic.RenderConfig
import com.stewsters.zomgrl.graphic.StatusBar
import com.stewsters.zomgrl.input.CharacterInputListener
import com.stewsters.zomgrl.item.Inventory
import com.stewsters.zomgrl.map.LevelMap
import com.stewsters.zomgrl.map.gen.CityMapGenerator
import com.stewsters.zomgrl.map.gen.MapGenerator
import com.stewsters.zomgrl.sfx.DeathFunctions
import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.fov.BasicRadiusStrategy
import squidpony.squidgrid.fov.EliasLOS
import squidpony.squidgrid.fov.TranslucenceWrapperFOV
import squidpony.squidgrid.gui.awt.event.SGMouseListener
import squidpony.squidgrid.gui.swing.SwingPane
import squidpony.squidgrid.util.Direction

import javax.swing.*
import javax.swing.event.MouseInputListener
import java.awt.*

public class HelloDungeon {


    public SwingPane display
    public JFrame frame
    public LevelMap levelMap
    public Entity player

    public int selectX = 0
    public int selectY = 0


    public static void main(String[] args) {
        HelloDungeon helloDungeon = new HelloDungeon()
    }

    public HelloDungeon() {

        // Setup window
        RenderConfig.fov = new TranslucenceWrapperFOV()
        RenderConfig.los = new EliasLOS()
        RenderConfig.strat = BasicRadiusStrategy.CIRCLE
        SColorFactory.addPallet("light", SColorFactory.asGradient(RenderConfig.litNear, RenderConfig.litFar));

        // Generate map

        //MapGenerator mapGen = new StaticMapGenerator();
//        MapGenerator mapGen = new TestMapGenerator();
//        MapGenerator mapGen = new SimpleMapGenerator()
        MapGenerator mapGen = new CityMapGenerator()

        levelMap = mapGen.reGenerate()

        player = new Entity(map: levelMap, x: mapGen.playerStartX, y: mapGen.playerStartY,
                ch: '@', name: 'Sir Jack', color: SColor.WHITE, blocks: true,
                priority: 130, faction: Faction.human,
                ai: new LocalPlayer(),
                inventory: new Inventory(),
                fighter: new Fighter(hp: 10, defense: 1,
                        marksman: 2, power: 2,
                        max_infection: 4,
                        max_stamina:10,
                        deathFunction: DeathFunctions.playerDeath)

        )
        player.ai.owner = player

        // set up display
        frame = new JFrame("ZOMG Rogue Like")
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frame.setLayout(new BorderLayout())

        display = new SwingPane()
        display.initialize(RenderConfig.screenWidth, RenderConfig.screenHeight, new Font("Ariel", Font.BOLD, 12))
        clear(display)

        frame.add(display, BorderLayout.SOUTH)
        frame.setVisible(true)

        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.repaint()


        CharacterInputListener dil = new CharacterInputListener(this, player)

        int cellWidth = display.getCellDimension().width
        int cellHeight = display.getCellDimension().height
        MouseInputListener mil = new SGMouseListener(cellWidth, cellHeight, dil)
        display.addMouseListener(mil) //listens for clicks and releases
        display.addMouseMotionListener(mil) //listens for movement based events
        frame.addKeyListener(dil)

        render()
        Game.state = GameState.playing

    }


    public void render() {


        if (Game.state == GameState.selecting) {
            levelMap.render(display: display, player: player, viewX: selectX, viewY: selectY)
        } else {
            levelMap.render(display: display, player: player)
        }

        //render stats
        StatusBar.render(display, 0, (2 * RenderConfig.windowRadiusY) + 2, 10, 'hp', player?.fighter?.hp ?: 0, player?.fighter?.max_hp ?: 0, SColor.RED)
        MessageLog.render(display)

        //render inventory
        player.inventory.render(display)

        //done rendering this frame
        display.refresh();
    }


    public void clear(SwingPane display) {
        for (int x = 0; x < RenderConfig.screenWidth; x++) {
            for (int y = 0; y < RenderConfig.screenHeight; y++) {
                display.clearCell(x, y)
            }
        }
        display.refresh()
    }

    public void stepSim() {
        //Run sim

        //This is not an efficient way to do this..
        levelMap.objects.toArray().each { Entity entity ->
            if (entity.ai)
                entity.ai.takeTurn()
        }

        levelMap.objects.sort({ it.priority })
        render()
    }


    public void move(Direction dir) {
        if (Game.state == GameState.playing) {

            int x = player.x + dir.deltaX
            int y = player.y + dir.deltaY

            //check for legality of move based solely on map boundary
            if (x >= 0 && x < levelMap.xSize && y >= 0 && y < levelMap.ySize) {
                player.moveOrAttack(dir.deltaX, dir.deltaY)
                stepSim()
            }
        } else if (Game.state == GameState.selecting) {
            selectX += dir.deltaX
            selectY += dir.deltaY
            render()
        }
    }

    public void fire() {
        if (Game.state == GameState.playing) {
            //select a target in view
            // shoot at it

            stepSim()
        }


    }

    public void grab() {
        if (Game.state == GameState.playing) {
            //To change body of created methods use File | Settings | File Templates.
            player.grab()
            stepSim()
        }

    }

    public void standStill() {
        if (Game.state == GameState.playing) {
            stepSim()
        }
    }

    public void drop() {
        if (Game.state == GameState.playing) {
            player.drop()
            stepSim()
        }
    }

    public void useItem(int id) {
        if (Game.state == GameState.playing) {
            if (player.inventory.useById(id - 1)) {
                stepSim()
            }
        }
    }

    private def oldState

    public void inspect() {

        if (Game.state != GameState.selecting) {
            oldState = Game.state
            Game.state = GameState.selecting
            selectX = player.x
            selectY = player.y
            display.setCellBackground(RenderConfig.windowRadiusX, RenderConfig.windowRadiusY, SColor.EDO_BROWN)
            render()

        } else {
            Game.state = oldState
            display.setCellBackground(RenderConfig.windowRadiusX, RenderConfig.windowRadiusY, SColor.BLACK)
            render()

        }
    }

    public void reload() {
        //doesnt do anything
    }
}