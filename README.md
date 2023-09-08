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

Endpoints:

* GET ``/release/{id}`` - fetch release by id. UUID format of identifier is required.
* GET ``/releases`` - fetch releases by filter. You can search by any part of name and description, release date between two dates, by status name.
Paging is available as well, if you not enter it will return first 100 records that are matching other filter requirements.
* POST ``/release`` Save object into the database
* PUT  ``/release/{id}`` - Update record with that UUID.
* DELETE ``/release/{id}`` - Delete record with that UUID.

`NOTE: system is using soft-delete method of removing(hiding) data, so Records will not be lost during delete process but only inactive.`

System currently contains two entities:
- Release
- Release Status that is related to release

Release status is populated into system from csv file that is set in resource folder of the service module on application startup.
