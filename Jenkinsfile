pipeline {
    agent any
    stages {
        stage('FetchCode from master') {
            when{
                branch 'master'
            }
            steps {
                echo 'Fetch Code from master...'
            }
        }
        stage('FetchCode from test') {
            when {
                branch 'test'
            }
            steps{
                echo 'Fetch Code from test ...'
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
        stage('API test'){
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