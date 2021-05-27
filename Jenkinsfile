pipeline {
    agent any
    stages {
        stage('master_Build') {
            when{
                branch 'master'
            }
            steps {
                echo 'build master branch'
                sh './scripts/build.sh'
            }
        }
        stage('test_Build') {
            when {
                branch 'test'
            }
            steps{
                echo 'build test branch'
                sh './scripts/build.sh'
            }
        }
        stage('API_Test'){
            steps{
                echo 'APT test Area...'
                bat 'call ./scripts/test_JMeter.bat'
            }
        }
    }
    post{
        always{
            sh './scripts/kill-service.sh'
        }
        success{
            echo 'This workflow is successful!'
        }
        failure{
            echo 'This workflow is failed!'
        }
    }
}