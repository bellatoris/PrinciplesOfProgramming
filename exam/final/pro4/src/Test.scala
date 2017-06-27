package pp201701.fin4test
import pp201701.fin.Data.DataBundle._
import pp201701.fin.Data.BoxObj._
import pp201701.fin.Printer._
import pp201701.fin.DictImpl._

object Test extends App {
  implicit val dproxy = DictDictionary[String, String]
  implicit val rproxy = ReportDictionary[String, String]("TestDict", (a:String)=>a, (a:String)=>a)

  val mydict0 : Dictionary[String, String] = dproxy.empty
  val mydict1 = dproxy.add(mydict0, "key1", "val1")
  val mydict2 = dproxy.add(mydict1, "key2", "val2")
  val mydict3 = dproxy.add(mydict2, "key1", "val3")

  val report3 = pretty_printer(mydict3)
  println(report3)

  // Tests
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  print_result(report3 == "<TestDict>\n * key1 : val3\n * key2 : val2\n")

}
