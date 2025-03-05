# Socket 통신 & 멀티스레드 예시

이 리포지토리는 **소켓 통신**과 **멀티스레드** 개념을 간단히 학습할 수 있도록 구성된 예시 코드들을 포함합니다.
- 서버(`ServerMock.java`)와 클라이언트(`ClientMock.java`)를 각각 분리
- 서버는 내부적으로 `ClientHandler` 스레드를 활용해 여러 클라이언트를 동시 처리
- JUnit 기반 테스트 2개(`SingleClientTest.java`, `MultiClientTest.java`)를 통해 단일/다중 시나리오 확인

아래는 각 파일의 역할과 사용 방법을 단계별로 설명합니다.

---

## 1. 프로젝트 구조

```
.
├─ ServerMock.java         // 멀티스레드 소켓 서버
├─ ClientMock.java         // 간단한 소켓 클라이언트
├─ SingleClientTest.java   // 단일 클라이언트, 다중 메시지 통신 테스트
├─ MultiClientTest.java    // 다중 클라이언트 병렬 테스트
└─ (기타 리소스/빌드 스크립트 등)
```

### 1.1 ServerMock.java
- **설명**
    - `ServerSocket`을 열고, 클라이언트 요청을 `accept()`로 수락
    - 접속이 들어올 때마다 `ClientHandler` 스레드를 생성해 메시지를 처리
    - `ClientHandler`는 클라이언트와 **반복적인** 송수신을 담당 (에코, `exit` 명령어 처리 등)
- **주요 메서드**
    - `start(int port)`: 지정된 `port`로 서버 소켓을 열고, 무한 루프(`while`)로 클라이언트를 받음
    - `stop()`: 서버 소켓 종료. 테스트 시에는 `@AfterAll` 등에서 호출
- **핵심 포인트**
    1. **멀티스레드**: 여러 클라이언트가 동시에 접속해도 각각 **독립된 스레드**에서 처리
    2. **스레드 이름 지정**: `setName("ClientHandler-" + socket.getPort())` 형태로 설정해 로그 시각화
    3. **while + readLine()**: 클라이언트로부터 메시지를 지속적으로 읽고, `exit`이면 종료

### 1.2 ClientMock.java
- **설명**
    - 서버에 연결하여 여러 메시지를 순차적으로 전송하고, 각 응답을 읽어오는 **소켓 클라이언트**
- **주요 메서드**
    - `connectAndChat(String host, int port, String[] messages)`:
        1. 서버 연결
        2. `messages` 배열의 문자열들을 차례대로 전송
        3. 서버가 보내주는 응답을 동일 길이의 배열로 수신
        4. 마지막에 `"exit"`를 전송하여 종료
    - `closeConnection()`: 소켓과 스트림을 닫아 자원 정리
- **핵심 포인트**
    1. **try-finally**: 예외 발생 여부와 상관없이 `closeConnection()`을 통해 자원 해제
    2. **에코 프로토콜**: 서버는 `[Echo] <원본>` 형태로 응답 (예시)
    3. **테스트 친화적**: 메시지를 배열에 담아 보내고, 응답도 배열로 돌려주므로 테스트에서 결과 검증 용이

---

## 2. 테스트 코드

### 2.1 SingleClientTest.java
- **목적**:
    - **한 명의 클라이언트**가 여러 메시지를 순차적으로 전송할 때, 서버가 제대로 응답(Echo)하는지 검사
- **흐름**:
    1. `@BeforeAll`: 서버 스레드(`ServerMock.start()`) 실행
    2. 테스트 메서드:
        - `connectAndChat("localhost", 6666, ["HelloServer", "test123", "foo bar"])`
        - 서버 응답이 `"[Echo] HelloServer"`, `"[Echo] test123"`, `"[Echo] foo bar"`인지 체크
    3. `@AfterAll`: `server.stop()`으로 서버 소켓 종료
- **로그 예시** (서버 콘솔)
  ```
  [ClientHandler-52133] 받은 메시지: HelloServer -> 응답: [Echo] HelloServer
  [ClientHandler-52133] 받은 메시지: test123 -> 응답: [Echo] test123
  [ClientHandler-52133] 클라이언트 종료 요청
  ```  
    - `ClientHandler-포트번호` 스레드가 메시지를 처리 중임을 쉽게 식별

### 2.2 MultiClientTest.java
- **목적**:
    - **여러 클라이언트**를 동시에(병렬) 실행하여 서버가 **멀티스레드**로 요청을 처리하는지 확인
- **흐름**:
    1. `@BeforeAll`: 서버 스레드 기동
    2. 테스트 메서드:
        - `ExecutorService`나 `CountDownLatch`를 활용해, 예: **3명의 클라이언트** 동시 실행
        - 각 클라이언트는 `[msg1, msg2]`를 전송하고 에코 응답을 받음
    3. `assertEquals`로 각 응답이 `"[Echo] msg"` 형태인지 검증
    4. `@AfterAll`: 서버 종료
- **로그 예시** (서버 콘솔)
  ```
  [ClientHandler-52309] 받은 메시지: client0-msg1 -> 응답: [Echo] client0-msg1
  [ClientHandler-52310] 받은 메시지: client1-msg1 -> 응답: [Echo] client1-msg1
  [ClientHandler-52312] 받은 메시지: client2-msg1 -> 응답: [Echo] client2-msg1
  ...
  ```
    - 스레드 이름(숫자)이 다 달라 **병렬**로 처리되는 장면을 알 수 있음

---

## 3. 실행 방법

### 3.1 수동 실행 (직접 확인)
1. **서버**
   ```bash
   javac ServerMock.java
   java ServerMock
   ```
    - 콘솔에 `[Server] 서버 시작 (포트: 6666)` 표시
2. **클라이언트**
   ```bash
   javac ClientMock.java
   java ClientMock
   ```
    - 내부의 `main` 메서드가 `"hello server"` 등 메시지를 전송 (코드에 따라 커스터마이징 가능)
3. 콘솔을 양쪽 번갈아 보면서 메시지 흐름 및 스레드 동작 확인

### 3.2 테스트 실행 (JUnit)
1. **빌드 도구**: Gradle 또는 Maven 환경이라 가정
2. **테스트 실행**
   ```bash
   ./gradlew test
   ```
   또는
   ```bash
   mvn test
   ```
3. **결과 확인**
    - JUnit 리포트를 통해 테스트가 모두 통과하면 **정상 통신**
    - 서버 콘솔에서는 여러 스레드 이름과 메시지 로깅이 뒤섞여 출력

---

## 4. 소켓 통신 & 멀티스레드 개념 정리
1. **ServerSocket & Socket**
    - `ServerSocket`은 지정된 포트로 **연결 대기**
    - `accept()`가 호출되면, 클라이언트와 연결된 **Socket** 리턴
    - 이후, `InputStream / OutputStream`으로 메시지 송수신
2. **멀티스레드**
    - 하나의 서버 프로세스가 접속마다 **새로운 스레드**(`ClientHandler`)를 생성
    - 클라이언트 개수가 증가해도 병렬/동시 처리가 가능
    - CPU·메모리 자원 고려, 과도한 스레드는 성능 저하를 유발 → **스레드 풀** 고려 가능
3. **exit 로직**
    - 단순히 `while` 루프에서 `"exit"`라는 문자열을 받으면 **break**
    - 실제 프로덕션 환경에서는 세션 종료, DB나 로깅 등의 부가 작업 수행 가능

---

## 5. 확장 아이디어
- **스레드 풀(ExecutorService)**
    - 스레드를 직접 생성하지 않고, 스레드 풀에서 스레드를 빌려 사용해 성능 최적화
- **비동기 I/O(NIO)**
    - Java NIO나 Netty 같은 라이브러리를 사용하면, 이벤트 기반으로 더 많은 접속을 효율적으로 처리
- **프로토콜 설계**
    - 단순 Echo 외에도 커맨드(`LOGIN`, `LOGOUT`)나 메시지 포맷(JSON/XML 등)을 설계해 확장 가능
- **로깅 프레임워크**
    - `System.out.printf` 대신 Logback, Log4j, Slf4j 등으로 더 체계적 로그를 기록

---

## 6. 결론
이 샘플 코드는 **소켓 통신**과 **멀티스레드** 개념을 가장 기초적으로 접할 수 있는 예제입니다.
- **ServerMock**: 여러 클라이언트를 동시에 수락하고, 각 스레드로 분산 처리
- **ClientMock**: 서버에 여러 번 메시지를 보낸 뒤 종료
- **테스트 코드**: 단일/다중 클라이언트 시나리오를 자동화로 확인
- **로그 시각화**: 스레드 이름으로 어떤 요청이 어떤 스레드에서 처리되는지 쉽게 파악

초보 개발자분들은 이 리포지토리를 통해,
1. **소켓 프로그래밍**의 전반적 흐름(`ServerSocket`/`Socket`)
2. **스레드 생성 및 자원 정리**(I/O 스트림 닫기)
3. **통합 테스트 작성**(JUnit + 실제 네트워크 통신)

을 체험해 볼 수 있습니다. 추가로 궁금한 점이나 개선 사항이 있다면 자유롭게 확장하시길 바랍니다. 감사합니다!