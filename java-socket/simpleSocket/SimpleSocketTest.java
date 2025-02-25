package simpleSocket;

import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSocketTest {
    public static void main(String[] args) throws Exception {
        ServerMock server = new ServerMock();
        ClientMock client = new ClientMock();
        
        ServerSocket ss = server.start(6666);
        
        Thread.sleep(1000);
        Socket socketClient = client.connectToServer();
        
        Thread.sleep(1000);
        server.handleClient(ss);
        
        Thread.sleep(1000);
        client.handleServer(socketClient);

        Thread.sleep(4000);
        server.stop();
    }

}
