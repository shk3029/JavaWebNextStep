# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 4장. HTTP 웹 서버 구현을 통해 HTTP 이해 (1/1~1/20)
  - 요구사항 1 : index.html 응답하기
    - jdk7 이상부터 try 안에 InputStream, OutputStream과 같은 Closeable를 implement하는 클래스의 경우 try가 끝나면 자동 close가 됨
    - 요청라인에서 url를 가져와 응답하도록 함
  - 요구사항 2 : GET 방식으로 회원가입
    - GET 방식의 경우, URL로 경로와 쿼리스트링이 함께 들어옴
    - 경로와 쿼리스트링을 분리
    - 쿼리스트링의 값을 map으로 담은 후, 꺼내어 User객체에 저장
  - 요구사항 3 : POST 방식으로 회원가입
    - 먼저 jsp 파일의 form method 속성 값을 POST로 변경
    - POST 방식에서는 요청 URL에 쿼리스트링이 없고, HTTP 요청의 Body를 통해 전달
    - 이 Body의 길이가 Content-Length라는 필드 이름으로 전달
    - GET 방식 : 서버에 존재하는 데이터를 가져올때
    - POST 방식 : 서버에 요청을 보내 데이터 추가, 수정, 삭제와 같은 작업을 실행
  - 요구사항 4 : 302 status code 적용
    - 현재 새로고침할 떄, 회원가입 요청이 계속 재요청됨
    - 302 상태코드를 통해 회원가입 후, 리다이랙트 시켜줌
  - 요구사항 5 : 로그인하기
    - Map을 이용해 User 정보를 저장
    - 로그인 정보가 맞으면 쿠키값으로 저장
  - 요구사항 6 : 사용자 목록 출력
    - 쿠키값 logined가 true이면 목록출력 / 아니면 index페이지로
  - 요구사항 7 : css 지원
    - url이 css로 끝나는 파일의 경우, Content-Type 헤더 값을 text/css로 변경
### 5장. 웹 서버 리팩토링, 서블릿 컨테이너와 서블릿의 관계
  - 5.1 HTTP 웹 서버 리팩토링 실습
