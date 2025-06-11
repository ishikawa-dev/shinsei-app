# ベースイメージとして JDK を使う（JDK 21）
FROM openjdk:21

# 作業ディレクトリの設定（任意）
WORKDIR /app

# jar ファイルをコンテナにコピー（コピー先のファイル名も指定）
COPY target/shinsei_app-0.0.1-SNAPSHOT.jar /app/app.jar

# コンテナ起動時のコマンドを指定
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# 使用するポート番号（application.propertiesと合わせる）
EXPOSE 8083
