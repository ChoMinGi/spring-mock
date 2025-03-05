package socketWithThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ServerMock {
    private ServerSocket serverSocket; // 클라이언트의 연결을 대기하는 서버 소켓
    private Socket clientSocket;       // 연결된 클라이언트 소켓
    private PrintWriter out;           // 클라이언트에게 데이터를 전송하는 출력 스트림
    private BufferedReader in;         // 클라이언트로부터 데이터를 수신하는 입력 스트림
    
    private boolean running;           // 서버 실행 여부를 나타내는 플래그

    /**
     * 서버를 시작하여 클라이언트의 접속을 대기하는 메서드
     * @param port 사용할 포트 번호
     * @throws IOException 소켓 생성 실패 시 예외 발생
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port); // 지정된 포트에서 서버 소켓 생성
        running = true;
        
        System.out.println("[Server] 서버 시작 (포트: " + port + ")");
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] 클라이언트 접속 완료");
                
                // 새 스레드에서 클라이언트 처리
                new ClientHandler(clientSocket).start();
            } catch (SocketException e) {
                // 서버 종료 시 서버소켓이 닫혀서 SocketException 발생 가능
                System.out.println("[Server] 서버소켓이 닫혔습니다.");
            }
        }
    }

    /**
     * 서버 종료 메서드
     * - 모든 스트림 및 소켓을 닫아 리소스를 정리함
     * @throws IOException 스트림 및 소켓 닫기 실패 시 예외 발생
     */
    public void stop() throws IOException {
        running = false; // 서버 실행 플래그를 false로 변경
        
        // 모든 리소스가 null이 아닌 경우 닫아줌
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        System.out.println("서버 종료됨.");
    }
    
}
