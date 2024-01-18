# DAI Lab - HTTP Infrastructure

## Step 1 - Static Web site

In this step, we configured an Nginx configuration file. Here is the annotated file:

```apacheconf
# This file is included by the native Nginx configuration file, inside the http block (meaning anything put in this file will apply to http connections). Here we define a server block. There can by any amount of them within the http block, enabling to run multiple websites on the same machine.

server { 
    # This tells Nginx on which port he should expect connections.
    listen 80;

    # This defines the root of our website on the machine.
    root /contentStaticWebSite; 

    # Here, we define a new location, meaning that any URL which matches this location (in this case, this will be the homepage, i.e. when the URL is <mywebsiteaddress>/)
    location / {
        # This tells Nginx which file to display.
        index index.html;
    }
}
```

The Dockerfile is quite straightforward, it simply copies the repo's static html page and puts it in the `/contentStaticWebSite` directory of the container (which matches our Nginx's configuration file). It also copies the `nginx.conf` file to the Nginx configuration folder.

Commands to use for demo :
```
# to build image
docker build -t <imageName> ./staticWebSite
# to run container
docker run -it -d -p 8080:80 --name <containerName> <imageName>
```

## Step 2 - Docker compose

This step was quite straightforward. We added a simple `docker-compose.yml` file, which can build and run our docker image containtig the static website. The build directive enables us to re-build the image, by running :
```
docker-compose build
``` 

or
```
docker-compose up --build
```

## Step 3 - HTTP API server

In this step, we added a basic API which supports all CRUD operations. This was done using Javalin, which helped a lot with the implementation.

We decided to start with a very simple API, enabling us to manage quotes defined by the actual quote, and an author. We then created a Java class `Quote` with only those two fields as String, and public getters.

We then proceeded to add a `QuoteController` class, with a simple constructor adding basic data.  Everything is stored withing the Java program, we did not add a database.
It has the following public methods to handle the requests:

- `getOne`
- `getAll`
- `create`
- `delete`
- `update`

These all take a Javalin `Context` as a parameter.

Finally, our `Main` class takes care of creating the Javalin app, which listens on a port (80 in our case). We then initialize the `QuoteController`, and binds it's methods to handle different
HTTP requests.

## Step 4 - Reverse proxy with Traefik

### Implementation
Our solution was to add a new service to docker-compose with the official Traefik image. We mount a volume to this image, enabling it to monitor the different containers running on our machine. We specified the Docker provider, and added two ports binding:

- One for the port 80
- One for the port 8080, for the Traefik dashboard

We then added rules to every service, defining the host address (localhost in our case) and, for the API, the path prefix to be added to the host address to access the service.

We also removed the port binding for our 2 services from the docker-compose. Instead, we added `EXPOSE` clauses to both Dockerfiles in order to specify which port to listen on within the container.

### Reverse proxy uses
A reverse proxy is very useful, as it enables us to hide the server addresses to the users of our services, which enables to do some load balancing in case we have multiple servers providing the same service.  It can also act as a firewall, with requirements to be met by the users before they can access the main server.

### Traefik dashboard
To access the Traefik dashboard, one should connect to the port 8080 (or whichever port is bound to the 8080 port of the reverse-proxy container). This dahsboard gives an overview of all the available services, and it shows the errors that may have happened.

### Configurations in `docker-compose.yml`

```
version: '3.8'

# add network
networks:
    traefik:
        name: traefik

services:
    static-web-site:
        build: ./staticWebSite
        image: http/site
        # add route for Traefik
        labels:
           - traefik.http.routers.static-web-site.rule=Host("localhost")

    http-api-server:
        build: ./httpApiServer
        image: http/api
        # add route for Traefik
        labels:
            - traefik.http.routers.http-api-server.rule=Host("localhost") && PathPrefix("/api")

    # add reverse-proxy service
    reverse-proxy:
        image: traefik:v2.10
        # access to dashboard
        command: --api.insecure=true --providers.docker
        ports:
            # api and static web site
            - "80:80"
            # dashboard
            - 8080:8080
        volumes:
            # access to container's list
            - /var/run/docker.sock:/var/run/docker.sock
```

## Step 5 - Scalability and load balancing

To add load balancing to our infrastructure, we simply had to add the following to each service:

```yml
  <service>:
    deploy: 
      replicas: <n>
```

which will deploy n instances of the service.

Instances of a service can be added dynamically by calling
```sh
docker compose up --scale <service>=<n>
```
which will change the number of replicas of a service. If we want to do it for several services, we can do
```sh
docker service scale <service1>=<n1> ... <servicex>=<nx>
```

To verify that the load balancing is happening properly, we can run

```sh
docker compose logs
```

after accessing a service multiple times, and check that the ip address changes between connections.

## Step 6 - Load balancing with round-robin and sticky sessions

Adding sticky sessions to our infrastructure was very easy, all that way needed was to add the following labels to our api service:

- traefik.http.services.http-api.loadBalancer.sticky.cookie=true

We also needed to add some sort of logging to our api to prove that our sticky sessions worked, which we did by printing a message for the get all requests. We can then see that every request from a same client are logged by the same Docker container.

## Step 7 - Securing Traefik with HTTPS

### Certificates

The certificates were generated by running the following command: 

```
openssl req  -nodes -new -x509  -keyout key.pem -out cert.pem
```

These are mounted to the traefik Docker container as a volume.

### Traefik config

This file defines simple HTTP and HTTPS entry points with the correct port. We also specify the path to our certificates, the provider (Docker) and enable the dashboard. Some of this was previously done in our `docker-compose.yml` file.

This file is mounted to our traefik container as a volume. 

### Activating the entrypoints

This step was done in `docker-compose.yml`, where we added the following  labels to both container's routers: 
```yaml
- traefik.http.routers.<router-name>.tls=true
- traefik.http.routers.<router-name>.entrypoints=https # our HTTPS entrypoint is named https
```

We also had to change the following port forwarding to the traefik container: 
```yaml
- "443:443"
```

## Optional step 1: Management UI

We use *Portainer* to manage the containers of our infrastructure. We add this config to the docker-compose.yml :

```
agent:
    image: portainer/agent
    volumes:
        - /var/run/docker.sock:/var/run/docker.sock
        - /var/lib/docker/volumes:/var/lib/docker/volumes

portainer:
    image: portainer/portainer-ce
    command: -H tcp://agent:9001 --tlsskipverify
    ports:
        - "9000:9000"
    volumes:
        - /var/run/docker.sock:/var/run/docker.sock
        - portainer_data:/data
    depends_on:
    - agent

volumes:
    portainer_data:
```

To manage the containers, go on http://localhost:9000 and enter :
    
 - user : admin
 - password : heigcoursdailabo.

## Optional step 2: Integration API - static Web site

We try to add `displayList()` in *scripts.js* but failed. The goal was to click on the button and all the quotes appear. Unfortunately, it doesn't work...
This work hasn't been merge on the *main* branch but is on the *feature-link_api-website* branch.
