package com.codenjoy.dojo.knibert.model.artifacts;

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


import com.codenjoy.dojo.knibert.model.Hero;
import com.codenjoy.dojo.knibert.model.Walls;

public class ConstantArtifactGenerator implements ArtifactGenerator {

    @Override
    public Stone generateStone(Hero hero, Apple apple, Walls walls, int boardSize) {
        return new Stone(0, 0);
    }

    @Override
    public Apple generateApple(Hero hero, Apple apple, Stone stone, Walls walls, int boardSize) {
        return new Apple(1, 1);
    }

}
