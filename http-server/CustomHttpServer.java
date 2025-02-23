import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CustomHttpServer {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;


    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Custom HTTP Server started on port " + PORT);
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            Thread thread = new Thread(() -> handleRequest(clientSocket));
            thread.start();
        }
    }
    
    private void handleRequest(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream output = socket.getOutputStream()) {
            
            // 1️⃣ 요청 읽기
            String requestLine = reader.readLine();
            System.out.println("Received: " + requestLine);
            
            // 2️⃣ 간단한 응답 보내기
            String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nHello World";
            output.write(response.getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new CustomHttpServer().start();
    }
}