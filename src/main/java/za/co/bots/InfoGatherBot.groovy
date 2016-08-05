package za.co.bots

import za.co.DBManager
import za.co.data.OrderBook
import za.co.data.Ticker
import za.co.poloniex.RestClient

import java.util.logging.Logger

/**
 * Created by Alvin on 30 Jul 2016.
 */
class InfoGatherBot {

    static Logger log = Logger.getLogger(this.getClass().canonicalName)
    RestClient client
    String currency

    public InfoGatherBot(RestClient client, String currency){
        this.client = client;
        this.currency = currency
    }

    def process(def tickerData){
        ticker(tickerData)
        returnOrderBook()
    }

    private def ticker(def tickerData){
        Ticker ticker = tickerData."$currency"
        ticker.currency = currency
        DBManager.instance.doInsert(ticker)
    }

    private def returnOrderBook(){
        OrderBook orderBook = client.getOrderBook(currency)
        println orderBook
    }
}
