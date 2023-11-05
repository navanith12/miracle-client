pipeline {
    agent any
    stages {
        stage('Build'){
            steps{
                sh 'mvn --version'    
                sh 'mvn clean package'
                //sh 'sudo su root' 
                sh 'newgrp docker -p ""'
                sh 'docker images'
                sh 'docker build . -t miracleclient'
                sh 'docker run -i -t miracleclient'
            }
        }
    }
}

