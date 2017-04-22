package io.github.apfelcreme.MbPetsNoLD.Database;

import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection extends DatabaseConnector {

    /**
     * initializes the database connection
     */
    @Override
    public void initConnection() {
        if (MbPetsConfig.getDatabase() != null && !MbPetsConfig.getDatabase().isEmpty()) {
            initTables();
        }
    }

    /**
     * returns a mysql database connection to work with
     *
     * @return a connection object
     */
    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://"
                            + MbPetsConfig.getDbUrl() + "/"
                            + MbPetsConfig.getDatabase(),
                    MbPetsConfig.getDbUser(),
                    MbPetsConfig.getDbPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * closes all db connections
     */
    @Override
    public void close() {
        //this should never be called anyway, as every connection that is opened is closed at a later point
    }


}
