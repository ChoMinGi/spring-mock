package simpleSocket;
import java.io.*;
import java.net.*;

public class ClientMock {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Socket connectToServer() {
        try {
            socket = new Socket("localhost", 6666);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("서버에 접속되었습니다.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // 서버에 메시지 전송
            System.out.println("서버에 전송할 메시지를 작성하세요.");
            String message = reader.readLine();
            out.println(message);

            return socket; // 소켓을 닫지 않고 반환

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 클라이언트 닫기
    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("클라이언트 종료됨.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 서버로부터 메시지를 받는 메서드 추가
    public void handleServer(Socket socket) {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 서버로부터 메시지 수신
            String response = in.readLine();
            System.out.println("서버로부터 받은 메시지: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        ClientMock client = new ClientMock();
        Socket socket = client.connectToServer();
        // 서버와 연결이 끊어지면 프로그램 종료
        client.handleServer(socket);
    }
}
