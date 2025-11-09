# ====== ビルドステージ ======
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Gradleラッパーと設定をコピー
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# キャッシュを有効にしてビルド（テストはスキップ）
RUN ./gradlew bootJar -x test --no-daemon

# ====== 実行ステージ ======
FROM eclipse-temurin:17-jdk
WORKDIR /app

# ビルド済みJARをコピー
COPY --from=builder /app/build/libs/*.jar app.jar

# Renderが環境変数 $PORT を自動で指定するため、それを使用
ENV PORT=8080
EXPOSE 8080

# アプリを起動
ENTRYPOINT ["java","-jar","app.jar"]
