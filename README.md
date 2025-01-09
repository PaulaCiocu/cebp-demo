## Steps to Run the Backend (Spring Application)

1. **Build the Docker Image for the Backend:**

   ```bash
   docker build -t spring-app .
   ```

2. **Run the Docker Container for the Backend:**

   ```bash
   docker run -d -p 8000:8000 --name spring-app-container spring-app
   ```

3. **Access the Swagger API Documentation:**
   
   Web browser URL: [http://localhost:8000/swagger-ui/index.html](http://localhost:8000/swagger-ui/index.html)

---

## Steps to Run the Frontend Application

1. **Build the Docker Image for the Frontend:**

   ```bash
   docker build -t frontend-app .
   ```

2. **Run the Docker Container for the Frontend:**

   ```bash
   docker run -d -p 3000:3000 --name frontend-app-container frontend-app
   ```

3. **Access the Frontend Application:**

   Web browser URL: [http://localhost:3000](http://localhost:3000)
