package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Styleable;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Rabbit;

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
public class RabbitPet extends Pet<Rabbit> implements Ageable, Styleable<Rabbit.Type> {


    private Boolean isBaby = null;
    private Rabbit.Type style = null;

    public RabbitPet(UUID owner, Integer number) {
        super(owner, PetType.RABBIT, number);
    }

    /**
     * returns the style
     *
     * @return the style
     */
    @Override
    public Rabbit.Type getStyle() {
        return style;
    }

    /**
     * sets the style
     *
     * @param style the style
     */
    @Override
    public void setStyle(Rabbit.Type style) {
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
        super.applyAttributes();
        getEntity().setRabbitType(getStyle());
        getEntity().setAgeLock(true);
        if (isBaby) {
            getEntity().setBaby();
        } else {
            getEntity().setAdult();
        }
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.RABBIT_AVOID_ENTITY);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.RABBIT_PANIC);
    }
}
