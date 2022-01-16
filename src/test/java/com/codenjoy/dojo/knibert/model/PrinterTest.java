package com.codenjoy.dojo.knibert.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.knibert.model.artifacts.Apple;
import com.codenjoy.dojo.knibert.model.artifacts.BasicWalls;
import com.codenjoy.dojo.knibert.model.artifacts.Stone;
import com.codenjoy.dojo.knibert.services.GameSettings;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrinterTest {

    private static final int BOARD_SIZE = 7;
    private Printer printer;
    private Hero hero;
    private Field board;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();
    private EventListener listener = mock(EventListener.class);

    @Before
    public void init() {
        board = mock(Field.class);
        when(board.size()).thenReturn(BOARD_SIZE);
        when(board.getApple()).thenReturn(null);
        when(board.getStone()).thenReturn(null);
        when(board.getWalls()).thenReturn(new Walls());
        when(board.hero()).thenReturn(null);

        printer = printerFactory.getPrinter(new BoardReader<Player>() {
            @Override
            public int size() {
                return BOARD_SIZE;
            }

            @Override
            public void addAll(Player player, Consumer<Iterable<? extends Point>> processor) {
                processor.accept(new HashSet<>(){{
                    board.getWalls().forEach(this::add);
                    if (board.hero() != null) {
                        board.hero().forEach(this::add);
                    }
                    add(board.getApple());
                    add(board.getStone());
                    remove(null);
                }});
            }
        }, null);
    }

    @Test
    public void checkCleanBoard() {
        assertEquals("       \n       \n       \n       \n       \n       \n       \n", printer.print());
    }

    @Test
    public void checkPrintWall() {
        Walls walls = new Walls();
        walls.add(2, 2);
        walls.add(3, 3);
        walls.add(4, 4);
        when(board.getWalls()).thenReturn(walls);

        assertEquals(
                "       \n" +
                "       \n" +
                "    ☼  \n" +
                "   ☼   \n" +
                "  ☼    \n" +
                "       \n" +
                "       \n", printer.print());
    }

    @Test
    public void checkPrintBasicWalls() {   // тут тестируем больше BasicWalls чем printer
        when(board.getWalls()).thenReturn(new BasicWalls(BOARD_SIZE));

        assertEquals(
                "☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n", printer.print());
    }

    @Test
    public void checkPrintApple() {
        when(board.getApple()).thenReturn(new Apple(2, 2));

        assertEquals(
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "  ☺    \n" +
                "       \n" +
                "       \n", printer.print());
    }

    @Test
    public void checkPrintStone() {
        when(board.getStone()).thenReturn(new Stone(4, 4));

        assertEquals(
                "       \n" +
                "       \n" +
                "    ☻  \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n", printer.print());
    }

    @Test
    public void checkPrintHero() {
        shouldHero();
        moveUp();
        moveRight();
        moveUp();
        moveRight();

        assertHero( 
                "       \n" +
                "    ╔► \n" +
                "   ╔╝  \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    private void assertHero(String expected) {
        when(board.hero()).thenReturn(hero);
        assertEquals(expected, printer.print());
    }

    private void moveUp() {
        move(0, 1);
        hero.up();
    }

    private void move(int dx, int dy) {
        hero.move(hero.getX() + dx, hero.getY() + dy);
        hero.grow();
    }

    private void moveDown() {
        move(0, -1);
        hero.down();
    }

    private void moveLeft() {
        move(-1, 0);
        hero.left();
    }

    private void shouldHero() {
        hero = new Hero(3, 3);

        when(board.createHero()).thenReturn(hero);
        when(board.freeRandom(any(Player.class))).thenReturn(null);
        listener = mock(EventListener.class);
        Player player = new Player(listener, mock(GameSettings.class));
        player.newHero(board);

        hero.right();
    }

    private void moveRight() {
        move(1, 0);
        hero.right();
    }

    @Test
    public void checkPrintHeroTailRight() {
        shouldHero();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "  ╘►   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailLeft() {
        shouldHero();
        moveLeft();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "  ◄╕   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailDown() {
        shouldHero();
        moveDown();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "   ▼   \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailUp() {
        shouldHero();
        moveUp();
        assertHero(
                "       \n" +
                "       \n" +
                "   ▲   \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailVerticalUp() {
        shouldHero();
        moveUp();
        moveUp();
        assertHero(
                "       \n" +
                "   ▲   \n" +
                "   ║   \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailVerticalDown() {
        shouldHero();
        moveDown();
        moveDown();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "   ║   \n" +
                "   ▼   \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailHorizontalLeft() {
        shouldHero();
        moveLeft();
        moveLeft();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                " ◄═╕   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }


    @Test
    public void checkPrintHeroTailHorizontalRight() {
        shouldHero();
        moveRight();
        moveRight();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╘═► \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailRotateLeftUp() {
        shouldHero();
        moveLeft();
        moveUp();
        assertHero(
                "       \n" +
                "       \n" +
                "  ▲    \n" +
                "  ╚╕   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailRotateLeftDown() {
        shouldHero();
        moveLeft();
        moveDown();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "  ╔╕   \n" +
                "  ▼    \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailRotateUpLeft() {
        shouldHero();
        moveUp();
        moveLeft();
        assertHero(
                "       \n" +
                "       \n" +
                "  ◄╗   \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailRotateUpRight() {
        shouldHero();
        moveUp();
        moveRight();
        assertHero(
                "       \n" +
                "       \n" +
                "   ╔►  \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailRotateDownLeft() {
        shouldHero();
        moveDown();
        moveLeft();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "  ◄╝   \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailRotateDownRight() {
        shouldHero();
        moveDown();
        moveRight();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "   ╚►  \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailRotateRightDown() {
        shouldHero();
        moveRight();
        moveDown();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╘╗  \n" +
                "    ▼  \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHeroTailHorizontalRightUp() {
        shouldHero();
        moveRight();
        moveUp();
        assertHero(
                "       \n" +
                "       \n" +
                "    ▲  \n" +
                "   ╘╝  \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintHero2() {
        shouldHero();
        moveDown();
        moveLeft();
        moveDown();
        moveLeft();
        assertHero(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "  ╔╝   \n" +
                " ◄╝    \n" +
                "       \n");
    }


}
