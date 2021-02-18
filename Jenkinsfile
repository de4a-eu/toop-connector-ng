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
	    environment {
		VERSION=readMavenPom().getVersion()
	    }
	    agent {
		docker {
		    image 'maven:3.6.3-jdk-11'
		    args '-v $HOME/.m2:/root/.m2 --network docker-ci_default'
		}
	    }
	    steps {
		script {
		    env.COMMIT= '$(git rev-parse --short HEAD)'
		}
		sh 'mvn clean package'
	    }

	    post {
		success {
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
}
