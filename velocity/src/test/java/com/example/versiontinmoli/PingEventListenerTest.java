package com.example.versiontinmoli;

import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

/**
 * Unit tests for PingEventListener.
 */
class PingEventListenerTest {
    private static final Logger logger = LoggerFactory.getLogger(PingEventListenerTest.class);
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
        
        ServerPing.Version originalVersion = mock(ServerPing.Version.class);
        when(originalVersion.getProtocol()).thenReturn(763);
        
        ServerPing originalPing = mock(ServerPing.class);
        when(originalPing.getVersion()).thenReturn(originalVersion);
        
        ServerPing.Builder builder = mock(ServerPing.Builder.class);
        when(originalPing.asBuilder()).thenReturn(builder);
        when(builder.version(any())).thenReturn(builder);
        
        ServerPing modifiedPing = mock(ServerPing.class);
        when(builder.build()).thenReturn(modifiedPing);
        
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getPing()).thenReturn(originalPing);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert
        verify(builder).version(argThat(version -> 
            version.getName().equals("Custom Server 1.0") &&
            version.getProtocol() == 763
        ));
        verify(event).setPing(modifiedPing);
    }

    @Test
    void testOnProxyPingHandlesNullServerPing() {
        // Arrange
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getPing()).thenReturn(null);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert - should not call setPing when ServerPing is null
        verify(event, never()).setPing(any());
    }

    @Test
    void testOnProxyPingHandlesNullVersion() {
        // Arrange
        when(configLoader.getVersionName()).thenReturn("Custom Version");
        
        ServerPing originalPing = mock(ServerPing.class);
        when(originalPing.getVersion()).thenReturn(null);
        
        ServerPing.Builder builder = mock(ServerPing.Builder.class);
        when(originalPing.asBuilder()).thenReturn(builder);
        when(builder.version(any())).thenReturn(builder);
        
        ServerPing modifiedPing = mock(ServerPing.class);
        when(builder.build()).thenReturn(modifiedPing);
        
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getPing()).thenReturn(originalPing);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert - should handle null version gracefully with protocol -1
        verify(builder).version(argThat(version -> 
            version.getName().equals("Custom Version") &&
            version.getProtocol() == -1
        ));
        verify(event).setPing(modifiedPing);
    }

    @Test
    void testOnProxyPingWithMiniMessageFormat() {
        // Arrange
        when(configLoader.getVersionName()).thenReturn("<red>Custom Server</red>");
        
        ServerPing.Version originalVersion = mock(ServerPing.Version.class);
        when(originalVersion.getProtocol()).thenReturn(763);
        
        ServerPing originalPing = mock(ServerPing.class);
        when(originalPing.getVersion()).thenReturn(originalVersion);
        
        ServerPing.Builder builder = mock(ServerPing.Builder.class);
        when(originalPing.asBuilder()).thenReturn(builder);
        when(builder.version(any())).thenReturn(builder);
        
        ServerPing modifiedPing = mock(ServerPing.class);
        when(builder.build()).thenReturn(modifiedPing);
        
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getPing()).thenReturn(originalPing);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert - MiniMessage format should be converted to legacy format (§c for red)
        verify(builder).version(argThat(version -> 
            version.getName().contains("§c") && version.getName().contains("Custom Server")
        ));
    }

    @Test
    void testOnProxyPingPreservesOriginalPingFields() {
        // Arrange
        when(configLoader.getVersionName()).thenReturn("Test Version");
        
        ServerPing.Version originalVersion = mock(ServerPing.Version.class);
        when(originalVersion.getProtocol()).thenReturn(763);
        
        ServerPing originalPing = mock(ServerPing.class);
        when(originalPing.getVersion()).thenReturn(originalVersion);
        
        ServerPing.Builder builder = mock(ServerPing.Builder.class);
        when(originalPing.asBuilder()).thenReturn(builder);
        when(builder.version(any())).thenReturn(builder);
        
        ServerPing modifiedPing = mock(ServerPing.class);
        when(builder.build()).thenReturn(modifiedPing);
        
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getPing()).thenReturn(originalPing);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert - asBuilder() should be called to preserve all fields
        verify(originalPing).asBuilder();
        verify(builder).version(any());
        verify(builder).build();
        verify(event).setPing(modifiedPing);
    }

    @Test
    void testOnProxyPingHandlesBuilderException() {
        // Arrange
        when(configLoader.getVersionName()).thenReturn("Test");
        
        ServerPing originalPing = mock(ServerPing.class);
        when(originalPing.getVersion()).thenThrow(new RuntimeException("Builder error"));
        
        ProxyPingEvent event = mock(ProxyPingEvent.class);
        when(event.getPing()).thenReturn(originalPing);
        
        // Act
        listener.onProxyPing(event);
        
        // Assert - should not call setPing when exception occurs
        verify(event, never()).setPing(any());
    }
}
