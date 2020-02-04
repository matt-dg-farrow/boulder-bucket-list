pipeline {
    agent any
    stages {
        stage('---Clear---') {
            steps {
                sh "docker stop bbl-backend-test"
                sh "docker rm bbl-backend-test"
                sh "docker rmi -f bbl-backend-test"
		sh "cd"
		sh "sudo rm -r boulder-bucket-list"
            }
        }
	stage('--Clone project--') {
            steps {
                sh "git clone -b development https://github.com/j97b/boulder-bucket-list.git"
                }
        }
	stage('--Remove static folder--') {
            steps {
                sh "mv boulder-bucket-list/src/main/resources/static ."
		sh "cd boulder-bucket-list"
                }
        }
	stage('--Mvn clean package--') {
            steps {
                sh "mvn clean package"
                }
        }
        stage('--Build back-end--') {
            steps {
                sh "docker build -t bbl-backend-test ."
                }
        }
        stage('--Containerize back-end--') {
          steps {
                sh "docker run --name bbl-backend-test -d -p 8085:8082 bbl-backend-test"
                }
          }
	//stage('--Deploy--') {
          //steps {
                //sh "cd .."
		//sh "sudo cp /var/lib/jenkins/workspace/${JOB_NAME}/Java/target/boulder-bucket-list.jar"
                //}
          //}
    }
}
