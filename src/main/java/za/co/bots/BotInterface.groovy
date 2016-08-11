package za.co.bots

import za.co.constants

/**
 * Created by Alvin on 08 Aug 2016.
 */
abstract class BotInterface implements constants {

    BotInterface(String currency){
        this.currency = currency;
    }
    String currency
    abstract def process()

}