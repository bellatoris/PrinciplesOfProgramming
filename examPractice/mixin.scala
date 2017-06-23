object Main {
  def main(args: Array[String]): Unit = {
    trait Iter[A] {
      def getValue: Option[A]
      def getNext: Iter[A]
    }

    class ListIter[A](list: List[A]) extends Iter[A] {
      def getValue = list.headOption
      def getNext = new ListIter(list.tail)
    }
    /*
    trait MRIter[A] extends Iter[A] {
      override def getNext: MRIter[A]
      def mapReduce[B, C](combine: (B, C) => C, ival: C, f: A => B): C =
      getValue match {
        case None => ival
        case Some(v) => combine(f(v), getNExt.mapReduce(combine, ival, f))      
      }
    }

    class MRListIter[A](list: List[A]) extends ListIter(list) with MRIter[A] {
      override def getNext: MRIter = new MRListIter(list.tail)
    }
    */

    trait MRIter[A] extends Iter[A] {
      def mapReduce[B, C](combine: (B, C) => C, ival: C, f: A => B): C = {
        def go(xs: Iter[A]): C = xs.getValue match {
          case None => ival
          case Some(n) => combine(f(n), go(xs.getNext))
        }
        go(this)
      }
    }

    class MRListIter[A](list: List[A]) extends ListIter(list) with MRIter[A] 

    val mr = new MRListIter[Int](List(3, 4, 5))
    println(mr.mapReduce[Int, Int]((b, c) => b + c, 0, a => a * a))
  }
}
