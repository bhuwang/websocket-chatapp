/**
 *
 */
package com.bhuwan.websocket.chatapp;

/**
 * Note: Configuration only required for programmatic based websocket. You can remove
 * this class in case of annotation based.
 *
 * @author bhuwan
 *
 */
public class EchoEndpointConfig {
        //implements ServerApplicationConfig {

    /*@Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
        return null;
    }

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
        Set<ServerEndpointConfig> result = new HashSet<>();

        if (set.contains(EchoEndPoint.class)) {
            result.add(ServerEndpointConfig.Builder.create(EchoEndPoint.class, "/echo").build());
        }
        System.out.println("result: " + result);
        return result;
    }*/

}
