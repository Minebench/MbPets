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
    HORSE(EntityType.HORSE),
    PIG(EntityType.PIG),
    SHEEP(EntityType.SHEEP),
    WOLF(EntityType.WOLF),
    CHICKEN(EntityType.CHICKEN),
    COW(EntityType.COW),
    MUSHROOM_COW(EntityType.MUSHROOM_COW),
    OCELOT(EntityType.OCELOT),
    POLAR_BEAR(EntityType.POLAR_BEAR),
    BAT(EntityType.BAT),
    IRON_GOLEM(EntityType.IRON_GOLEM),
    RABBIT(EntityType.RABBIT),
    PARROT(EntityType.PARROT),
    SKELETON_HORSE(EntityType.SKELETON_HORSE),
    UNDEAD_HORSE(EntityType.ZOMBIE_HORSE),
    DONKEY(EntityType.DONKEY),
    MULE(EntityType.MULE),
    LLAMA(EntityType.LLAMA),
    ENDERMAN(EntityType.ENDERMAN),
    MAGMA_CUBE(EntityType.MAGMA_CUBE),
    SLIME(EntityType.SLIME),
    VEX(EntityType.VEX),
    TURTLE(EntityType.TURTLE);

    private EntityType entityType;

    PetType(EntityType entityType) {
        this.entityType = entityType;
    }

    /**
     * returns the matching entitytype for spawning the pet
     *
     * @return the matching EntityType
     */
    public EntityType getEntityType() {
        return entityType;
    }

}
