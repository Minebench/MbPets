package io.github.apfelcreme.MbPetsNoLD.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

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
public class HikariCPConnection extends DatabaseConnector {

    HikariDataSource dataSource;

    /**
     * initializes the database connection
     */
    @Override
    public void initConnection() {
        if (MbPetsConfig.getDatabase() != null && !MbPetsConfig.getDatabase().isEmpty()) {
            HikariConfig hikariConfig = new HikariConfig();

            String dataSourceClassName = tryDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
            if (dataSourceClassName == null) {
                dataSourceClassName = tryDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            }
            if (dataSourceClassName != null) {
                MbPets.getInstance().getLogger().log(Level.INFO, "Using " + dataSourceClassName + " database source");
                hikariConfig.setDataSourceClassName(dataSourceClassName);
            }

            if (dataSourceClassName == null) {
                String driverClassName = tryDriverClassName("org.mariadb.jdbc.Driver");
                if (driverClassName == null) {
                    driverClassName = tryDriverClassName("com.mysql.cj.jdbc.Driver");
                }
                if (driverClassName == null) {
                    driverClassName = tryDriverClassName("com.mysql.jdbc.Driver");
                }

                if (driverClassName != null) {
                    MbPets.getInstance().getLogger().log(Level.INFO, "Using " + driverClassName + " database driver");
                    hikariConfig.setDriverClassName(driverClassName);
                } else {
                    throw new RuntimeException("Could not find database driver or data source class! Plugin wont work without a database!");
                }
            }

            hikariConfig.addDataSourceProperty("url", "jdbc:mysql://" + MbPetsConfig.getDbUrl() + "/" + MbPetsConfig.getDatabase() + MbPetsConfig.getDbUrlParameters());
            hikariConfig.setUsername(MbPetsConfig.getDbUser());
            hikariConfig.setPassword(MbPetsConfig.getDbPassword());
            hikariConfig.setConnectionTimeout(5000);

            dataSource = new HikariDataSource(hikariConfig);
            initTables();
        }
    }

    private String tryDriverClassName(String className) {
        try {
            Class.forName(className).newInstance();
            return className;
        } catch (Exception ignored) {}
        return null;
    }

    private String tryDataSourceClassName(String className) {
        try {
            Class.forName(className);
            return className;
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * returns a connection from a HikariCP connection pool
     *
     * @return a connection from a HikariCP connection pool
     */
    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
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
        dataSource.close();
    }

}
