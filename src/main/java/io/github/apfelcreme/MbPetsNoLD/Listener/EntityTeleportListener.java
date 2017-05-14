package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;

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
public class EntityTeleportListener implements Listener {

    //TODO Chicken machen keinen schaden?

    /**
     * removes Entity from the activePets-List when going through a Nether
     * Portal bc this caused some kind of endless replication bug...
     *
     * @param event
     */
    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        Pet pet = PetManager.getInstance().getPetByEntity(event.getEntity());
        if (pet != null) {
            // a pet ran through a portal
            pet.uncall();
        }

    }
}