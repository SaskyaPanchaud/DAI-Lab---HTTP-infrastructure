# image de base avec java directement installe
FROM openjdk:21
WORKDIR /api
# fichier .jar copie dans l'image
COPY ./target/api.jar /api
# fichier .jar execute dans l'image
CMD ["java", "-jar", "api.jar"]
# FIXME : fichier .jar n'est pas compile par docker mais seulement deplace donc doit etre compile manuellement avant pour que l'image se base sur la derniere version