pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Your build steps
                jiraSendBuildInfo()
            }
        }

        stage('Deploy') {
            steps {
                // Your deployment steps
                jiraSendDeploymentInfo(environmentId: 'dev', environmentName: 'Development')
            }
        }
    }
}
