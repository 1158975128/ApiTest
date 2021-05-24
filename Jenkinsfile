pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'This is build!!!'
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