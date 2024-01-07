# Stage 1: Compile and Build angular codebase # Use official node image as the base image
FROM node:latest as build
WORKDIR /usr/local/app
COPY ./ /usr/local/app/
RUN npm install
RUN npm run build
# Stage 2: Serve app with nginx server # Use official nginx image as the base image
FROM nginx:latest
COPY --from=build /usr/local/app/dist/tic-tac-toe-aws /usr/share/nginx/html
EXPOSE 80
