package com.playares.commons;

public interface AresService {
    /**
     * Returns the name of this service
     * @return Name of service
     */
    String getName();

    /**
     * Returns the owner of this service
     * @return AresPlugin
     */
    AresPlugin getOwner();

    /**
     * Handles reloading this service
     */
    default void reload() {}

    /**
     * Handles starting this service
     */
    void start();

    /**
     * Handles stopping this service
     */
    void stop();
}
