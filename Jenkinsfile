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
                def scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey='DeployBack' -Dsonar.host.url='http://localhost:9000' -Dsonar.login='554334bd70fc48ab296ecdc88f345fe34d767d67' -Dsonar.java.binaries='target' -Dsonar.coverage.exclusions='**/.mvn/**,**/src/test/**, **/model/**, **Application**'"
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
                dir('api-test') {
                    git branch: 'main', credentialsId: 'github_login', url: 'https://github.com/rafaellbarros/api-test'
                    sh 'mvn test'
                }
            }
        }
        stage ('Deploy front-end') {
            steps {
                 dir('front-end') {
                    git branch: 'master', credentialsId: 'github_login', url: 'https://github.com/rafaellbarros/tasks-frontend'
                    sh 'mvn clean package -DskipTests=true'
                    deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks', war: 'target/tasks.war'
                 }
            }
        }
        stage ('Functional Test') {
            steps {
                dir('functional-test') {
                    git branch: 'main', credentialsId: 'github_login', url: 'https://github.com/rafaellbarros/tasks-functional-test'
                    sh 'mvn test'
                }
            }
        }
        stage ('Deploy Prod') {
            steps {
                sh 'docker-compose build'
                sh 'docker-compose up -d'
            }
        }
        stage ('Health Check') {
            steps {
                sleep(10)
                dir('functional-test') {
                    git branch: 'main', credentialsId: 'github_login', url: 'https://github.com/rafaellbarros/tasks-functional-test'
                    sh 'mvn verify -Dskip.surefire.tests'
                }
            }
        }                                
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functional-test/target/surefire-reports/*.xml, functional-test/target/failsafe-reports/*.xml'
            archiveArtifacts artifacts: 'target/tasks-backend.war, front-end/target/tasks.war', followSymlinks: false, onlyIfSuccessful: true
        }
        unsuccessful {
            emailext attachLog: true, body: 'See the attached log below', subject: 'Build $BUILD_NUMBER has failed', to: 'rafaelbarros.df+jenkins@gmail.com'
        }
        fixed {
            emailext attachLog: true, body: 'See the attached log below', subject: 'Build is fine!!!', to: 'rafaelbarros.df+jenkins@gmail.com'
        }
    }
}

