#!groovy

stage 'Prepare environment'

// Set build properties
properties([[$class: 'GithubProjectProperty',
               displayName: 'clj-factory',
               projectUrlStr: 'https://github.com/duck1123/clj-factory/'],
            [$class: 'BuildDiscarderProperty',
            strategy: [$class: 'LogRotator', numToKeepStr: '5']]]);

def clojure = docker.image('clojure:alpine')
clojure.pull()

node {
    sh 'env | sort'
}

stage 'Unit Tests'

clojure.inside {
    checkout scm
    wrap([$class: 'AnsiColorBuildWrapper']) {
        sh 'lein midje'
    }
    step([$class: 'JUnitResultArchiver', testResults: 'target/surefire-reports/TEST-*.xml'])
}

stage 'Generate Reports'

clojure.inside {
    checkout scm
    sh 'lein doc'
    step([$class: 'JavadocArchiver', javadocDir: 'doc', keepAll: true])
    step([$class: 'TasksPublisher', high: 'FIXME', normal: 'TODO', pattern: '**/*.clj,**/*.cljs'])
}

stage 'Generate Artifacts'

node {
    archive 'target/*jar'
}

// TODO: Skip for features and PRs
// stage 'Deploy Artifacts'
// sh 'lein deploy'
    
stage 'Set Status'

node {
    step([$class: 'GitHubCommitStatusSetter'])
}
