package za.co.bots

import za.co.DBManager
import za.co.data.OrderBook
import za.co.data.Ticker
import za.co.poloniex.RestClient

import java.util.logging.Logger

/**
 * Created by Alvin on 30 Jul 2016.
 */
class InfoGatherBot extends BotInterface {

    static Logger log = Logger.getLogger(this.getClass().canonicalName)
    RestClient client
    def tickerData

    public InfoGatherBot(RestClient client, String currency){
        super(currency)
        this.client = client
    }

    def process(){
        ticker()
        orderBook()
    }

    private def ticker(){
        Ticker ticker = tickerData."$currency"
        ticker.currency = currency
        DBManager.instance.doInsert(ticker)
    }

    private def orderBook(){
        OrderBook orderBook = client.getOrderBook(currency, 1000)
        orderBook.currency = currency
        DBManager.instance.doInsert(orderBook)
    }
}
