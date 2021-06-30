pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage('Compile') {
            steps {
                echo 'Compiling..'
                bat "mvn compile"
            }
        }
        stage('Build') {
            steps {
                echo 'Building..'
                bat "mvn -version"
                bat "mvn clean install"
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                bat "mvn test"
            }
        }
        stage('Postman Test') {
            steps {
                echo 'Postman testing...'
                bat "cd C:\Users\Simple\Desktop\postmanTaxi"
            }
        }
    }
    post { // good practice to clean up workspace
        always {
            cleanWs()
        }
    }
}
// bat instead of sh
