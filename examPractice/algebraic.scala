import scala.annotation._


object Main {
  def main(args: Array[String]): Unit = {
    sealed abstract class Attr
    case class Name(name: String) extends Attr
    case class Age(age: Int) extends Attr
    case class DOB(year: Int, month: Int, day: Int) extends Attr
    case class Height(height: Double) extends Attr

    val a: Attr = Name("Chulsoo Kim")
    val b: Attr = DOB(2000, 3, 10)

    sealed abstract class IList
    case class INil() extends IList
    case class ICons(hd: Int, tl: IList = INil()) extends IList

    val x: IList = ICons(2, ICons(1, INil()))

    sealed abstract class IOption 
    case class INone() extends IOption
    case class ISome(some: Int) extends IOption

    sealed abstract class BTree
    case class Leaf() extends BTree
    case class Node(value: Int, left: BTree, right: BTree) extends BTree

    def length(xs: IList): Int = xs match {
      case INil() => 0
      case ICons(hd, tl) => 1 + length(tl)
    }

    def length2(xs: IList): Int = {
      @tailrec
      def nested(length: Int, xs: IList): Int = xs match {
        case INil() => length
        case ICons(hd, tl) => nested(length + 1, tl)
      }
      nested(0, xs)
    }

    def gen(n: Int): IList = {
      if (n < 0) INil()
      else ICons(n, gen(n - 1))
    }

    def gen2(n: Int): IList = {
      @tailrec
      def nestedGen(m: Int, list: IList): IList = {
        if (m > n) list
        else nestedGen(m + 1, ICons(m, list))
      }
      nestedGen(0, INil())
    } 

    def printList(xs: IList): String = xs match {
      case INil() => "INil"
      case ICons(hd, tl) => hd.toString + " " + printList(tl)
    }
    
    val c = gen2(3)
    println(printList(c))
    println(printList(gen(3)))

    def secondElmt(xs: IList): IOption = xs match {
      case INil() | ICons(_, INil()) => INone()
      case ICons(_, ICons(hd, _)) => ISome(hd)
      case _ => INone()
    }

    def factorial(n: Int): Int = n match {
      case 0 => 1
      case _ => n * factorial(n - 1)
    }

    def fib(n: Int): Int = n match {
      case 0 | 1 => 1
      case _ => fib(n - 1) + fib(n - 2)
    }

    println(factorial(3))
    println(fib(10))
  }
}
