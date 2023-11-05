pipeline {
    agent any
    stages {
        stage('Build'){
            steps{
                sh 'mvn --version'    
                sh 'mvn clean package'
                //sh 'newgrp docker'
                sh 'sudo docker images'
                sh 'docker build . -t miracleclient'
                sh 'docker run -i -t miracleclient'
            }
        }
    }
}

