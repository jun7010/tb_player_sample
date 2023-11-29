# TB AI Challenge용 샘플 프로젝트

## 목록
Android : Exoplayer 기반 프론트엔드용 프로젝트
nodejs : Node.js 기반 Express 웹서버 백엔드용 프로젝트

## Features
- Android에서 사용자 지정 URL을 통한 동영상 스트리밍 재생
- 캡쳐 버튼을 통해 현재 화면 PNG 저장
- 저장된 PNG 파일은 node.js 백엔드 서버로 전송
- Node.js에서 저장된 이미지 파일을 후처리하여 JSON 데이터 생성
- Android에서는 리턴된 JSON 데이터를 기반으로 RecyclerView List 출력

## Install - Android

Android Studio를 통한 실행 및 수정 가능
android/TB_player/tb_player_sample.apk를 통해 설치 후 사용


## Install - node.js

Requirement : node.js 18

npm install
node main.js



## License

MIT
