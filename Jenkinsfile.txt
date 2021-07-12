pipeline{
    
    agent any
    
    environment{
        scannerHome = tool 'sonarqube-scanner';
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
                withSonarQubeEnv('sonarqube') {
					bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=NAGP_jenkinsPipeline -Dsonar.host.url=http://localhost:9000 -Dsonar.login=b6fc4a5edeaedce4c6b76def4af2d4307fc591d5 -Dsonar.java.binaries=target/classes"
                }
            }
        }
    }
}