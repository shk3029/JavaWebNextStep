# 자바 웹 프로그래밍 Next Step (2019/1/1 ~)
~~~
1. 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

2. 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.
~~~
## 4장. HTTP 웹 서버 구현을 통해 HTTP 이해
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
## 5장. 웹 서버 리팩토링, 서블릿 컨테이너와 서블릿의 관계
### 1. 리팩토링 과제설명
  - 요청 데이터를 처리하는 로직을 별도의 클래스로 분리(HttpRequest)
  ~~~
  * Hint
  - 클라이언트 요청 데이터를 담고 있는 InputStream을 생성자로 받아 HTTP메소드, URL, 헤더, 본문을 분리하는 작업
  - 헤더는 Map<String, String>에 저장해 관리하고, getHeader("필드 이름") 메소드를 통해 접근 가능하도록 구현
  - GET / POST 메소드에 따라 전달되는 인자를 Map<String, String>에 저장해 관리하고 getParameter("인자 이름") 메소드를 통해 접근 가능하도록 구현
  ~~~
  - 응답 데이터를 처리하는 로직을 별도의 클래스로 분리(HttpResponse)
  ~~~
  * Hint
  - RequestHandler 클래스를 보면 응답 데이터 처리를 위한 많은 중복이 있다. 이 중복을 제거한다.
  - 응답 헤더 정보를 Map<String, String>으로 관리
  - 응답을 보낼 때, HTML, CSS, 자바스크립트 파일을 직접 읽어 응답으로 보내는 메소드는 forword(), 다른 URL로 리다이렉트하는 메소드는 sendRedirect() 메소드를 나누어 구현
  ~~~
  - 다형성을 활용해 클라이언트 요청 URL에 대한 분기 처리를 제거
  ~~~
  * Hint
  - 각 요청과 응답에 대한 처리를 담당하는 부분을 추상화해 인터페이스로 만든다. 인터페이스는 다음과 같이 구현할 수 있음
    public interface Controller {
      void service(HttpRequest request, HttpResponse response);
    }
  - 각 분기문을 Controller 인터페이스를 구현하는(implement) 클래스를 만들어 분리한다.
  - 이렇게 생성한 Controller 구현체를 Map<String, Controller>에 저장한다. Map의 key에 해당하는 String은 요청 URL, value에 해당하는 Controller는 Controller 구현체이다.
  - 클라이언트 요청 URL에 해당하는 Controller를 찾아 service() 메소드를 호출한다.
  - Controller 인터페이스를 구현하는 AbstractController 추상클래스를 추가해 중복을 제거하고, service() 메소드에서 GET과 POST HTTP 메소드에 따라 doGet(), doPost() 메소드를 호출하도록 한다.
  ~~~

### 2. 리팩토링 구현 및 설명
 : 각 객체가 한 가지 책임을 가지도록 설계를 개선하는 리팩토링을 진행!!
1. 요청 데이터를 처리하는 로직을 별도의 클래스로 분리 (HttpRequest)
  - HttpRequest의 책임은 클라이언트 요청 데이터를 읽은 후, 각 데이터를 사용하기 좋은 형태로 분리하는 역할
  - 이렇게 분리된 데이터를 RequestHandler에서 사용
  - 즉, 데이터를 파싱하는 작업과 사용하는 부분을 분리
  - 복잡도가 높은 로직은 메소드로 분리
  - 메소드가 복잡도가 높으면 테스트가 필요한데, private로 정의된 메소드는 테스트하기가 어려우니 클래스로 분리 (processRequestLine 메소드를 RequestLine 클래스로 분리하여 Junit 테스트 진행)
  - GET, POST 문자열처럼 상수 값이 서로 연관되어 있는 경우 자바 enum을 쓰기 적합
  - [Enum 학습하기](http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html)
  ~~~
  public enum HttpMethod {
    GET,POST;
  }
  ~~~
  - RequestHandler에서 HttpRequest를 사용하는 코드를 짜자!
