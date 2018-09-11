package io.github.apfelcreme.MbPetsNoLD.Pet;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
 * <p>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme
 */
public class PetLevel {

    private int level;
    private double attackStrengthModifier;
    private double receivedDamageModifier;
    private Effect effect;
    private int expThreshold;

    public PetLevel(int level, double attackStrengthModifier, double receivedDamageModifier,
                    Effect effect, int expThreshold) {
        this.level = level;
        this.attackStrengthModifier = attackStrengthModifier;
        this.receivedDamageModifier = receivedDamageModifier;
        this.effect = effect;
        this.expThreshold = expThreshold;
    }

    /**
     * Get a pet level object from the amount of exp the pet has
     *
     * @param currentExp the pets current amount of exp
     * @return the level that results from the amount of exp
     */
    public static PetLevel from(int currentExp) {
        return PetManager.getInstance().getLevelFromExp(currentExp);
    }

    /**
     * Get a pet level object from the amount of exp the pet has
     *
     * @param level the level
     * @return the level that results from the amount of exp
     */
    public static PetLevel fromLevel(int level) {
        return PetManager.getInstance().getLevel(level);

    }

    /**
     * returns the level integer
     *
     * @return the pet level
     */
    public int getLevel() {
        return level;
    }

    /**
     * returns the attack strength modifier
     *
     * @return the attack strength modifier
     */
    public double getAttackStrengthModifier() {
        return attackStrengthModifier;
    }

    /**
     * returns the received damage modifier
     *
     * @return the received damage modifier
     */
    public double getReceivedDamageModifier() {
        return receivedDamageModifier;
    }

    /**
     * returns the effect
     *
     * @return the effect
     */
    public Effect getEffect() {
        return effect;
    }

    /**
     * returns the exp threshold to reach that level
     *
     * @return
     */
    public int getExpThreshold() {
        return expThreshold;
    }

    @Override
    public String toString() {
        return "PetLevel{" +
                "level=" + level +
                ", attackStrengthModifier=" + attackStrengthModifier +
                ", effect=" + effect +
                '}';
    }
}
