package socketWithThread;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.concurrent.*;
	
	public class MultiClientMockTest {
		private static final int PORT = 6666;
		private static ServerMock server;
		
		@BeforeAll
		static void startServer() throws IOException {
			server = new ServerMock();
			Thread serverThread = new Thread(() -> {
				try {
					server.start(PORT);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}, "ServerThread");
			serverThread.start();
			
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
		@DisplayName("여러 클라이언트가 동시에 여러 메시지를 보낸다")
		void testMultipleClientsConcurrently() throws InterruptedException {
			int clientCount = 3; // 예: 3명의 클라이언트
			ExecutorService executor = Executors.newFixedThreadPool(clientCount);
			CountDownLatch latch = new CountDownLatch(clientCount);
			
			String[][] allResponses = new String[clientCount][];
			
			for (int i = 0; i < clientCount; i++) {
				final int index = i;
				executor.submit(() -> {
					try {
						// 각 클라이언트별 메시지
						String[] messages = {"client" + index + "-msg1", "client" + index + "-msg2"};
						ClientMock client = new ClientMock();
						String[] resp = client.connectAndChat("localhost", PORT, messages);
						allResponses[index] = resp;
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						latch.countDown();
					}
				});
			}
			
			latch.await(); // 모든 클라이언트 종료 기다림
			executor.shutdown();
			
			// 검증: 에코 확인
			for (int i = 0; i < clientCount; i++) {
				assertEquals("[Server Echo] client" + i + "-msg1", allResponses[i][0]);
				assertEquals("[Server Echo] client" + i + "-msg2", allResponses[i][1]);
			}
		}
	}
