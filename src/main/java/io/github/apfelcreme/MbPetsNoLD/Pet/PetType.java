package io.github.apfelcreme.MbPetsNoLD.Pet;

import org.bukkit.entity.EntityType;

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
public enum PetType {
    HORSE,
    PIG,
    SHEEP,
    WOLF,
    CHICKEN,
    COW,
    MUSHROOM_COW,
    OCELOT,
    POLAR_BEAR,
    BAT,
    IRON_GOLEM,
    RABBIT,
    SKELETON_HORSE,
    UNDEAD_HORSE,
    DONKEY,
    MULE,
    LLAMA,
    ENDERMAN,
    MAGMA_CUBE,
    SLIME;

    /**
     * returns the matching entitytype for spawning the pet
     * @param type the PetType
     * @return the matching EntityType
     */
    public static EntityType getEntityType(PetType type) {
        switch (type) {
            case HORSE:
                return EntityType.HORSE;
            case PIG:
                return EntityType.PIG;
            case SHEEP:
                return EntityType.SHEEP;
            case WOLF:
                return EntityType.WOLF;
            case CHICKEN:
                return EntityType.CHICKEN;
            case COW:
                return EntityType.COW;
            case MUSHROOM_COW:
                return EntityType.MUSHROOM_COW;
            case OCELOT:
                return EntityType.OCELOT;
            case POLAR_BEAR:
                return EntityType.POLAR_BEAR;
            case BAT:
                return EntityType.BAT;
            case IRON_GOLEM:
                return EntityType.IRON_GOLEM;
            case RABBIT:
                return EntityType.RABBIT;
            case SKELETON_HORSE:
                return EntityType.SKELETON_HORSE;
            case UNDEAD_HORSE:
                return EntityType.ZOMBIE_HORSE;
            case MULE:
                return EntityType.MULE;
            case DONKEY:
                return EntityType.DONKEY;
            case LLAMA:
                return EntityType.LLAMA;
            case ENDERMAN:
                return EntityType.ENDERMAN;
            case MAGMA_CUBE:
                return EntityType.MAGMA_CUBE;
            case SLIME:
                return EntityType.SLIME;
        }
        return null;
    }

}
