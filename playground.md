# Протокол

Публичный ключ шифрует сообщение, а закрытый может его расшифровать.

Code verifier :: Байты в base64 для URL (вместо `/` и `+` используют `-` и `_`);
Code challenge :: `base64url(sha256(code_verifier))` (сначала передают его);

Клиент

client_id: `Gleaming-Tiger-79`
client_secret: `zFmLQz21bswqCPKgUu9-Q3YSqRKmptjKoGvSvQggY95aTQ4T`

Пользователь

login: `motionless-iguana@example.com`
password: `Gleaming-Tiger-79`

code challenge: `-y_2milLSToWC6QuiTA275JlyyjIHcpqwAid5a4BCqI`
code verifier: `xEF8U2QzWeUdRdGEKWXUcxkrSm-pIGudQ97XCNoCPRDAu1CZ`

state: `gGq7WQ8zE8J7qAvp`

redirect_uri: `https://www.oauth.com/playground/authorization-code-with-pkce.html`

Клиент должен хранить state чтобы проверить state из запроса на указанный в `redirect_uri` роут клиента.

uri после редиректа: `https://www.oauth.com/playground/authorization-code-with-pkce.html?state=gGq7WQ8zE8J7qAvp&code=Mv-E8cphEjPqYN9B0H_S6c0M9waMS_JiVmM7UJYm0GhR4t1E`


Пример GET запроса на сервер авторизации на получение кода:
```
https://authorization-server.com/authorize?
  response_type=code
  &client_id=dCAdEUevDMCgHVcCBh_WZSLN
  &redirect_uri=https://www.oauth.com/playground/authorization-code-with-pkce.html
  &scope=photo+offline_access
  &state=xAcTrySmGnCBW1Yh
  &code_challenge=g6XpcXQhuEnkUHJZQoIAY1IvHLKIRatWxQbrD9_DrUg
  &code_challenge_method=S256
```

Пример POST запроса на сервер авторизации с кодом, полученным в запросе от сервера:

```
POST https://authorization-server.com/token

grant_type=authorization_code
&client_id=dCAdEUevDMCgHVcCBh_WZSLN
&client_secret=zFmLQz21bswqCPKgUu9-Q3YSqRKmptjKoGvSvQggY95aTQ4T
&redirect_uri=https://www.oauth.com/playground/authorization-code-with-pkce.html
&code=Mv-E8cphEjPqYN9B0H_S6c0M9waMS_JiVmM7UJYm0GhR4t1E
&code_verifier=xEF8U2QzWeUdRdGEKWXUcxkrSm-pIGudQ97XCNoCPRDAu1CZ
```

Пример ответа сервера на запрос выше

```json
{
  "token_type": "Bearer",
  "expires_in": 86400,
  "access_token": "qHWL-JmHc4savuF2wXkIIiWozq7fxNcrJPTSCcvmfpoKTyOpTUZQMmoTSCikn8hZ4xFD9HD2",
  "scope": "photo offline_access",
  "refresh_token": "WWQ13Ji0IGPybr9vkdnexwGX"
}
```

## Пример запроса 

Информация о сервере авторизации

```sh
clear ; \
curl -sS http://localhost:8001/.well-known/openid-configuration | jq
```

```sh
http://localhost:8001/connect/logout
```

```json
{
  "authorizationRequest": "http://localhost:8001/oauth2/authorize?response_type=code&client_id=oidc-client&redirect_uri=http://127.0.0.1:8081/login/oauth2/code/oidc-client&scope=read&code_challenge=evTsOqeC_AJvKWANFTUcbgitcF_z_uSYOwx_yYcGFgc&code_challenge_method=S256&state=GOiLm11bWApvC0IG",
  "codeVerifier": "wKtsmwgKAbNzf_uMnuGcq5Dhjr_nsHaZsV9x7qs4h8w"
}
```

http://127.0.0.1:8081/login/oauth2/code/oidc-client?code=W9l2iS3cgAaNPHARrrxr5K5EagRfw9wzbyFx3ky09lpwzvevhDQP3DJ59_GIjft3p26Gfd-H0pT5mxcngNiVHCFnDWkQb8Ti2of5Pxqu_-0I3bfvUjbEE__wdHFaVY5l&state=GOiLm11bWApvC0IG

http://127.0.0.1:8081/login/oauth2/code/oidc-client?code=lVzhleZGN0h6Ofh87aNvsnswS_5yhXoHbU3ZbQCDI0UK42dKeU2HQ5wCP87yggGv5VTOGqrWoMwuEqO__561eSgi6sY2ZHja8ZdZYixClt3x87OISRnKiFxIjXLU41AV&state=hOWbTCiMx7i329Vf
```sh
clear ; \
curl -sS -w '\n%{response_code}\n' -X POST \
    --data-urlencode 'client_id=oidc-client' \
    --data-urlencode 'redirect_uri=http://127.0.0.1:8081/login/oauth2/code/oidc-client' \
    --data-urlencode 'grant_type=authorization_code' \
    --data-urlencode 'code=W9l2iS3cgAaNPHARrrxr5K5EagRfw9wzbyFx3ky09lpwzvevhDQP3DJ59_GIjft3p26Gfd-H0pT5mxcngNiVHCFnDWkQb8Ti2of5Pxqu_-0I3bfvUjbEE__wdHFaVY5l' \
    --data-urlencode 'code_verifier=wKtsmwgKAbNzf_uMnuGcq5Dhjr_nsHaZsV9x7qs4h8w' \
    --header 'Authorization: Basic b2lkYy1jbGllbnQ6MTI0MzU2' \
    'http://localhost:8001/oauth2/token'
```

```json
{
  "access_token": "eyJraWQiOiI2OWMxMzVjZi03OGMzLTQwYWUtOWQ0OS1lZWFhZGQ1YTlkZjEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI5MTJmYTk5MC1iODUyLTQwZTktOGQwYi1kOTYyODkwMTIzODkiLCJhdWQiOiJvaWRjLWNsaWVudCIsIm5iZiI6MTc0MzcwMTc4Mywic2NvcGUiOlsicmVhZCJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwMDEiLCJleHAiOjE3NDM3MDIwODMsImlhdCI6MTc0MzcwMTc4MywianRpIjoiYjU0Mjg1OWMtYWJjZC00NTBmLWE0OTItZDJkMTdkN2I1NDNmIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.gOOxB-5jeo0gIiDM9_LwrPC5NIdjG6TYnb1WLPukxRydK4xam5yXDj2YTlPLfhEvgE4M6z4fULlqhJhC7TJfSfk_DXJEm6TaqG2jirSvunN0QlkuEkHSVVXGkDL1qRkuuxg_WO6ZGuFeeywqatkjlYV3SnNX5IJuWgN671gnuvTcdo-8LV8Pe7fjswDkO2Wa32VOAgNiG03wWJfxG3WmAB0R-XdzbeB4mAf19JRrVmkScLKdbY5lIVmKeebbJilX00ZnhlZIOZLsxsf2sd4Xf9U7eDTt09DU71gmiiV3jsfCOF5rnQ2iP3Prf06rrTGh-8unHmMJir69efT983agNv_qPOweO40mwgBjHJ7dYtll5RAoQDKBkLajyJX-KmJtUleIZHPOUGWKalL2gX9jKbWWxFK3nKtXhWxwDgFZq0UWHbZkcJAQeF6oX8jccp0TAV2AKNnJcAlPWE5kHAeVFWWkZNvvYQTj2JiSXRAut2ZesESENMDHpmBc6ZZ9vLq8",
  "refresh_token": "xZaEMFwrY3UV_hZQVC-EvYVRE9rMF8vnT5CVyjE8mML9t-5_uxLDamGAJWsFac4B2ONoOxNq0LBbnXSDct-PdTJ9CG80IVN9M3oOBCTeJrmj550CRc7QLcs6tqJiwtp5",
  "scope": "read",
  "token_type": "Bearer",
  "expires_in": 299
}
```