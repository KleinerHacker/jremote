package org.pcsoft.framework.jremote.api.config;

/**
 * JREmote Configuration for the server
 */
public final class JRemoteServerConfiguration extends JRemoteConfiguration {
    private int maxClientCount = -1;

    /**
     * Maximum count of {@link org.pcsoft.framework.jremote.api.RemoteClient} registered on {@link org.pcsoft.framework.jremote.api.RemoteServer} or value less
     * than 0 to allow unlimited count
     *
     * @return
     */
    public int getMaxClientCount() {
        return maxClientCount;
    }

    public void setMaxClientCount(int maxClientCount) {
        this.maxClientCount = maxClientCount;
    }
}
