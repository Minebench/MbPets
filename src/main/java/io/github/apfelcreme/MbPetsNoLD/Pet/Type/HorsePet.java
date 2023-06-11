package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Dyeable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Styleable;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;

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
public class HorsePet extends Pet<Horse> implements Ageable, Dyeable<Horse.Color>, Styleable<Horse.Style> {

    private Boolean isBaby = null;
    private Horse.Color color = null;
    private Horse.Style style = null;

    public HorsePet(UUID owner, Integer number) {
        super(owner, PetType.HORSE, number);
    }

    /**
     * returns the horse style
     *
     * @return the horse style
     */
    @Override
    public Style getStyle() {
        return style;
    }

    /**
     * sets the horse style
     *
     * @param style the horse style
     */
    @Override
    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public Style parseStyle(String style) {
        return MbPetsConfig.parseHorseStyle(style);
    }

    /**
     * returns the horse color
     *
     * @return the horse color
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * sets the horse color
     *
     * @param color the horse color
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color parseColor(String color) {
        return MbPetsConfig.parseHorseColor(color);
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
        getEntity().setColor(color);
        getEntity().setStyle(style);
        getEntity().setOwner(MbPets.getInstance().getServer().getPlayer(getOwner()));
        getEntity().setAgeLock(true);
        if (isBaby) {
            getEntity().setBaby();
        } else {
            getEntity().setAdult();
        }
    }

    /**
     * remove armor and saddle
     */
    @Override
    public void uncall() {
        Player player = MbPets.getInstance().getServer().getPlayer(getOwner());
        if (player != null) {
            if (getEntity().getInventory().getSaddle() != null) {
                if (player.getInventory().firstEmpty() > 0) {
                    player.getInventory().addItem(getEntity().getInventory().getSaddle());
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(),
                            getEntity().getInventory().getSaddle());

                }
            }
            if (getEntity().getInventory().getArmor() != null) {
                if (player.getInventory().firstEmpty() > 0) {
                    player.getInventory().addItem(getEntity().getInventory().getArmor());
                } else {
                        player.getWorld().dropItemNaturally(player.getLocation(),
                                getEntity().getInventory().getArmor());

                }
            }
        }
        super.uncall();
    }

}
