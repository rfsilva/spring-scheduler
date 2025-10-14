#!/bin/bash
echo "Gerando classes a partir do XSD..."
./mvnw jaxb2:xjc
echo "Compilando o projeto..."
./mvnw clean package -DskipTests
echo "Compilação concluída!"