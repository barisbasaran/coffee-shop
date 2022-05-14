FROM openjdk:18

WORKDIR /app

COPY app-config.yml ./
COPY target/coffee-shop-1.0-SNAPSHOT.jar ./libs/
COPY target/dependency ./libs/

CMD ["java", "-cp", "libs/*", "io.baris.coffeeshop.CoffeeShopApplication", "server", "app-config.yml"]
