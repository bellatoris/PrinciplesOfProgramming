package pp201701.fin3test
import pp201701.fin3.Main._
import pp201701.fin3.Data.DataBundle._
import pp201701.fin3.Data.BoxObj._

object Test extends App {
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  implicit def listIter[A] : Iter[List[A], A] =
    new Iter[List[A],A] {
      def getValue(a: List[A]) = a.headOption
      def getNext(a: List[A]) = a.tail
    }

  val kv : KeyVal[(String, String)] = new KeyVal[(String, String)] {
    def get_key(a:(String, String)) = a._1
    def get_val(a:(String, String)) = a._2
  }

  implicit val proxy : Report[(String, List[(String, String)])] =
    new Report[(String, List[(String, String)])] {
      type A = (String, String)
      def title(r:(String, List[(String, String)])) : String = r._1
      def it : Iterable[(String, List[(String, String)]), (String, String)] =
        new Iterable[(String, List[(String, String)]), (String, String)] {
          def iter(r:(String, List[(String, String)])) : Box2[Iter, (String, String)] =
            r._2 // Box2(r._2, listIter)
        }
      def keyval = kv
    }

  val report1 : (String, List[(String, String)]) =
    ("mytitle", List(("key1", "val1"), ("key2", "val2")))
  val dyn_report1 : Box[Report] = report1

  print_result(pretty_printer(dyn_report1) == "<mytitle>\n * key1 : val1\n * key2 : val2\n")
  println(pretty_printer(dyn_report1))

  val report2 : (String, List[(String, String)]) =
    ("empty report", List())
  val dyn_report2 : Box[Report] = report2

  print_result(pretty_printer(dyn_report2) == "<empty report>\n")
  println(pretty_printer(dyn_report2))

}
