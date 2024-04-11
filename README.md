Steps:
 1.mvn clean install
 2. Start Springboot application by  spring-boot:run


1) Run the following curl to post a Transaction 
curl --location 'http://localhost:8080/postTransaction' \
--header 'Content-Type: application/json' \
--data '{
    "date": "2018-06-30",
    "desc": "purchase trade 100.10",
    "amount": 100.10
}'


eg: In Postman 
{
    "date": "2018-06-30",
    "desc": "purchase trade 100.10",
    "amount": 100.10
}

2) Run the following to retrive Purchase Transaction previously submitted for Specific country
   curl --location 'http://localhost:8080/transactions/1?currency=ALBANIA-LEK' \
--data ''

3) All Controller,Service and Repository Tests are written in test packages
4) The database details are mentioned in the application.properties and i have used H2 database
5) Added loggers to track the flow
