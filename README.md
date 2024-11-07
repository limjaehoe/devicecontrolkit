# DeviceControlKit

## 프로젝트 소개 
X-ray 장비 제어를 위한 안드로이드 애플리케이션입니다. 시리얼 통신을 통해 장비와 통신하며, 직관적인 UI로 kV와 mA 값을 제어할 수 있습니다.

### 주요 기능
- kV(관전압), mA(관전류) 실시간 제어
- 설정값 자동 저장/복원 
- 실시간 작업 로그 기록
- 에러 모니터링 및 처리

### 사용 기술
- Kotlin
- MVVM Architecture
- Android Jetpack (ViewModel, LiveData, ViewBinding)
- Coroutines & Flow
- SharedPreferences

### 폴더 구조
com.androidkotlin.devicecontrolkit/
├── data/
│   ├── repository/
│   └── preferences/
├── domain/
│   ├── model/
│   └── usecase/
└── ui/
├── main/
└── adapter/

### 주요 구현사항
- 시리얼 통신을 통한 안정적인 장비 제어
- 사용자 친화적 UI/UX
- 설정값 영구 저장 및 복원
- 작업 로그 관리 시스템

### 설치 방법
```bash
git clone https://github.com/yourusername/DeviceControlKit.git

