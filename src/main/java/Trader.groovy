import za.co.bots.InfoGatherBot
import za.co.bots.BotInterface
import za.co.poloniex.RestClient
import java.util.logging.Logger

/**
 * Created by Alvin on 21 Jul 2016.
 */

//API Key	BZ719HPC-90UYH16H-5NA7GI8D-669HTVKV	Delete
//Secret	0b9384d0fd315e3b7a15b3583986134b59b2078ebfb86d9ff591f6b9c2c5d3cfa39eaab4da5bbeb76c3a9e8119db210b4d7e96674d2cfa61d1f05d868a87ac30
class Trader {

    static def client = RestClient.instance
    static int timeBetweenRuns = 60000
    static Logger log = Logger.getLogger(this.getClass().canonicalName)

    List<BotInterface> bots = [new InfoGatherBot(client, 'BTC_ETH'), new InfoGatherBot(client, 'BTC_ETC')];

    static def main(def args) {
        def me = new Trader();
        def running = true
        def runStart
        def runEnd
        while (running) {
            runStart = new Date().toTimestamp()
            me.runBots()
            runEnd = new Date().toTimestamp()
            def sleepTime = timeBetweenRuns-(runEnd.time-runStart.time)
            log.info("Done with tick, time till next tick--> "+sleepTime+" ms")
            sleep(sleepTime)
        }
    }

    def runBots(){
        def tickerData = client.getTicker()
        bots.each { bot ->
            if(bot instanceof InfoGatherBot) {
                bot.setTickerData(tickerData)
            }
            bot.process()
        }
    }
}
