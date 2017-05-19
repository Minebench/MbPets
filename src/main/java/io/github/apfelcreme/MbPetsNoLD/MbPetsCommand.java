package io.github.apfelcreme.MbPetsNoLD;

import io.github.apfelcreme.MbPetsNoLD.ChatInput.Operation;
import io.github.apfelcreme.MbPetsNoLD.Command.*;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import net.zaiyers.UUIDDB.bukkit.UUIDDB;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
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
public class MbPetsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command cmd, String lbl, final String[] args) {
        MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (commandSender instanceof Player) {
                    Player sender = (Player) commandSender;
                    if (cmd.getName().equalsIgnoreCase("pet")) {
                        ChatInput chatInput = exploreArgs(args, sender);
                        SubCommand subCommand = null;
                        if (chatInput != null) {
                            switch (chatInput.getOperation()) {
                                case BABY:
                                case COLOR:
                                case STYLE:
                                case SIZE:
                                case TYPE:
                                case NAME:
                                    subCommand = new CreatePetCommand();
                                    break;
                                case CALL:
                                    subCommand = new CallCommand();
                                    break;
                                case CANCEL:
                                    subCommand = new CancelPreparationCommand();
                                    break;
                                case CONFIRM:
                                    subCommand = new ConfirmCommand();
                                    break;
                                case CONVERT:
                                    subCommand = new ConvertCommand();
                                    break;
                                case DELETE:
                                    subCommand = new DeleteCommand();
                                    break;
                                case DESPAWN:
                                    subCommand = new DespawnCommand();
                                    break;
                                case FLUSH:
                                    subCommand = new FlushCommand();
                                    break;
                                case HELP:
                                    subCommand = new HelpCommand();
                                    break;
                                case INFO:
                                    subCommand = new InfoCommand();
                                    break;
                                case LEASH:
                                    subCommand = new LeashCommand();
                                    break;
                                case LIST:
                                    subCommand = new ListCommand();
                                    break;
                                case MODIFY:
                                    subCommand = new ModifyCommand();
                                    break;
                                case MONITOR:
                                    subCommand = new MonitorCommand();
                                    break;
                                case RELOAD:
                                    subCommand = new ReloadCommand();
                                    break;
                                case SELL:
                                    subCommand = new SellCommand();
                                    break;
                                case STATUS:
                                    subCommand = new PrintStatusCommand();
                                    break;
                                case UNCALL:
                                    subCommand = new UncallCommand();
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (subCommand != null) {
                            subCommand.execute(chatInput);
                        }
                    }
                }

            }
        });
        return false;
    }

    /**
     * explodes the users chat input and stores it into a {@link ChatInput}
     * object
     *
     * @param args   the command args
     * @param sender the command sender
     * @return nicely stored information
     */
    private ChatInput exploreArgs(String[] args, Player sender) {
        if (args.length == 0) {
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.head"));
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.type"));
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.confirm"));
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.call"));
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.cancel"));
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.info"));
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.list"));
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.status"));
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.uncall"));
            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("help.Options.bottom"));
            return null;
        } else {
            ChatInput chatInput = new ChatInput();
            chatInput.setSender(sender);
            Operation mainOperation = Operation.getOperation(args[0]);
            if (mainOperation != null) {
                chatInput.setOperation(mainOperation);
            } else {
                MbPets.sendMessage(sender, MbPetsConfig.getTextNode("error.wrongFunction")
                        .replace("{0}", args[0]));
                return null;
            }
            for (int i = 0; i < args.length; i++) {
                Operation operation = Operation.getOperation(args[i]);
                if (NumberUtils.isNumber(args[i])) {
                    chatInput.setNumber(Integer.parseInt(args[i]));
                    continue;
                }
                if (operation != null) {
                    if (i + 1 < args.length || !Operation.needsValue(operation)) {
                        switch (operation) {
                            case BABY:
                                chatInput.setBaby(args[i + 1]);
                                continue;
                            case COLOR:
                                chatInput.setColor(args[i + 1]);
                                continue;
                            case NAME:
                                chatInput.setName(args[i + 1]);
                                continue;
                            case SIZE:
                                chatInput.setSize(args[i + 1]);
                                continue;
                            case STYLE:
                                chatInput.setStyle(args[i + 1]);
                                continue;
                            case TYPE:
                                chatInput.setType(args[i + 1]);
                                continue;
                            default:
                                continue;
                        }
                    } else {
                        MbPets.sendMessage(sender, MbPetsConfig.getTextNode(
                                "error.missingValue").replace(
                                "{0}",
                                WordUtils.capitalize(operation.name().toLowerCase())));
                        List<String> possibleValues = new ArrayList<>();
                        switch (operation) {
                            case BABY:
                            case NAME:
                                possibleValues.add(MbPetsConfig.getTextNode("help." + operation));
                                break;
                            case COLOR:
                                possibleValues.add("(" + MbPetsConfig.getNode("PetTypes." + PetType.HORSE.name() + ".displaytext") + ")\n"
                                        + ChatColor.GRAY + StringUtils.join(MbPetsConfig.getAvailableHorseColors(), ", "));
                                possibleValues.add("(" + MbPetsConfig.getNode("PetTypes." + PetType.LLAMA.name() + ".displaytext") + ")\n"
                                        + ChatColor.GRAY + StringUtils.join(MbPetsConfig.getAvailableLlamaColors(), ", "));
                                possibleValues.add("(" + MbPetsConfig.getNode("PetTypes." + PetType.WOLF.name() + ".displaytext")
                                        + " & " + MbPetsConfig.getNode("PetTypes." + PetType.SHEEP.name() + ".displaytext") + ")\n"
                                        + ChatColor.GRAY + StringUtils.join(MbPetsConfig.getAvailableDyeColors(), ", "));
                                break;
                            case SIZE:
                                possibleValues.add("(" + MbPetsConfig.getNode("PetTypes." + PetType.SLIME.name() + ".displaytext") + ")\n"
                                        + ChatColor.GRAY + StringUtils.join(MbPetsConfig.getAvailableSlimeSizes(), ", "));
                                break;
                            case STYLE:
                                possibleValues.add("(" + MbPetsConfig.getNode("PetTypes." + PetType.HORSE.name() + ".displaytext") + ")\n"
                                        + ChatColor.GRAY + StringUtils.join(MbPetsConfig.getAvailableHorseStyles(), ", "));
                                possibleValues.add("(" + MbPetsConfig.getNode("PetTypes." + PetType.RABBIT.name() + ".displaytext") + ")\n"
                                        + ChatColor.GRAY + StringUtils.join(MbPetsConfig.getAvailableRabbitTypes(), ", "));
                                possibleValues.add("(" + MbPetsConfig.getNode("PetTypes." + PetType.OCELOT.name() + ".displaytext") + ")\n"
                                        + ChatColor.GRAY + StringUtils.join(MbPetsConfig.getAvailableOcelotStyles(), ", "));
                                break;
                            case TYPE:
                                possibleValues.add(StringUtils.join(MbPetsConfig.getAvailableTypes(), ", "));
                                break;
                            default:
                                possibleValues.add("I don't know :(");
                        }
                        for (String possibleValue : possibleValues) {
                            MbPets.sendMessage(sender, MbPetsConfig.getTextNode("error.possibleValues").replace("{0}", possibleValue));
                        }
                        return null;
                    }
                }
                if (UUIDDB.getInstance() != null) {
                    if (UUIDDB.getInstance().getStorage().getUUIDByName(args[i], false) != null) {
                        chatInput.setTargetPlayer(MbPets
                                .getInstance()
                                .getServer()
                                .getOfflinePlayer(
                                        UUID.fromString(UUIDDB.getInstance().getStorage().getUUIDByName(args[i], false))));
                    }
                }
            }
            return chatInput;

        }
    }

}
