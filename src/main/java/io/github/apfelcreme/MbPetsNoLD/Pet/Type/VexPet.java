package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Vex;

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
    public boolean canNavigate() {
        return false;
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Bukkit.getMobGoals().removeGoal(getEntity(), VanillaGoal.VEX_COPY_TARGET_OF_OWNER);
    }
}
