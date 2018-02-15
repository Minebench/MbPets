package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import io.github.apfelcreme.MbPetsNoLD.Tasks.FollowTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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
public class PlayerTeleportListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo().getWorld().equals(event.getFrom().getWorld())) {
            Pet pet = PetManager.getInstance().getPets().get(event.getPlayer().getUniqueId());
            if (pet != null) {
                pet.getEntity().teleport(FollowTask.getLocationNextTo(event.getTo(), 2.2));
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Pet pet = PetManager.getInstance().getPets().get(event.getPlayer().getUniqueId());
        if (pet != null) {
            pet.call();
        }
    }
}
