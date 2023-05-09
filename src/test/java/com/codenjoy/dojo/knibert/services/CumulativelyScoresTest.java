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

import org.junit.Test;

import static com.codenjoy.dojo.knibert.services.GameSettings.Keys.EAT_STONE_PENALTY;
import static com.codenjoy.dojo.knibert.services.GameSettings.Keys.GAME_OVER_PENALTY;
import static com.codenjoy.dojo.services.event.Mode.CUMULATIVELY;

public class CumulativelyScoresTest extends ScoresTest {

    @Override
    public GameSettings settings() {
        return super.settings()
                .initScore(CUMULATIVELY);
    }

    @Test
    public void shouldCollectScores() {
        assertEvents("100:\n" +
                "EAT_APPLE,3 > +3 = 103\n" +
                "EAT_APPLE,4 > +4 = 107\n" +
                "EAT_APPLE,5 > +5 = 112\n" +
                "EAT_APPLE,6 > +6 = 118\n" +
                "EAT_STONE > -10 = 108\n" +
                "KILL > -50 = 58");
    }

    @Test
    public void shouldHeroLengthCantLessThen3() {
        assertEvents("0:\n" +
                "EAT_STONE > +0 = 0\n" +
                "EAT_STONE > +0 = 0\n" +
                "EAT_APPLE,3 > +3 = 3");
    }

    @Test
    public void shouldShortLengthWhenEatStone() {
        // given
        settings.integer(EAT_STONE_PENALTY, -10);

        // when then
        assertEvents("100:\n" +
                "EAT_APPLE,3 > +3 = 103\n" +
                "EAT_APPLE,4 > +4 = 107\n" +
                "EAT_APPLE,5 > +5 = 112\n" +
                "EAT_APPLE,6 > +6 = 118\n" +
                "EAT_APPLE,7 > +7 = 125\n" +
                "EAT_APPLE,8 > +8 = 133\n" +
                "EAT_APPLE,9 > +9 = 142\n" +
                "EAT_APPLE,10 > +10 = 152\n" +
                "EAT_APPLE,11 > +11 = 163\n" +
                "EAT_APPLE,12 > +12 = 175\n" +
                "EAT_STONE > -10 = 165\n" +
                "EAT_APPLE,3 > +3 = 168\n" +
                "EAT_APPLE,4 > +4 = 172");
    }

    @Test
    public void shouldClearScoreTogetherWithHeroLength() {
        assertEvents("100:\n" +
                "EAT_APPLE,3 > +3 = 103\n" +
                "EAT_APPLE,4 > +4 = 107\n" +
                "EAT_APPLE,5 > +5 = 112\n" +
                "EAT_APPLE,6 > +6 = 118\n" +
                "EAT_APPLE,7 > +7 = 125\n" +
                "EAT_APPLE,8 > +8 = 133\n" +
                "EAT_APPLE,9 > +9 = 142\n" +
                "EAT_APPLE,10 > +10 = 152\n" +
                "EAT_APPLE,11 > +11 = 163\n" +
                "EAT_APPLE,12 > +12 = 175\n" +
                "(CLEAN) > -175 = 0\n" +
                "EAT_APPLE,3 > +3 = 3\n" +
                "EAT_APPLE,4 > +4 = 7");
    }

    @Test
    public void shouldStartsFrom3_afterDead() {
        // given
        settings.integer(GAME_OVER_PENALTY, -50);

        // when then
        assertEvents("100:\n" +
                "KILL > -50 = 50\n" +
                "EAT_APPLE,3 > +3 = 53\n" +
                "EAT_APPLE,4 > +4 = 57");
    }

    @Test
    public void shouldNotBeLessThanZero() {
        assertEvents("0:\n" +
                "KILL > +0 = 0");
    }

    @Test
    public void shouldStillZero_afterEatStone() {
        assertEvents("0:\n" +
                "EAT_STONE > +0 = 0");
    }

    @Test
    public void shouldClearScore() {
        assertEvents("100:\n" +
                "EAT_APPLE,3 > +3 = 103\n" +
                "EAT_APPLE,4 > +4 = 107\n" +
                "(CLEAN) > -107 = 0\n" +
                "EAT_APPLE,3 > +3 = 3\n" +
                "EAT_APPLE,4 > +4 = 7");
    }
}