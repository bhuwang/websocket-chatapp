package com.bhuwan.websocket.chatapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author bhuwan
 */
@ServerEndpoint("/chat")
public class ChatEndPoint {

    private Session session;
    static List<ChatEndPoint> clients = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig _) {
        this.session = session;
        clients.add(this);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("socket closed: " + reason.getReasonPhrase());
        clients.remove(this);
    }

    @OnMessage
    public void onMessage(String message) {
        broadcast(message);
    }

    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, boolean complete) {
        System.out.println("Inside onMessage binary:");
        try {
            
            buffer.write(byteBuffer.array());
            if (complete) {
                for (ChatEndPoint client : clients) {
                    try {
                        client.session.getBasicRemote().sendText("{\"call\":\"complete\"}");
                    } catch (IOException ex) {
                        clients.remove(this);
                        try {
                            client.session.close();
                        } catch (IOException ex1) {
                            // log here.
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void broadcast(String message) {
        for (ChatEndPoint client : clients) {
            try {
                client.session.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                clients.remove(this);
                try {
                    client.session.close();
                } catch (IOException ex1) {
                    // log here.
                }
            }
        }
    }

}
