pipeline {
    agent any
    stages {
        stage('FetchCode') {
            steps {
                echo 'Fetch Code step...'
            }
        }
        stage('UnitTest'){
            steps{
                echo 'Unit Test...'
            }
        }
        stage('Build'){
            steps{
                echo 'Build Area...'
            }
        }
        Stage('API test'){
            steps{
                echo 'APT test Area...'
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