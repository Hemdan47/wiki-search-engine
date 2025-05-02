
# Wiki Search Engine
![Java](https://img.shields.io/badge/Java-17-brightgreen)  ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-green)  ![Maven](https://img.shields.io/badge/Maven-3.x-blue)  ![Jsoup](https://img.shields.io/badge/Jsoup-1.18.3-orange)  ![OpenNLP](https://img.shields.io/badge/OpenNLP-2.3.1-lightblue)  ![HTML](https://img.shields.io/badge/HTML-5-red)  ![CSS](https://img.shields.io/badge/CSS-3-blue)  ![JavaScript](https://img.shields.io/badge/JavaScript-ES6-yellow)
## 🚀 Overview

**Wiki Search Engine** is a powerful search application designed for efficiently indexing and searching Wikipedia content. This application utilizes modern Java technologies along with natural language processing to provide fast, accurate, and relevant search results for wiki content.

## ✨ Features

- **Advanced Search Functionality** – Quickly search and navigate through wiki content with precision  
- **Tokenization & Stemming** – Using Apache OpenNLP to tokenize and stem terms for more accurate search matching  
- **RESTful API** – Spring Boot backend providing robust search endpoints  
- **HTML Parsing** – Jsoup integration for effective content extraction from wiki pages  
- **Front-End Interface** – Desktop-focused UI built with HTML, CSS, and JavaScript, featuring a search input box and a paginated results list each result links directly to the original Wikipedia article 
- **Customizable Crawling** – Define custom seed URLs and crawl limits via command line 

## 📂 Project Structure

```plaintext
wiki-search-engine/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ir/wikisearchengine/
│   │   │       ├── controller/      # REST controllers to handle API requests
│   │   │       ├── crawler/         # Logic for crawling and fetching wiki pages
│   │   │       ├── dto/             # Data transfer objects used for request and response models
│   │   │       ├── indexing/        # Core logic for indexing and storing wiki data
│   │   │       ├── model/           # Data models representing wiki content and related entities
│   │   │       ├── Service/         # Business logic and service layer components
│   │   │       ├── startup/         # Initialization logic that runs on application startup
│   │   │       └── WikiSearchEngineApplication.java  # Main application class and entry point
│   │   └── resources/
│   │       ├── static/              # Static web resources (CSS, JS)
│   │       ├── templates/           # HTML templates for rendering views
│   │       └── application.properties # Application configuration
│   └── test/
│       └── java/
│           └── com/ir/wikisearchengine/
│               └── WikiSearchEngineApplicationTests.java  # Unit and integration tests
├── target/                          # Compiled output and packaged artifacts
├── .gitignore                       # Git ignore file
├── pom.xml                          # Maven dependencies and project configuration
└── README.md                        # Project documentation
```

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.4.5  
- **NLP**: Apache OpenNLP 2.3.1  
- **HTML Parsing**: JSoup 1.18.3  
- **Development Tools**: Lombok  
- **Build Tool**: Maven  

## ⚡ Installation and Setup

### Prerequisites

- Java 17 or higher  
- Maven 3.x  

### Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Hemdan47/wiki-search-engine.git
   cd wiki-search-engine
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application with optional crawl parameters**:

   You can pass seed URLs and the max number of pages to crawl via command line arguments. If no arguments are given, the application defaults to:
   - Seed: `https://en.wikipedia.org/wiki/Main_Page`
   - Max pages: `100`

   **Example usage**:
   ```bash
   ./mvnw spring-boot:run '-Dspring-boot.run.arguments=https://en.wikipedia.org/wiki/List_of_pharaohs https://en.wikipedia.org/wiki/Pharaoh 10'
   ```

4. **Access the application**:
   - Open a web browser and navigate to `http://localhost:8080`

## 🔧 How It Works

### 🕸️ Crawling

The application begins by reading a set of **seed URLs** and a **maximum number of pages** from the command-line arguments. If no arguments are provided, it defaults to crawling from `https://en.wikipedia.org/wiki/Main_Page` and limits itself to 100 pages.

- A queue (`LinkedList<String>`) is used to manage URLs to be visited.
- A `HashSet<String>` tracks visited URLs to avoid duplicates.
- Only internal Wikipedia links are followed.
- Jsoup is used to fetch and parse HTML content.

---

### 📚 Indexing

An **inverted index** is created to map each word to the documents in which it appears.

Steps:
- Extract and normalize text: tokenize using regex (split("\\W+")), lowercase, and apply OpenNLP stemming.
- Build a `Map<String, List<Posting>>` where each `Posting` contains:
  - `documentId`
  - `term frequency (TF)`

This allows fast lookup of terms and efficient search processing.

---

### 📐 TF-IDF Calculation

Each document is converted into a TF-IDF vector.

- **TF**:  
  ```java
  weight = 1 + log10(tf)
  ```

- **IDF**:  
  ```java
  idf = log10(N / df)
  ```

- **TF-IDF**:  
  ```java
  tfIdf = tfWeight * idf
  ```

Where `N` is the total number of documents and `df` is how many documents contain the term.

---


### 🧠 Query Processing and Cosine Similarity

- User inputs a query string.
- Query is tokenized and TF-IDF weighted like documents.
- Cosine similarity is calculated:
  ```java
  cosineSimilarity = dot(d, q) / (norm(d) * norm(q))
  ```
- Results are **ranked by similarity score** and **returned in pages** using a built-in pagination system.
---

### 📖 Usage

#### Web Interface
- Built using **HTML, CSS, and JavaScript** for a clean experience.
- Enter your search query in the search box.
- Results are paginated and ranked by relevances.
- Click a result to view the full wiki content.

#### REST API
- `GET /search?query={searchTerm}&page={pageNumber}` – Perform paginated search across indexed documents.

---

## 📝 License

This project is open source and available under the [MIT License](LICENSE).
