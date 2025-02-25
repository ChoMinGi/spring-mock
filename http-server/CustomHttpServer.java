import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * CustomHttpServer 클래스는 간단한 HTTP 서버를 구현합니다.
 * 서버는 지정된 포트에서 클라이언트 요청을 수신하고, 간단한 "Hello World" 응답을 반환합니다.
 * 
 * <p>주요 기능:
 * <ul>
 *   <li>서버 시작 및 클라이언트 연결 수락</li>
 *   <li>클라이언트 요청 처리 및 응답 전송</li>
 * </ul>
 * 
 * <p>사용 예:
 * <pre>
 * {@code
 * public static void main(String[] args) throws IOException {
 *     new CustomHttpServer().start();
 * }
 * }
 * </pre>
 * 
 * @author 
 * @version 1.0
 */

public class CustomHttpServer {
    private static final int PORT = 8080;

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
            
            // 요청 읽기
            String requestLine = reader.readLine();
            System.out.println("Received: " + requestLine);
            
            // 간단한 응답 보내기
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