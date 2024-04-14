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

package fr.uvsq.isty.puzzlyapi.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.uvsq.isty.puzzlyapi.core.utils.CookiesConfig;
import fr.uvsq.isty.puzzlyapi.dtos.AuthResponseDTO;
import fr.uvsq.isty.puzzlyapi.dtos.LoginRequestDTO;
import fr.uvsq.isty.puzzlyapi.dtos.PlayerRegistrationDTO;
import fr.uvsq.isty.puzzlyapi.models.Auth;
import fr.uvsq.isty.puzzlyapi.models.Authority;
import fr.uvsq.isty.puzzlyapi.models.Player;
import fr.uvsq.isty.puzzlyapi.models.enums.AuthorityNameEnum;
import fr.uvsq.isty.puzzlyapi.repositories.AuthorityRepository;
import fr.uvsq.isty.puzzlyapi.repositories.PlayerRepository;
import fr.uvsq.isty.puzzlyapi.services.security.AuthUserService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "auth")
public class SecurityController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private CookiesConfig cookiesConfig;

    @Value(value = "${jwt.access-token-cookie-name}")
    private String cookieName;

    @GetMapping(value = "/infos")
    public AuthResponseDTO authInfos(Authentication authentication) {
        return new AuthResponseDTO(
                authentication.isAuthenticated(),
                authentication.getName(),
                authentication.getAuthorities(),
                authUserService.loadUserByUsername(authentication.getName()));
    }

    @GetMapping(value = "/profile")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(
            HttpServletResponse response,
            @RequestBody LoginRequestDTO loginRequestDTO) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()));

            Instant instant = Instant.now();
            String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
            JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(2, ChronoUnit.DAYS))
                    .subject(loginRequestDTO.getUsername())
                    .claim("scope", scope)
                    .build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                    .from(JwsHeader.with(MacAlgorithm.HS512).build(), claimsSet);

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

            return new ResponseEntity<>(Map.of("token", jwt), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(Map.of("message", "Nom d'utilisateur ou mot de passe incorrectes", "timestamp",
                    System.currentTimeMillis()), HttpStatus.UNAUTHORIZED);
        }
    }

    // /auth/register
    @PostMapping("/register")
    public ResponseEntity<Player> postMethodName(@RequestBody PlayerRegistrationDTO playerDTO) {
        System.out.println(playerDTO);
        Player player = new Player();
        player.setUsername(playerDTO.getUsername());
        player.setAuth(new Auth(null, player, encoder.encode(playerDTO.getPassword())));
        player.setFName(playerDTO.getFname());
        player.setLName(playerDTO.getLname());
        player.setAccountNonExpired(true);
        player.setAccountNonLocked(true);

        // Get player authority from database
        Authority playerAuthority = authorityRepository.findByName(AuthorityNameEnum.PLAYER)
                .orElse(new Authority(null, AuthorityNameEnum.PLAYER, null));

        player.setAuthorities(List.of(playerAuthority));
        player.setCredentialsNonExpired(true);
        player.setEnabled(true);

        player = playerRepository.save(player);

        return new ResponseEntity<Player>(player, HttpStatus.OK);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        System.out.println("logout");
        cookiesConfig.clear(response, cookieName);
        return new ResponseEntity<Object>(new HashMap<>() {
            {
                put("message", "Deconnecté avec succès");
            }
        }, HttpStatus.OK);
    }

}
