FROM node:18-alpine

# Set working directory
WORKDIR /app

# Copy package.json and install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy application code
COPY . .

# Build the application
RUN npm run build

# Install a lightweight static file server
RUN npm install -g serve

# Expose port 3000
EXPOSE 3000

# Command to serve the build directory
CMD ["serve", "-s", "build", "-l", "3000"]
