package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.entity.Mule;

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
public class MulePet extends Pet implements /*Styleable<Horse.Style>, Dyeable<Horse.Color>,*/ Ageable {

    // color and style does not work (1.10)
    private Boolean isBaby = null;
//    private Horse.Color color = null;
//    private Horse.Style style = null;

    public MulePet(UUID owner, Integer number) {
        super(owner, PetType.MULE, number);
    }

//    /**
//     * returns the horse style
//     *
//     * @return the horse style
//     */
//    @Override
//    public Horse.Style getStyle() {
//        return style;
//    }
//
//    /**
//     * sets the horse style
//     *
//     * @param style the horse style
//     */
//    @Override
//    public void setStyle(Horse.Style style) {
//        this.style = style;
//    }
//
//    /**
//     * returns the horse color
//     *
//     * @return the horse color
//     */
//    @Override
//    public Horse.Color getColor() {
//        return color;
//    }
//
//    /**
//     * sets the horse color
//     *
//     * @param color the horse color
//     */
//    @Override
//    public void setColor(Horse.Color color) {
//        this.color = color;
//    }

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
//        ((Horse) getEntity()).setColor(color);
//        ((Horse) getEntity()).setStyle(style);
        ((Mule) getEntity()).setOwner(MbPets.getInstance().getServer().getPlayer(getOwner()));
        ((Mule) getEntity()).setTamed(true);
        ((Mule) getEntity()).setAgeLock(true);
        if (isBaby) {
            ((Mule) getEntity()).setBaby();
        } else {
            ((Mule) getEntity()).setAdult();
        }
    }

//    @Override
//    public void uncall() {
//        if (((Mule) getEntity()).getInventory().get != null) {
//            MbPets.getInstance().getServer().getPlayer(getOwner()).getInventory().addItem(((Horse) getEntity()).getInventory().getSaddle());
//        }
//        super.uncall();
//    }

}
