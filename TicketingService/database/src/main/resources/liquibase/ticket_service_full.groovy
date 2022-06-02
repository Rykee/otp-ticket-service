package liquibase

databaseChangeLog(){
    include(file: 'ticket_service_1_0_0.groovy', relativeToChangelogFile: true)
}