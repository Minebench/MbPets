package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Interface.Dyeable;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.entity.Parrot;

import java.util.UUID;

/**
 * Copyright (C) 2017 Lord36 aka Apfelcreme
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
public class ParrotPet extends Pet<Parrot> implements  Dyeable<Parrot.Variant> {

    private Parrot.Variant color;

    public ParrotPet(UUID owner, Integer number) {
        super(owner, PetType.PARROT, number);
    }

    /**
     * applies all attributes to the entity
     */
    @Override
    public void applyAttributes() {
        super.applyAttributes();
        getEntity().setVariant(color);
        getEntity().setOwner(MbPets.getInstance().getServer().getPlayer(getOwner()));
    }

    /**
     * returns the color of the pet
     *
     * @return the color of the pet
     */
    @Override
    public Parrot.Variant getColor() {
        return color;
    }

    /**
     * sets the color
     *
     * @param color the color
     */
    @Override
    public void setColor(Parrot.Variant color) {
        this.color = color;
    }
}
