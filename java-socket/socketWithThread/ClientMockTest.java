package socketWithThread;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.concurrent.*;

public class ClientMockTest {
    
    private static final int TEST_PORT = 6666;
    private static ServerMock server;
    
    @BeforeAll
    static void startServer() throws IOException {
        server = new ServerMock();
        
        // 서버를 별도 스레드로 구동
        Thread serverThread = new Thread(() -> {
            try {
                server.start(TEST_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "ServerThread");
        serverThread.start();
        
        // 서버가 완전히 구동될 때까지 잠시 대기 (임시)
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @AfterAll
    static void stopServer() throws IOException {
        server.stop();
    }
    
    @Test
    @DisplayName("단일 클라이언트가 여러 메시지를 전송하면 각각 [Echo] 형태로 응답한다")
    void testMultipleMessagesSingleClient() throws IOException {
        // 1. 클라이언트 생성
        ClientMock client = new ClientMock();
        
        // 2. 여러 개의 메시지 준비
        String[] messages = {"hello", "test", "123"};
        
        // 3. 서버에 전송 후 응답 받기
        String[] responses = client.connectAndChat("localhost", TEST_PORT, messages);
        
        // 4. 검증: 서버는 [Echo] prefix를 붙여 응답해야 함
        assertEquals("[Server Echo] hello", responses[0]);
        assertEquals("[Server Echo] test",  responses[1]);
        assertEquals("[Server Echo] 123",   responses[2]);
    }
    
    @Test
    @DisplayName("여러 클라이언트를 동시에 접속시켜 각각 메시지를 주고받는다")
    void testMultipleClientsConcurrently() throws InterruptedException {
        // 스레드 풀로 여러 클라이언트 병렬 실행
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // 클라이언트 3명을 동시에 실행한다고 가정
        int clientCount = 3;
        CountDownLatch latch = new CountDownLatch(clientCount);
        
        // 클라이언트의 응답 결과를 저장하기 위한 배열
        String[][] allResults = new String[clientCount][];
        
        for (int i = 0; i < clientCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    // 각 클라이언트별로 다른 메시지
                    String[] messages = {
                        "client" + index + "-msg1",
                        "client" + index + "-msg2"
                    };
                    
                    ClientMock client = new ClientMock();
                    String[] responses = client.connectAndChat("localhost", TEST_PORT, messages);
                    allResults[index] = responses;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(); // 모든 클라이언트 종료 대기
        executor.shutdown();
        
        // 검증: 각 클라이언트의 응답이 Echo로 잘 돌아왔는지 확인
        for (int i = 0; i < clientCount; i++) {
            String[] responses = allResults[i];
            assertEquals("[Server Echo] client" + i + "-msg1", responses[0]);
            assertEquals("[Server Echo] client" + i + "-msg2", responses[1]);
        }
    }
}
