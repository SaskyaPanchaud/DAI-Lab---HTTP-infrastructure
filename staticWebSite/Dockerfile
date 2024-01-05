# image de base = serveur nginx
FROM nginx:latest
# copie tout le contenu du site (html, css et javaScript) dans l'image
COPY ./content/ /contentStaticWebSite
# copie la configuration du serveur nginx dans l'image
COPY nginx.conf /etc/nginx/conf.d/default.conf