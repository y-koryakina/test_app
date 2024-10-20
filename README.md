# Документация
Проект включает реализацию следующих методов: 
- Получение списка всех продавцов 
- Получение информации о конкретном продавце
- Создание нового продавца
- Обновление информации о продавце
- Удаление продавца
- Получение списка всех транзакций
- Получение информации о конкретной транзакции
- Создание новой транзакции
- Получение всех транзакций продавца
- Получение самого продуктивного продавца за указанный период
- Получение списка продавцов с суммой меньше указанной (с возможностью указать период)
### Инструкция по сборке проекта
1) Импортируйте проект в IntelliJ IDEA.
2) Создайте базу данных PostgreSQL и назовите ее "crm".
3) Создайте нового юзера и назовите его "crm", указав пароль "crm".
4) Выполните следующие SQL скрипты для создания таблиц:
```
CREATE TABLE IF NOT EXISTS seller ( 
id SERIAL PRIMARY KEY, 
name VARCHAR(255) NOT NULL, 
contact_info VARCHAR(255), 
registration_date TIMESTAMP NOT NULL  );
```

```
CREATE TABLE IF NOT EXISTS Transaction ( 
id BIGSERIAL PRIMARY KEY, 
seller_id BIGINT NOT NULL, amount DECIMAL(10, 2) NOT NULL, 
payment_type VARCHAR(20) NOT NULL CHECK (payment_type IN ('CASH', 'CARD', 'TRANSFER')), 
transaction_date TIMESTAMP NOT NULL, 
FOREIGN KEY (seller_id) REFERENCES Seller(id) );
```
5) Для запуска проекта запустите файл *CrmAppApplication.java*. Файл application.yaml уже настроен. 
### Необходимые зависимости
В архиве в файле build.gradle все зависимости уже настроены: ниже код из файла.
```
dependencies {  
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'  
    implementation 'org.springframework.boot:spring-boot-starter-web'  
    compileOnly 'org.projectlombok:lombok'  
    runtimeOnly 'org.postgresql:postgresql'  
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'  
    annotationProcessor 'org.projectlombok:lombok'  
    testImplementation 'org.springframework.boot:spring-boot-starter-test'  
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'  
    implementation 'org.hibernate.orm:hibernate-envers:6.5.3.Final'  
    implementation 'org.hibernate.orm:hibernate-core:6.5.3.Final'  
    testImplementation 'com.h2database:h2'  
}
```
