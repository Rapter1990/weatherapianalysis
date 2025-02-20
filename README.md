# Case Study - Weather Api Analysis Api

<p align="center">
    <img src="screenshots/main_screenshot.png" alt="Main Information" width="800" height="700">
</p>

### 📖 Information

<ul style="list-style-type:disc">
  <li>
    This project provides a Spring Boot API for Air Quality Management, allowing users to fetch air quality data based on request parameters. The API supports logging for request tracking.
  </li>
  <li>
    <b>Air Quality Management:</b>
    <ul>
      <li>
        Retrieve real-time air quality data based on latitude, longitude, and date.
      </li>
    </ul>
  </li>
  <li>
    <b>Logging:</b>
    <ul>
      <li>
        <b>Custom Logging Aspect:</b> Logs details of REST controller method calls and
        exceptions, including HTTP request and response details.
      </li>
    </ul>
  </li>
</ul>


### Explore Rest APIs

Endpoints Summary
<table style="width:100%;">
    <tr>
        <th>Method</th>
        <th>Url</th>
        <th>Description</th>
        <th>Request Body</th>
        <th>Path Variable</th>
        <th>Response</th>
    </tr>
    <tr>
        <td>POST</td>
        <td>/api/v1/airquality</td>
        <td>Retrieve air quality data</td>
        <td>AirQualityRequest</td>
        <td>None</td>
        <td>CustomResponse&lt;CustomAirQualityResponse&gt;</td>
    </tr>
</table>



### Technologies

---
- Java 21
- Spring Boot 3.0
- Restful API
- Mapstruct
- Open Api (Swagger)
- Maven
- Junit5
- Mockito
- Integration Tests
- Docker
- Docker Compose
- CI/CD (Github Actions - Jenkins)
- Postman
- TestContainer
- Postgres
- Prometheus
- Grafana
- Kubernetes
- JaCoCo (Test Report)
- Sonarqube

### Postman

```
Import postman collection under postman_collection folder
```


### Prerequisites

#### Define Variable in .env file

```
WEATHER_DB_IP=localhost
WEATHER_DB_PORT=5432
POSTGRES_USER=postgres
POSTGRES_PASSWORD=111111
OPEN_WEATHER_API_KEY={OPEN_WEATHER_API_KEY}
```

### Open Api (Swagger)

```
http://localhost:1100/swagger-ui/index.html
```

---

### JaCoCo (Test Report)

After the command named `mvn clean install` completes, the JaCoCo report will be available at:
```
target/site/jacoco/index.html
```
Navigate to the `target/site/jacoco/` directory.

Open the `index.html` file in your browser to view the detailed coverage report.

---

### Maven, Docker and Kubernetes Running Process


### Maven Run
To build and run the application with `Maven`, please follow the directions shown below;

```sh
$ cd weatherapianalyis
$ mvn clean install
$ mvn spring-boot:run
```

---

### Docker Run
The application can be built and run by the `Docker` engine. The `Dockerfile` has multistage build, so you do not need to build and run separately.

Please follow directions shown below in order to build and run the application with Docker Compose file;

```sh
$ cd weatherapianalyis
$ docker-compose up -d
```

If you change anything in the project and run it on Docker, you can also use this command shown below

```sh
$ cd weatherapianalyis
$ docker-compose up --build
```

To monitor the application, you can use the following tools:

- **Prometheus**:  
  Open in your browser at [http://localhost:9090](http://localhost:9090)  
  Prometheus collects and stores application metrics.

- **Grafana**:  
  Open in your browser at [http://localhost:3000](http://localhost:3000)  
  Grafana provides a dashboard for visualizing the metrics.  
  **Default credentials**:
    - Username: `admin`
    - Password: `admin`

- Define prometheus data source url, use this link shown below

```
http://prometheus:9090
```

---


### Kubernetes Run
To build and run the application with `Maven`, please follow the directions shown below;

- Start Minikube

```sh
$ minikube start
```

- Open Minikube Dashboard

```sh
$ minikube dashboard
```

- To deploy the application on Kubernetes, apply the Kubernetes configuration file underneath k8s folder

```sh
$ kubectl apply -f k8s
```

- To open Prometheus, click tunnel url link provided by the command shown below to reach out Prometheus

```sh
minikube service prometheus-service
```


- To open Grafana, click tunnel url link provided by the command shown below to reach out Prometheus

```sh
minikube service grafana-service
```

- Define prometheus data source url, use this link shown below

```
http://prometheus-service.default.svc.cluster.local:9090
```

- To open Sonarqube, click tunnel url link provided by the command shown below to reach out Sonarqube

```sh
minikube service sonarqube
```


---
### Docker Image Location

```
https://hub.docker.com/repository/docker/noyandocker/jenkins-jenkins/general
https://hub.docker.com/repository/docker/noyandocker/weatherapianalysis/general
```

### Jenkins

- Go to `jenkins` folder
- Run `docker-compose up -d`
- Open Jenkins in the browser via `localhost:8080`
- Go to pipeline named `weatherapianalysis`
- Run Pipeline
- Show `Pipeline Step` to verify if it succeeded or failed


### Screenshots

<details>
<summary>Click here to show the screenshots of project</summary>
    <p> Figure 1 </p>
    <img src ="screenshots/screenshot_1.PNG">
    <p> Figure 2 </p>
    <img src ="screenshots/screenshot_2.PNG">
    <p> Figure 3 </p>
    <img src ="screenshots/screenshot_3.PNG">
    <p> Figure 4 </p>
    <img src ="screenshots/screenshot_4.PNG">
    <p> Figure 5 </p>
    <img src ="screenshots/screenshot_5.PNG">
    <p> Figure 6 </p>
    <img src ="screenshots/screenshot_6.PNG">
    <p> Figure 7 </p>
    <img src ="screenshots/swagger.PNG">
    <p> Figure 8 </p>
    <img src ="screenshots/jacoco.PNG">
    <p> Figure 9 </p>
    <img src ="screenshots/prometheus_docker.PNG">
    <p> Figure 10 </p>
    <img src ="screenshots/grafana_docker_1.PNG">
    <p> Figure 11 </p>
    <img src ="screenshots/grafana_docker_2.PNG">
    <p> Figure 12 </p>
    <img src ="screenshots/grafana_docker_3.PNG">
    <p> Figure 13 </p>
    <img src ="screenshots/grafana_docker_4.PNG">
    <p> Figure 14 </p>
    <img src ="screenshots/grafana_docker_5.PNG">
    <p> Figure 15 </p>
    <img src ="screenshots/docker_sonarqube.PNG">
    <p> Figure 16 </p>
    <img src ="screenshots/kubernetes_1.PNG">
    <p> Figure 17 </p>
    <img src ="screenshots/kubernetes_2.PNG">
    <p> Figure 18 </p>
    <img src ="screenshots/kubernetes_3.PNG">
    <p> Figure 19 </p>
    <img src ="screenshots/kubernetes_prometheus.PNG">
    <p> Figure 20 </p>
    <img src ="screenshots/kubernetes_grafana_1.PNG">
    <p> Figure 21 </p>
    <img src ="screenshots/kubernetes_grafana_2.PNG">
    <p> Figure 22 </p>
    <img src ="screenshots/kubernetes_grafana_3.PNG">
    <p> Figure 23 </p>
    <img src ="screenshots/kubernetes_grafana_4.PNG">
    <p> Figure 24 </p>
    <img src ="screenshots/kubernetes_grafana_5.PNG">
    <p> Figure 25 </p>
    <img src ="screenshots/kubernetes_sonarqube_1.PNG">
    <p> Figure 26 </p>
    <img src ="screenshots/kubernetes_sonarqube_2.PNG">
    <p> Figure 27 </p>
    <img src ="screenshots/kubernetes_sonarqube_3.PNG">
    <p> Figure 28 </p>
    <img src ="screenshots/kubernetes_sonarqube_4.PNG">
    <p> Figure 29 </p>
    <img src ="screenshots/kubernetes_sonarqube_5.PNG">
    <p> Figure 30 </p>
    <img src ="screenshots/kubernetes_sonarqube_6.PNG">
    <p> Figure 31 </p>
    <img src ="screenshots/kubernetes_sonarqube_7.PNG">
    <p> Figure 32 </p>
    <img src ="screenshots/kubernetes_sonarqube_8.PNG">
    <p> Figure 33 </p>
    <img src ="screenshots/jenkins_1.PNG">
    <p> Figure 34 </p>
    <img src ="screenshots/jenkins_2.PNG">
    <p> Figure 35 </p>
    <img src ="screenshots/jenkins_3.PNG">
</details>


### Contributors

- [Sercan Noyan Germiyanoğlu](https://github.com/Rapter1990)