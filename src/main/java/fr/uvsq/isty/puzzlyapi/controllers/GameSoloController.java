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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.uvsq.isty.puzzlyapi.models.GameSolo;
import fr.uvsq.isty.puzzlyapi.models.Player;
import fr.uvsq.isty.puzzlyapi.services.GameSoloService;
import fr.uvsq.isty.puzzlyapi.services.security.AuthUserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * A REST Controller to manage Solo games
 * 
 * @author Yasmine EL AAFIA
 * 
 */
@RestController
@RequestMapping(value = "/api/solo")
@Secured(value = "SCOPE_PLAYER")
public class GameSoloController {
    @Autowired
    private GameSoloService gameSoloService;

    @Autowired
    private AuthUserService authUserService;

    /**
     * Save the solo game
     * 
     * @param game
     * @param authentication
     * @return ResponseEntity
     */
    @PostMapping("/save")
    public ResponseEntity saveGame(@RequestBody GameSolo game, Authentication authentication) {
        try {
            Player currentPlayer = (Player) authUserService.loadUserByUsername(authentication.getName());
            game.setPlayer(currentPlayer);
            gameSoloService.saveGame(game);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
