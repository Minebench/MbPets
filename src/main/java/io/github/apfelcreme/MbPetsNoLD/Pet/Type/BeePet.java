package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Bee;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.UUID;

/*
 * MbPets
 * Copyright (c) 2020 Max Lee aka Phoenix616 (mail@moep.tv)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class BeePet extends Pet<Bee> implements Ageable {

    private Boolean isBaby = null;

    public BeePet(UUID owner, Integer number) {
        super(owner, PetType.BEE, number);
    }
    
    @Override
    public void onSpecifyTarget(LivingEntity target, EntityTargetEvent.TargetReason reason, Event event) {
        super.onSpecifyTarget(target, reason, event);
        getEntity().setAnger(100);
    }

    @Override
    public boolean onAttack() {
        super.onAttack();
        getEntity().setAnger(100);
        return true;
    }
    
    @Override
    public boolean canNavigate() {
        return false;
    }

    @Override
    public Boolean isBaby() {
        return isBaby;
    }

    @Override
    public void setBaby(Boolean isBaby) {
        this.isBaby = isBaby;
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        getEntity().setAgeLock(true);
        getEntity().setCannotEnterHiveTicks(Integer.MAX_VALUE);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.BEE_BECOME_ANGRY);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.BEE_ATTACK);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.BEE_ENTER_HIVE);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.BEE_GO_TO_HIVE);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.BEE_GO_TO_KNOWN_FLOWER);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.BEE_GROW_CROP);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.BEE_LOCATE_HIVE);
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.BEE_POLLINATE);

        if (isBaby) {
            getEntity().setBaby();
        } else {
            getEntity().setAdult();
        }
    }
}
