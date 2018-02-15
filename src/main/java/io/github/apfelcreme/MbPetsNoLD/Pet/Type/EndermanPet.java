package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTeleportEvent;

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
public class EndermanPet extends Pet<Enderman> {


    public EndermanPet(UUID owner, Integer number) {
        super(owner, PetType.ENDERMAN, number);
    }
    
    @Override
    public void onTeleport(EntityTeleportEvent event) {
        super.onTeleport(event);
        event.setCancelled(true);
    }
    
    @Override
    public void onSpecifyTarget(Entity target, Event event) {
        super.onSpecifyTarget(target, event);
        Location teleportTo = target.getLocation().add(target.getLocation().getDirection().normalize());
        getEntity().teleport(teleportTo);
        getEntity().setTarget((LivingEntity) target);
    }
    
    @Override
    public void onKill(LivingEntity killed, EntityDeathEvent event) {
        super.onKill(killed, event);
        getEntity().setTarget(null);
    }
}
