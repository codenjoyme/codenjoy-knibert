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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.games.knibert.Element;
import com.codenjoy.dojo.knibert.model.Knibert;
import com.codenjoy.dojo.knibert.model.Player;
import com.codenjoy.dojo.knibert.model.artifacts.BasicWalls;
import com.codenjoy.dojo.knibert.model.artifacts.RandomArtifactGenerator;
import com.codenjoy.dojo.knibert.services.ai.AISolver;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.knibert.services.GameSettings.Keys.BOARD_SIZE;

public class GameRunner extends AbstractGameType<GameSettings> {

    public static final String GAME_NAME = "knibert";

    @Override
    public PlayerScores getPlayerScores(Object score, GameSettings settings) {
        return new ScoresImpl<>(Integer.parseInt(score.toString()), settings.calculator());
    }

    @Override
    public GameField createGame(int levelNumber, GameSettings settings) {
        int size = settings.integer(BOARD_SIZE);

        return new Knibert(new RandomArtifactGenerator(getDice()),
                new BasicWalls(size),
                size,
                settings);
    }

    @Override
    public Parameter<Integer> getBoardSize(GameSettings settings) {
        return settings.integerValue(BOARD_SIZE);
    }

    @Override
    public String name() {
        return GAME_NAME;
    }

    @Override
    public CharElement[] getPlots() {
        return Element.values();
    }

    @Override
    public GameSettings getSettings() {
        return new GameSettings();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return AISolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }

    @Override
    public MultiplayerType getMultiplayerType(GameSettings settings) {
        return MultiplayerType.SINGLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, int teamId, String playerId, GameSettings settings) {
        return new Player(listener, settings).inTeam(teamId);
    }
}