package za.co.data

import groovy.json.JsonBuilder

/**
 * Created by Alvin on 31 Jul 2016.
 */
class OrderBook extends SQLObject {
    int itemRow
    def processed
    def asks
    def bids
    def isFrozen
    def seq
    def currency

    def insertSQL() {
        //asks("\\xAC\\xED\\x00\\x05sr...","")
        return ["""insert into OrderBook (processed,asks,bids,isFrozen,seq,currency) values (now(),?,?,?,?,?) """,
                [asks,bids,isFrozen,seq, currency]]
    }

    def updateSQL() {
        return null
    }

    String tableName() {
        return "OrderBook"
    }
}
