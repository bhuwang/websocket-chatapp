package com.bhuwan.websocket.chatapp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
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

    ByteBuffer buffer = ByteBuffer.allocate(204888);

    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, boolean complete) {
        System.out.println("Inside onMessage binary:");
        buffer.put(byteBuffer);
        if (complete) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("/tmp/bhuwan.jpg");
                fos.write(buffer.array());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            for (ChatEndPoint client : clients) {
                buffer.rewind();
                client.session.getAsyncRemote().sendBinary(buffer, new SendHandler() {
                    @Override
                    public void onResult(SendResult result) {
                        System.out.println(result.isOK());
                    }
                });
            }
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
