package za.co.data

/**
 * Created by Alvin on 30 Jul 2016.
 */
abstract class SQLObject implements Serializable {

    abstract def insertSQL()
    abstract def updateSQL()
    abstract String tableName()
    def currency;

    @Override
    public String toString(){
        String result = "-------------------"
        result +=  this.getClass()
        result +=  "-------------------\n"
        this.properties.collect{it}.each { prop ->
            result += prop.key +"-->"+prop.value+"\n"
        }
        result +=  "-------------------\n"
        return result
    }
}