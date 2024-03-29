package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

/*
 * MbPetsNoLibsDisguises
 * Copyright (c) 2018 Max Lee aka Phoenix616 (mail@moep.tv)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Turtle;

import java.util.UUID;

public class TurtlePet extends Pet<Turtle> implements Ageable {

    private Boolean isBaby = null;

    public TurtlePet(UUID owner, Integer number) {
        super(owner, PetType.TURTLE, number);
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
        if (isBaby) {
            getEntity().setBaby();
        } else {
            getEntity().setAdult();
        }
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.TURTLE_GO_HOME);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.TURTLE_LAY_EGG);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.TURTLE_PANIC);
    }
}
