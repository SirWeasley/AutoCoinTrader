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

    def process(){
        String conditions = " where currency = ? order by processed desc limit 10"
        def params = [currency]
        def results =  DBManager.instance.doReadData(new OrderBook(), conditions, params)

        results.each {
            println it
        }
    }
}
