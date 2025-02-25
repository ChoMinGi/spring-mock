package simpleSocket;
import java.io.*;
import java.net.*;

public class ServerMock {
    private ServerSocket serverSocket; // 클라이언트의 연결을 대기하는 서버 소켓
    private Socket clientSocket;       // 연결된 클라이언트 소켓
    private PrintWriter out;           // 클라이언트에게 데이터를 전송하는 출력 스트림
    private BufferedReader in;         // 클라이언트로부터 데이터를 수신하는 입력 스트림

    /**
     * 서버를 시작하여 클라이언트의 접속을 대기하는 메서드
     * @param port 사용할 포트 번호
     * @return 생성된 ServerSocket 객체
     * @throws IOException 소켓 생성 실패 시 예외 발생
     */
    public ServerSocket start(int port) throws IOException {
        serverSocket = new ServerSocket(port); // 지정된 포트에서 서버 소켓 생성
        System.out.println("서버가 시작되었습니다. 클라이언트 접근 대기중...");
        return serverSocket; // 서버 소켓 반환
    }

    /**
     * 클라이언트의 요청을 처리하는 메서드
     * @param ss 서버 소켓 객체
     * @throws IOException 입출력 오류 발생 시 예외 처리
     */
    public void handleClient(ServerSocket ss) throws IOException {
        // 클라이언트의 연결을 대기하고 연결이 들어오면 accept()
        clientSocket = ss.accept();
        System.out.println("클라이언트 접속 완료");

        // 클라이언트 소켓의 읽기 시간 제한 (3초)
        clientSocket.setSoTimeout(3000);

        // 클라이언트와 데이터 송수신을 위한 스트림 생성
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        try {
            // 클라이언트로부터 메시지를 수신
            String message = in.readLine();
            System.out.println("클라이언트로부터 받은 메시지: " + message);

            // 서버가 클라이언트에게 보낼 메시지를 입력 받음
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("클라이언트에게 전송할 메시지를 작성하세요.");
            String response = reader.readLine(); // 서버 사용자의 입력 대기

            // 클라이언트에게 응답 전송
            out.println(response);
            System.out.println("클라이언트에게 응답 완료");

        } catch (SocketTimeoutException e) {
            // 클라이언트의 응답이 없을 경우 타임아웃 예외 처리
            System.out.println("클라이언트 응답 시간 초과");
        }
    }

    /**
     * 서버 종료 메서드
     * - 모든 스트림 및 소켓을 닫아 리소스를 정리함
     * @throws IOException 스트림 및 소켓 닫기 실패 시 예외 발생
     */
    public void stop() throws IOException {
        // 모든 리소스가 null이 아닌 경우 닫아줌
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null) clientSocket.close();
        if (serverSocket != null) serverSocket.close();
        System.out.println("서버 종료됨.");
    }

    /**
     * 서버 실행을 위한 메인 메서드
     * - 서버를 시작하고, 클라이언트를 처리한 뒤 종료
     * @param args 실행 인자
     * @throws IOException 입출력 예외 처리
     */
    public static void main(String[] args) throws IOException {
        ServerMock server = new ServerMock();  // 서버 객체 생성
        server.start(6666);                   // 서버 시작 및 클라이언트 대기
        server.handleClient(server.serverSocket); // 클라이언트 요청 처리
        server.stop();                         // 서버 종료
    }
}
