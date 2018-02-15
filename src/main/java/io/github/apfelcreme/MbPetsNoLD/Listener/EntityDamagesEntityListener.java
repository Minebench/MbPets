package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;

import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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

    /**
     * target navigation
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerEntityDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && PetManager.getInstance().getPetByEntity(event.getDamager()) != null && !event.getEntity().getWorld().getPVP()) {
            event.setCancelled(true);
        } else if (event.getDamager() instanceof Player) {
            //Player attacks entity
            Pet pet = PetManager.getInstance().getPets().get(event.getDamager().getUniqueId());
            if (pet != null) {
                pet.onSpecifyTarget(event.getEntity(), event);
            }
        }
    }
}

