package za.co.bots

import za.co.DBManager
import za.co.data.OrderBook
import za.co.data.OrderBookAnalysis
import za.co.data.Ticker

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
        def askResult = []
        def bidResult = []

        results.each {
            OrderBook book = it

            thresholds.each { th ->
                def overCalc = howManyOver(book.asks, th)
                askResult += [overCalc]

                overCalc = howManyOver(book.bids, th)
                bidResult += [overCalc]
            }
            askResults += [askResult]
            bidResults += [bidResult]
        }
        compareResults(askResults, bidResults);
    }

    def compareResults(ArrayList askResults, ArrayList bidResults) {
        OrderBookAnalysis analysis = new OrderBookAnalysis()
        analysis.askTotal = askResults[0]
        analysis.bidTotal = bidResults[0]
        analysis.askDirection = direction(askResults)
        analysis.bidDirection = direction(bidResults)
        analysis.currency = currency

        //if bids(buys) are bigger then - returned
        //if ask(sells) are bigger positives
        //this is on sums on thresholds bigger than set of Bitcoin values
        def compareResult = 0
        thresholds.each { th ->
            def row = analysis.askDirection[th].compareTo(analysis.bidDirection[th])
            compareResult += (row*th)
        }
        analysis.compareDirection = compareResult
        DBManager.instance.doInsert(analysis)
    }

    def direction(def results){
        def directionArray = [:]
        def lastArray = [:]
        results.each { outer ->
            outer.each { inner ->
                int i = inner[0]
                if(lastArray[i]==null){
                    lastArray[i] = 0
                }
                if(inner[1] > lastArray[i]){
                    directionArray[i] = directionArray[i] != null ? directionArray[i]+1: 0
                }else if (inner[1] < lastArray[i]) {
                    directionArray[i]--
                }
                lastArray[i] = inner[1]
            }
        }
        return directionArray
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
