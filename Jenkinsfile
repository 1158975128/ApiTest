pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'echo "This is from shell script"'
            }
        }
    }
    post{
        always{
            echo 'This will always run'
        }
        success{
            echo 'This will run only if successful'
        }
        failure{
            echo 'This will run only if failed'
        }
    }
}