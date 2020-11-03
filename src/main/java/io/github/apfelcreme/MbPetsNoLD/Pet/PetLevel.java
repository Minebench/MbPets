package io.github.apfelcreme.MbPetsNoLD.Pet;

import org.bukkit.Particle;

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
    private double regenerationModifier;
    private Particle particle;
    private final int particleCount;
    private final double particleExtra;
    private final Object particleData;
    private int expThreshold;

    public PetLevel(int level, double attackStrengthModifier, double receivedDamageModifier, double regenerationModifier,
                    Particle particle, int particleCount, double particleExtra, Object particleData, int expThreshold) {
        this.level = level;
        this.attackStrengthModifier = attackStrengthModifier;
        this.receivedDamageModifier = receivedDamageModifier;
        this.regenerationModifier = regenerationModifier;
        this.particle = particle;
        this.particleCount = particleCount;
        this.particleExtra = particleExtra;
        this.particleData = particleData;
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
     * returns the regeneration modifier
     *
     * @return the regeneration modifier
     */
    public double getRegenerationModifier() {
        return regenerationModifier;
    }

    /**
     * returns the particle
     *
     * @return the particle
     */
    public Particle getParticle() {
        return particle;
    }

    /**
     * Count of the particles (often speed)
     *
     * @return the particle count
     */
    public int getParticleCount() {
        return particleCount;
    }

    /**
     * Extra for the display of the particle (often speed)
     *
     * @return the extra particle information
     */
    public double getParticleExtra() {
        return particleExtra;
    }

    /**
     * Data for the display of the particle
     *
     * @return the particle data
     */
    public Object getParticleData() {
        return particleData;
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
                ", particle=" + particle +
                ", particleCount=" + particleCount +
                ", particleExtra=" + particleExtra +
                ", particleData=" + particleData +
                '}';
    }
}
