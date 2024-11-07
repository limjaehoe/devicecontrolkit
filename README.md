DeviceControlKit
📱 프로젝트 소개
DeviceControlKit은 안드로이드 기반의 의료 장비 제어 애플리케이션입니다. 시리얼 통신을 통해 X-ray 장비와 통신하며, 직관적인 UI로 kV와 mA 값을 제어할 수 있습니다.
⚙️ 주요 기능

실시간 장비 제어

kV(관전압) 제어: 0-100 범위 조절
mA(관전류) 제어: 0-100 범위 조절
연결 상태 모니터링


설정값 관리

설정값 자동 저장
앱 재시작 시 이전 설정 복원
장비 자동 재연결


로그 시스템

실시간 작업 로그
에러 모니터링
작업 이력 추적



🛠️ 사용 기술

Language: Kotlin
Architecture: MVVM
Android Jetpack

ViewModel
LiveData
ViewBinding


비동기 처리

Coroutines
Flow


데이터 저장

SharedPreferences



📐 아키텍처
Copycom.androidkotlin.devicecontrolkit/
├── data/
│   ├── repository/
│   │   └── DeviceRepository
│   └── preferences/
│       └── DevicePreferences
├── domain/
│   ├── model/
│   │   └── DeviceError
│   └── usecase/
│       └── DeviceControlUseCase
└── ui/
    ├── main/
    │   ├── MainActivity
    │   └── MainViewModel
    └── adapter/
        └── LogAdapter
🎯 주요 구현사항

안정적인 시리얼 통신

통신 오류 자동 복구
재연결 메커니즘


사용자 친화적 UI

직관적인 제어 인터페이스
실시간 피드백
에러 상황 명확한 표시


데이터 관리

설정값 영구 저장
작업 로그 관리
에러 로깅



🔄 업데이트 내역

v1.0.0 (2024-01-06)

초기 버전 릴리즈
기본 제어 기능 구현
로그 시스템 추가



📋 향후 계획

 다크 모드 지원
 그래프 모니터링 기능
 데이터 내보내기 기능
 자동 테스트 모드

🔧 설치 및 실행

Android Studio에서 프로젝트 클론

bashCopygit clone https://github.com/yourusername/DeviceControlKit.git

프로젝트 빌드 및 실행

📱 스크린샷
[스크린샷 추가 예정]
🤝 기여하기
프로젝트에 기여하고 싶으신가요? Issue나 Pull Request를 환영합니다!
📝 라이센스
이 프로젝트는 MIT 라이센스를 따릅니다.
👨‍💻 개발자 정보

이름: [귀하의 이름]
이메일: [귀하의 이메일]
GitHub: [귀하의 GitHub 프로필]


⭐ 이 프로젝트가 도움이 되었다면 스타를 눌러주세요!
