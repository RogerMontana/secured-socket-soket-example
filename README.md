# socket-stomp-example
 An example application made to use a socket and send messages to the frontend through a channel with authorization that supports the session for users
 
#### Technology stack

##### Front-end
- Using SockJS over socket connection on UI
- STOMP as messaging protocol

##### Back-end
- Using Spring Messaging
- Using SockJS over socket connection
- STOMP protocol as standard for transferring data

##### LINKS
- SockJS: 
https://github.com/sockjs
- Using Spring Messaging: 
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/simp/package-summary.html


#### SOCKET CONNECTION DIAGRAM

- WebSocketMessageBrokers - configuration for messaging and endpoint
- WebSocketConfig - registration for socket (message broker and stomp endpoint )
- ChatController - client endpoints 

#### SECURITY
Common Connection flow 

     ┌──────┐                                                                   ┌───────┐
     │UI_APP│                                                                   │SERVICE│
     └──┬───┘                                                                   └───┬───┘
        │                         do http handshake (/info)                         │    
        │ ──────────────────────────────────────────────────────────────────────────>    
        │                                                                           │    
        │                 provide info about WebSocketMessageBrokers                │    
        │ <──────────────────────────────────────────────────────────────────────────    
        │                                                                           │    
        │  esteblish connection on endpoint "/greeting-websocket" (WebSocketConfig) │    
        │ ──────────────────────────────────────────────────────────────────────────>    
        │                                                                           │    
        │        subscribe on "/user/queue/topic/greetings" (WebSocketConfig)       │    
        │ <──────────────────────────────────────────────────────────────────────────    
        │                                                                           │    
        │               send message to "/app/hello" (ChatController)               │    
        │ ──────────────────────────────────────────────────────────────────────────>    
        │                                                                           │    
        │ send message to session to "/user/queue/topic/greetings" (WebSocketConfig)│    
        │ <──────────────────────────────────────────────────────────────────────────    
     ┌──┴───┐                                                                   ┌───┴───┐
     │UI_APP│                                                                   │SERVICE│
     └──────┘                                                                   └───────┘
     
security components diagram (intreception security flow from http to socket channel):

                          ┌─────────────┐          ┌───────────────────────────────┐               ┌─────────────────────────────┐          ┌─────────────────────────────┐
                          │     UI      │          │HttpSessionHandshakeInterceptor│               │AuthChannelInterceptorAdapter│          │WebSocketAuthenticatorService│
                          └──────┬──────┘          └───────────────┬───────────────┘               └──────────────┬──────────────┘          └──────────────┬──────────────┘
                                 │                                 │                                              │                                        │               
                                 |                                 │                                              │                                        │               
                                 │                                 │                                              │                                        │               
                                 │    sending request over http    │                                              │                                        │               
                                 │ ────────────────────────────────>                                              │                                        │               
                                 │                                 │                                              │                                        │               
                                 │                                 │────┐                                                                                  │               
                                 │                                 │    │ get auth header and put in socket channel                                        │               
                                 │                                 │<───┘                                                                                  │               
                                 │                                 │                                              │                                        │               
                                 │                                 │     intrecept and getSessionAttributes()     │                                        │               
                                 │                                 │ ─────────────────────────────────────────────>                                        │               
                                 │                                 │                                              │                                        │               
                                 │                                 │                                              │  validate token allow esteblish socket │               
                                 │                                 │                                              │ ───────────────────────────────────────>               
                          ┌──────┴──────┐          ┌───────────────┴───────────────┐               ┌──────────────┴──────────────┐          ┌──────────────┴──────────────┐
                          │     UI      │          │HttpSessionHandshakeInterceptor│               │AuthChannelInterceptorAdapter│          │WebSocketAuthenticatorService│
                          └─────────────┘          └───────────────────────────────┘               └─────────────────────────────┘          └─────────────────────────────┘

