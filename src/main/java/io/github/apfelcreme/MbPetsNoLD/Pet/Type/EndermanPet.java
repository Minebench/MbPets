package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
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
    public void onSpecifyTarget(LivingEntity target, EntityTargetEvent.TargetReason reason, Event event) {
        super.onSpecifyTarget(target, reason, event);
        Location teleportTo = target.getLocation().add(target.getLocation().getDirection().normalize());
        getEntity().teleport(teleportTo);
        getEntity().setTarget(target);
    }
    
    @Override
    public void onKill(LivingEntity killed, EntityDeathEvent event) {
        super.onKill(killed, event);
        getEntity().setTarget(null);
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.ENDERMAN_FREEZE_WHEN_LOOKED_AT);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.ENDERMAN_TAKE_BLOCK);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.ENDERMAN_LEAVE_BLOCK);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.ENDERMAN_LOOK_FOR_PLAYER);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.LOOK_AT_PLAYER);
    }
}
