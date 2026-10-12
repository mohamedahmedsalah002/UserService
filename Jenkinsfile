pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'moosalah02/vr-user-service'
        TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Build & Test') {
            steps {
                sh 'mvn -version'
                sh 'mvn -B clean package'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    appImage = docker.build("${DOCKER_IMAGE}:${TAG}")
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                        appImage.push()
                        appImage.push('latest')
                    }
                }
            }
        }
    }

    post {
        success { echo "Successfully built and pushed ${DOCKER_IMAGE}:${TAG}" }
        failure { echo 'Pipeline failed. Check the console output.' }
    }
}