package io.github.apfelcreme.MbPetsNoLD.Command;

import io.github.apfelcreme.MbPetsNoLD.ChatInput;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import io.github.apfelcreme.MbPetsNoLD.Tasks.FollowTask;
import io.github.apfelcreme.MbPetsNoLD.Tasks.ParticleTask;
import org.bukkit.ChatColor;

import java.sql.Connection;

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
public class MonitorCommand implements SubCommand {
    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(final ChatInput chatInput) {
        MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
            @Override
            public void run() {

                if (!chatInput.getSender().hasPermission("MbPets.monitor")) {
                    MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.noPermission"));
                    return;
                }
                Connection connection = MbPets.getInstance().getDatabaseConnector().getConnection();
                try {
                    MbPets.sendMessage(chatInput.getSender(), MbPetsConfig
                            .getTextNode("info.monitorDB")
                            .replace(
                                    "{0}", connection != null
                                            ? ChatColor.GREEN
                                            + "aktiv"
                                            : ChatColor.RED + "inaktiv"));
                    MbPets.sendMessage(chatInput.getSender(), MbPetsConfig
                            .getTextNode("info.monitorSpawnedPets")
                            .replace("{0}",
                                    Integer.toString(PetManager.getInstance().getPets().size())));
                    MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.monitorPreparedPets")
                            .replace(
                                    "{0}",
                                    Integer.toString(PetManager.getInstance().getConfigurations()
                                            .size())));
                    MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.monitorVersion")
                            .replace(
                                    "{0}", MbPets.getInstance().getDescription().getVersion()));
                    MbPets.sendMessage(chatInput.getSender(), MbPetsConfig
                            .getTextNode("info.monitorFollowTask")
                            .replace(
                                    "{0}", FollowTask.isActive()
                                            ? ChatColor.GREEN
                                            + "aktiv"
                                            : ChatColor.RED + "inaktiv"));
                    MbPets.sendMessage(chatInput.getSender(), MbPetsConfig
                            .getTextNode("info.monitorParticleTask")
                            .replace(
                                    "{0}", ParticleTask.isActive()
                                            ? ChatColor.GREEN
                                            + "aktiv"
                                            : ChatColor.RED + "inaktiv"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MbPets.getInstance().getDatabaseConnector().closeConnection(connection);
                }
            }
        });
    }
}
