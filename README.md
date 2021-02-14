# 2등 수상!!
    🔥 2등을 수상했습니다!!

# Testing Environment
    현재 테스트 완료한 개발환경은 다음과 같습니다.
    안드로이드 8.1이상의 기기
    Google Pixel2 이후 기기
    삼성 갤럭시 s8 이후기기
    
    에서 작동을 확인했습니다. 이외의 기기에서는 UI가 깨지거나 설치가 안될 수 있습니다.
    설치가 안될경우
    https://zinle.tistory.com/520
    여기를 참고하시면 됩니다.


# Components
    1. MobileApp - 모바일 앱 개발 (Kotlin)
    2. ServerApp - 실시간 지하철 정보를 불러오기 위한 서버개발 (Python On Ubuntu)

# 대회 URL
[](생활_개선_앱개발_경진대회_안내)
[생활_개선_앱개발_경진대회_안내](https://www.skku.edu/skku/campus/skk_comm/notice01.do?mode=view&articleNo=87141&article.offset=10&articleLimit=10, "go link")

    * 2020.12.31까지 신청서 접수
    * 2020.01.31까지 최종 개발 앱 접수
    * 신청서, 최종개발보고서, 최종결과물 제출
![image](https://user-images.githubusercontent.com/50725139/103212324-d03e8600-494d-11eb-9a24-fa8e7081bd10.jpeg)

# 개발환경
       python의 경우 가상환경을 열어서 그 안에서 코딩해야함 (버전관리, 환경관리)
       pipenv shell을 치면 가상환경이 열림 (없다면 설치)
       pipenv install [package]를 통해 package를 설치가능

# Convention
### 변수명, 함수명
    1. global한 변수의 경우 전체 대문자
    2. local한 변수의 경우 전체 소문자와 언더바(_)로 구분
    3. 함수명의 경우 _(언더바)없이, 대문자로 구분
        ex) updateData()

### 분할 정복
    1. 하나의 함수에 두개 이상의 기능 넣지 않기 (분할)
    2. 한개의 모듈에는 관련있는 함수끼리만 묶기

### 주석
    함수나 변수에서 이해하기 어려운 곳의 경우 주석으로 설명달기

### 함수설명
    함수 정의 바로 밑에 """ """ 이걸로 처리하면
    나중에 클릭하면 바로 볼 수 있도록 나옴

# Commit Rule
    1. 함수 하나가 완성되면 "앱/모듈명/함수명 ~변경" 같은 식으로 커밋
    2. 기존의 것과 겹쳐서 변경이 필요하면 상의
   
