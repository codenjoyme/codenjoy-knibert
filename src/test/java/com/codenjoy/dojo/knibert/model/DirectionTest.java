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


import com.codenjoy.dojo.knibert.model.artifacts.EmptySpace;
import com.codenjoy.dojo.knibert.model.artifacts.Tail;
import com.codenjoy.dojo.knibert.services.GameSettings;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DirectionTest {

    private Field board;
    private Hero hero;
    private EventListener listener;

    @Before
    public void setup() {
        board = mock(Field.class);
        when(board.getSize()).thenReturn(100);
        when(board.getAt(any(PointImpl.class))).thenReturn(new EmptySpace(pt(0, 0)));
        when(board.freeRandom(any(Player.class))).thenReturn(null);

        hero = new Hero(50, 50);
        when(board.createHero()).thenReturn(hero);
        listener = mock(EventListener.class);
        Player player = new Player(listener, mock(GameSettings.class));
        player.newHero(board);
    }

    @Test
    public void shouldHeroTailDirectionInvertedToHead_whenHeroLengthIs2() {
        assertHeadAntTail(50, 50, Direction.RIGHT, Direction.LEFT);

        heroUp();
        assertHeadAntTail(50, 51, Direction.UP, Direction.DOWN);

        heroLeft();
        assertHeadAntTail(49, 51, Direction.LEFT, Direction.RIGHT);

        heroDown();
        assertHeadAntTail(49, 50, Direction.DOWN, Direction.UP);

        heroRight();
        assertHeadAntTail(50, 50, Direction.RIGHT, Direction.LEFT);
    }

    @Test
    public void shouldHeroTailDirection_whenHeroLengthIs3() {
        heroGrow();
        assertHeadAntTail(51, 50, Direction.RIGHT, Direction.LEFT);

        heroUp();
        assertHeadAntTail(51, 51, Direction.UP, Direction.LEFT);

        heroLeft();
        assertHeadAntTail(50, 51, Direction.LEFT, Direction.DOWN);

        heroDown();
        assertHeadAntTail(50, 50, Direction.DOWN, Direction.RIGHT);

        heroRight();
        assertHeadAntTail(51, 50, Direction.RIGHT, Direction.UP);

        heroWalk();
        assertHeadAntTail(52, 50, Direction.RIGHT, Direction.LEFT);
    }

    @Test
    public void shouldBodyDirection_whenHeroLengthIs3_goCounterclockwise() {
        heroGrow();
        assertBody(50, 50, BodyDirection.HORIZONTAL);

        heroUp();
        assertBody(51, 50, BodyDirection.TURNED_LEFT_UP);

        heroUp();
        assertBody(51, 51, BodyDirection.VERTICAL);

        heroLeft();
        assertBody(51, 52, BodyDirection.TURNED_LEFT_DOWN);

        heroLeft();
        assertBody(50, 52, BodyDirection.HORIZONTAL);

        heroDown();
        assertBody(49, 52, BodyDirection.TURNED_RIGHT_DOWN);

        heroDown();
        assertBody(49, 51, BodyDirection.VERTICAL);

        heroRight();
        assertBody(49, 50, BodyDirection.TURNED_RIGHT_UP);

        heroRight();
        assertBody(50, 50, BodyDirection.HORIZONTAL);
    }

    @Test
    public void shouldBodyDirection_whenHeroLengthIs3_goClockwise() {
        heroGrow();
        assertBody(50, 50, BodyDirection.HORIZONTAL);

        heroDown();
        assertBody(51, 50, BodyDirection.TURNED_LEFT_DOWN);

        heroDown();
        assertBody(51, 49, BodyDirection.VERTICAL);

        heroLeft();
        assertBody(51, 48, BodyDirection.TURNED_LEFT_UP);

        heroLeft();
        assertBody(50, 48, BodyDirection.HORIZONTAL);

        heroUp();
        assertBody(49, 48, BodyDirection.TURNED_RIGHT_UP);

        heroUp();
        assertBody(49, 49, BodyDirection.VERTICAL);

        heroRight();
        assertBody(49, 50, BodyDirection.TURNED_RIGHT_DOWN);

        heroRight();
        assertBody(50, 50, BodyDirection.HORIZONTAL);
    }

    @Test
    public void shouldHeroIteratorStartsFromHead() {
        heroGrow();

        Iterator<Tail> iterator = hero.iterator();
        Tail head = iterator.next();
        Tail body = iterator.next();
        Tail tail = iterator.next();

        assertTrue(hero.itsMyHead(head));
        assertTrue(hero.itsMyBody(body));
        assertTrue(hero.itsMyTail(tail));
    }


    private void heroWalk() {
        hero.walk(board);
    }

    private void heroGrow() {
        hero.grow();
        heroWalk();
    }

    private void heroRight() {
        hero.right();
        heroWalk();
    }

    private void heroDown() {
        hero.down();
        heroWalk();
    }

    private void heroLeft() {
        hero.left();
        heroWalk();
    }

    private void heroUp() {
        hero.up();
        heroWalk();
    }

    private void assertHeadAntTail(int x, int y, Direction headDirection, Direction tailDirection) {
        assertEquals( "[headX, headY, headDirection, tailDirection]",
                asString(x, y, headDirection, tailDirection),

                asString(hero.getHead().getX(), hero.getHead().getY(),
                        hero.getDirection(), hero.getTailDirection()));
    }

    private void assertBody(int x, int y, BodyDirection bodyDirection) {
        Iterator<Tail> iterator = hero.iterator();
        Tail head = iterator.next();
        Tail body = iterator.next();

        assertEquals( "[bodyX, bodyY, bodyDirection]",
                asString(x, y, bodyDirection),

                asString(body.getX(), body.getY(),
                        hero.getBodyDirection(body)));
    }

    private String asString(Object...args) {
        return Arrays.asList(args).toString();
    }
}
