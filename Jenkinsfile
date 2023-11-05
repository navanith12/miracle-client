pipeline {
    agent any
    stages {
        stage('Build'){
            steps{
                sh 'mvn --version'    
                sh 'mvn clean package'
                sh 'sudo docker build . -t miracleclient'
                sh 'sudo docker run -i -t miracleclient'
            }
        }
    }
}

