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

import java.util.stream.IntStream;

import static com.codenjoy.dojo.knibert.services.GameSettings.Keys.EAT_STONE_DECREASE;
import static com.codenjoy.dojo.services.event.Mode.MAX_VALUE;

public class MaxScoresTest extends ScoresTest {

    @Override
    public GameSettings settings() {
        return super.settings()
                .initScore(MAX_VALUE);
    }

    private int scoreFor(int length) {
        return IntStream.rangeClosed(3, length).sum();
    }

    @Test
    public void shouldCollectScores() {
        assertEvents(scoreFor(7) + ":\n" +
                "EAT_APPLE,8 > +8 = 33\n" +
                "EAT_APPLE,9 > +9 = 42\n" +
                "EAT_APPLE,10 > +10 = 52\n" +
                "EAT_APPLE,11 > +11 = 63\n" +
                "EAT_APPLE,12 > +12 = 75\n" +
                "EAT_APPLE,13 > +13 = " + scoreFor(13));
    }

    @Test
    public void shouldHeroLengthCantLessThen3() {
        assertEvents("0:\n" +
                "EAT_STONE > +0 = 0\n" +
                "EAT_STONE > +0 = 0\n" +
                "EAT_APPLE,2 > +0 = " + scoreFor(2)); // TODO
    }

    @Test
    public void shouldShortLength_whenEatStone() {
        // given
        settings.integer(EAT_STONE_DECREASE, 5);

        // when then
        assertEvents(scoreFor(7) + ":\n" +
                "EAT_APPLE,8 > +8 = 33\n" +
                "EAT_APPLE,9 > +9 = 42\n" +
                "EAT_APPLE,10 > +10 = 52\n" +
                "EAT_STONE > +0 = 52\n" +
                "EAT_APPLE,5 > +0 = 52\n" +
                "EAT_APPLE,6 > +0 = 52\n" +
                "EAT_APPLE,7 > +0 = 52\n" +
                "EAT_APPLE,8 > +0 = 52\n" +
                "EAT_APPLE,9 > +0 = 52\n" +
                "EAT_APPLE,10 > +0 = 52\n" +
                "EAT_APPLE,11 > +11 = 63\n" +
                "EAT_APPLE,12 > +12 = 75\n" +
                "EAT_APPLE,13 > +13 = " + scoreFor(13));
    }

    @Test
    public void shouldShortLength_whenDead() {
        assertEvents(scoreFor(7) + ":\n" +
                "EAT_APPLE,8 > +8 = 33\n" +
                "EAT_APPLE,9 > +9 = 42\n" +
                "EAT_APPLE,10 > +10 = 52\n" +
                "KILL > +0 = 52\n" +
                "EAT_APPLE,3 > +0 = 52\n" +
                "EAT_APPLE,4 > +0 = 52\n" +
                "EAT_APPLE,5 > +0 = 52\n" +
                "EAT_APPLE,6 > +0 = 52\n" +
                "EAT_APPLE,7 > +0 = 52\n" +
                "EAT_APPLE,8 > +0 = 52\n" +
                "EAT_APPLE,9 > +0 = 52\n" +
                "EAT_APPLE,10 > +0 = 52\n" +
                "EAT_APPLE,11 > +11 = 63\n" +
                "EAT_APPLE,12 > +12 = 75\n" +
                "EAT_APPLE,13 > +13 = " + scoreFor(13));
    }

    @Test
    public void shouldClearScoreTogetherWithHeroLength() {
        assertEvents(scoreFor(7) + ":\n" +
                "EAT_APPLE,8 > +8 = 33\n" +
                "EAT_APPLE,9 > +9 = 42\n" +
                "EAT_APPLE,10 > +10 = 52\n" +
                "(CLEAN) > -52 = 0\n" +
                "EAT_APPLE,3 > +3 = 3\n" +
                "EAT_APPLE,4 > +4 = 7\n" +
                "EAT_APPLE,5 > +5 = 12\n" +
                "EAT_APPLE,6 > +6 = 18\n" +
                "EAT_APPLE,7 > +7 = 25\n" +
                "EAT_APPLE,8 > +8 = 33\n" +
                "EAT_APPLE,9 > +9 = 42\n" +
                "EAT_APPLE,10 > +10 = 52\n" +
                "EAT_APPLE,11 > +11 = 63\n" +
                "EAT_APPLE,12 > +12 = 75\n" +
                "EAT_APPLE,13 > +13 = " + scoreFor(13));
    }

    @Test
    public void shouldStartsFromMaxScore_afterDead() {
        assertEvents("100:\n" +
                "KILL > +0 = 100\n" +
                "EAT_APPLE,3 > +0 = 100\n" +
                "EAT_APPLE,4 > +0 = 100");
    }

    @Test
    public void shouldStillZeroAfterDead() {
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
        assertEvents("0:\n" +
                "EAT_APPLE,3 > +3 = 3\n" +
                "(CLEAN) > -3 = 0");
    }
}