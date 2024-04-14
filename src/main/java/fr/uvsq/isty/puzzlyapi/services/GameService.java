/*
 * Puzzly - A puzzle game project
 *
 * (C) 2024 Yasmine EL AAFIA <elaafiayasmine@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Puzzly is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.uvsq.isty.puzzlyapi.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.uvsq.isty.puzzlyapi.dtos.HallOfFameDTO;
import fr.uvsq.isty.puzzlyapi.models.Player;
import fr.uvsq.isty.puzzlyapi.repositories.PlayerRepository;

@Service
public class GameService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<HallOfFameDTO> getHallOfFame(Player currentPlayer) {
        // Fetch all users with eager fetching for games
        List<Player> allPlayers = playerRepository.findAll();
        allPlayers.sort(Comparator.comparing(Player::getTotalScore).reversed());
        return allPlayers.stream().map(player -> {
            String playerName = player.getFName() + " " + player.getLName();
            Double score = player.getTotalScore();
            Boolean isCurrentPlayer = player.equals(currentPlayer);
            return new HallOfFameDTO(playerName, score, isCurrentPlayer);
        }).collect(Collectors.toList());
    }
}
