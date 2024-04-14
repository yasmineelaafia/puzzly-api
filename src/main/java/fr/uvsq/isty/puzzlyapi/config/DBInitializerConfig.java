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

package fr.uvsq.isty.puzzlyapi.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.uvsq.isty.puzzlyapi.models.Admin;
import fr.uvsq.isty.puzzlyapi.models.Auth;
import fr.uvsq.isty.puzzlyapi.models.Authority;
import fr.uvsq.isty.puzzlyapi.models.Game;
import fr.uvsq.isty.puzzlyapi.models.GameSolo;
import fr.uvsq.isty.puzzlyapi.models.Player;
import fr.uvsq.isty.puzzlyapi.models.enums.AuthorityNameEnum;
import fr.uvsq.isty.puzzlyapi.repositories.AdminRepository;
import fr.uvsq.isty.puzzlyapi.repositories.AuthorityRepository;
import fr.uvsq.isty.puzzlyapi.repositories.GameRepository;
import fr.uvsq.isty.puzzlyapi.repositories.PlayerRepository;

import java.nio.file.*;
import java.io.IOException;

@Configuration
public class DBInitializerConfig {

    // initialize the DB
    @Bean
    CommandLineRunner initializeJpaData(
            PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository,
            PlayerRepository playerRepository,
            AdminRepository adminRepository,
            GameRepository gameRepository) {
        return (args) -> {
            System.out.println("application started");

            // Authorities
            Authority AAdmin = new Authority(null, AuthorityNameEnum.ADMIN, null);
            Authority APlayer = new Authority(null, AuthorityNameEnum.PLAYER, null);

            authorityRepository.saveAll(List.of(AAdmin, APlayer));

            Auth authAd1 = new Auth(null, null, passwordEncoder.encode("passadmin1"));
            Auth authAd2 = new Auth(null, null, passwordEncoder.encode("passadmin2"));
            Auth authPl1 = new Auth(null, null, passwordEncoder.encode("passplayer1"));
            Auth authPl2 = new Auth(null, null, passwordEncoder.encode("passplayer2"));
            Auth authPl3 = new Auth(null, null, passwordEncoder.encode("passplayer3"));
            Auth authYasmine = new Auth(null, null, passwordEncoder.encode("passyasmine"));

            Admin admin1 = getAdmin("a000001", "admin.one", authAd1, AAdmin, "Admin", "One");
            // ##
            Admin admin2 = getAdmin("a000002", "admin.two", authAd2, AAdmin, "Admin", "Two");
            // ##
            Player player1 = getPlayer("player.one", authPl1, APlayer,
                    "Player", "One");
            // ##
            Player player2 = getPlayer("player.two", authPl2, APlayer,
                    "Player", "Two");
            // ##
            Player player3 = getPlayer("player.three", authPl3, APlayer,
                    "Player", "Three");

            // ##
            Player yasmine = getPlayer("yasmine.el", authYasmine, APlayer,
                    "Yasmine", "El Aafia");
            // END

            authAd1.setUser(admin1);
            authAd2.setUser(admin2);
            authPl1.setUser(player3);
            authPl2.setUser(player2);
            authPl3.setUser(player3);
            authYasmine.setUser(yasmine);

            List<Admin> admins = adminRepository.saveAll(List.of(admin1, admin2));
            List<Player> players = playerRepository.saveAll(List.of(player1, player2, player3, yasmine));

            Game game1 = new GameSolo();
            game1.setPlayer(player1);
            game1.setElapsedTime(12);
            game1.setNumberSteps(5);
            game1.setScore(16);
            game1.setWon(true);

            Game game2 = new GameSolo();
            game2.setPlayer(player2);
            game2.setElapsedTime(9);
            game2.setNumberSteps(4);
            game2.setScore(16);
            game2.setWon(true);

            Game game3 = new GameSolo();
            game3.setPlayer(player1);
            game3.setElapsedTime(10);
            game3.setNumberSteps(5);
            game3.setScore(16);
            game3.setWon(true);

            Game game4 = new GameSolo();
            game4.setPlayer(yasmine);
            game4.setElapsedTime(8);
            game4.setNumberSteps(4);
            game4.setScore(16);
            game4.setWon(true);

            Game game5 = new GameSolo();
            game5.setPlayer(yasmine);
            game5.setElapsedTime(12);
            game5.setNumberSteps(4);
            game5.setScore(16);
            game5.setWon(true);

            gameRepository.saveAll(List.of(game1, game2, game3, game4, game5));
        };
    }

    private static Player getPlayer(String username, Auth auth,
            Authority authority, String firstname, String lastname) {
        Player player = new Player();
        player.setAccountNonExpired(true);
        player.setAccountNonLocked(true);
        player.setCredentialsNonExpired(true);
        player.setEnabled(true);
        player.setUsername(username);
        player.setAuth(auth);
        player.setAuthorities(List.of(authority));
        player.setFName(firstname);
        player.setLName(lastname);
        return player;
    }

    private static Admin getAdmin(String code, String username, Auth auth, Authority authority, String firstname,
            String lastname) {
        Admin admin = new Admin(code);
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);
        admin.setUsername(username);
        admin.setAuth(auth);
        admin.setAuthorities(List.of(authority));
        admin.setFName(firstname);
        admin.setLName(lastname);
        return admin;
    }

    private static byte[] readFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            byte[] fileData = Files.readAllBytes(path);
            return fileData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
