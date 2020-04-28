package com.playares.commons.connect;

public interface Connectable {
    /**
     * Open a new connection
     */
    void openConnection();

    /**
     * Close this connection instance
     */
    void closeConnection();

    /**
     * Returns connection status
     * @return Connection status
     */
    boolean isConnected();

    /**
     * Returns the connection type
     * @return Connection type
     */
    ConnectionType getConnectionType();

    enum ConnectionType {
        MONGO, REDIS
    }
}