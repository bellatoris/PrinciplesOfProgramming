package pp201701.mid4
import scala.annotation.tailrec
import pp201701.mid4.Data.DataBundle._

/*
 *  Midterm of Principles of Programming 2017
 *  
 *  General Instructions:
 *  - './compile.sh' compiles your code
 *  - './run.sh' runs a simple test case given in Test.scala
 *  - Before submission, check your code by running './run.sh'. Uncompilable code will get 0 points.
 *  - Do not use imperative features. (ex. for, while, var, etc)
 *  - Compress the 'src' folder with '.zip' format for submission.
 */

object Main {
  /*
   Problem 4: Tail-recursive list reverse

   Implement a reverse function that reverses the order of the given IList.
   Your implementation should be tail-recursive so that the function can handle very long lists.
   Do not use the Scala built-in List type and functionality.
   */
  def reverse(xs: IList): IList = {
    @tailrec
    def nested(xs: IList, acc: IList): IList = xs match {
      case INil() => acc
      case ICons(hd, tl) => nested(tl, ICons(hd, acc))
    }
    
    nested(xs, INil())
  }
}
