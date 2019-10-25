package io.github.apfelcreme.MbPetsNoLD;

import io.github.apfelcreme.MbPetsNoLD.ChatInput.Operation;

import java.util.*;

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
        ArrayList<String> list = new ArrayList<String>();
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
                                    list.addAll(getDisplayTexts("HorseColors"));
                                    break;
                                case SHEEP:
                                case WOLF:
                                    list.addAll(getDisplayTexts("DyeColors"));
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            list.addAll(getDisplayTexts("HorseColors"));
                            list.addAll(getDisplayTexts("DyeColors"));
                        }
                        break;
                    case SIZE:
                        list.addAll(getDisplayTexts("SlimeSizes"));
                        break;
                    case STYLE:
                        if (petConfiguration != null) {
                            switch (petConfiguration.getType()) {
                                case HORSE:
                                    list.addAll(getDisplayTexts("HorseStyles"));
                                    break;
                                case CAT:
                                    list.addAll(getDisplayTexts("CatTypes"));
                                    break;
                                case RABBIT:
                                    list.addAll(getDisplayTexts("RabbitTypes"));
                                    break;
                                case FOX:
                                    list.addAll(getDisplayTexts("FoxTypes"));
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            list.addAll(getDisplayTexts("HorseStyles"));
                            list.addAll(getDisplayTexts("CatTypes"));
                            list.addAll(getDisplayTexts("RabbitTypes"));
                            list.addAll(getDisplayTexts("FoxTypes"));
                        }
                        break;
                    case TYPE:
                        list.addAll(getDisplayTexts("PetTypes"));
                        break;
                    default:

                        break;
                }
            }
        }
        //remove duplicates & sort
        List<String> ret = new ArrayList<>();
        ret.addAll(new HashSet<>(list));
        Collections.sort(ret);
        return ret;
    }

    /**
     * returns a list of nice strings
     * @param section the ConfigurationSection name
     * @return the list of displaytexts
     */
    private List<String> getDisplayTexts(String section) {
        List<String> list = new ArrayList<String>();
        for (String key : MbPetsConfig.getLanguageConfiguration().getConfigurationSection(section).getKeys(true)) {
            if (key.endsWith("displaytext")) {
                list.add(MbPetsConfig.getLanguageConfiguration().getString(section + "." + key));
            }
        }
        return list;
    }
}
