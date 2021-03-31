liquibase
 --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
 --classpath="~/git/benzapp-backend/db-work/h2-1.4.200.jar"
 --url="jdbc:h2:file:~/git/benzapp-backend/target/h2db/db/benzapp;DB_CLOSE_DELAY=-1"
 --changeLogFile="~/git/benzapp-backend/export/generateChangeLog.xml"
 --username=benzapp
 --password=
 --logLevel=info
 --defaultSchemaName=PUBLIC
generateChangeLog
