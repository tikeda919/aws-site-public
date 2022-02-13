pipeline {
  agent any
  stages {
    stage('STOP APPLICATION') {
      when {
        expression {
          def APP_CONTAINER = sh(returnStdout: true, script: "docker ps --format \"{{.Names}}\" --filter \"name=${INPUT_APP_CONTAINER}\"").trim()
          return APP_CONTAINER == INPUT_APP_CONTAINER
        }

      }
      steps {
        sh "docker stop ${INPUT_APP_CONTAINER} ; docker rm ${INPUT_APP_CONTAINER} ; docker rmi ${INPUT_APP_IMAGE_NAME} "
      }
    }

    stage('APP IMAGE RECREATE') {
      steps {
        sh "docker build -t ${INPUT_APP_IMAGE_NAME} --no-cache ."
      }
    }

    stage('NETWORK CREATE') {
      when {
        expression {
          def NETWORK_NAME = sh(returnStdout: true, script: "docker network ls --format \"{{.Name}}\" --filter \"name=${INPUT_NETWORK_NAME}\"").trim()
          return !(NETWORK_NAME == INPUT_NETWORK_NAME)
        }

      }
      steps {
        sh "docker network create ${INPUT_NETWORK_NAME}"
        sh "echo ------------------------------NETWORK IS CREATED AS ${INPUT_NETWORK_NAME}------------------------------"
      }
    }

    stage('RUN MYSQL') {
      when {
        expression {
          def MYSQL_CONTAINER = sh(returnStdout: true, script: "docker ps -a --format \"{{.Names}}\" --filter \"name=${INPUT_MYSQL_CONTAINER}\"").trim()
          return !(MYSQL_CONTAINER == INPUT_MYSQL_CONTAINER)
        }

      }
      steps {
        sh "docker run --network ${INPUT_NETWORK_NAME} --name ${INPUT_DB_NAME} -e MYSQL_DATABASE=${INPUT_DB_NAME} -e MYSQL_USER=${INPUT_MYSQL_USER_NAME} -e MYSQL_PASSWORD=${INPUT_MYSQL_USER_PASS} -e MYSQL_RANDOM_ROOT_PASSWORD=yes -d mysql:5.7 --character-set-server=utf8"
        sh "echo ------------------------------DATABASE IS CREATED AS ${INPUT_MYSQL_CONTAINER}------------------------------"
      }
    }

    stage('RUN APPLICATION') {
      steps {
        sh "docker run --name ${INPUT_APP_CONTAINER} --network ${INPUT_NETWORK_NAME} -d -p 18082:8080 ${INPUT_APP_IMAGE_NAME}"
        sh "echo ------------------------------APPLICATION IS CREATED AS ${INPUT_APP_CONTAINER}------------------------------"
      }
    }

    stage('DELETE DANGLING IMAGES') {
      steps {
        script {
          def retCnt = sh(returnStdout: true, script: 'docker images --filter dangling=true --format "{{.ID}}" | wc -l').trim()

          if(Integer.parseInt(retCnt) > 0){
            sh 'docker system prune -f --volumes'
            sh "echo ------------------------------${retCnt} OF DANGLING IMAGES ARE DELETED------------------------------"
          } else {
            sh "echo ------------------------------DANGLING IMAGE IS NOTHING------------------------------"
          }
        }

      }
    }

  }
  environment {
    INPUT_NETWORK_NAME = 'memoapp-network'
    INPUT_MYSQL_CONTAINER = 'memoapp-db'
    INPUT_APP_CONTAINER = 'my-tomcat-app'
    INPUT_APP_IMAGE_NAME = 'my-tomcat-app-img'
    INPUT_DB_NAME = 'memoapp-db'
    INPUT_MYSQL_USER_NAME = 'memoapp'
    INPUT_MYSQL_USER_PASS = 'memoapp'
  }
  parameters {
    string(name: 'GREP_FALSE', defaultValue: 'false', description: '')
  }
}