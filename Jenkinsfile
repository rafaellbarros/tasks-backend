pipeline {
    agent any
    stages {
        stage ('Build back-end') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }     
    }
}