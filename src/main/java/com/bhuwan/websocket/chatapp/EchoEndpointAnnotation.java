/**
 *
 */
package com.bhuwan.websocket.chatapp;

import java.io.IOException;

import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * if you are using the annotation based websocket then you have to remove the
 * EchoEndpointConfig.java file.
 *
 * @author bhuwan
 *
 */
@ServerEndpoint("/echoa")
public class EchoEndpointAnnotation {

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        Basic remoteEndpointBasic = session.getBasicRemote();
        session.addMessageHandler(new EchoMessageHandler(remoteEndpointBasic));
    }

    private static class EchoMessageHandler implements MessageHandler.Whole<String> {

        private final Basic remoteEndpointBasic;

        public EchoMessageHandler(RemoteEndpoint.Basic remoteEndpointBasic) {
            this.remoteEndpointBasic = remoteEndpointBasic;
        }

        @Override
        public void onMessage(String message) {
            try {
                if (remoteEndpointBasic != null) {
                    remoteEndpointBasic.sendText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
