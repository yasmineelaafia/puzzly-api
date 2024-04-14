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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.uvsq.isty.puzzlyapi.core.exceptions.RowNotFoundException;
import fr.uvsq.isty.puzzlyapi.models.User;
import fr.uvsq.isty.puzzlyapi.services.UserService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * A REST Controller to manage Users
 * 
 * @author Yasmine EL AAFIA
 * 
 */
@RestController
@RequestMapping(value = "api/user")
public class UserController extends HandlerController {

    @Autowired
    private UserService usrService;

    /**
     * Get list of the users
     * 
     * @return List<User>
     */
    @GetMapping(value = { "/", "" })
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(usrService.all(), HttpStatus.OK);
    }

    /**
     * Returns a user by it ID
     * 
     * @param id
     * @return User
     * @throws RowNotFoundException
     */
    @GetMapping(value = "/find")
    public ResponseEntity<User> getOneUser(@RequestParam(name = "id") UUID id) throws RowNotFoundException {
        return new ResponseEntity<User>(usrService.findByID(id), HttpStatus.OK);
    }

}
