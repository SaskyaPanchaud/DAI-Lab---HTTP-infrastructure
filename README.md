# DAI Lab - HTTP Infrastructure

## Step 1 - Static Web site

File *nginx.conf* explained :
```
server {
    # This tells Nginx on which port he should expect connections.
    listen 80;

    # This defines the root of our static web site on the machine.
    root /contentStaticWebSite;

    # This defines a new location (in this case, it will be the homepage, i.e. when the URL is <mywebsiteaddress>/).
    location / {
        # This tells Nginx which file to display.
        index index.html;
    }
}
```

Commands to use for demo :
```
# to build image
docker build -t <imageName> ./staticWebSite
# to run container
docker run -it -d -p 8080:80 --name <containerName> <imageName>
```

## Step 2 - Docker compose

File *docker-compose.yml* explained :
```
version: '3.8'

services:

static-web-site:
    build: ./staticWebSite
    image: http/site
    ports:
        - "8081:80"
```

Commands to use for demo :
```
# to build the infrastructure
docker compose build
# to run the infrastructure
docker compose up
# to stop the infrastructure
docker compose down
```

## Step 3 - HTTP API server

## Step 4 - Reverse proxy with Traefik

## Step 5 - Scalability and load balancing

## Step 6 - Load balancing with round-robin and sticky sessions

## Step 7 - Securing Traefik with HTTPS
