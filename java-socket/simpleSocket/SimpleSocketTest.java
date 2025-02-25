package simpleSocket;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * SimpleSocketTest 클래스
 * - 단일 스레드에서 서버와 클라이언트를 순차적으로 실행하며 소켓 통신을 실습하는 테스트 클래스
 */
public class SimpleSocketTest {
    public static void main(String[] args) throws Exception {
        // 서버 및 클라이언트 객체 생성
        ServerMock server = new ServerMock(); // 서버 인스턴스 생성
        ClientMock client = new ClientMock(); // 클라이언트 인스턴스 생성

        // 1. 서버 시작 및 클라이언트 연결 대기
        ServerSocket ss = server.start(6666); // 지정된 포트(6666)에서 서버 소켓 시작
        System.out.println("[테스트] 서버가 시작되었습니다. 클라이언트의 접속을 대기합니다.");

        // 2. 클라이언트가 연결을 시도하기 전에 서버가 완전히 실행되도록 1초 대기
        Thread.sleep(1000);

        // 3. 클라이언트가 서버에 연결 시도
        Socket socketClient = client.connectToServer();
        if (socketClient == null) {
            System.out.println("[테스트] 클라이언트가 서버에 연결하지 못했습니다.");
            return; // 연결 실패 시 프로그램 종료
        }
        System.out.println("[테스트] 클라이언트가 서버에 정상적으로 연결되었습니다.");

        // 4. 서버가 클라이언트의 메시지를 수신할 수 있도록 1초 대기
        Thread.sleep(1000);

        // 5. 서버가 클라이언트의 메시지를 수신 및 처리
        server.handleClient(ss);
        System.out.println("[테스트] 서버가 클라이언트의 메시지를 정상적으로 처리했습니다.");

        // 6. 클라이언트가 서버의 응답을 수신할 수 있도록 1초 대기
        Thread.sleep(1000);

        // 7. 클라이언트가 서버의 응답을 수신하고 출력
        client.handleServer(socketClient);
        System.out.println("[테스트] 클라이언트가 서버의 응답을 정상적으로 수신했습니다.");

        // 8. 모든 통신이 완료된 후 4초 대기 (테스트 확인용)
        Thread.sleep(4000);

        // 9. 서버 종료 및 자원 정리
        server.stop();
        System.out.println("[테스트] 서버가 정상적으로 종료되었습니다.");
    }
}
