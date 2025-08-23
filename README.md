# URL Shortener Service

## 프로젝트 소개

이 프로젝트는 긴 URL을 짧고 관리하기 쉬운 URL로 변환해주는 웹 애플리케이션입니다.

또한, 관리자 대시보드를 통해 URL 생성 통계 및 클릭 로그 분석 기능을 제공하여 URL을 관리할 수 있습니다.

<br>

## 주요 기능

- **URL 단축**: 긴 원본 URL을 고유하고 짧은 키를 가진 URL로 변환합니다.
- **빠른 리디렉션**: 생성된 단축 URL로 접속 시, 즉시 원본 URL로 리디렉션됩니다.
- **클릭 통계**: 단축 URL이 클릭될 때마다 관련 로그를 기록하여 통계를 추적합니다.
- **관리자 대시보드**:
    - 관리자 인증 (JWT 기반)
    - 전체 URL 목록 조회
    - URL별 클릭 통계 및 상세 로그 확인 (시간, IP 주소, Referer 등)
 
<br>

## 기술 스택

### Backend (`url_backend`)

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.5.4
- **데이터베이스**: MySQL, H2 (테스트용)
- **핵심 라이브러리**:
    - **ORM**: Spring Data JPA
    - **Query**: QueryDSL
    - **Security**: Spring Security, JWT (JSON Web Tokens)
    - **Caching**: Spring Cache (Ehcache)
    - **기타**: Lombok
 
<br>

### Frontend (`url_frontend`)

- **언어**: JavaScript (ES6+)
- **프레임워크**: Vue.js 3
- **빌드 도구**: Vite
- **UI 라이브러리**: Vuetify, Tailwind CSS
- **핵심 라이브러리**:
    - **Routing**: Vue Router
    - **HTTP Client**: Axios
    - **Charts**: Chart.js

 <br>


## 🗄️ 백엔드 아키텍처

백엔드는 **도메인 중심의 패키지 구조**로 기능별 응집도를 높이고 다른 기능과의 결합도를 낮췄습니다.

프로젝트는 크게 두 개의 최상위 패키지, **`feature`** 와 **`common`** 으로 나뉩니다.

```
be/url_backend/
├── feature/          # 핵심 비즈니스 기능 (도메인)
│   ├── url/          # URL 단축 기능
│   ├── admin/        # 관리자 기능
│   ├── log/          # 클릭 로그 기능
│   └── stats/        # 통계 기능
│
└── common/           # 공통 인프라 및 유틸리티
    ├── config/       # 애플리케이션 설정
    ├── dto/          # 공통 데이터 전송 객체
    ├── entity/       # 공통 베이스 엔티티
    ├── exception/    # 전역 예외 처리
    └── util/         # 공통 유틸리티
```

<br>

## 주요 화면


### 메인 화면

<img width="2832" height="1394" alt="image" src="https://github.com/user-attachments/assets/adfc0574-c17c-43e2-a1ea-71b1c37a76d1" />
<br>

### 관리자 로그인 화면
<img width="2820" height="1386" alt="image" src="https://github.com/user-attachments/assets/540d0dc2-e8fa-43f6-b045-ecd0c6e288e6" />
<br>

### 백오피스 화면
<img width="2876" height="1404" alt="image" src="https://github.com/user-attachments/assets/1e330cad-6167-4cdc-b977-e1ef59d28e77" />

--- 
