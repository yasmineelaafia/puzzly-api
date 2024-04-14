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

package fr.uvsq.isty.puzzlyapi.core.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookiesConfig {

    @Value(value = "${server-host}")
    private String domain;

    /**
     * Create an Http only cookie for auth
     * 
     * @param response
     * @param cookieName
     * @param value
     * @param secure
     * @param maxAge
     */
    public void create(HttpServletResponse response, String cookieName, String value, Boolean secure, Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setPath("/");
        cookie.setSecure(secure);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }

    /**
     * Remove the Http only cookie
     * 
     * @param response
     * @param cookieName
     */
    public void clear(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1);
        
        response.addCookie(cookie);
    }
}
