pipeline {
    agent none
    stages {
	stage('Test') {
	    agent {
		docker {
		    image 'maven:3.6.3-jdk-11'
		    args '-v $HOME/.m2:/root/.m2 --network docker-ci_default'
		}
	    }
	    steps {
		sh 'mvn clean test sonar:sonar -Dsonar.host.url=http://sonarqube:9000/sonarqube -Dsonar.login=$SONAR_TOKEN'
	    }
	}

	stage('Build'){
	    agent {
		docker {
		    image 'maven:3.6.3-jdk-11'
		    args '-v $HOME/.m2:/root/.m2 --network docker-ci_default'
		}
	    }
	    steps {
		sh 'mvn clean package'
	    }

	}
	
	stage('Docker') {
	    agent { label 'master' }
	    environment {
		VERSION=readMavenPom().getVersion().replace("-SNAPSHOT","")
		COMMIT="${GIT_COMMIT[0..7]}"
	    } 
	    steps {
		script{
		    def img
		    if (env.BRANCH_NAME == 'development') {
			dir('tc-webapp') {
			    img = docker.build('de4a/dev-connector','--build-arg VERSION=$VERSION --build-arg CHASH=$COMMIT .')
			    docker.withRegistry('','docker-hub-token') { 
				img.push('latest')
				img.push('$VERSION')
			    }				 
			}
		    }
		    if (env.BRANCH_NAME == 'master') {
			dir('tc-webapp') {
			    img = docker.build('de4a/connector','--build-arg VERSION=$VERSION --build-arg CHASH=$COMMIT .')
			    docker.withRegistry('','docker-hub-token') { 
				img.push('latest')
				img.push('$VERSION')
			    }				 
			}
		    }
		}
	    }

	    post {
		always {
		    sh 'docker system prune -f'
		}
	    }

	}
    }
    post {
	failure {
	    node('master') {
	    // /var/jenkins_home/jobs/DE4A_WP5/jobs/toop-connector-ng/branches/PR-1/builds/8
		echo "${JOB_BASE_NAME}"
		sh 'cat ${JENKINS_HOME}/jobs/${JOB_NAME}/builds/${BUILD_NUMBER}/log | grep -B 1 -A 5 >/tmp/${BUILD_NUMBER}.fail'
		slackSend color: "danger", message: ":darth_maul: Build fail! :darth_maul:\nJob name: ${env.JOB_NAME}, Build number: ${env.BUILD_NUMBER}\nGit Author: ${env.CHANGE_AUTHOR}, Branch: ${env.GIT_BRANCH}, ${env.GIT_URL}\n"
		slackUploadFile filePath: "/tmp/${BUILD_NUMBER}.fail", initialComment: "Maven [ERROR] Log output" 
		sh 'rm /tmp/${BUILD_NUMBER}.fail'
	    }
	}
	success {
	    node('master') {
		script { 
		    if(currentBuild.getPreviousBuild() &&
			    currentBuild.getPreviousBuild().getResult().toString() != 'SUCCESS') {
			slackSend color: "good", message: ":baby-yoda: This is the way! :baby-yoda: \nJob name: ${env.JOB_NAME}, Build number: ${env.BUILD_NUMBER}\nGit Author: ${env.CHANGE_AUTHOR}, Branch: ${env.GIT_BRANCH}, ${env.GIT_URL}\n"
		    }
		}
	    }
	}
    }
}
