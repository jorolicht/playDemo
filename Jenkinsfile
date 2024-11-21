pipeline{
  agent any
  stages{
    stage('Compile'){
      steps{
        sh 'sbt compile'
      }
      post{
        success{
          echo "project build success...!"
        }
        failure{
          echo "project build failed"
        }
      }
    }
    stage('test'){
      steps{
        sh 'sbt test'
          
      }      
      post{
        success{
          echo "Testing Completed...!"
        }
        failure{
          echo "Testing failed"
        }
      }
    }
    stage('docStage'){
      steps{
        sh 'sbt docker:stage'
          
      }      
      post{
        success{
          echo "docker:stage Completed...!"
        }
        failure{
          echo "docker:stage failed"
        }
      }
    }
    stage('docPublishLocal'){
      steps{
        sh 'sbt docker:publishLocal'
          
      }      
      post{
        success{
          echo "docker:publishLocal Completed...!"
        }
        failure{
          echo "docker:publishLocal failed"
        }
      }
    }
    stage('dockerRunApp'){
      steps{
        sh 'docker run --rm -d -p 9500:9500 --name myProverbApp proverb-app:1.0.1'
          
      }      
      post{
        success{
          echo "--------------------------------------------"
          echo "dockerRunApp Completed...! "
          echo "To stop the container use below code"
          echo "`docker container stop myProverbApp` "
          echo "--------------------------------------------"
        }
        failure{
          echo "dockerRunApp failed"
        }
      }
    }
    
    
  }
}