
# PixPortal

**PixPortal** is a dynamic, cloud-powered photo management and sharing platform designed for modern users who value speed, scalability, and security. Whether for personal use or enterprise-level needs, PixPortal enables seamless photo organization and sharing.

## Features
- **Effortless Uploads**: Instantly upload and manage photos with drag-and-drop ease.
- **Smart Albums**: Auto-categorize photos with tags, dates, and locations for a streamlined gallery.
- **Secure Cloud Storage**: Store your memories safely with end-to-end encryption and cloud storage powered by AWS.
- **Collaborative Sharing**: Share photos or albums with customizable permissions.
- **Scalable Architecture**: Optimized for growth, ensuring a fast and reliable experience.

## Microservices Architecture
PixPortal follows a microservices-based architecture, built for flexibility and scalability:

1. **PhotoAppDiscoveryService**: Handles service discovery, ensuring seamless communication between microservices.
2. **AccountManagement**: Manages user registration, authentication, and account settings.
3. **ApiGateway**: Provides a unified entry point for all API requests, enhancing security and load balancing.
4. **PhotoAppApiUsers**: Manages user-related actions, including uploading and managing photos.

## Tech Stack
- **Backend**: Java, Spring Boot, Spring Cloud (Eureka, API Gateway)
- **Frontend**: React.js (future development)
- **Database**: MySQL (RDS), S3 for photo storage
- **Authentication**: JWT, OAuth2
- **Cloud**: AWS (S3, EC2, CloudFront)
- **CI/CD**: Jenkins, Docker, Kubernetes

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/smahma100/PixPortal.git
   ```
2. Navigate to each microservice directory and build using Maven:
   ```bash
   cd PhotoAppApiUsers
   mvn clean install
   ```
3. Run each service:
   ```bash
   mvn spring-boot:run
   ```

## Contributing
Contributions are welcome! Feel free to fork the repository and submit a pull request.

## License
This project, PixPortal, is owned and maintained by Safar Mahmadov.
Permission is granted for anyone to use it for free.
