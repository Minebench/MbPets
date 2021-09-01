package io.github.apfelcreme.MbPetsNoLD.Pet;

import io.github.apfelcreme.MbPetsNoLD.Pet.Type.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ZombieHorse;

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
    HORSE(          EntityType.HORSE,           HorsePet.class),
    PIG(            EntityType.PIG,             PigPet.class),
    SHEEP(          EntityType.SHEEP,           SheepPet.class),
    WOLF(           EntityType.WOLF,            WolfPet.class),
    CAT(            EntityType.CAT,             CatPet.class),
    CHICKEN(        EntityType.CHICKEN,         ChickenPet.class),
    COW(            EntityType.COW,             CowPet.class),
    MUSHROOM_COW(   EntityType.MUSHROOM_COW,    MooshroomPet.class),
    OCELOT(         EntityType.OCELOT,          OcelotPet.class),
    POLAR_BEAR(     EntityType.POLAR_BEAR,      PolarBearPet.class),
    PANDA(          EntityType.PANDA,           PandaPet.class),
    BAT(            EntityType.BAT,             BatPet.class),
    IRON_GOLEM(     EntityType.IRON_GOLEM,      IronGolemPet.class),
    RABBIT(         EntityType.RABBIT,          RabbitPet.class),
    PARROT(         EntityType.PARROT,          ParrotPet.class),
    FOX(            EntityType.FOX,             FoxPet.class),
    SKELETON_HORSE( EntityType.SKELETON_HORSE,  SkeletonHorsePet.class),
    UNDEAD_HORSE(   EntityType.ZOMBIE_HORSE,    UndeadHorsePet.class),
    DONKEY(         EntityType.DONKEY,          DonkeyPet.class),
    MULE(           EntityType.MULE,            MulePet.class),
    LLAMA(          EntityType.LLAMA,           LlamaPet.class),
    ENDERMAN(       EntityType.ENDERMAN,        EndermanPet.class),
    MAGMA_CUBE(     EntityType.MAGMA_CUBE,      MagmaCubePet.class),
    SLIME(          EntityType.SLIME,           SlimePet.class),
    VEX(            EntityType.VEX,             VexPet.class),
    TURTLE(         EntityType.TURTLE,          TurtlePet.class),
    BEE(            EntityType.BEE,             BeePet.class),
    STRIDER(        EntityType.STRIDER,         StriderPet.class),
    HOGLIN(         EntityType.HOGLIN,          HoglinPet.class),
    GOAT(           EntityType.GOAT,            GoatPet.class);

    private EntityType entityType;
    private final Class<? extends Pet> petClass;

    PetType(EntityType entityType, Class<? extends Pet> petClass) {
        this.entityType = entityType;
        this.petClass = petClass;
    }

    /**
     * returns the matching entitytype for spawning the pet
     *
     * @return the matching EntityType
     */
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * returns the matching pet class for creating the pet instance
     *
     * @return the matching class extending Pet
     */
    public Class<? extends Pet> getPetClass() {
        return petClass;
    }
}
