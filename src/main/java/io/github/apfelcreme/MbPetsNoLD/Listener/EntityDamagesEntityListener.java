package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;

import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

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
public class EntityDamagesEntityListener implements Listener {

    private Map<Pet, BukkitTask> resetTimers = new HashMap<Pet, BukkitTask>();

    /**
     * target navigation
     *
     * @param event
     */
    @EventHandler
    public void onPlayerEntityDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && PetManager.getInstance().getPetByEntity(event.getDamager()) != null) {
            event.setCancelled(true);
        } else {
            final Pet pet = PetManager.getInstance().getPets().get(event.getDamager().getUniqueId());
            if (event.getDamager() instanceof Player && pet != null) {
                //Player attacks entity
                pet.setTarget(event.getEntity());
                pet.setSpeed(MbPetsConfig.getEnhancedPetSpeed(pet.getType()));
                pet.getEntity().getWorld().playSound(pet.getEntity().getLocation(), MbPetsConfig.getPetSound(pet.getType()), 5, 1);

                if (resetTimers.get(pet) != null) {
                    resetTimers.get(pet).cancel();
                    resetTimers.remove(pet);
                }

                BukkitTask task = MbPets.getInstance().getServer().getScheduler().runTaskLater(MbPets.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        pet.setTarget(event.getDamager());
                        pet.setSpeed(MbPetsConfig.getPetSpeed(pet.getType()));
                    }
                }, 200L);
                resetTimers.put(pet, task);
            }
        }
    }
}

