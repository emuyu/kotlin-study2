# TODO:構築終わったらちゃんと書く

# Name
Docker理解用の練習リポジトリ
- SpringBoot 2.7.10
- gradle
- Java 11
- MySQL 8
- Redis(予定)
-
# DEMO
コンテナを理解するために適当に作成しました

# Features
SpringBootとコンテナの記事があまり無かったのでやってみた。
IntelliJを利用しながらDocker操作ができなかったので工夫が必要？？

# Requirement
gradleで下記を利用した。
```
dependencies {
implementation 'org.springframework.boot:spring-boot-starter-web'
compileOnly 'org.projectlombok:lombok'
developmentOnly 'org.springframework.boot:spring-boot-devtools'
runtimeOnly 'com.mysql:mysql-connector-j'
annotationProcessor 'org.projectlombok:lombok'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

# Installation
- SpringBootの設定は下記のリンクから
    - https://start.spring.io/

- ここらの記事を参考に進めました
    - https://blog.tiqwab.com/2017/03/21/docker-java.html
    - https://zenn.dev/nishiharu/articles/7f27b8c580f896
    - https://qiita.com/Katan/items/9c72d4286e94bddaa42f
    - https://www.early2home.com/blog/programming/java/post-1479.html

# Usage
環境構築終了後、下記を実行
```
docker compose up -d

docker compose exec spring-api-test bash
sh gradlew build
java -jar build/libs/〇〇〇〇.jar

http://localhost:9090/greeting
http://localhost:9090/greeting?name=aaa
```

# Note
深夜テンションで適当にエイヤで作成したので穴がすごいです。<br>
徐々に直していくのでお手柔らかに(3/29)

そ、し、て、コードも何も考えずに適当に実施している！！
今後は下記を実施
- [ ] レイヤードアーキテクチャを利用
- [ ] Mybatisを利用してMySQLと疎通できるように作成
- [ ] リファクタリングを行う
- [ ] 基本的なCURDができるAPI群を作成
- [ ] APIを作成したらAPIGatewayで管理 → ECS → Lambdaに移行できないか考える

# Author
作成情報を列挙する

* 作成者: emuyu
* 所属:  バックエンドエンジニア
