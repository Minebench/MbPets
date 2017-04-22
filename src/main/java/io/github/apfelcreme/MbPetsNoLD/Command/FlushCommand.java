package io.github.apfelcreme.MbPetsNoLD.Command;

import io.github.apfelcreme.MbPetsNoLD.ChatInput;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
public class FlushCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(final ChatInput chatInput) {
        MbPets.getInstance().getServer().getScheduler()
                .runTaskAsynchronously(MbPets.getInstance(), new Runnable() {
                    public void run() {
                        Connection connection = MbPets.getInstance().getDatabaseConnector().getConnection();
                        if (!chatInput.getSender().hasPermission("MbPets.delete")) {
                            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.noPermission"));
                            return;
                        }
                        if (chatInput.getTargetPlayer() == null) {
                            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.missingValue")
                                    .replace("{0}", "Player"));
                            return;
                        }
                        PreparedStatement statement;
                        try {
                            statement = connection
                                    .prepareStatement("DELETE from MbPets_Pet WHERE playerid = (Select playerid from MbPets_Player where uuid = ?)");
                            statement.setString(1, chatInput.getTargetPlayer()
                                    .getUniqueId().toString());
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            MbPets.getInstance().getDatabaseConnector().closeConnection(connection);
                        }
                        MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("info.petsFlushed"));
                    }

                });
    }
}
