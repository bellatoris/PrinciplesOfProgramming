object Main {
  def main(args: Array[String]): Unit = {
    trait Iter[A] {
      def getValue: Option[A]
      def getNext: Iter[A]
    }

    class ListIter[A](list: List[A]) extends Iter[A] {
      def getValue = list.headOption
      def getNext: Iter[A] = new ListIter(list.tail)
    }

    trait MRIter[A] extends Iter[A] {
      override def getNext: MRIter[A]
      def mapReduce[B, C](combine: (B, C) => C, ival: C, f: A => B): C = getValue match {
        case None => ival
        case Some(v) => combine(f(v), getNext.mapReduce(combine, ival, f))
      }
    }

    class MRListIter[A](list: List[A]) extends ListIter(list) with MRIter[A] {
      override def getNext: MRListIter[A] = new MRListIter(list.tail)
    }

  }
}

