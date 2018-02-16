package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vex;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.UUID;

/**
 * Copyright (C) 2017 Max Lee (https://github.com/Phoenix616)
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
 * @author Max Lee aka Phoenix616
 */
public class VexPet extends Pet<Vex> {

    public VexPet(UUID owner, Integer number) {
        super(owner, PetType.VEX, number);
    }
    
    @Override
    public void onSpecifyTarget(LivingEntity target, EntityTargetEvent.TargetReason reason, Event event) {
        super.onSpecifyTarget(target, reason, event);
        getEntity().setTarget(target);
    }
    
    @Override
    public void onKill(LivingEntity killed, EntityDeathEvent event) {
        super.onKill(killed, event);
        getEntity().setTarget(null);
    }

}
