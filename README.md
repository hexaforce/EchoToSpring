# golangからspringにDockerネットワーク内でアクセスする例

## 1.mvn

```
cd spring
mvn clean package
```

## 2.docker-compose
```
cd ..
docker-compose up --build
```

## 3.curl

```
curl -X GET localhost:1323/demo/90909090 --header "Content-Type: application/json;charset=utf-8" | jq
```
