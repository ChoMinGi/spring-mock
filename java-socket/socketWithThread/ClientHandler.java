package socketWithThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.setName("ClientHandler-" + clientSocket.getPort());
	}

	@Override
	public void run() {
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String message;
			while ((message = in.readLine()) != null) {
				// 클라이언트 메시지 수신
				System.out.println("클라이언트: " + message);
				
				// 서버에서 처리 후 응답 (여기서는 Echo 형태로 간단히 반환)
				out.println("[Server Echo] " + message);
				// 어떤 스레드가 이걸 처리했는지 출력
				System.out.printf("[%s] 받은 메시지: %s -> 응답: %s\n", getName(), message, "[Server Echo] " + message);
				
				
				
				// 만약 클라이언트가 'exit'를 입력하면 종료
				if ("exit".equalsIgnoreCase(message)) {
					out.println("bye");
					System.out.printf("[%s] 클라이언트 종료 요청\n", getName());
					break;
				}
			}
			closeConnection();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeConnection () throws IOException {
		if (in != null)
			in.close();
		if (out != null)
			out.close();
		if (clientSocket != null)
			clientSocket.close();
		System.out.println("클라이언트 연결이 종료되었습니다.");
	}
}
