package com.llewkcor.ares.commons.connect.mongodb;

import com.llewkcor.ares.commons.connect.Connectable;
import com.llewkcor.ares.commons.logger.Logger;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

public final class MongoDB implements Connectable {
    private final String uri;
    @Getter public MongoClient client;
    @Getter public boolean connected;

    public MongoDB(String uri) {
        this.uri = uri;
        this.client = null;
    }

    @Override
    public void openConnection() {
        Logger.print("Establishing connection to MongoDB instance");
        this.client = MongoClients.create(uri);
        this.connected = true;
    }

    @Override
    public void closeConnection() {
        if (client == null) {
            return;
        }

        Logger.warn("Closing connection to MongoDB instance");
        client.close();

        this.client = null;
        this.connected = false;
    }

    /**
     * Returns a Mongo Database matching the given name, creates a new one if not found
     * @param name Database Name
     * @return MongoDatabase instance matching provided name
     */
    public MongoDatabase getDatabase(String name) {
        if (client == null || !isConnected()) {
            return null;
        }

        return client.getDatabase(name);
    }

    /**
     * Returns a Mongo Collection matching the given name in the given db, creates a new one if not found
     * @param database Database Name
     * @param collection Collection Name
     * @return MongoCollection instance matching provided name in the given db
     */
    public MongoCollection<Document> getCollection(String database, String collection) {
        final MongoDatabase db = getDatabase(database);

        if (client == null || !isConnected() || db == null) {
            return null;
        }

        return db.getCollection(collection);
    }
}