package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Dyeable;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.DyeColor;
import org.bukkit.entity.Wolf;

import java.util.UUID;

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
public class WolfPet extends Pet implements Ageable, Dyeable<DyeColor> {

    private DyeColor color = null;
    private Boolean isBaby = null;

    public WolfPet(UUID owner, Integer number) {
        super(owner, PetType.WOLF, number);
    }

    /**
     * returns the color
     *
     * @return the color
     */
    @Override
    public DyeColor getColor() {
        return color;
    }

    /**
     * sets the color
     *
     * @param color the color
     */
    @Override
    public void setColor(DyeColor color) {
        this.color = color;
    }

    /**
     * is the pet a baby
     *
     * @return true or false
     */
    @Override
    public Boolean isBaby() {
        return isBaby;
    }

    /**
     * sets the "age"
     *
     * @param isBaby true or false
     */
    @Override
    public void setBaby(Boolean isBaby) {
        this.isBaby = isBaby;
    }

    /**
     * applies all attributes to the entity
     */
    @Override
    public void applyAttributes() {
        getEntity().setCustomName(getName());
        ((Wolf) getEntity()).setCollarColor(color);
        ((Wolf) getEntity()).setTamed(true);
        ((Wolf) getEntity()).setAgeLock(true);
        ((Wolf) getEntity()).setSitting(false);
        if (isBaby) {
            ((Wolf) getEntity()).setBaby();
        } else {
            ((Wolf) getEntity()).setAdult();
        }
    }
}
