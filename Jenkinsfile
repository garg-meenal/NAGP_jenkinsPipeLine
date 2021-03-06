pipeline{
    
    agent any
    
    environment{
        scannerHome = tool 'SonarQubeScanner'
        username = 'meenalgarg2610'
        registry = 'meenalgarg2610/jenkins-pipeline'
    }
    tools{
        maven 'Maven3'
    }
    // trigger for polling any change in github
    triggers {
      pollSCM('H/2 * * * *') 
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
                git poll:true, credentialsId: 'github-java' , url: 'https://github.com/garg-meenal/NAGP_jenkinsPipeLine.git'
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
                
                //Test_Sonar - name of configuration in jenkins
                withSonarQubeEnv('Test_Sonar') {
					bat "${scannerHome}/bin/sonar-scanner \
					-Dsonar.projectKey=sonar_meenalgarg2610 \
					-Dsonar.host.url=http://localhost:9000 \
					-Dsonar.java.binaries=target/classes"
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
				bat "docker tag i-${username}-master ${registry}:latest"
                
                // DockerHub credential is provided in readme.txt
                withDockerRegistry(credentialsId: 'DockerHub', url: ''){
                bat "docker push ${registry}:${BUILD_NUMBER}"
				bat "docker push ${registry}:latest"
                }
            }
        }
		
		stage('Remove previous docker container'){
			steps{
				echo 'Remove already running docker container'
				script{
                    try{
                        // stop already running container
                        bat "docker stop c-${username}-master"
                        // remove the old container
                        bat "docker container rm c-${username}-master"
                    }catch(Exception e){
                        // Nothing to be done here, added only to prevent the failure of pipeline 
                        //because when pipeline will run for the first time, 
                        //there won't be any container to remove
                    }
                }
			}
		}
		
		stage('Docker deployment'){
			steps{
				echo 'docker deployment step'
				bat "docker run --name c-${username}-master -d -p 7100:8800 ${registry}:${BUILD_NUMBER}"
			}
		}
		
        //stage('Local Kubernetes Deployment'){
			//steps{
				//echo 'Deploying on local kubernetes'
				//bat 'kubectl apply -f deployment.yaml'
			//}
		//}
		
		stage('GKE Deployment'){
			steps{
				step([$class: 'KubernetesEngineBuilder', projectId: 'sodium-burner-319611', clusterName: 'demo-cluster', location: 'us-central1', manifestPattern: 'deployment.yaml', credentialsId: 'NAGP_jenkinsPipeline', verifyDeployment: true])
			}
		}        
    }
}
