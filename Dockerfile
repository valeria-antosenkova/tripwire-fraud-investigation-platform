FROM ubuntu:latest
LABEL authors="v-an"

ENTRYPOINT ["top", "-b"]