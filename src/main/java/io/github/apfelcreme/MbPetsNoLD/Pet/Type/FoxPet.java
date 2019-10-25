package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Styleable;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.entity.Fox;

import java.util.UUID;

/**
 * Copyright (C) 2019 Lord36 aka Apfelcreme
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
public class FoxPet extends Pet<Fox> implements Styleable<Fox.Type>, Ageable {

    private Boolean isBaby = null;
    private Fox.Type style = null;

    public FoxPet(UUID owner, Integer number) {
        super(owner, PetType.FOX, number);
    }

    /**
     * returns the style of the pet
     *
     * @return the style of the pet
     */
    @Override
    public Fox.Type getStyle() {
        return style;
    }

    /**
     * sets the style
     *
     * @param style the style
     */
    @Override
    public void setStyle(Fox.Type style) {
        this.style = style;
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
     * set the "age"
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
        ((Fox) getEntity()).setFoxType(style);
        ((Fox) getEntity()).setAgeLock(true);

        if (isBaby) {
            ((Fox) getEntity()).setBaby();
        } else {
            ((Fox) getEntity()).setAdult();
        }
    }
}
