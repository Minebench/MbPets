package io.github.apfelcreme.MbPetsNoLD.Database;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;

import java.sql.Connection;
import java.sql.SQLException;

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
public abstract class DatabaseConnector {

    /**
     * initializes the database connection
     */
    public abstract void initConnection();

    /**
     * returns the database connection to work with
     *
     * @return a connection object
     */
    public abstract Connection getConnection();

    /**
     * closes a connection
     *
     * @param connection a connection
     */
    public void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            } else {
                MbPets.getInstance().getLogger().warning("connection = null, mbpets, cannot close");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the database and tables
     */
    protected void initTables() {
        Connection connection = getConnection();
        try {
            connection.createStatement().execute(
                    "CREATE DATABASE IF NOT EXISTS " + MbPetsConfig.getDatabase());
            connection.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS MbPets_Player("
                            + "playerid BIGINT auto_increment not null,"
                            + "playername VARCHAR(50) NOT NULL,"
                            + "uuid VARCHAR(50) UNIQUE NOT NULL,"
                            + "PRIMARY KEY (playerid));");
            connection.createStatement()
                    .execute(
                            "CREATE TABLE IF NOT EXISTS MbPets_Pet("
                                    + "petId BIGINT auto_increment not null,"
                                    + "playerid BIGINT,"
                                    + "petname VARCHAR(50),"
                                    + "type VARCHAR(50),"
                                    + "sheepcolor VARCHAR(50),"
                                    + "wolfcolor VARCHAR(50),"
                                    + "horsecolor VARCHAR(50),"
                                    + "horsestyle VARCHAR(50),"
                                    + "cattype VARCHAR(50),"
                                    + "rabbittype VARCHAR(50),"
                                    + "llamacolor VARCHAR(50),"
                                    + "parrotcolor VARCHAR(50),"
                                    + "foxtype VARCHAR(50),"
                                    + "baby boolean,"
                                    + "slimesize TINYINT,"
                                    + "number BIGINT,"
                                    + "exp BIGINT,"
                                    + "FOREIGN KEY (playerid) REFERENCES MbPets_Player(playerid),"
                                    + "PRIMARY KEY (petId));");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * closes all db connections
     */
    public abstract void close();
}
