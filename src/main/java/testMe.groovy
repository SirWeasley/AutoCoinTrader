/**
 * Created by alvin on 8/11/16.
 */
class testMe {

    static def main(def args) {
        def start = 1
        (1..365).each{
            start = start + (start*0.0002)
            println start
        }
    }
}
