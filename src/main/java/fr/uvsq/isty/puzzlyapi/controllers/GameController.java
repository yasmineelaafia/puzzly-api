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

package fr.uvsq.isty.puzzlyapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.uvsq.isty.puzzlyapi.dtos.HallOfFameDTO;
import fr.uvsq.isty.puzzlyapi.models.Player;
import fr.uvsq.isty.puzzlyapi.services.GameService;
import fr.uvsq.isty.puzzlyapi.services.security.AuthUserService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "/api/game")
@Secured("SCOPE_PLAYER")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private AuthUserService authUserService;

    @GetMapping("/hall-of-fame")
    public ResponseEntity<List<HallOfFameDTO>> getHallOfFame(Authentication authentication) {
        Player currentPlayer = (Player) authUserService.loadUserByUsername(authentication.getName());
        return new ResponseEntity<List<HallOfFameDTO>>(gameService.getHallOfFame(currentPlayer), HttpStatus.OK);
    }

}
