package com.codenjoy.dojo.knibert;

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

import com.codenjoy.dojo.knibert.services.GameSettings;

import static com.codenjoy.dojo.knibert.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.event.Mode.CUMULATIVELY;

public class TestGameSettings extends GameSettings {

    /**
     * Here you can override the settings for all tests.
     */
    public TestGameSettings() {
        initScore(CUMULATIVELY);

        integer(BOARD_SIZE, 15);
        integer(GAME_OVER_PENALTY, -50);
        integer(START_HERO_LENGTH, 2);
        integer(EAT_STONE_PENALTY, -10);
        integer(EAT_STONE_DECREASE, 10);
    }
}
