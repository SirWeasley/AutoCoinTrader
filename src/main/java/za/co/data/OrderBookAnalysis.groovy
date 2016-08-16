package za.co.data
/**
 * Created by Alvin on 31 Jul 2016.
 */
class OrderBookAnalysis extends SQLObject {
    int itemRow
    def processed
    def askTotal
    def bidTotal
    def askDirection
    def bidDirection
    def compareDirection

    def insertSQL() {
        return ["""insert into ${tableName()} (processed,askTotal,bidTotal,askDirection,bidDirection,compareDirection,currency)
                        values (now(),?,?,?,?,?,?) """,
                [ Arrays.deepToString(askTotal), Arrays.deepToString(bidTotal),
                  Arrays.deepToString(askDirection), Arrays.deepToString(bidDirection),
                  compareDirection, currency]]
    }

    def updateSQL() {
        return null
    }

    String tableName() {
        return "OrderBookAnalysis"
    }
}
