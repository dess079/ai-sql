FROM node:24-alpine AS build

# Arborescence miroir de l'hôte : /app = /Users/sd/dev
#   /app/ai-sql/frontend + /app/libs/{shared-components,ai-sql-lib}
# Les chemins RELATIFS du lockfile (`file:../../libs/...`) restent valides.
#
# NOTE 1 : le .dockerignore de shared-components exclut dist/ → il faut
# construire la lib dans le conteneur (npx rollup -c).
# NOTE 2 : le contexte ai-sql-lib embarque le node_modules pnpm de l'hôte ;
# npm ci du frontend résout `file:../..` via CE node_modules périmé → purge.
COPY --from=ai_sql_lib . /app/libs/ai-sql-lib
COPY --from=shared_components . /app/libs/shared-components
COPY . /app/ai-sql/frontend

RUN rm -rf /app/libs/ai-sql-lib/node_modules

WORKDIR /app/libs/shared-components

RUN npm ci --ignore-scripts --no-audit --no-fund \
    && npx rollup -c

WORKDIR /app/ai-sql/frontend

RUN npm ci --ignore-scripts --no-audit --no-fund

ARG VITE_BACKEND_URL=http://localhost:4001
ENV VITE_BACKEND_URL=$VITE_BACKEND_URL
# Active les alias absolus de libs dans vite.config.ts (arbo /app figée)
ENV AI_SQL_CONTAINER_BUILD=1

RUN npm run build

FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/ai-sql/frontend/dist /usr/share/nginx/html

EXPOSE 80