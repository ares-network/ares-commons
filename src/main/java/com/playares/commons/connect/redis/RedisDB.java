package com.playares.commons.connect.redis;

import com.playares.commons.connect.Connectable;
import com.playares.commons.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;

public final class RedisDB implements Connectable {
    private final String uri;
    @Getter public Jedis client;
    @Getter @Setter public boolean connected;
    @Getter public final ConnectionType connectionType = ConnectionType.REDIS;

    public RedisDB(String uri) {
        this.uri = uri;
        this.client = null;
    }

    @Override
    public void openConnection() {
        Logger.print("Establishing connection to Redis instance");
        this.client = new Jedis(uri);
        this.connected = true;
    }

    @Override
    public void closeConnection() {
        if (client == null) {
            return;
        }

        Logger.warn("Saving Redis instance before closing...");
        final String response = client.save();
        Logger.print("Finished saving Redis instance. Response: " + response);

        Logger.warn("Closing connection to Redis instance");
        client.close();

        this.client = null;
        this.connected = false;
    }
}