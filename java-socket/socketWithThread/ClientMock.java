package socketWithThread;

import java.io.*;
import java.net.*;

public class ClientMock {
    private Socket socket;         // 서버와 통신할 소켓
    private PrintWriter out;       // 서버로 데이터를 전송하는 출력 스트림
    private BufferedReader in;     // 서버로부터 데이터를 수신하는 입력 스트림
    private BufferedReader keyboard;
    
    /**
     * 서버 연결 & 다중 메시지 전송 후 종료
     *
     * @param host      서버 호스트명 (예: "localhost")
     * @param port      서버 포트 (예: 6666)
     * @param messages  서버로 전송할 메시지 배열
     * @return          서버가 보낸 응답들 (메시지 순서대로)
     * @throws IOException 연결 오류 시 발생
     */
    public String[] connectAndChat(String host, int port, String[] messages) throws IOException {
        try {
            socket = new Socket(host, port);
            System.out.println("[Client] 서버(" + host + ":" + port + ") 연결 완료.");
            
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // 메시지별 응답을 담을 리스트
            String[] responses = new String[messages.length];
            
            // 1) 반복적으로 메시지 전송
            for (int i = 0; i < messages.length; i++) {
                String msg = messages[i];
                System.out.println("[Client] 전송: " + msg);
                out.println(msg);
                
                // 서버 응답 수신
                String serverResp = in.readLine();
                System.out.println("[Client] 서버 응답: " + serverResp);
                responses[i] = serverResp;
            }
            
            // 2) "exit" 전송으로 통신 종료 요청
            out.println("exit");
            String exitResp = in.readLine();
            System.out.println("[Client] 종료 응답: " + exitResp);
            
            return responses;
            
        } finally {
            closeConnection();
        }
    }
    
    /**
     * 클라이언트 종료 메서드 (리소스 정리)
     * - 입력 및 출력 스트림, 소켓을 닫음
     */
    public void closeConnection() {
        try {
            if (keyboard != null) keyboard.close();
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("클라이언트 리소스가 정리되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}