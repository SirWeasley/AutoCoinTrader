/**
 * Created by alvin on 8/11/16.
 */
class testMe {

    static def main(def args) {
        def start = 0.3
        (1..365).each{
            start = start + (start*0.02/100)
            println start
        }
    }
}
