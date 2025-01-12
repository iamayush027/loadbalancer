<<<<<<< HEAD
# loadbalancer
=======

Setting Up the Load Balancer
To set up the project, you need to have the following:

Java 11 or higher installed.
Gradle to build the project.

Clone the Repository

First, clone the repository to your local machine:
git clone <repository-url>
cd <project-directory>


Build the Project
Using Gradle:

./gradlew clean build

This will compile the project and generate the .jar file in the build/libs/ directory.

Running the Load Balancer
After building the project, you can run the load balancer application on a specified port.

Command to run the Load Balancer:
You can start the load balancer with the following command:

java -jar build/libs/your-loadbalancer-app.jar --server.port=<port>
Replace <port> with the port number you wish to use for the load balancer (e.g., 8080).

Example:
bash
Copy code
java -jar build/libs/your-loadbalancer-app.jar --server.port=8080
By default, the load balancer will use the Round-Robin algorithm. You can switch to Random Selection by specifying the algorithm in the command line:

bash
Copy code
java -jar build/libs/your-loadbalancer-app.jar --server.port=8080 --loadbalancer.algorithm=RANDOM
Adding/Removing Backend Servers
The load balancer allows you to dynamically add and remove backend servers, while one dummy will be added in the application properties as initial configuration

Adding Backend Servers
You can add backend servers by configuring them in the load balancer or via API calls.

Example (API Call to Add Backend Server):

Use the following API endpoint to add a new backend server:
http
Copy code
POST /backend-server/<url>
This will add a backend server at the specified URL.

Removing Backend Servers
Similarly, you can remove a backend server by calling the appropriate API endpoint:

http
Copy code
DELETE /backend-server/remove/<url>
Request Body:

This will remove the backend server.

Configuration-based Setup
In addition to API calls, you can configure backend servers directly in the application configuration file (e.g., application.yml or application.properties).

Testing with Mock Servers
To test the load balancer, you can use a mock server. A mock CRUD application is provided in the repository that runs on multiple ports. This will allow you to simulate requests and test the load balancing functionality.

Mock Server Setup
Mock CRUD Application:
The mock CRUD application is a simple RESTful application that simulates backend server functionality. It is used as a placeholder for real backend servers.
Run the Mock Servers:
A script (run-app.sh) is provided to run the mock server on 4 different ports (8081, 8082, 8083, 8084).
Running the Mock Servers
To start 4 instances of the mock server, run the following script:


./run-app.sh
This will start the mock servers on ports:

http://localhost:8081
http://localhost:8082
http://localhost:8083
http://localhost:8084
These mock servers can be used to simulate backend servers for testing the load balancer's behavior.

APIs
Here are the placeholder APIs that the load balancer exposes for interacting with the backend servers:

Add Backend Server
URL: /api/backend-server/<url>
Method: POST


Extending the Load Balancer
Adding New Load Balancing Algorithms:

The load balancing algorithm is designed to be extensible. You can implement a new algorithm (e.g., Least Connections) by creating a new class implementing the LoadBalancingAlgorithm interface and configuring it in the LoadBalancerFactory.
Dynamic Backend Server Management:

In the current implementation, backend servers can be added and removed using configuration or APIs. For future scalability, you can integrate dynamic discovery services (e.g., using Eureka or Consul).
Health Check Implementation (Optional)
You can extend the load balancer with a health-check mechanism. The load balancer will periodically check the health of each backend server (using HTTP ping or other methods). If a backend server fails the health check, it will be temporarily removed from the load balancing pool until it becomes healthy again.

This can be done by implementing a HealthCheckService and scheduling health checks using a task scheduler.
>>>>>>> 1f0cac9 (Initial commit)
