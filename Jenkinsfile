pipeline {
    agent any
    triggers {
        GenericTrigger(
            causeString: 'GitHub PR Merged',
            token: 'your-secret-token',
            regexpFilterText: '$payload.pull_request.merged',
            regexpFilterExpression: 'true'
        )
    }
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
