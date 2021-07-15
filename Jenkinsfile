pipeline{
    
    agent any
    
    environment{
        scannerHome = tool 'SonarQubeScanner'
        username = 'meenalgarg2610'
        registry = 'meenalgarg2610/jenkins-pipeline'
    }
    tools{
        maven 'Maven'
    }
    
    options{
        timestamps()
        timeout(time: 1, unit: 'HOURS')
        skipDefaultCheckout()
        buildDiscarder(logRotator(
            numToKeepStr: '3',
            daysToKeepStr: '30'
            ))
    }
    
    stages{
        stage('Checkout'){
            steps{
                echo 'Checkout step'
                git credentialsId: 'github-java', url: 'https://github.com/meenalgarg/NAGP_jenkinsPipeLine.git'
            }
        }
        
        stage('Build'){
            steps{
                echo 'maven build step'
                bat 'mvn clean install'
            }
        }
        
        stage('Unit Testing'){
            steps{
                echo 'unit testing step'
                bat 'mvn test'
            }
        }
        stage('SonarQube code analysis'){
            steps{
                echo 'sonarQube code analysis step'
                withSonarQubeEnv('Test_Sonar') {
					bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=sonar_meenalgarg2610 -Dsonar.host.url=http://localhost:9000 -Dsonar.login=b6fc4a5edeaedce4c6b76def4af2d4307fc591d5 -Dsonar.java.binaries=target/classes"
                }
            }
        }
        stage('Docker image'){
            steps{
                echo 'create docker image step'
                bat "docker build -t i-${username}-master --no-cache -f Dockerfile ."
            }
        }
        stage('Push image to registry'){
            steps{
                echo 'push image to docker hub step'
                bat "docker tag i-${username}-master ${registry}:${BUILD_NUMBER}"
                withDockerRegistry(credentialsId: 'dockerHub', url: ''){
                bat "docker push ${registry}:${BUILD_NUMBER}"
                }
            }
        }
        stage('Docker Deployment'){
            steps{
                echo 'docker deployment step'
                bat "docker run --name c-${username}-master -d -p 7100:8800 ${registry}:${BUILD_NUMBER}"
            }
        }
    }
}