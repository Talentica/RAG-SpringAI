# OpenAI Application

This is a Spring Boot application that integrates with OpenAI and Redis for document processing and chat functionalities.

## Prerequisites

- Java 17
- Maven
- Redis-Search

## Getting Started

### Clone the repository

```
git clone https://github.com/your-repo/openAiApplication.git
cd openAiApplication
```
### Configure Redis
Ensure Redis is running on your local machine or update the application.yml file with your Redis configuration.  
Set OpenAI API Key
Set the OpenAI API key in your environment:

```
export OPEN_AI_KEY=your_openai_api_key
```

```
mvn clean install
mvn spring-boot:run
```

### Endpoints

#### Load Documents
**Note - The documents are loaded from the `resources/guides` folder in the project to redis**

- **URL:** `/spring/load`
- **Method:** `POST`
- **Response:** Confirmation message after loading documents.


#### Chat Generation
- **URL:** `/spring/ai`
- **Method:** `GET`
- **Parameters:**
    - `userInput` (String): The input query from the user.
- **Response:** The generated response from the OpenAI model.

