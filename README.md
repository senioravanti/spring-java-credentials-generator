# Утилита для генерации паролей

## Примеры запросов

### На генерацию запроса на получение кода

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
    --json "$CLIENT_REQUEST"\
    http://localhost:8080/api/oauth2/authorization-url | jq
```

### На генерацию учетных данных

```sh
clear ; \
curl -sS \
  http://localhost:8080/api/credentials/bcrypt | jq
```

### На генерацию запроса на получение токена

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
    --json "$TOKEN_REQUEST"\
    http://localhost:8080/api/oauth2/token-request
```