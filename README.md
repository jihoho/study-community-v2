# [PAGU] 파티원 구해요

스터디원을 모집하기 힘들었던 우리, 주제에 맞게 더 편하게 스터디원을 모집하는 '파티원 구해요'

## 📄 서비스개요

🎈**기획**  :  스터디 모집 플랫폼

🎈**기획 배경** : 스터디를 참여하는데 제한을 느껴서 개발자들을 위한 스터디 사이트를 직접 만들게 되었다.

🎈**기획 목적** : 좀 더 명확한 스터디와 인원을 모집기능을 제공한다. 협업을 경험한다.

🎈**주요 기능 :** 회원가입, 로그인, 게시물 관리, 태그, 게시물 검색

🎈**주요 고객** : 개발을 좋아하는 누구나

🎈**서비스 채널** : 웹

## 📄 개발 환경

- Intellij
- java
- springboot
- gradle
- mySQL
- docker
- jenkins
- Goole Cloud Platform
- git
- notion
- figma

## 📄 주요 기능

- 회원가입
    - 이메일 인증 - spring-mail
    - 구글 계정 가입 - google ouath
- 회원 권한
    - 프로필 작성시 게시물 권한 부여 - spring security
- 프로필
    - 프로필 등록, 수정, 삭제
- 게시물
    - 게시물 작성, 수정 삭제
    - 주제, 기술 등록 시 태그 기반으로 작성
    - 페이징
- 댓글
    - 댓글 작성, 수정, 삭제

추후 영상으로 등록 예정

## 📄PAGU 화면 v1

![paguView](https://user-images.githubusercontent.com/58363663/120105823-8f18ff00-c195-11eb-9f77-d66076efedc3.png)
[피그마 링크](https://www.figma.com/file/tYKKDBOz7GYEfGUUD9qm39/study-comunity-web-ui?node-id=0%3A1)

## 📄 프로젝트 DB ERD

ERD-V1

<img width="1250" alt="pagu-erd" src="https://user-images.githubusercontent.com/58363663/120105854-aa840a00-c195-11eb-9b26-cd1075cb5c12.png">

## 📄 CI/CD

- GCP 인스턴스 1대에서 WAS, DB, Jenkins를 컨테이너로 띄워 관리

![pagu-ci-cd](https://user-images.githubusercontent.com/58363663/120105927-e028f300-c195-11eb-97f3-2126b35cb28a.png)

### CI

- PR시 마다 Build or Test 트리거 처리
- 테스트 실패시 merge reject

### CD

- master branch push 이벤트 발생 시 Github Webhook을 통해 Jenkins에서 처리
- Jenkins에서 Build 된 jar파일을 Target Server(WAS)로 전송 및 deploy script 실행

### 개선 방안

- jar파일 전송이 아닌 docker image로 버전 관리
- 프록시 서버 추가하여 무중단 배포
- docker-compose로 현재 컨테이너들 관리

## 📄 Git flow

- 개발전
    - master, develop 업데이트
    - feature 브랜치 생성
- 커밋 메세지

    ```
    Feat : 코드나 테스트를 추가했을 때
    Fix : 버그를 수정했을 때
    Remove : 코드를 제거했을 때
    Update : 코드보다는 문서나 라이브러리 등을 보완했을 때
    Docs : 문서를 수정했을 때
    Style : 코드 포맷팅에 대한 부분 변경, CSS 등
    Rename : 이름을 변경했을 때
    Move : 코드를 이동할 때
    Refac : 코드 리팩토링
    ```

- 참고 문헌

  [우아한 형제들 기술 블로그](https://woowabros.github.io/experience/2017/10/30/baemin-mobile-git-branch-strategy.html)