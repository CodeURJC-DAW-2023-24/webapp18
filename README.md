# webapp18
# Nombre: H2OS
## Integrantes: 
  - Pedro Cristino Moreno - PedroCristino2020 - p.cristino.2020@alumnos.urjc.es
  - Jorge Sánchez Díaz - RisiGT - j.sanchezdi.2020@alumnos.ujrc.es 
  - Marcos del Valle Hontanar - MarcoDelValleH - m.delvalle.2020@alumnos.urjc.es




## Diagrama de los clases y templates

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e7f730fc-fe3f-4e41-bacd-3d00ab52ede9)



## Diagrama de entidades

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e28fbed3-cf45-4144-907c-b3614b940f9c)


## Participación de miembros (FASE 3)

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
  - [Added UserRestController (to check if it works)](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/489c8d52a8e5a4e52be2a6fe04bdb741c536129f)
  - [Setting passwords in the API works and improved put and post methods. Need to check if it works with images](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/36d2a3b67a6147f978c0ae8a873e6db294176c19)
  - [Added Get Offers and OffersAccepted and also Delete one of your offers or offersAccepted for lifeguards. For employers get offers and delete offer permanently](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/4efcd6f9dc805fd1708282147e342d8dd025589f)
  - [Post, delete, put and get user images works. Added permissions to api user](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/cb3ad53084086d6639f0ce157cb9e528920328ba)
  - [Added documentation of UserRestController with Open API](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/ee30fa4730bade866f0794d72a4f3224d2b4f5ae)
#### Ficheros: 
  - [LifeguardRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/LifeguardRestController.java)
  - [EmployerRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/EmployerRestController.java)
  - [LifeguardDTO](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/DTO/LifeguardDTO.java)
  - [EmployerDTO](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/DTO/EmployerDTO.java)
  - [LoginRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/LoginRestController.java)

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


## Participación de miembros (FASE 4)

### Jorge:
#### Tareas:
#### Commits: 
#### Ficheros: 
     
### Marcos:
#### Tareas: 
#### Commits: 
#### Ficheros: 

     
### Pedro:
#### Tareas:
Implementar lo relacionado a usuarios (employers y lifeguards) en el frontend
#### Commits: 
  - [Initialized Angular web, doesnt work](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/9faa13b93e5f391c129c37e8c1b4086ccbd2dd10)
  - [implemented user-detail and user-form. I haven't tried if it works](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/b930ae386113e419f474c9f46ab9266ede285a7d)
  - [profile and user_form works correctly (photo not implemented yet). You need to reload to update header](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/f30ec6584c99f1fd0fac56f1d4a1656b8ac16a4f)
  - [upload photo completed and form improved](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/10b838f17f88c1e1d4c607748330438c2617aa9d)
  - [implemented pool form](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/00aeb8a1524f774f5d92e80a637bf2013c85d1c5)
#### Ficheros: 
  - user.service.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/services/user.service.ts)
  - user-detail.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/user/user-detail.component.ts)
  - user-form.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/user/user-form.component.ts)
  - user-login.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/user/user-login.component.ts)
  - pool-form.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/pools/pool-form.component.ts)

#### Tareas:
Sección de estadísticas implementando un componente de Google Charts
#### Commits:
  - Stadistics component
  - Service for getting the pieChart
  - Google chart connected with pieChart component
  - pieChart component font size of the tooltip resized
#### Ficheros:
  - stadistics.component.html
  - stadistics.component.css
  - stadistics.component.ts
  - app.module.ts
  - stadistics.service.ts

## Diagrama de clases y templates de la SPA:


# Documentación de la API REST: 

Se puede consultar pulsando [aquí](https://raw.githack.com/CodeURJC-DAW-2023-24/webapp18/fase4/backend/h2os/api-docs/index.html)

# Instrucciones para desplegar la aplicación con docker
1. Instalar docker.
2. Ejecutarlo (mantener la aplicacion de escritorio abierta en windows, en linux see inicia automáticamente)
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
   


   


