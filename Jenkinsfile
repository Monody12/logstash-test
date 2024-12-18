pipeline {
    agent {
        kubernetes {
            label 'jenkins-jenkins-agent'
        }
    }
    environment {
        HARBOR_URL = 'harbor.dluserver.cn'
        HARBOR_PROJECT = 'logstash-test'
        IMAGE_NAME = 'logstash-test'
        KUBE_NAMESPACE = 'logstash-test'
        KUBE_DEPLOYMENT = 'logstash-test'
    }
    stages {
        stage('Test ENV') {
            steps {
                script {
                    sh "docker -v"
                    sh "kubectl version"
                    sh "helm version"
                }
            }
        }
        stage('Git') {
            steps {
                git branch: 'main', url: 'https://github.com/Monody12/logstash-test.git'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew bootJar'
            }
        }
        stage('Docker') {
            steps {
                script {
                    def imageTag = "${env.HARBOR_URL}/${env.HARBOR_PROJECT}/${env.IMAGE_NAME}:${env.BUILD_NUMBER}"
                    sh "docker build -t ${imageTag} ."
                    sh "docker login ${env.HARBOR_URL} -u admin -p Harbor12345"
                    sh "docker push ${imageTag}"
                    env.IMAGE_TAG = imageTag
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    sh "kubectl set image deployment/${env.KUBE_DEPLOYMENT} -n ${env.KUBE_NAMESPACE} ${env.KUBE_DEPLOYMENT}=${env.IMAGE_TAG}"
                    sh "kubectl rollout status deployment/${env.KUBE_DEPLOYMENT} -n ${env.KUBE_NAMESPACE}"
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check logs for details.'
        }
    }
}
