package za.co

import groovy.sql.Sql
import za.co.data.SQLObject

import java.util.logging.Logger

/**
 * Created by Alvin on 30 Jul 2016.
 */
@Singleton
class DBManager {

    static Logger log = Logger.getLogger(this.getClass().canonicalName)

    def getConnection(){
        return Sql.newInstance("jdbc:mysql://localhost:3306/trader", "admin", "root", "com.mysql.jdbc.Driver")
    }

    def doInsert(SQLObject item){
        log.fine("doInsert Start for :"+item)
        def sql = getConnection()
        try{
            def insertSql = item.insertSQL()[0];
            checkTableExist(item, sql);
            sql.execute(insertSql, item.insertSQL()[1])
        }catch (Exception e){
            log.severe("DB Insert problems -- "+e.getMessage())
            log.severe(e.getStackTrace())
        }finally {
            sql.close()
            log.fine("doInsert DONE for :"+item)
        }
    }

    private def checkTableExist(SQLObject p, def sql) {
        def metadata = sql.connection.getMetaData()
        def tables = metadata.getTables(null,null,p.tableName(),null)
        if (!tables.next()) {
            createTable(p, sql)
        }
    }

    private createTable(SQLObject p, def sql){
        log.fine  "about to create new Table--->"+p.tableName()
        def createStart = "create table "+p.tableName()+" ("
        def columns = "itemRow MEDIUMINT NOT NULL AUTO_INCREMENT, processed TIMESTAMP NOT NULL"
        def createEnd = ", primary key (itemRow))"
        def inValidProps = ['class','update','tableName','insert', 'itemRow', 'processed']
        p.properties.collect{it}.each {prop ->
            if(!inValidProps.contains(prop.key)){
                columns+=", "+prop.key+" varchar(100) NOT NULL"
            }
        }
        sql.execute createStart+columns+createEnd
    }
}
