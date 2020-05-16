package io.github.apfelcreme.MbPetsNoLD;

import io.github.apfelcreme.MbPetsNoLD.ChatInput.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.apfelcreme.MbPetsNoLD.Pet.PetConfiguration;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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
public class MbPetsTabCompleter implements TabCompleter {

    public MbPetsTabCompleter() {
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command,
                                      String arg2, String[] args) {
        Set<String> list = new HashSet<>();
        if (args.length > 1) {
            Operation operation = Operation.getOperation(args[args.length - 2]);
            PetConfiguration petConfiguration = PetManager.getInstance().getConfigurations().get(((Player)commandSender).getUniqueId());
            if (operation != null) {
                switch (operation) {
                    case BABY:
                        list.addAll(Arrays.asList("true", "false"));
                        break;
                    case COLOR:
                        if (petConfiguration != null) {
                            switch (petConfiguration.getType()) {
                                case HORSE:
                                    list.addAll(MbPetsConfig.getAvailableHorseColors());
                                    break;
                                case LLAMA:
                                    list.addAll(MbPetsConfig.getAvailableLlamaColors());
                                    break;
                                case PARROT:
                                    list.addAll(MbPetsConfig.getAvailableParrotColors());
                                    break;
                                case SHEEP:
                                case WOLF:
                                    list.addAll(MbPetsConfig.getAvailableDyeColors());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            list.addAll(MbPetsConfig.getAvailableHorseColors());
                            list.addAll(MbPetsConfig.getAvailableLlamaColors());
                            list.addAll(MbPetsConfig.getAvailableParrotColors());
                            list.addAll(MbPetsConfig.getAvailableDyeColors());
                        }
                        break;
                    case SIZE:
                        list.addAll(MbPetsConfig.getAvailableSlimeSizes());
                        break;
                    case STYLE:
                        if (petConfiguration != null) {
                            switch (petConfiguration.getType()) {
                                case HORSE:
                                    list.addAll(MbPetsConfig.getAvailableHorseStyles());
                                    break;
                                case CAT:
                                    list.addAll(MbPetsConfig.getAvailableCatTypes());
                                    break;
                                case RABBIT:
                                    list.addAll(MbPetsConfig.getAvailableRabbitTypes());
                                    break;
                                case FOX:
                                    list.addAll(MbPetsConfig.getAvailableFoxTypes());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            list.addAll(MbPetsConfig.getAvailableHorseStyles());
                            list.addAll(MbPetsConfig.getAvailableCatTypes());
                            list.addAll(MbPetsConfig.getAvailableRabbitTypes());
                            list.addAll(MbPetsConfig.getAvailableFoxTypes());
                        }
                        break;
                    case TYPE:
                        list.addAll(MbPetsConfig.getAvailableTypes(commandSender));
                        break;
                    default:

                        break;
                }
            }
        }
        //remove duplicates & sort
        return list.stream()
                .filter(s -> args[args.length - 1].isEmpty() || s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
    }
}
