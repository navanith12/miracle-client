pipeline {
    agent any
    stages {
        stage('Build'){
            steps{
                sh 'mvn --version'    
                sh 'mvn clean package'
                sh 'sudo su azureuser -p "Welcome@2468"' 
                //sh 'newgrp docker -p ""'
                sh 'docker images'
                sh 'docker build . -t miracleclient'
                sh 'docker run -i -t miracleclient'
            }
        }
    }
}

