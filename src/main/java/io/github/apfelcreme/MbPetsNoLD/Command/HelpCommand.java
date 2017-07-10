package io.github.apfelcreme.MbPetsNoLD.Command;

import io.github.apfelcreme.MbPetsNoLD.ChatInput;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
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
public class HelpCommand implements SubCommand {
    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(ChatInput chatInput) {

        if (!chatInput.getSender().hasPermission("MbPets.print")) {
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        if (chatInput.getType() == null) {
            // user only entered /pet help
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.head"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.type"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.confirm"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.call"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.cancel"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.info"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.list"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.status"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.uncall"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Options.bottom"));
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.types") + ChatColor.GREEN
                            + StringUtils.join(MbPetsConfig.getAvailableTypes(), ", "));
            return;
        }
        MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("help.Head"));
        PetType type = MbPetsConfig.parseType(chatInput.getType());
        switch (type) {
            case CHICKEN:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case COW:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case HORSE:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Color")
                                .replace("{1}",
                                        StringUtils.join(MbPetsConfig.getAvailableHorseColors(), ", ")));
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Style")
                                .replace("{1}", ChatColor.DARK_GRAY +
                                        StringUtils.join(MbPetsConfig.getAvailableHorseStyles(), ", ")));
                break;
            case MUSHROOM_COW:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case OCELOT:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Style")
                                .replace("{1}", ChatColor.DARK_GRAY +
                                        StringUtils.join(MbPetsConfig.getAvailableOcelotStyles(), ", ")));
                break;
            case PIG:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case POLAR_BEAR:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                        .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case BAT:
                break;
            case IRON_GOLEM:
                break;
            case RABBIT:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Style")
                                .replace("{1}", ChatColor.DARK_GRAY +
                                        StringUtils.join(MbPetsConfig.getAvailableRabbitTypes(), ", ")));
                break;
            case SHEEP:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                        .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Color")
                        .replace("{1}", ChatColor.DARK_GRAY +
                                StringUtils.join(MbPetsConfig.getAvailableDyeColors(), ", ")));
                break;
            case SLIME:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Size")
                        .replace("{1}", ChatColor.DARK_GRAY +
                                StringUtils.join(MbPetsConfig.getAvailableSlimeSizes(), ", ")));
                break;
            case DONKEY:
                break;
            case MULE:
                break;
            case LLAMA:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                        .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Color")
                        .replace("{1}", StringUtils.join(MbPetsConfig.getAvailableLlamaColors(), ", ")));
                break;
            case ENDERMAN:
                break;
            case MAGMA_CUBE:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Size")
                        .replace("{1}", ChatColor.DARK_GRAY +
                                StringUtils.join(MbPetsConfig.getAvailableSlimeSizes(), ", ")));
                break;
            case PARROT:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                        .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Color")
                        .replace("{1}", ChatColor.DARK_GRAY +
                                StringUtils.join(MbPetsConfig.getAvailableParrotColors(), ", ")));
                break;
            case SKELETON_HORSE:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case UNDEAD_HORSE:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                break;
            case WOLF:
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Baby")
                                .replace("{1}", MbPetsConfig.getTextNode("help.BABY")));
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                                .replace("{0}", ChatColor.DARK_GREEN + "Color")
                                .replace("{1}", ChatColor.DARK_GRAY +
                                        StringUtils.join(MbPetsConfig.getAvailableDyeColors(), ", ")));
                break;
            default:
                MbPets.sendMessage(chatInput.getSender(), ChatColor.DARK_GREEN + "keine");
                return;
        }
        MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.Element")
                        .replace("{0}", ChatColor.DARK_GREEN + "Name")
                        .replace("{1}", MbPetsConfig.getTextNode("help.NAME")));
    }
}
