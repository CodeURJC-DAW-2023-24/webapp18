# webapp18
# Nombre: Hs0s
## Integrantes: 
  - Pedro Cristino Moreno - PedroCristino2020 - p.cristino.2020@alumnos.urjc.es
  - Jorge Sánchez Díaz - RisiGT - j.sanchezdi.2020@alumnos.ujrc.es 
  - Marcos del Valle Hontanar - MarcoDelValleH - m.delvalle.2020@alumnos.urjc.es

# Fase 0

## Entidades
  - Usuarios (empleadores y socorristas)
  - Piscinas
  - Comentarios
  - Ofertas
## Relaciones
  - Los empleadores publican ofertas para los socorristas.
  - Las ofertas tienen una piscina asociada.
  - Las piscinas tienen comentarios de otros socorristas.

## Permisos usuarios
  - Socorrista: Puede ver y aceptar ofertas. Puede comentar en el foro de cada piscina.
  - Empleadores: Pueden ver y publicar ofertas. Pueden elegir un socorrista de los propuestos para la oferta.
  - Anónimo: Puede ver ofertas y los comentarios de las piscinas.
  - Admin: Además de poder hacer todo lo de los anteriores, también puede dar de alta empleadores.

## Imágenes
  - Tienen imágenes asociadas los socorristas (su titulación, foto personal) y las piscinas (foto de la piscina) y los emleadores (logo de la empresa)

## Gráficos
  - Mostrará el número de socorristas que reúnen cada aptitud.

## Tecnología complementaria
  - Google Maps

## Algoritmo de consulta avanzada
  - Mostrara a cada socorrista ofertas personalizadas para él (basado en la distancia, aptitudes...)


# Fase 1

## Diagrama E-R

![imagen](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/53cc30c7-72a6-4c19-95b8-3823bf6a607c)


# Fase 2



## Navegación 
![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/d62c1427-e17f-47ca-8b95-b1a422a1ef59)


## Instrucciones de ejecución
Para iniciar la aplicación web se deben seguir los siguientes pasos:
  - Instalar las extensiones y librerias relativas a Spring y Java
  - Iniciar la base de datos mySQL
  - Descargar la aplicación
  - Configurar en aplication.properties el puerto en el que se inicial el programa, el usuario de la base de datos local, su contraseña y su dirección.
  - Iniciar Application.java
  - Acceder desde el navegador (https://localhost:x, donde x es el puerto configurado en el paso anterior)




## Diagrama de los clases y templates

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/d5bc4008-3850-4338-ae25-5a2651e55881)


## Diagrama de entidades

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e28fbed3-cf45-4144-907c-b3614b940f9c)

## Usuarios de ejemplo (nombre de usuario y contraseña)
### administrador: admin, admin
### empleador: e1,e1
### socorrista: s1, s1
## Participación de miembros

El reparto de tareas tenía sentido en una fase inicial. Luego hicimos una lista de cosas pendientes y cada uno ibamos realizando tareas sin ningun criterio de reparto en especial. 

### Jorge:
#### Tareas:
Implementación de las ofertas, paginación, edición de usuario, google maps. Diseño de la página web. Refactorización de código.
#### Commits: 
  - Applied builder pattern + Pool inside Offer
  - Refactoring services and their connections with the database
  - Implemented Google Maps
  - Implemented edit user
  - Implemented offers pagination
#### Ficheros: 
  - maps.js
  - offers.js
  - user_form.html
  - UserService.java
  - UserController.java
     
### Marcos:
#### Tareas:
Gráfica, implementar lo relacionado a pools y messages, las offer applyances, privilegios de admin. 
#### Commits: 
  - [Offer applying and checking added](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/ab07b9be3f03737aaa02bd75446cedaf6d69f917)
  - [Graphic added](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/7e562666737ab5471b687abf588b777729bcf857)
  - [Merge branch 'pruebaSecurity' into fase2](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/b642aca33493b114cef790bbf5caed46e3a392c0)
  - [Added form to create pools](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/8f4728d92ef972f61f96e56bd8a4d403f8dd4131)
  - [AddMessage and delete message now working through DB](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/7f3eab1b42a0488f37e94b2c2dab5419128d1df1)
#### Ficheros: 
  - PoolController
  - PieController
  - OfferController
  - Pool
  - Offer
     
### Pedro:
#### Tareas:
Implementar lo relacionado a usuarios (employers y lifeguards), inicio de sesión, registrarse, perfil y seguridad.
#### Commits: 
  - Added employers and lifeguard databases
  - works login and view your profile
  - initialize database and management of added roles
  - logout implementation and header improvement
  - Added list of lifeguards and employers can remove them from accepted offers
#### Ficheros: 
  - UserController
  - Person
  - Lifeguard
  - Employer
  - profile

### Adrián:
#### Tareas:
API REST de ofertas, edición y actualización.
#### Commits: 
  - offer updates
  - offer edit get and post rest methods
#### Ficheros: 
  - offer.html
  - offer_form.html
  - OfferControles.java

# Fase 3
## Diagrama de los clases y templates

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e7f730fc-fe3f-4e41-bacd-3d00ab52ede9)



## Diagrama de entidades

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e28fbed3-cf45-4144-907c-b3614b940f9c)


## Participación de miembros

El reparto de tareas tenía sentido en una fase inicial. Luego hicimos una lista de cosas pendientes y cada uno ibamos realizando tareas sin ningun criterio de reparto en especial. 

### Jorge:
#### Tareas:
Implementación de los métodos GET y PUT de ofertas, piscinas y mensajes. Refactorización de código y pulir funcionalidades.
#### Commits: 
  - Implemented getOffers for API REST
  - Implemented editOffer for API REST
  - Implemented editPool for API REST
  - Created isAuthorized
  - Checked authorization in delete Pool and Message
#### Ficheros: 
  - OfferRestController.java
  - PoolRestController.java
  - Message.java
  - MessageRepository.java
  - UserService.java
     
### Marcos:
#### Tareas:
RestController de la Grafica y de Offer 
#### Commits: 
  - [Cambios en la seguridad necesarios para el soporte de las peticiones rest](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/393193c2d27e96c866d223055feb222ddd8fa195)
  - [Implementado el rest controller de la gráfica](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/fede9006a2786aac60c26ac1527cfd10ed0bdc72)
  - [Añadido el directorio DOT y offer DTO + implementación parcial del rest controller de offer](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/4266d87739f28db155053c1351d5dd24f5990680)
  - [Cambios en la inicialización de la base de datos](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/c0db575823d546f954b0c4b446d2ede93c6e1904)
  - [Documentation added, error messages added and fixed creating offer without a valid pool ID](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/960fc3a1930560410e5c48c6ae551db6f1983b6c)
#### Ficheros: 
  - [OfferDTO](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/DTO/OfferDTO.java)
  - [PieChartRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/ChartRestController.java)
  - [OfferRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/OfferRestController.java)
  - [LoginRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/LoginRestController.java)

     
### Pedro:
#### Tareas:
Implementar lo relacionado a usuarios (employers y lifeguards) en la API REST
#### Commits: 
  - Added UserRestController (to check if it works)
  - Setting passwords in the API works and improved put and post methods. Need to check if it works with images
  - Added Get Offers and OffersAccepted and also Delete one of your offers or offersAccepted for lifeguards. For employers get offers and delete offer permanently
  - Post, delete, put and get user images works. Added permissions to api user
  - Added documentation of UserRestController with Open API
#### Ficheros: 
  - LifeguardRestController
  - EmployerRestController
  - LifeguardDTO
  - EmployerDTO
  - LoginRestController

### Nico:
#### Tareas:
Implementar el RestController de Pool y sus dependencias
#### Commits:
  - Creation PoolDTO
  - Creation PoolRestController
  - Changes in PoolRestController to resemble code
  - Added service on PoolRestController
  - Added UriLocation in PoolRestController
#### Ficheros:
  - PoolRestController
  - MessageDTO
  - PoolDTO
  - Pool
  - Message

### Adrián:
#### Tareas:
Dockerizar la aplicación y desplegarla en el servidor de la Universidad. Post methods de las piscinas.
#### Commits:
  - App image
  - Compose the db and the web app
  - Shell file to push the image
  - Pool post method for posting an image
  - Fix problems with the jar files
#### Ficheros:
  - Dockerfile
  - docker-compose.yml
  - PoolControler.java
  - create-image.sh
     
## Usuarios de ejemplo (nombre de usuario y contraseña)
### administrador: admin, admin
### empleador: e1,e1
### socorrista: s1, s1


# Documentación de la API REST: 

Se puede consultar pulsando [aquí](https://raw.githack.com/CodeURJC-DAW-2023-24/webapp18/fase4/backend/h2os/api-docs/index.html)

# Instrucciones para desplegar la aplicación con docker
1. Instalar docker.
2. Ejecutarlo (mantener la aplicacion de escritorio abierta en windows, en linux se inicia automáticamente)
3. Clonamos el repositorio: ```git clone https://github.com/CodeURJC-DAW-2023-24/webapp18```
4. Vamos a la fase 3: ```git checkout fase3```
5. Vamos al directorio del docker: ```cd webapp18/docker```
6. Desplegamos nuestra aplicación: ```docker-compose up```
7.  Accedemos a ella en https://localhost:8443/

## Publicación de la imagen
1. Repetir los pasos del apartado anterior hasta el 4 (incluido).
2. Ejecutar el create_image.sh que hay en la carpeta docker (es posible que previamente se necesite darle al script los permisos necesarios).

Posteriormente a su publicación se podrá acceder a la imagen desde: https://hub.docker.com/r/mdelvalle2020/webapp18

# Despliegue en la máquina virtual
A continuación se muestra como levantar la aplicacón web desde la máquina virtual proporcionada:

1. Conectarnos a la vpn de la universidad. Nosotros hemos seguido los pasos indicados en el siguiente manual: https://www.urjc.es/principal-intranet/documentos/general/82-configuracion-vpn-urjc/file
2. Obtener la clave, el usuairo y la IP. En nuestro caso eran:     - Usuario: vmuser  - IP: 10.100.139.246   - Clave Privada: prAppWeb18.key
3. Tras descargar la clave navegamos al directorio de descarga y abrimos una cmd para ejecutar: ```ssh -i prAppWeb18.key vmuser@10.100.139.246```
4. Es posible que al ejecutar el paso 3 obtengamos un error relacionado con los permisos de la clave. En ese caso deberemos ejecutar: ```icacls prAppWeb11.key /inheritance:r``` y ```icacls prAppWeb11.key /grant:r "%USERNAME%":F``` Volvemos a realizar el paso 3.
5. Si el paso 3 ha ido bien esteremos dentro de la terminal de la máquina virtual. Clonamos la aplicación y vamos a la fase 3. ```git clone https://github.com/CodeURJC-DAW-2023-24/webapp18``` y ```git checkout fase3```
6. Vamos al directorio del docker ```cd webapp18/docker```
7. Desplegamos nuestra aplicación: ```docker-compose up```
8. Podremos acceder a ella desde: https://10.100.139.246:8443 


# Fase 4

## Preparación del entorno de desarrollo

Tras ejecutar el backend igual que en las fases anteriores se debe proceder a iniciar el frontend.

Para ello se necesita tener instalado Angular CLI. Lo podemos instalar con el siguiente comando: ``` npm install -g @angular/cli```

Si hay algún error es posible que no esté instalado Node.js. Se debe instalar desde la página oficial: https://nodejs.org/en

A continuación se debe ir al directorio del frontend:   ``` cd frontend/h2os```

Instalamos las dependencias necesarias: ``` npm intall --force```

Ejecutamos el frontend ``` npm start ```

Ahora podemos acceder a la página desde: http://localhost:4200/

## Diagrama de clases y templates de la SPA

![Diagram_Classes_and_Templates_SPA](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/ae5ae1fc-c9ee-446c-8765-94738129f264)

## Usuarios de ejemplo (nombre de usuario y contraseña)
### administrador: admin, admin
### empleador: e1,e1
### socorrista: s1, s1
## Participación de miembros

     
### Marcos:
#### Tareas:
Página oferta, lo relacionado a los mensajes de las piscinas, actualizar el docker para que funcione el frontend y SPA controller.
#### Commits: 
  - [Cambios en el docker añadidos a la rama de trabajo](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/39ef5e229ac8967919431a2e563677fb1517298b)
  - [Mensajes de la piscina implementados](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/efa1d48ea7e3f145ae6e5f2377f2fc435cef6551)
  - [Implementada la creación de oferta](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/c2af922a64e15c36657fab59bc1683fca6600a61)
  - [Implementado el editar oferta](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/1c442d6a444535a756be52aad17a00c80ac10ec0)
  - [Desarrollo de la oferta](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/a7f88e10d3e85b289195380abf0996605a0532cd).
#### Ficheros: 
  - [offer.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/offer/offer.component.ts)
  - [offer.create.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/offer/offer.create.component.ts)
  - [Dockerfile](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/docker/Dockerfile)
  - [offer.service.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/services/offer.service.ts).


## Link al video de youtube
