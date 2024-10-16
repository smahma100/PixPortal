### PixPortal

PixPortal is a dynamic, cloud-powered photo management and sharing platform designed for modern users who value speed, scalability, and security. Suitable for personal and enterprise-level needs, PixPortal enables seamless photo organization and sharing.

### Features

- **Effortless Uploads**: Instantly upload and manage photos with drag-and-drop ease.
- **Smart Albums**: Auto-categorize photos using tags, dates, and locations for a streamlined gallery.
- **Secure Cloud Storage**: End-to-end encryption and cloud storage powered by AWS.
- **Collaborative Sharing**: Share photos or albums with customizable permissions.
- **Scalable Architecture**: Built for growth, ensuring a fast and reliable experience.

### Microservices Architecture

PixPortal follows a microservices-based architecture, offering flexibility and scalability:

- **PhotoAppDiscoveryService**: Manages service discovery, ensuring seamless microservices communication.
- **ConfigServer**: Centralized configuration management for all microservices.
- **AccountManagement**: Manages user registration, authentication, and account settings.
- **ApiGateway**: Provides a unified entry point for API requests, enhancing security and load balancing.
- **PhotoAppApiUsers**: Handles user-related actions, including photo uploads and management.
- **AlbumsService**: Manages photo albums, including categorization and metadata management.
- **NotificationService**: Sends real-time updates and notifications to users.


### Tech Stack

- **Backend**: Java, Spring Boot, Spring Cloud (Eureka, API Gateway, Config Server)
- **Frontend**: React.js (future development)
- **Messaging**: RabbitMQ for asynchronous communication between microservices.
- **Search and Analytics**: Elasticsearch for fast search capabilities; Logstash for log processing; Kibana for visualizing logs and metrics.
- **Database**: MySQL (RDS), AWS S3 for photo storage
- **Authentication**: JWT, OAuth2
- **Cloud**: AWS (S3, EC2, CloudFront, SQS, SNS)
- **CI/CD**: Jenkins, Docker, Kubernetes
- **Monitoring and Tracing**: Zipkin, CloudWatch, and Kibana for monitoring logs, tracing, and metrics.


### Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/smahma100/PixPortal.git
   ```
2. **Build each microservice**:
   ```bash
   cd PhotoAppApiUsers
   mvn clean install
   ```
3. **Run each service**:
   ```bash
   mvn spring-boot:run
   ```

### Contributing

Contributions are welcome! Fork the repository and submit a pull request.

### License

This project, PixPortal, is owned and maintained by Safar Mahmadov. Permission is granted for free usage.
