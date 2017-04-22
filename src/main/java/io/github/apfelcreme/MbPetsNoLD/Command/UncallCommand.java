package io.github.apfelcreme.MbPetsNoLD.Command;

import io.github.apfelcreme.MbPetsNoLD.ChatInput;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;

/**
 * Copyright (C) 2015 Lord36 aka Apfelcreme
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
 * @author Lord36 aka Apfelcreme on 01.07.2015.
 */
public class UncallCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(ChatInput chatInput) {
        if (!chatInput.getSender().getPlayer().hasPermission("MbPets.call")) {
            MbPets.sendMessage(chatInput.getSender(),
                    MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        Pet pet = PetManager.getInstance().getPets().get(chatInput.getSender().getUniqueId());
        if (pet != null) {
            pet.uncall();
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.petUncalled"));
        } else {
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.noActivePet"));
        }
    }
}
