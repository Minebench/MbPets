package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.text.DecimalFormat;

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
public class EntityDeathListener implements Listener {

    /**
     * the damage a pets takes is modified by its level
     *
     * @param event the event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Pet pet = PetManager.getInstance().getPetByTargetEntity(event.getEntity());
        if (pet != null) {
            pet.onKill(event.getEntity(), event);
        }
    }

    /**
     * if a pet dies, the user cannot call another one for the next x seconds
     *
     * @param event the event
     */
    @EventHandler
    public void onPetDeath(EntityDeathEvent event) {
        Pet pet = PetManager.getInstance().getPetByEntity(event.getEntity());
        if (pet != null) {
            pet.onDeath(event);
            PetManager.getInstance().getCooldowns().put(pet.getOwner(), System.currentTimeMillis());
            pet.uncall();
            MbPets.sendMessage(pet.getOwner(), MbPetsConfig.getTextNode("info.petDied")
                    .replace("{0}", new DecimalFormat("0").format(MbPetsConfig.getPetDeathCooldown() / 1000)));
            event.getDrops().clear();
        }
    }
    
    /**
     * Fix slimes by checking if pet gets damage above remaining life
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSlimeDeath(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Slime)
                && ((Slime) event.getEntity()).getSize() > 0
                && ((Slime) event.getEntity()).getHealth() - event.getFinalDamage() <= 0) {
            Pet pet = PetManager.getInstance().getPetByEntity(event.getEntity());
            if (pet != null) {
                ((Slime) event.getEntity()).setSize(0);
            }
        }
    }
}
