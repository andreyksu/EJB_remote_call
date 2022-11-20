## Try the remote call EJB

### The folder ```remote_invocation_ejb``` contains simple EJB invocation. Without an authorization.
#### Order:
1. Build and install **server** side:
    ```sh
    mvn clean install
    ```
2. Build client side as executable .jar:
    ```sh
    mvn clean compile assembly:single
    # mvn clean package assembly:single
    ```
3. Deploy the server side to WildFly:
    - Just copy jar to the folder ```${wildfly-home}/standalone/deployments/```
4. For receive calls from other hosts, you need to change the file ```standalone-full.xml```. Change the ```127.0.0.1``` to ```0.0.0.0```
    ```xml
    <interfaces>
        <interface name="management">
            <inet-address value="${jboss.bind.address.management:0.0.0.0}"/>
        </interface>
        <interface name="public">
            <inet-address value="${jboss.bind.address:0.0.0.0}"/>
        </interface>
        <interface name="unsecure">
            <inet-address value="${jboss.bind.address.unsecure:0.0.0.0}"/>
        </interface>
    </interfaces>
    ```
5. Run WildFly server:
    ```sh
    ${wildfly-home}/bin/standalone.sh --server-config=standalone-full.xml
    ```
6. Run client side as .jar file:
    ```sh
    java -jar try-remote-call-ejb-cient-jar-with-dependencies.jar
    ```

### The folder ```remote_invocation_ejb_auth``` contains simple EJB invocation over HTTP. With the authorization.
#### Order:
1. Build and install **parent** project:
    ```sh
    mvn clean install
    ```
2. Build **client** side as executable .jar file:
    ```sh
    mvn clean compile assembly:single
    # mvn clean package assembly:single
    ```
    - Or only compile client side:
        ```sh
        mvn clean compile
        ```
3. Add user to WildFly:
    ```sh
    ${wildfly-home}/bin/add-user.sh
    #The name and password should match "testov"
    ```
4. Deploy the server side to WildFly:
    - Just copy jar to the folder ```${wildfly-home}/standalone/deployments/```
5. Run client side as jar
    ```sh
    java -jar try-remote-call-ejb-cient-jar-with-dependencies.jar
    ```
    - Or run via maven:
        ```sh
        maven exec:exec
        ```

#### Problems:
1. If you are calling from **localhost** then everything is going right (call complited saccessfull)
2. Cannot call EJB over HTTP:
    - If call via ```java -jar <...>.jar``` then throws exception 
        ```
        Exception in thread "main" javax.naming.InvalidNameException: WFNAM00007: Invalid URL scheme name "http"
        ```
        - It doesn't matter where you are calling from (from a local host or from another host)

    - If call from other host via ```mvn clean compile``` -> ```maven exec:exec``` then throws exception
        ```
        Exception in thread "main" jakarta.ejb.EJBException: java.nio.channels.ClosedChannelException
                at org.jboss.ejb.client.EJBInvocationHandler.invoke(EJBInvocationHandler.java:212)
                at org.jboss.ejb.client.EJBInvocationHandler.invoke(EJBInvocationHandler.java:116)
                at jdk.proxy2/jdk.proxy2.$Proxy3.plus(Unknown Source)
                at ru.annikonenkov.ejb.FirstRemoteClient.invokeStatelessBean(FirstRemoteClient.java:21)
                at ru.annikonenkov.ejb.FirstRemoteClient.main(FirstRemoteClient.java:12)
        Caused by: java.nio.channels.ClosedChannelException
                at io.undertow.client.http2.Http2ClientConnection.close(Http2ClientConnection.java:306)
                at io.undertow.client.http2.Http2ClientConnection$Http2ReceiveListener.handleEvent(Http2ClientConnection.java:482)
                at io.undertow.client.http2.Http2ClientConnection$Http2ReceiveListener.handleEvent(Http2ClientConnection.java:387)
                at org.xnio.ChannelListeners.invokeChannelListener(ChannelListeners.java:92)
                at io.undertow.server.protocol.framed.AbstractFramedChannel$FrameReadListener.handleEvent(AbstractFramedChannel.java:959)
                at io.undertow.server.protocol.framed.AbstractFramedChannel$FrameReadListener.handleEvent(AbstractFramedChannel.java:939)
                at org.xnio.ChannelListeners.invokeChannelListener(ChannelListeners.java:92)
                at org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady(ReadReadyHandler.java:66)
                at org.xnio.nio.NioSocketConduit.handleReady(NioSocketConduit.java:89)
                at org.xnio.nio.WorkerThread.run(WorkerThread.java:591)
                17:20[ERROR] Command execution failed.
        ```

##### TODO:
- Разобораться с elytron - для успешного вызова с удаленной машины.
    - Т.к. открытие интерфейсов для доступа из вне не помогает.
    - Не помоглает и пробрасование партов через ssh (putty)
- Собрать с отдельным каталогом /lib - где будут все зависимости.
    