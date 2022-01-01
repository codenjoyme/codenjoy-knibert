package com.codenjoy.dojo.knibert.model.artifacts;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.knibert.model.Hero;
import com.codenjoy.dojo.knibert.model.Walls;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class RandomArtifactGenerator implements ArtifactGenerator {

    public static final int MAX = 100;
    private Dice dice;

    public RandomArtifactGenerator(Dice dice) {
        this.dice = dice;
    }

    @Override
    public Stone generateStone(Hero hero, Apple apple, Walls walls, int boardSize) {
        Point pt;
        boolean noSoGoodPlace;

        if (checkForMaxLength(hero, walls, boardSize)){
            return new Stone(-1, -1);
        }

        int c = 0;
        do {
            pt = PointImpl.random(dice, boardSize);

            boolean onHero = hero.itsMe(pt);
            boolean onApple = (apple != null) && apple.itsMe(pt);
            boolean onWall = walls.itsMe(pt);

            boolean onHeroWayWhenGoRight = (hero.getX() + 1) <= pt.getX() && pt.getX() <= boardSize && pt.getY() == hero.getY() && hero.getDirection().equals(Direction.RIGHT);
            boolean onHeroWayWhenGoLeft = 0 <= pt.getX() && pt.getX() <= (hero.getX() - 1) && pt.getY() == hero.getY() && hero.getDirection().equals(Direction.LEFT);
            boolean onHeroWayWhenGoDown = (hero.getY() + 1) <= pt.getY() && pt.getY() <= boardSize && pt.getX() == hero.getX() && hero.getDirection().equals(Direction.DOWN);
            boolean onHeroWayWhenGoUp = 0 <= pt.getY() && pt.getY() <= (hero.getY() - 1) && pt.getX() == hero.getX() && hero.getDirection().equals(Direction.UP);
            boolean onHeroWay = onHeroWayWhenGoRight || onHeroWayWhenGoLeft || onHeroWayWhenGoDown || onHeroWayWhenGoUp;

            boolean whenStandstill = isStandstill(apple, pt, boardSize);

            boolean outOfBoard = pt.isOutOf(boardSize);

            noSoGoodPlace = outOfBoard || onHero || onHeroWay || onApple || whenStandstill || onWall;
        } while (noSoGoodPlace && c++ < MAX);

        if (c >= MAX) {
            return new Stone(-1, -1);
        }

        return new Stone(pt);
    }

    private boolean isStandstill(Point apple, Point stone, int boardSize) {
        if (apple == null) {
            return false;
        }

        int D = 1; // TODO это ширина BasicWalls стен, надо тут учесть реальную конфигурацию стен
        int LEFT = 0 + D;
        int TOP = 0 + D;
        int RIGHT = boardSize - 1 - D;
        int BOTTOM = boardSize - 1 - D;

        boolean whenLeftTopStandstill = apple.itsMe(LEFT, TOP) && (stone.itsMe(LEFT, TOP + 1) || stone.itsMe(LEFT + 1, TOP));
        boolean whenLeftBottomStandstill = apple.itsMe(LEFT, BOTTOM) && (stone.itsMe(LEFT, BOTTOM - 1) || stone.itsMe(LEFT + 1, BOTTOM));
        boolean whenRightTopStandstill = apple.itsMe(RIGHT, TOP) && (stone.itsMe(RIGHT, TOP + 1) || stone.itsMe(RIGHT - 1, TOP));
        boolean whenRightBottomStandstill = apple.itsMe(RIGHT, BOTTOM) && (stone.itsMe(RIGHT, BOTTOM - 1) || stone.itsMe(RIGHT - 1, BOTTOM));

        return whenLeftTopStandstill || whenLeftBottomStandstill || whenRightTopStandstill || whenRightBottomStandstill;
    }

    private boolean checkForMaxLength(Hero hero, Walls walls, int boardSize) {
        int maxHeroSize = boardSize * boardSize - walls.getCountOfWalls() - 1;
        return hero.getLength() == maxHeroSize;
    }

    @Override
    public Apple generateApple(Hero hero, Apple apple, Stone stone, Walls walls, int boardSize) {
        Point pt;
        boolean noSoGoodPlace;

        if (checkForMaxLength(hero, walls, boardSize)){
            return new Apple(-1, -1);
        }

        int c = 0;
        do {
            pt = PointImpl.random(dice, boardSize);

            boolean onHero = hero.itsMe(pt);
            boolean onStone = stone.itsMe(pt);
            boolean onWall = walls.itsMe(pt);
            boolean onApple = apple != null && apple.itsMe(pt);

            boolean whenStandstill = isStandstill(pt, stone, boardSize);

            boolean outOfBoard = pt.isOutOf(boardSize);

            noSoGoodPlace = outOfBoard || onHero || onStone || whenStandstill || onWall || onApple;
        } while (noSoGoodPlace && c++ < MAX);

        if (c >= MAX) {
            return new Apple(-1, -1);
        }

        return new Apple(pt);
    }

}
