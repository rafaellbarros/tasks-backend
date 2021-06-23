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
                    sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey='DeployBack' -Dsonar.host.url='http://localhost:9000' -Dsonar.login='554334bd70fc48ab296ecdc88f345fe34d767d67' -Dsonar.java.binaries='target' -Dsonar.coverage.exclusions='**/.mvn/**,**/src/test/**, **/model/**,**Application.java'"
                }
            }
        }
        stage ('Quality Gate') {
            steps {
                sleep(5)
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage ('Deploy back-end') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }
        stage ('API Test') {
            steps {
                git branch: 'main', credentialsId: 'github_login', url: 'https://github.com/rafaellbarros/api-test'
                sh 'mvn test'
            }
        }                      
    }
}

