# Use node for build
FROM node:18-alpine AS builder

WORKDIR /app

COPY . .

ENV NODE_ENV=development
RUN npm install
RUN npm run build

# Use nginx to serve
FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html

# Optional: remove default nginx conf and expose port
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 8082

CMD ["nginx", "-g", "daemon off;"]
