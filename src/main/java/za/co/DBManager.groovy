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

    def doReadData(SQLObject item, String conditions, List params){
        log.fine("doReadData Start for :"+item)
        def sql = getConnection()
        try{
            def readSql =  """SELECT * FROM ${item.tableName()} """
            if(conditions.toUpperCase().contains("WHERE")){
                readSql+=conditions
                return sql.rows(readSql, params)
            }else {
                return sql.rows(readSql)
            }
        }catch (Exception e){
            log.severe("DB read problems -- "+e.getMessage())
            log.severe(e.getStackTrace())
        }finally {
            sql.close()
            log.fine("doReadData DONE for :"+item)
        }
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
        def inValidProps = ['class','update','tableName','insert', 'itemRow', 'processed', 'metaClass']
        def blobType = ['asks','bids']
        p.properties.collect{it}.each {prop ->
            if(!inValidProps.contains(prop.key)&&!blobType.contains(prop.key)){
                columns+=", "+prop.key+" varchar(255) NOT NULL"
            }
            if(blobType.contains(prop.key)){
                columns+=", "+prop.key+" LONGTEXT NOT NULL"
            }
        }
        sql.execute createStart+columns+createEnd
    }
}
