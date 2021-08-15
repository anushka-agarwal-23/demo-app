pipeline {
    environment {
        sonarScanner = tool name: 'SonarQubeScanner'
        username = 'anushkaagarwal'
        BRANCH_NAME = 'master'
        port = '7200'
        k8port = '30157'
    }
    agent any
    tools {
        maven 'Maven3'
    }
    stages {
        stage('Build') {
            steps {
                bat 'mvn clean install'
                echo 'Code Build completed'
            }
        }
        stage('Unit Testing') {
            steps {
                bat 'mvn test'
                echo 'Test cases executed'
            }
        }
        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'Test_Sonar', credentialsId: 'sonar-token') {
                    bat "${sonarScanner}/bin/sonar-scanner -e -Dsonar.projectKey=sonar-${username} -Dsonar.java.binaries=target/classes"
                }
                echo 'Sonar report generated'
            }
        }
        stage('Docker image') {
            steps {
                bat "docker build -t i-${username}-$env.BRANCH_NAME:$BUILD_NUMBER --no-cache -f Dockerfile ."
                echo 'Docker image created'

                bat "docker tag i-${username}-$env.BRANCH_NAME:$BUILD_NUMBER ${username}/$env.BRANCH_NAME:$BUILD_NUMBER"
                bat "docker tag i-${username}-$env.BRANCH_NAME:$BUILD_NUMBER ${username}/$env.BRANCH_NAME:latest"
                echo 'Docker image tagged with build number/latest tag'
            }
        }
        stage('Containers') {
            steps {
                parallel(
                'Precontainer Check': {
                        script {
                            env.containerId = bat(script:"docker ps -a -f publish=${port} -q", returnStdout: true).trim().readLines().drop(1).join('')
                            if (env.containerId != '') {
                                echo 'Stopping and removing existing container'
                                bat "docker stop $env.containerId && docker rm $env.containerId"
                            } else {
                                echo 'No container running on specified port.'
                            }
                        }
                    },
                    'Push to Docker Hub': {
                        withDockerRegistry(credentialsId: 'DockerHub', url: '') {
                            bat "docker push ${username}/$env.BRANCH_NAME:$BUILD_NUMBER"
                            bat "docker push ${username}/$env.BRANCH_NAME:latest"
                        }
                        echo 'Docker image pushed to DockerHub'
                    }
                )
            }
        }
        stage('Docker deployment') {
            steps {
                script {
                    bat "docker run --name c-${username}-$env.BRANCH_NAME -d -p ${port}:8080 ${username}/$env.BRANCH_NAME:latest"
                    echo 'Docker container deployed and running'
                }
            }
        }
        stage('Kubernetes Deployment') {
            steps {
				        bat "kubectl apply -f config.yaml --namespace=kubernetes-cluster-${username}"
                bat "kubectl apply -f deployment.yaml --namespace=kubernetes-cluster-${username}"
                script{
                    try {
                         bat "gcloud compute firewall-rules create ${username}-$env.BRANCH_NAME-node-port --allow tcp:${k8port}"
                    } catch (Exception e) {
                          echo 'Exception occurred: ' + e.toString()
                    }
                }
                echo 'Deployment on k8 completed'
            }
        }
    }
}
