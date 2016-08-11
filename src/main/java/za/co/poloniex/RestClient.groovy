package za.co.poloniex

import groovyx.net.http.HTTPBuilder

import java.util.logging.Logger

import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.Method.GET

/**
 * Created by Alvin on 21 Jul 2016.
 */
@Singleton
class RestClient {

    static Logger log = Logger.getLogger(this.getClass().canonicalName)
    def http = new HTTPBuilder( 'https://poloniex.com/' )
    def slurper = new groovy.json.JsonSlurper()

    def publicCommand(def query){
        def result;
        log.info("doing call for -->"+query)
        http.request(GET, TEXT) { req ->
            uri.path = '/public'
            uri.query =  query
            headers.'User-Agent' = 'Mozilla/5.0'

            response.success = { resp, json ->
                assert resp.status == 200
                json.eachLine { line ->
                    result = slurper.parseText(line)
                }
                if(result.error){
                    log.severe("Error response -->"+result.error)
                    throw Exception("RestClient:"+result.error)
                }
            }
            response.'404' = { resp ->
                log.severe 'Not found for command --> ${command}'
            }
        }
        return result
    }
    def getTicker() {
        return publicCommand([command:'returnTicker'])
    }

    def getOrderBook(def currency, def depth = 1000){
        return publicCommand([command:'returnOrderBook',currencyPair:currency,depth:depth])
    }
}
