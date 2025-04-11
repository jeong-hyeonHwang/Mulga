# Use an official nginx image from Docker Hub
FROM nginx:alpine

# Copy the HTML website files into the nginx container
COPY . /usr/share/nginx/html

# Expose port 80 to be able to access the site
EXPOSE 80