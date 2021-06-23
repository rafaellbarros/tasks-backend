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
        stage ('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    sh "${scannerHome}/bin/sonar-scanner -e sonar.projectKey=DeployBack sonar.host.url=http://localhost:9000 -Dsonar.login=554334bd70fc48ab296ecdc88f345fe34d767d67 sonar.java.binaries=target sonar.coverage.exclusions=**/.mvn/**, **/src/test/**, **/model/**, **Application.java"
                }
            }
        }                   
    }
}

