package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.Material;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

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
public class DonkeyPet extends Pet implements Ageable {

    private Boolean isBaby = null;

    public DonkeyPet(UUID owner, Integer number) {
        super(owner, PetType.DONKEY, number);
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
        ((Donkey) getEntity()).setOwner(MbPets.getInstance().getServer().getPlayer(getOwner()));
        ((Donkey) getEntity()).setTamed(true);
        ((Donkey) getEntity()).setAgeLock(true);
        if (isBaby) {
            ((Donkey) getEntity()).setBaby();
        } else {
            ((Donkey) getEntity()).setAdult();
        }
    }
    
    @Override
    public boolean onRightClick(Player player, PlayerInteractEntityEvent event) {
        boolean r = super.onRightClick(player, event);
        if (player.getInventory().getItemInMainHand().getType() == Material.CHEST) {
            player.getInventory().addItem(new ItemStack(Material.CHEST, 1));
            player.getInventory().removeItem(new ItemStack(Material.CHEST, 1));
            event.setCancelled(true);
        }
        return r;
    }
}
