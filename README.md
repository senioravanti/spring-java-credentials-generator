# Утилита для генерации паролей и тестовых запросов к серверу авторизации OAuth2 

## Выполнить сборку образа

Multiplatform build требует включенного параметра containerd image store в настройках Docker Desktop, [см.](https://docs.docker.com/build/building/multi-platform/)

Для выполнения команды ниже необходимо перейти в корень проекта.

Зададим прослушиваемый контейнером порт как переменную среды

```sh
CREDENTIALS_GENERATOR_PORT=8005
```

```sh
clear ; \
docker build . \
  -f ./docker/Dockerfile \
  --push\
  --platform 'linux/arm64,linux/amd64' \
  --tag 'stradiavanti/credentials-generator:0.0.1' \
  --build-arg "CREDENTIALS_GENERATOR_PORT=$CREDENTIALS_GENERATOR_PORT"
```

```sh
clear ; \
docker run -d \
  -p "$CREDENTIALS_GENERATOR_PORT:$CREDENTIALS_GENERATOR_PORT" \
  --name 'credentials-generator' \
  --restart 'on-failure:3' \
  'stradiavanti/credentials-generator:0.0.1'
```

## Примеры запросов к генератору

### На генерацию учетных данных

```sh
clear ; \
curl -sS \
  "http://localhost:$CREDENTIALS_GENERATOR_PORT/api/credentials/bcrypt" | jq
```

### Запрос на генерацию запроса на получение кода

Минимизируем `json`

```sh
CLIENT_REQUEST=$(cat <<EOF 
{
  "scope": "read",
  
  "clientHost": "127.0.0.1",
  "clientPort": "8081",
  "clientId": "oidc-client",
  
  "serverHost": "localhost",
  "serverPort": "8001"
}
EOF
)
```

```sh
clear ; \
curl -sS -X POST \
  --json "$CLIENT_REQUEST" \
  "http://localhost:$CREDENTIALS_GENERATOR_PORT/api/oauth2/authorization-url" | jq
```

### Запрос на генерацию запроса на получение токена

```sh
TOKEN_REQUEST=$(cat <<EOF 
{
  "serverHost": "localhost",
  "serverPort": "8001",
  
  "clientId": "oidc-client",
  "clientSecret": "124356",
  
  "redirectUri": "http://127.0.0.1:8081/login/oauth2/code/oidc-client",
  
  "code": "W9l2iS3cgAaNPHARrrxr5K5EagRfw9wzbyFx3ky09lpwzvevhDQP3DJ59_GIjft3p26Gfd-H0pT5mxcngNiVHCFnDWkQb8Ti2of5Pxqu_-0I3bfvUjbEE__wdHFaVY5l",
  "codeVerifier": "wKtsmwgKAbNzf_uMnuGcq5Dhjr_nsHaZsV9x7qs4h8w"
}
EOF
)
```

```sh
clear ; \
curl -sS -X POST \
  --json "$TOKEN_REQUEST" \
  "http://localhost:$CREDENTIALS_GENERATOR_PORT/api/oauth2/token-request"
```