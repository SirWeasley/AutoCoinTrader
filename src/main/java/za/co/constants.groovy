package za.co
/**
 * Created by alvin on 8/11/16.
 */
interface constants {
    //TODO:make a list so adding or change bots should be based on list of currencies being monitor
    static String eth = 'BTC_ETH'
    static String etc = 'BTC_ETC'
    static int timeBetweenRuns = 60000

    static String depthCheck = 10
    static String depth = 1000
    //bitcoin level to check for in order book
    static def thresholds = [1, 5, 10, 20]
}