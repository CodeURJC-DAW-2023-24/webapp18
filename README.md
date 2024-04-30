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
#### Commits:
#### Ficheros:
     
## Usuarios de ejemplo (nombre de usuario y contraseña)
### administrador: admin, admin
### empleador: e1,e1
### socorrista: s1, s1


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
   
