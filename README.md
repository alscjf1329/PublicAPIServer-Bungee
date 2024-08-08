# PublicAPIServer-Bungee

## 개요

PublicAPIServer-Bungee는 Minecraft Bungee 서버 플러그인으로, 해당 서버의 Public API를 제공합니다.

## 기능

### 해당 닉네임의 유저가 온라인인지 확인하는 기능

- **온라인 유저 확인 API**: 요청한 닉네임들의 유저들이 온라인인지 확인합니다.

## 적용 방법

### 플러그인 적용 방법

- Bungee 서버 내 `plugins` 디렉토리에 `PublicAPIServer-Bungee.jar` 파일 존재해야 합니다.
- Bungee 서버 내 `plugins/libs` 디렉토리에 존재해야 하는 라이브러리:
    - `json-20231013` 이상의 라이브러리
        - [JSON 라이브러리 - Maven Repository](https://mvnrepository.com/artifact/org.json/json)

### `config.yml` 설정 파일

- 플러그인을 적용한 후 서버를 시작하면 `plugins/PublicAPIServer-Bungee` 디렉토리에 `config.yml` 설정 파일이 생성됩니다.

```yaml
server:
  port: 15000     # 서버 포트
  log_flag: true  # 서버 정보 출력 여부
  path: # 서버 api endpoints 설정 
    validateOnlineUserPath: /player-status
```

## 적용 확인 테스트

### [Postman URL](https://www.postman.com/maintenance-astronaut-53396501/workspace/minecraft-api/request/25507989-7676887b-140c-4fbc-a0a6-98c3ef6202d8?ctx=documentation)

- POST `localhost:15000/player-status` 요청 보낸 결과 <br>
  ![인증 성공.png](./img/유저%20온라인%20상태%20확인.png)<br>
  위 사진에서 다음을 확인할 수 있습니다:
    + statuses : 온라인 상태

## ERROR

- `config.yml` 파일 초기화를 원할 경우 파일을 삭제하고 Bungee 서버를 다시 시작합니다.
