package io.github.apfelcreme.MbPetsNoLD.Pet.Type;

import io.github.apfelcreme.MbPetsNoLD.Interface.Sizeable;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import org.bukkit.entity.MagmaCube;

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
public class MagmaCubePet extends Pet<MagmaCube> implements Sizeable {

    private Integer size = null;

    public MagmaCubePet(UUID owner, Integer number) {
        super(owner, PetType.MAGMA_CUBE, number);
    }

    /**
     * returns the size
     *
     * @return the size
     */
    @Override
    public Integer getSize() {
        return size;
    }

    /**
     * sets the size
     *
     * @param size the size
     */
    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * applies all attributes to the entity
     */
    @Override
    public void applyAttributes() {
        super.applyAttributes();
        getEntity().setSize(size);
    }
    
    @Override
    public boolean canNavigate() {
        return false;
    }

}
