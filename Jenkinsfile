pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage("Read from Maven POM"){
            steps{
                script{
                    projectArtifactId = readMavenPom().getArtifactId()
                    projectVersion = readMavenPom().getModelVersion()
                }
                echo "Building ${projectArtifactId}:${projectVersion}"
            }
        }
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
        stage("Build JAR file"){
            steps{
                bat "mvn install -Dmaven.test.skip=true"
            }
        }
        stage("Build image"){
            steps {
                echo "Building service image and pushing it to DockerHub"
                    withCredentials([usernamePassword(credentialsId: 'dockerHub', usernameVariable: "dockerLogin",
                        passwordVariable: "dockerPassword")]) {
                            bat "docker login -u ${dockerLogin} -p ${dockerPassword}"
                            bat "docker image build -t ${dockerLogin}/${projectArtifactId} ."
                            bat "docker push ${dockerLogin}/${projectArtifactId}"
                   }
                echo "Building image and pushing it to DockerHub is successful done"
            }
        }
        stage("Deploy"){
            steps{
                bat "docker-compose --file docker-compose.yml up --detach"
                sleep(45)
                echo "Server is hopefully up"
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
            }
        }
    }
    post { // good practice to clean up workspace
        always {
            cleanWs()
        }
    }
}