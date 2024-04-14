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

package fr.uvsq.isty.puzzlyapi.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue(value = "PLAYER")
public class Player extends User {

    @Transient
    private double totalScore;

    public double getTotalScore() {
        if (totalScore == 0.0) {
            totalScore = calculateTotalScore();
        }
        return totalScore;
    }

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private List<Game> games = new ArrayList<>();

    @PrePersist
    public void calculateTotalScoreBeforePersist() {
        this.totalScore = calculateTotalScore();
    }

    public double calculateTotalScore() {
        double totalScore = 0.0;
        for (Game game : games) {
            totalScore += calculateScoreForGame(game);
        }
        return totalScore;
    }

    private double calculateScoreForGame(Game game) {
        // Base score based on winning the game
        double baseScore = game.isWon() ? 100 : 0;

        // Bonus for correct placements
        double placementBonus = game.getScore() * 10;

        // Penalty for time taken (adjust coefficient as needed)
        double timePenalty = game.getElapsedTime() * 0.5;

        // Penalty for unnecessary steps (optimal steps = number of positions)
        int optimalSteps = 4; // Replace with actual number of positions if needed
        double stepPenalty = (game.getNumberSteps() - Math.min(game.getNumberSteps(), optimalSteps)) * 2f;

        // Combine all components for the final score
        double score = baseScore + placementBonus - timePenalty - stepPenalty;

        // Ensure the score is non-negative
        return Math.max(score, 0f);
    }
}
