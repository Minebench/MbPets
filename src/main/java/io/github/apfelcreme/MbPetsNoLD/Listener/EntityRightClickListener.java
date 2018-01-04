package io.github.apfelcreme.MbPetsNoLD.Listener;


import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import io.github.apfelcreme.MbPetsNoLD.Pet.Type.DonkeyPet;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;

import io.github.apfelcreme.MbPetsNoLD.Pet.Type.LlamaPet;
import org.bukkit.Material;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

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
public class EntityRightClickListener implements Listener {

    /**
     * cancels all interacts on entities
     *
     * @param e
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityRightClick(PlayerInteractEntityEvent e) {
        final Pet pet = PetManager.getInstance().getPetByEntity(e.getRightClicked());
        if (pet != null) {
            if ((pet instanceof DonkeyPet) || pet instanceof LlamaPet) {
                if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.CHEST) {
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.CHEST, 1));
                    e.getPlayer().getInventory().removeItem(new ItemStack(Material.CHEST, 1));
                    e.setCancelled(true);
                }
            }
            String newName = pet.getRightClickName();
            pet.getEntity().setCustomName(newName);
            MbPets.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(MbPets.getInstance(), () -> pet.getEntity().setCustomName(pet.getName()), 60L);
        }
    }
}
