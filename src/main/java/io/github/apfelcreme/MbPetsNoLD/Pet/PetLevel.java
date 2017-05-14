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

    private Integer level;
    private Double attackStrengthModifier;
    private Double receivedDamageModifier;
    private Effect effect;
    private Integer expThreshold;

    private PetLevel(Integer level, Double attackStrengthModifier, Double receivedDamageModifier,
                     Effect effect, Integer expThreshold) {
        this.level = level;
        this.attackStrengthModifier = attackStrengthModifier;
        this.receivedDamageModifier = receivedDamageModifier;
        this.effect = effect;
        this.expThreshold = expThreshold;
    }

    /**
     * creates a pet level object from the amount of exp the pet has
     *
     * @param currentExp the pets current amount of exp
     * @return the level that results from the amount of exp
     */
    public static PetLevel from(Integer currentExp) {
        Integer level = null;
        Double attackStrengthModifier = null;
        Double receivedDamageModifier = null;
        Effect effect = null;
        Integer expThreshold = null;
        ConfigurationSection levels = MbPets.getInstance().getConfig().getConfigurationSection("level");
        for (Map.Entry<String, Object> entry : levels.getValues(false).entrySet()) {
            if (currentExp >= MbPets.getInstance().getConfig().getInt("level." + entry.getKey() + ".expThreshold")) {
                level = Integer.parseInt(entry.getKey());
                attackStrengthModifier = MbPets.getInstance().getConfig().getDouble("level." + entry.getKey() + ".attackStrengthModifier");
                receivedDamageModifier = MbPets.getInstance().getConfig().getDouble("level." + entry.getKey() + ".receivedDamageModifier");
                expThreshold = MbPets.getInstance().getConfig().getInt("level." + entry.getKey() + ".expThreshold");
                if (!MbPets.getInstance().getConfig().getString("level." + entry.getKey() + ".effect").isEmpty()) {
                    effect = Effect.valueOf(MbPets.getInstance().getConfig().getString("level." + entry.getKey() + ".effect"));
                }
            }
        }
        return new PetLevel(level, attackStrengthModifier, receivedDamageModifier, effect, expThreshold);
    }

    /**
     * creates a pet level object from the amount of exp the pet haswwwwww
     *
     * @param level the level
     * @return the level that results from the amount of exp
     */
    public static PetLevel fromLevel(Integer level) {
        int highestLevel = 0;
        for (String key : MbPets.getInstance().getConfig().getConfigurationSection("level").getKeys(false)) {
            if (Integer.parseInt(key) > highestLevel) {
                highestLevel = Integer.parseInt(key);
            }
        }
        if (level > highestLevel) {
            level = highestLevel;
        }
        Double attackStrengthModifier = MbPets.getInstance().getConfig().getDouble("level." + level + ".attackStrengthModifier");
        Double receivedDamageModifier = MbPets.getInstance().getConfig().getDouble("level." + level + ".receivedDamageModifier");
        Integer expThreshold = MbPets.getInstance().getConfig().getInt("level." + level + ".expThreshold");
        Effect effect = null;
        if (!MbPets.getInstance().getConfig().getString("level." + level + ".effect").isEmpty()) {
            effect = Effect.valueOf(MbPets.getInstance().getConfig().getString("level." + level + ".effect"));
        }
        return new PetLevel(level, attackStrengthModifier, receivedDamageModifier, effect, expThreshold);

    }

    /**
     * returns the level integer
     *
     * @return the pet level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * returns the attack strength modifier
     *
     * @return the attack strength modifier
     */
    public Double getAttackStrengthModifier() {
        return attackStrengthModifier;
    }

    /**
     * returns the received damage modifier
     *
     * @return the received damage modifier
     */
    public Double getReceivedDamageModifier() {
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
    public Integer getExpThreshold() {
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
