docker run --name jpa-demo -p 5433:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=jpa -d postgres || docker start jpa-demo
