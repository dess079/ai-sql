FROM node:24-alpine AS build

WORKDIR /workspace

COPY --from=ai_sql_lib . /workspace/libs/ai-sql-lib
COPY --from=shared_components . /workspace/libs/shared-components
COPY . /workspace/ai-sql/frontend

WORKDIR /workspace/libs/shared-components

RUN npm install

RUN npm run build

WORKDIR /workspace/libs/ai-sql-lib

RUN npm install

RUN npm run build

WORKDIR /workspace/ai-sql/frontend

RUN npm install

ARG VITE_BACKEND_URL=http://localhost:4001
ENV VITE_BACKEND_URL=$VITE_BACKEND_URL

RUN npm run build

FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/ai-sql/frontend/dist /usr/share/nginx/html

EXPOSE 80
