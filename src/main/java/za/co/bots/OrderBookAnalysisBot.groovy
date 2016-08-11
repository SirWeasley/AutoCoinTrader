package za.co.bots

import za.co.DBManager
import za.co.data.OrderBook
import groovy.json.JsonSlurper

/**
 * Created by Alvin on 08 Aug 2016.
 */
class OrderBookAnalysisBot extends BotInterface {

    OrderBookAnalysisBot(String currency){
        super(currency)
    }

    def slurper = new groovy.json.JsonSlurper()
    def threasHolds = [1,5,10,20]

    def process(){
        String conditions = " where currency = ? order by processed desc limit 10"
        def params = [currency]
        def results =  DBManager.instance.doReadData(new OrderBook(), conditions, params)

        results.each {
            OrderBook book = it
            def asks = book.asks
            def askResults = []
            threasHolds.each { th ->
               askResults += howManyOver(asks, th)
            }
        }
    }

    def howManyOver(def orders, def threashold){
        def results = []
        def i = 0;
        orders.each { o ->
            def order = slurper.parseText(o)
            if(order[1]>threashold){
                results += [it,i]
            }
            i++
        }
        println results
        return results
    }
}
