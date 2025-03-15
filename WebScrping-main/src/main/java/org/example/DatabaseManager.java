package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private final String urlBase;
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private static Connection connection;

    public DatabaseManager(String urlBase, String host, String database, String username, String password) {
        this.database = database;
        this.host = host;
        this.urlBase = urlBase;
        this.password = password;
        this.username = username;

    }


    public Connection getConnexion() {
        return connection;
    }

    public void connexion() {
        if (!isOnline()) {
            try {
                connection = DriverManager.getConnection(this.urlBase + this.host + "/" + this.database, this.username, this.password);
                System.out.println("[DataBaseManager] Connexion Reusite");
            } catch (SQLException e) {
                System.out.println("[DataBaseManager] Erreur lors de la connection a la base de donné" + e.getMessage());
            }
        }
    }

    public void deconnexion() {
        if (isOnline()) {
            try {
                connection.close();
                System.out.println("[DataBaseManager] Deconnexion Reusite");
            } catch (SQLException e) {
                System.out.println("[DataBaseManager] Erreur lors de la déconnection a la base de donné");
            }
        }
    }

    public boolean isOnline() {
        try {
            return (connection != null) && (!connection.isClosed());
        } catch (SQLException e) {
            System.out.println("[DataBaseManager] Erreur lors de la verification de l'etat de la base");
        }
        return false;
    }
}
