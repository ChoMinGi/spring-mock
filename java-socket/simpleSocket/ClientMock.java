package simpleSocket;
import java.io.*;
import java.net.*;

public class ClientMock {
    private Socket socket;         // 서버와 통신할 소켓
    private PrintWriter out;       // 서버로 데이터를 전송하는 출력 스트림
    private BufferedReader in;     // 서버로부터 데이터를 수신하는 입력 스트림

    /**
     * 서버에 연결을 시도하고 메시지를 전송하는 메서드
     * @return 서버와 연결된 소켓 객체 (닫히지 않은 상태)
     */
    public Socket connectToServer() {
        try {
            // 서버의 IP 주소 및 포트 번호를 지정하여 소켓을 생성하고 연결 시도
            socket = new Socket("localhost", 6666);
            System.out.println("서버에 접속되었습니다.");

            // 출력 및 입력 스트림 초기화
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 사용자 입력을 위한 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // 사용자에게 메시지 입력 요청
            System.out.println("서버에 전송할 메시지를 작성하세요.");
            String message = reader.readLine(); // 사용자 입력 대기
            out.println(message); // 서버로 메시지 전송

            return socket; // 닫지 않은 소켓을 반환하여 이후 서버 응답을 받을 수 있도록 함

        } catch (IOException e) {
            e.printStackTrace();
            return null; // 예외 발생 시 null 반환
        }
    }

    /**
     * 클라이언트 종료 메서드 (리소스 정리)
     * - 입력 및 출력 스트림, 소켓을 닫음
     */
    public void close() {
        try {
            if (in != null) in.close();   // 입력 스트림 닫기
            if (out != null) out.close(); // 출력 스트림 닫기
            if (socket != null) socket.close(); // 소켓 닫기
            System.out.println("클라이언트 종료됨.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 서버로부터 메시지를 수신하는 메서드
     * @param socket 서버와 연결된 소켓 객체
     */
    public void handleServer(Socket socket) {
        try (
            // 서버와 통신하기 위한 스트림 생성 (닫히지 않은 소켓 사용)
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // 서버로부터 응답 메시지 수신
            String response = in.readLine();
            System.out.println("서버로부터 받은 메시지: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 클라이언트 실행 메인 메서드
     * - 서버에 연결 후 메시지를 보내고, 서버 응답을 받은 후 종료
     */
    public static void main(String[] args) {
        ClientMock client = new ClientMock(); // 클라이언트 객체 생성
        Socket socket = client.connectToServer(); // 서버 연결 및 메시지 전송

        if (socket != null) {
            client.handleServer(socket); // 서버 응답 처리
        }

        client.close(); // 클라이언트 종료
    }
}
