package za.co.data

/**
 * Created by Alvin on 31 Jul 2016.
 */
class OrderBook extends SQLObject {
    int itemRow
    def processed
    def ask
    def bids
    def isFrozen
    def seq

    def insertSQL() {
        return """insert into OrderBook (processed,ask,bids,isFrozen,seq) values (now(),?,?,?,?) """
    }

    def allRows() {
        return null
    }

    def updateSQL() {
        return null
    }

    String tableName() {
        return "OrderBook"
    }
}
