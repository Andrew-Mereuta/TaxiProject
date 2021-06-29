pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
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
    }
    post { // good practice to clean up workspace
        always {
            cleanWs()
        }
    }
}
// bat instead of sh
