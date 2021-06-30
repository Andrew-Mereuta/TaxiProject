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
                echo 'Postman testing..'
                bat "npm -v"
                // works only if the server is up and running
                bat "C:\\Users\\Simple\\AppData\\Roaming\\npm\\newman run https://www.getpostman.com/collections/94310e512a387b6414c5 --reporters html --reporter-html-export C:\\Users\\Simple\\Desktop\\postmanTaxi\\report\\report.html"
            }
        }
        stage('JMeter Test') {
            steps {
                echo 'JMeter testing..'
                // works only if the server is up and running
                bat "C:\\Users\\Simple\\Desktop\\jmeter\\apache-jmeter-5.4.1\\bin\\jmeter.bat -n -t C:\\Users\\Simple\\Desktop\\jmeter\\jmeter-tests\\clients.jmx -l report.html -e -o C:\\Users\\Simple\\Desktop\\jmeter\\jmeter-tests\\report"
                // bat "C:\\Users\\Simple\\Desktop\\jmeter\\apache-jmeter-5.4.1\\bin\\jmeter.bat -n -t C:\\Users\\Simple\\Desktop\\jmeter\\jmeter-tests\\clients.jmx"
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
// postman link https://www.getpostman.com/collections/94310e512a387b6414c5