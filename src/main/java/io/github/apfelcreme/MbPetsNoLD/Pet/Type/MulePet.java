package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AbstractHorseInventory;

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
public class MulePet extends Pet<Mule> implements /*Styleable<Horse.Style>, Dyeable<Horse.Color>,*/ Ageable {

    private Boolean isBaby = null;

    public MulePet(UUID owner, Integer number) {
        super(owner, PetType.MULE, number);
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
        getEntity().setOwner(MbPets.getInstance().getServer().getPlayer(getOwner()));
        getEntity().setAgeLock(true);
        if (isBaby) {
            getEntity().setBaby();
        } else {
            getEntity().setAdult();
        }
    }

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
        }
        super.uncall();
    }

}
