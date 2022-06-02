# TicketingService

## Modulok

- api: swagger API alapján generál Java kódot
- database: liquibase alapú groovy scriptek. Oracle adatbázisra lehet futtatni(változtatható build.gradle-ben)
  -  update gradle taskkal
- core: api modul alapján definiált végpontok kiszolgálása
  - (Összevontam a ticket modullal, mivel a core lényegében csak egy ValidatorService)

## Függőségek
- PartnerService client lib-re: hu.matray.partner:client:1.0.0-SNAPSHOT
  - publishToMavenLocal gradle task PartnerService-ben
- Az liquibase futtatáshoz szükséges a séma megléte: OTP_TICKET, a séma név, jelszó és az adatbázis url a database modul gradle.properties fileban változtatható

## Konfiguráció

```yaml
server.port: 8080

spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521/xe
    username: OTP_TICKET
    password: changeme
    driver-class-name: oracle.jdbc.OracleDriver

application:
  partnerFeignClient: #Partner szolgáltatás elérése
    url: http://localhost:9111
    username: ticketUser
    password: ticketPassword
```