package za.co.bots

import za.co.DBManager
import za.co.data.OrderBook

/**
 * Created by Alvin on 08 Aug 2016.
 */
class OrderBookAnalysisBot extends BotInterface {

    OrderBookAnalysisBot(String currency){
        super(currency)
    }

    def slurper = new groovy.json.JsonSlurper()

    def process(){
        String conditions = " where currency = ? order by processed desc limit ${depthCheck}"
        def params = [currency]
        def results =  DBManager.instance.doReadData(new OrderBook(), conditions, params)

        def askResults = []
        def bidResults = []

        results.each {
            OrderBook book = it

            thresholds.each { th ->
                def overCalc = howManyOver(book.asks, th)
                askResults += [overCalc]

                overCalc = howManyOver(book.bids, th)
                bidResults += [overCalc]
            }

        }
        compareResults(askResults, bidResults);
    }

    def compareResults(ArrayList askResults, ArrayList bidResults) {
        println "askResults"+askResults
        println "bidResults"+bidResults


    }

    def howManyOver(def orders, def threshold){
        def results = []
        def i = 0;
        orders = slurper.parseText(orders)
        orders[0].each { order ->
            if(order[1]>threshold){
                i++
            }
        }
        results += [threshold, i]
        return results
    }
}
