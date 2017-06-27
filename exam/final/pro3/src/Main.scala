package pp201701.fin3
import pp201701.fin3.Data.DataBundle._
import pp201701.fin3.Data.BoxObj._

/*
 * ** The submitted code should be runnable. Before upload, you MUST check it
      whether Test.scala runs successfully. Otherwise you may get 0 points for
      all problems.

 * ** Compress the 'src' folder only. Don't put .sh files in the zip file.
      You can put the .iml file together, if you feel difficulty for some reasons.

 * ** Use only the features taught in class. Especially, don't use imperative
      features such as var, while, for, return, and so on. You may get deduction
      in your points.

 * ** Do not use equivalent built-in features in Scala. The core logic should be
      implemented by you.

 */

object Main {
  /*
   Problem 3: Implement a pretty-printer for Report.
   Check "Test.scala" for the exact format.
   */
  def pretty_printer(r:Box[Report]) : String = {
    val title = "<" + r.i.title(r.d) +">\n"
    val cs = r.i.it.iter(r.d)

    def printElements[I](xs: I)(implicit iterProxy: Iter[I, r.i.A], 
                                         keyvalProxy: KeyVal[r.i.A]): String = {
      iterProxy.getValue(xs) match {
        case None => ""
        case Some(n) => {
          val str = " * " + keyvalProxy.get_key(n) + " : " + keyvalProxy.get_val(n) +"\n"
          str + printElements(iterProxy.getNext(xs))(iterProxy, keyvalProxy)
        }
      }
    }
  
    title + printElements(cs.d)(cs.i, r.i.keyval)
  }
}
