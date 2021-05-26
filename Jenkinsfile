pipeline {
    agent any
    stages {
        stage('build master') {
            when{
                branch 'master'
            }
            steps {
                echo 'build master branch'
                sh 'mvn clean package'
                sh 'nohup java -jar target/fuliye.jar &'
                sh 'sleep 3m'
            }
        }
        stage('build test') {
            when {
                branch 'test'
            }
            steps{
                echo 'build test branch'
                sh 'mvn clean package'
                sh 'nohup java -jar target/fuliye.jar &'
                sh 'sleep 3m'
            }
        }
        stage('API test'){
            steps{
                echo 'APT test Area...'
                bat 'jmeter -n -t "D:\\DevOps\\Test_JMeter\\HTTP Request.jmx" -l "D:\\DevOps\\Test_JMeter\\output_report.jtl"'
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