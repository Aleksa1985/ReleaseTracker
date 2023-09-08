# ReleaseTracker
Api for tracing releases.

Specification:
- java 17
- mysql or h2 for development purpose
- dockerized

Application contains 5 endpoints that are visible over open api UI.
Application is running in project root execution `docker-compose build` and afther that process is finished 
run `docker-compose up`.
After Spring application is up and ready, you can access UI on browser on URL:
`http://localhost:8080/swagger-ui/index.html#`

This project can be run without Docker, just need switch to dev env to application.yml. Database will be h2 in memory.
Example:
```
spring:
    profiles:
        active: dev
```
Endpoints:

* GET ``/release/{id}`` - fetch release by id. UUID format of identifier is required.
* GET ``/releases`` - fetch releases by filter. You can search by any part of name and description, release date between two dates, by status name.
Paging is available as well, if you not enter it will return first 100 records that are matching other filter requirements.
* POST ``/release`` Save object into the database
* PUT  ``/release/{id}`` - Update record with that UUID.
* DELETE ``/release/{id}`` - Delete record with that UUID.

_NOTE: system is using soft-delete method of removing(hiding) data, so Records will not be lost during delete process but only inactive._

System currently contains two entities:
- Release
- Release Status that is related to release

Release status is populated into system from csv file that is set in resource folder of the service module on application startup.
Not allowed duplicates in status names.

Project structure:
 
 * module model - it is used to store dto models for inbound objects. Validation on object is implements on the model level using bean validation.
It is separated in separate module because it can be wired as dependency to some other project that need these models if needed.
 * service model - module that contains entire business logic (controller, mapper, service, repository layers)
   
_NOTE: Inbound traffic validation is done on bean level and for some complex usecase it is done on validation component ReleaseValidatorImpl._  


**POSSIBLE IMPROVEMENTS:**
* implement some inbound and outbound logging linked with some traceID
* Make that some properties that are hardcoded in code, should be parameterized and fetched from some external source(configuration file, some other file etc...)
* Integration tests to test entire data flow.