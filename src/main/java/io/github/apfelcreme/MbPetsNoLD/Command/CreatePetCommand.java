package io.github.apfelcreme.MbPetsNoLD.Command;

import io.github.apfelcreme.MbPetsNoLD.ChatInput;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetConfiguration;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

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
public class CreatePetCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(ChatInput chatInput) {
        if (!chatInput.getSender().getPlayer().hasPermission("MbPets.buy")) {
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        PetConfiguration petConfiguration = PetManager.getInstance().getConfigurations().get(chatInput.getSender().getUniqueId());
        if (petConfiguration == null) {
            if (chatInput.getType() != null && MbPetsConfig.parseType(chatInput.getType()) != null) {
                PetType type = MbPetsConfig.parseType(chatInput.getType());
                if (type == PetType.SLIME || type == PetType.MAGMA_CUBE) {
                    MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.slimebug"));
                    return;
                }
                petConfiguration = new PetConfiguration(
                        chatInput.getSender().getUniqueId(),
                        MbPetsConfig.parseType(chatInput.getType()),
                        PetConfiguration.ConfigurationType.PURCHASE);
            } else {
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.missingType"));
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.types") + ChatColor.GREEN
                        + StringUtils.join(MbPetsConfig.getAvailableTypes(), ", "));
                return;
            }
        }

        // set the colors
        if (chatInput.getColor() != null) {
            if (petConfiguration.getType() == PetType.HORSE) {
                petConfiguration.setHorseColor(MbPetsConfig.parseHorseColor(chatInput.getColor()));
            } else if (petConfiguration.getType() == PetType.SHEEP) {
                petConfiguration.setSheepColor(MbPetsConfig.parseColor(chatInput.getColor()));
            } else if (petConfiguration.getType() == PetType.WOLF) {
                petConfiguration.setWolfColor(MbPetsConfig.parseColor(chatInput.getColor()));
            } else if (petConfiguration.getType() == PetType.LLAMA) {
                petConfiguration.setLlamaColor(MbPetsConfig.parseLlamaColor(chatInput.getColor()));
            }
        }

        // set the styles
        if (chatInput.getStyle() != null) {
            if (petConfiguration.getType() == PetType.HORSE/*|| petConfiguration.getType() == PetType.MULE*/) {
                petConfiguration.setHorseStyle(MbPetsConfig.parseHorseStyle(chatInput.getStyle()));
            } else if (petConfiguration.getType() == PetType.OCELOT) {
                petConfiguration.setOcelotType(MbPetsConfig.parseOcelotType(chatInput.getStyle()));
            } else if (petConfiguration.getType() == PetType.RABBIT) {
                petConfiguration.setRabbitType(MbPetsConfig.parseRabbitType(chatInput.getStyle()));
            }
        }

        // set the age
        if (chatInput.getBaby() != null) {
            petConfiguration.setBaby(Boolean.parseBoolean(chatInput.getBaby()));
        }

        // set the size
        if (chatInput.getSize() != null) {
            petConfiguration.setSlimeSize(MbPetsConfig.parseSlimeSize(chatInput.getSize()));
        }

        // set the name
        if (chatInput.getName() != null) {
            petConfiguration.setName(chatInput.getName());
        }

        // store the configuration object
        PetManager.getInstance().getConfigurations().put(chatInput.getSender().getUniqueId(), petConfiguration);
        MbPets.sendMessage(chatInput.getSender(), petConfiguration.getPetDescription().getDescription());
        if (petConfiguration.isFinished()) {
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Confirm"));
        }

    }

}
