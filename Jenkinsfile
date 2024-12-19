pipeline {
    agent {
        kubernetes {
            label 'jenkins-jenkins-agent'
        }
    }
    environment {
        HARBOR_URL = 'harbor.dluserver.cn'
        HARBOR_PROJECT = 'logstash-test-test'
        IMAGE_NAME = 'logstash-test-test'
        KUBE_NAMESPACE = 'logstash-test-test'
        KUBE_DEPLOYMENT = 'logstash-test-test'
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
                git branch: 'test', url: 'https://github.com/Monody12/logstash-test.git'
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
                    // 动态更新 YAML 文件的镜像字段
                    sh "sed -i 's|image: .*|image: ${env.IMAGE_TAG}|g' ./kube-config/springboot-deployment.yaml"

                    // 应用 YAML 文件
                    sh "kubectl apply -f ./kube-config/springboot-deployment.yaml -n ${env.KUBE_NAMESPACE}"
                    sh "kubectl apply -f ./kube-config/springboot-ingress.yaml -n ${env.KUBE_NAMESPACE}"

                    // 检查状态
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
