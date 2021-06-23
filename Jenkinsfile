pipeline {
    agent any
    stages {
        stage ('Build back-end') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }
        stage ('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }         
    }
}