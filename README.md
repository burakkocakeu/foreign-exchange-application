# Foreign Exchange Application

This is a web application that allows users to exchange currencies based on current exchange rates. The application uses the Open Exchange Rates API to retrieve exchange rates and allows users to save their exchange transactions.
## Getting Started

### Prerequisites

    Docker
    Docker Compose
    Node.js
    npm
    Java 17
    Angular CLI

### Running the Application

To run this application locally, you will need to have Node.js and MongoDB installed on your computer.

    Clone the repository: git clone https://github.com/[your-username]/foreign-exchange-application.git
    Navigate to the project root directory: cd foreign-exchange-application
    Build and start the Docker containers: docker-compose up -d
    Build and start the Spring Boot application using Maven: mvn clean spring-boot:run
    Install the Angular CLI globally: npm install -g @angular/cli
    Navigate to the Angular project directory: cd dashboard-ui
    Install the dependencies: npm install
    Start the Angular development server: ng serve
    Open your browser and navigate to http://localhost:4000 to access the application.

## Usage

To exchange currencies, follow these steps:

    Choose the currency you want to exchange from and to, as well as the amount.
    Select the date of the exchange.
    Click the "Add" button to add the exchange to the list.
    Once you've added all of your exchanges, click the "Save" button to save them.

To view your saved exchanges, click the "Exchanges" link in the navigation bar.
## Technologies Used

This application was built with the following technologies:

### Backend

    Java 17
    Spring Boot 3.0.3
    Spring Data MongoDB Reactive
    Spring Boot WebFlux
    Spring Boot Validation
    OpenFeign
    Resilience4j
    Lombok
    MapStruct
    MongoDB

### Frontend

    Angular 13
    Angular CLI 13
    Bootstrap 5
    TypeScript
    HTML
    CSS

### DevOps

    Docker
    Docker Compose

## Configuration

The application can be configured by modifying the application.yml file in the src/main/resources directory. The following properties can be configured:

    server.port: The port number that the application runs on. Default is 8080.
    spring.data.mongodb.uri: The MongoDB connection URI. Default is mongodb://root:example@localhost:27017/exchange?authSource=admin&authMechanism=SCRAM-SHA-1.
    foreign-exchange.service.host: The third-party API host. Default is https://api.apilayer.com.
    foreign-exchange.service.api-key: The API key for the third-party API. Default is a free account key that allows up to 250 requests per month.

## Contributing

If you find any issues or have suggestions for improvement, please feel free to submit an issue or a pull request.
## License

This project is licensed under the MIT License. See the LICENSE file for details.