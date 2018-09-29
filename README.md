# event module
Nuls is a global blockchain open-source project which is a highly customizable modular blockchain infrastructure.

Event module is another module that can be added to Nuls Blockchain.

#### Module Features
  - Event module publishes events (ex: new block, token transfer etc) which occur on Nuls Blockchain
  - This module can be plugged into existing Nuls BlockChain
  - Module can be bootstrapped like other modules
  - Any subscriber can subscribe to these events and can be used according to their requirement
  
#### Technical Details
  
  - Java 8
  - Spring boot and web sockets
  - Spring Boot supports web sockets  implementation using STOMP protocol and sock JS.
  - Spring scheduler to schedule the tasks required to get events from Nuls Blockcain
  - Events are published as JSON string, client can consume JSON with their own implementation
  - Sample Client code is available in [repository - Sample Client app](https://github.com/naveen7252/event-module-sample-client)

##### Event Subscriptions
     Supported events as of now..  
      1. To subscribe to new block:  /block/latest
      2. To subscribe to token receive event :  /tx/receiveToken/<your Nuls address>
      3. To subscribe to consensus reward:      /tx/receiveReward/<your Nuls address>
      4. To subscribe to YELLOW cards for your node:  /agent/yellowCard/<agent_address>
      5. To subscribe to RED cards for your node: /agent/redCard/<agent_address>
      
#### Running the module
   
  - Module can be integrated to existing Nuls modules
  - When Nuls node or wallet is started, automatically event module will be started and subscription end points are open for clients
   - ###### Run module independently
    
    ```
    git clone https://github.com/naveen7252/event-module.git
    cd event-module
    mvn clean install
    cd base/event-base
    mvn spring-boot:run
    ```
    
 #### Client - How to subscribe to events
  
  - Need following JS files
     
     - sock js client  
            <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
            for more information , see ["SockJS-Client"](https://github.com/sockjs/sockjs-client)
     - stomp js library 
             for more information, see ["Stomp-JS"](https://github.com/stomp-js/stompjs)
   - Connect to web socket at 
            
            http://<hostname>:<port>/nulsWSocket
   -  subscribe to interested events
            
            Supported events as of now..  
                  1. To subscribe to new block:  /block/latest
                  2. To subscribe to token receive event :  /tx/receiveToken/<your Nuls address>
                  3. To subscribe to consensus reward:      /tx/receiveReward/<your Nuls addtess>
                  4. To subscribe to YELLOW cards for your node:  /agent/yellowCard/<agent_address>
                  5. To subscribe to RED cards for your node: /agent/redCard/<agent_address>
                  
   - Refer to sample client application code [repository - Sample Client app](https://github.com/naveen7252/event-module-sample-client)
   
   
               
