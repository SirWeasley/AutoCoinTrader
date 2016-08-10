package za.co.data

import java.sql.Date

/**
 * Created by Alvin on 30 Jul 2016.
 */

class Ticker extends SQLObject {

    int itemRow
    def processed
    def id
    def baseVolume
    def high24hr
    def highestBid
    def last
    def low24hr
    def lowestAsk
    def percentChange
    def quoteVolume
    def isFrozen
    def currency

    def insertSQL() {
        return ["""insert into Ticker (processed, id, baseVolume, high24hr, highestBid, last, low24hr, lowestAsk, percentChange, quoteVolume, isFrozen, currency)
                  values (now(),?,?,?,?,?,?,?,?,?,?,?)""",
                [id, baseVolume, high24hr, highestBid, last, low24hr, lowestAsk, percentChange, quoteVolume, isFrozen, currency]]
    }

    def updateSQL() {
        return """update Ticker set processedTime=?, baseVolume=?, high24hr=?, highestBid=?, last=?, low24hr=?, lowestAsk=?, percentChange=?,
                         quoteVolume=? where row=?"""
    }

    String tableName(){
        return "Ticker"
    }
}
