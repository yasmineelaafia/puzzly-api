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

package fr.uvsq.isty.puzzlyapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.uvsq.isty.puzzlyapi.models.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findByWon(boolean won);

    @Query("SELECT g FROM Game g WHERE TYPE(g) = :gameType")
    List<Game> findByType(@Param("gameType") Class<? extends Game> gameType);

    @Query("SELECT g FROM Game g WHERE Type(g)=:gameType AND g.won=:isWon")
    List<Game> findByTypeAndWon(@Param("gameType") Class<? extends Game> gameType, @Param("isWon") boolean won);
}
