package io.github.apfelcreme.MbPetsNoLD.Command;

import io.github.apfelcreme.MbPetsNoLD.*;
import io.github.apfelcreme.MbPetsNoLD.Pet.*;

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
public class ModifyCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(final ChatInput chatInput) {
        if (MbPets.getInstance().isVaultEnabled()) {
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.noVault"));
            return;
        }
        if (!chatInput.getSender().hasPermission("MbPets.modify")) {
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        int number = chatInput.getNumber() != null ? chatInput
                .getNumber() : PetManager.getInstance()
                .getHighestPetNumber(chatInput.getSender().getUniqueId());
        Pet pet = PetManager.getInstance().loadPet(chatInput.getSender().getUniqueId(), number);
        if (pet != null) {
            PetConfiguration petConfiguration = new PetConfiguration(pet, PetConfiguration.ConfigurationType.MODIFICATION);
            MbPets.sendMessage(chatInput.getSender(), petConfiguration.getPetDescription().getDescription());
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Confirm"));
            PetManager.getInstance().getConfigurations().put(chatInput.getSender().getUniqueId(), petConfiguration);
        }
    }
}
