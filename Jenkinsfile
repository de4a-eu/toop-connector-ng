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

	    post {
		failure {
		    slackSend color: "danger", message: "Build failed: ${env.JOB_NAME}, ${env.BUILD_NUMBER}\nGit: ${env.GIT_COMMITTER_NAME}, ${env.GIT_BRANCH}, ${env.GIT_URL}\n"
		}
		success {
		    slackSend color: "good", message: "Build success: ${env.JOB_NAME}, ${env.BUILD_NUMBER}\nGit: ${env.GIT_COMMITTER_NAME}, ${env.GIT_BRANCH}, ${env.GIT_URL}\n"
		}

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
			}
		    }
		    if (env.BRANCH_NAME == 'master') {
			dir('tc-webapp') {
			    img = docker.build('de4a/connector','--build-arg VERSION=$VERSION --build-arg CHASH=$COMMIT .')
			}
		    }
		    docker.withRegistry('','docker-hub-token') { 
			img.push('latest')
			img.push('$VERSION')
		    }				 
		}
	    }

	}
    }
}
