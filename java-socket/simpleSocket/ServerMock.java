package simpleSocket;
import java.io.*;
import java.net.*;

public class ServerMock {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ServerSocket start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("서버가 시작되었습니다. 클라이언트 접근 대기중...");
        return serverSocket;
    }

    public void handleClient(ServerSocket ss) throws IOException {
        clientSocket = ss.accept();
        System.out.println("클라이언트 접속 완료");

        clientSocket.setSoTimeout(3000); // 30초 타임아웃 설정

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        try {
            String message = in.readLine();
            System.out.println("클라이언트로부터 받은 메시지: " + message);

            // 클라이언트로 응답
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("클라이언트에게 전송할 메시지를 작성하세요.");
            String response = reader.readLine();

            out.println(response);

            System.out.println("클라이언트에게 응답 완료");
        } catch (SocketTimeoutException e) {
            System.out.println("클라이언트 응답 시간 초과");
        }
    }

    public void receiveToClient (ServerSocket ss) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        try {
            String message = in.readLine();
            System.out.println("클라이언트로부터 받은 메시지: " + message);

            // 클라이언트로 응답
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("클라이언트에게 전송할 메시지를 작성하세요.");
            String response = reader.readLine();

            out.println(response);

            System.out.println("클라이언트에게 응답 완료");
        } catch (SocketTimeoutException e) {
            System.out.println("클라이언트 응답 시간 초과");
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
        System.out.println("서버 종료됨.");
    }

    public static void main(String[] args) throws IOException {
        // ServerMock server = new ServerMock();
        // server.start(6666);
        // server.handleClient(server.serverSocket);
        // server.receiveToClient(server.serverSocket);
        // server.stop(); // 서버 종료 처리 추가
    }
}
