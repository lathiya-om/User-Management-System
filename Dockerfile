# STEP 1: Build React App using Node.js
FROM node:18 AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# STEP 2: Serve React app using Nginx
FROM nginx:alpine

# Remove default Nginx page
RUN rm -rf /usr/share/nginx/html/*

# Copy React build output
COPY --from=build /app/build /usr/share/nginx/html

# Expose port 80 in container
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
