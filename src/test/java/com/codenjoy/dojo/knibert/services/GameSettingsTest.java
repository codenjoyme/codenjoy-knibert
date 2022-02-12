package com.codenjoy.dojo.knibert.services;

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

import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameSettingsTest {

    @Test
    public void shouldGetAllKeys() {
        assertEquals("BOARD_SIZE          =[Level] Board size\n" +
                    "GAME_OVER_PENALTY   =[Score] Game over penalty\n" +
                    "START_HERO_LENGTH   =[Game] Start hero length\n" +
                    "EAT_STONE_PENALTY   =[Score] Eat stone penalty\n" +
                    "EAT_STONE_DECREASE  =[Game] Eat stone decrease\n" +
                    "SCORE_COUNTING_TYPE =[Score] Counting score mode",
                TestUtils.toString(new GameSettings().allKeys()));
    }
}