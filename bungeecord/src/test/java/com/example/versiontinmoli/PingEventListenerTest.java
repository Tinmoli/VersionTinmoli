package com.example.versiontinmoli;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PingEventListener.
 */
class PingEventListenerTest {
    private static final Logger logger = Logger.getLogger(PingEventListenerTest.class.getName());
    private ConfigLoader configLoader;
    private PingEventListener listener;

    @BeforeEach
    void setUp() {
        configLoader = mock(ConfigLoader.class);
        listener = new PingEventListener(configLoader, logger);
    }

    @Test
    void testOnProxyPingModifiesVersionName() {
        // Arrange
        when(configLoader.getVersionName()).thenReturn("Custom Server 1.0");
        
        ServerPing.Protocol originalVersion = new ServerPing.Protocol("BungeeCord 1.20", 763);
        ServerPing response = mock(ServerPing.class);
        when(response.getVersion()).thenReturn(originalVersion);
        
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getResponse()).thenReturn(response);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert
        verify(response).setVersion(argThat(version -> 
            version.getName().equals("Custom Server 1.0") &&
            version.getProtocol() == 763
        ));
    }

    @Test
    void testOnProxyPingHandlesNullServerPing() {
        // Arrange
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getResponse()).thenReturn(null);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert - should not throw exception
        verify(event, times(1)).getResponse();
    }

    @Test
    void testOnProxyPingHandlesNullVersion() {
        // Arrange
        when(configLoader.getVersionName()).thenReturn("Custom Version");
        
        ServerPing response = mock(ServerPing.class);
        when(response.getVersion()).thenReturn(null);
        
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getResponse()).thenReturn(response);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert - should handle null version gracefully with protocol -1
        verify(response).setVersion(argThat(version -> 
            version.getName().equals("Custom Version") &&
            version.getProtocol() == -1
        ));
    }

    @Test
    void testOnProxyPingWithCustomVersionName() {
        // Arrange
        when(configLoader.getVersionName()).thenReturn("Premium Network");
        
        ServerPing.Protocol originalVersion = new ServerPing.Protocol("BungeeCord", 763);
        ServerPing response = mock(ServerPing.class);
        when(response.getVersion()).thenReturn(originalVersion);
        
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getResponse()).thenReturn(response);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert
        verify(response).setVersion(argThat(version -> 
            version.getName().equals("Premium Network")
        ));
    }
}
