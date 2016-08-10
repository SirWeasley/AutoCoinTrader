package za.co.bots

/**
 * Created by Alvin on 08 Aug 2016.
 */
abstract class BotInterface {

    BotInterface(String currency){
        this.currency = currency;
    }
    String currency
    abstract def process()

}