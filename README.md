## Getting Started

#### copy .env.example to .env in folder docker and set your environment variables
```bash
cp .env.example .env
```

#### build the docker image
```bash
docker build --build-arg FILE_ENV=docker/.env --file docker/Dockerfile -t server-organization-management-app .
```

#### run the docker container
```bash
docker run --env-file docker/.env -p 8080:8080 server-organization-management-app
```