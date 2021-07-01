pipeline {
    agent any
    tools {
        maven 'Maven'
    }
//     environment {
//         registry = "andrewmereuta/taxi-project"
//         registryCredential = 'dockerHub'
//         dockerImage = ''
//     }
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
                            bat "docker push ${dockerLogin}/${projectArtifactId}" // docker push andrewmereuta/taxi-project:$BUILD_NUMBER
                   }
                echo "Building image and pushing it to DockerHub is successful done"
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////
        stage("Deploy"){
            steps{
                bat "docker-compose --file docker-compose.yml up --detach"
                    timeout(time: 120, unit: 'SECONDS') {
                        waitUntil(initialRecurrencePeriod: 10000) {
                            script {
                                sleep(5000)
                                def result = sh script: "curl --silent --output /dev/null http://localhost:8080/test",
                                returnStatus: true
                                return (result == 0)
                                }
                            }
                    }
                echo "Server is up"
            }
        }
        ////////////////
//         stage('Docker Image Build') {
//             steps {
//                 echo 'Building docker image..'
//                 //bat "docker build -f Dockerfile -t taxi-project ."
//                 script {
//                     dockerImage = docker.build registry + ":$BUILD_NUMBER"
//                 }
//             }
//         }
//         //
//         stage('Deploy our image') {
//             steps {
//                 script {
//                     docker.withRegistry( '', registryCredential ) {
//                         dockerImage.push()
//                     }
//                 }
//             }
//         }
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
                // bat "C:\\Users\\Simple\\Desktop\\jmeter\\apache-jmeter-5.4.1\\bin\\jmeter.bat -n -t C:\\Users\\Simple\\Desktop\\jmeter\\jmeter-tests\\clients.jmx" // -l report.html
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