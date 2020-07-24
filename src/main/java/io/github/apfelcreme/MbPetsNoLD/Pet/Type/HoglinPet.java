package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Hoglin;

import java.util.UUID;

/**
 * Copyright (C) 2020 Lord36 aka Apfelcreme
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
public class HoglinPet extends Pet<Hoglin> implements Ageable  {

    private Boolean isBaby = null;

    public HoglinPet(UUID owner, Integer number) {
        super(owner, PetType.HOGLIN, number);
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
        super.applyAttributes();
        getEntity().setAgeLock(true);
        getEntity().setImmuneToZombification(true);
        getEntity().setIsAbleToBeHunted(false);
        if (isBaby) {
            getEntity().setBaby();
        } else {
            getEntity().setAdult();
        }
    }
}
