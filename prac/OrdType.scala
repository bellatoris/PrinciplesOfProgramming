object Main {
  def main(args: Array[String]): Unit = {
    abstract class Ord[A] {
      def cmp(me: A, you: A): Int

      def ===(me: A, you: A): Boolean = cmp(me, you) == 0
      def <(me: A, you: A): Boolean = cmp(me, you) < 0
      def >(me: A, you: A): Boolean = cmp(me, you) > 0
      def <=(me: A, you: A): Boolean = cmp(me, you) <= 0
      def >=(me: A, you: A): Boolean = cmp(me, you) >= 0
    }

    def max3[A](a: A, b: A, c: A)(implicit ord: Ord[A]): A =
      if (ord <= (a, b)) { if (ord <= (b, c)) c else b }
      else { if (ord < (a, c)) c else a }

    implicit val intOrd: Ord[Int] = new Ord[Int] {
      def cmp(me: Int, you: Int) = me - you 
    }

    println(max3(3, 2, 10))
    
    class Bag[A] protected (val toList: List[A])(implicit ord: Ord[A]) { 
      def this()(implicit ord: Ord[A]) = this(Nil)(ord)

      def add(x: A): Bag[A] = {
        def go(elmts: List[A]): List[A] = elmts match {
          case Nil => x :: Nil
          case e :: _ if (ord < (x, e)) => x :: elmts
          case e :: _ if (ord === (x, e)) => elmts
          case e :: rest => e :: go(rest)
        }
        new Bag(go(toList))
      }
    }

    println((new Bag[Int]()).add(3).add(2).add(10).toList)

    // lexicographic order
    implicit def tup2Ord[A, B](implicit ordA: Ord[A], ordB: Ord[B]) = {
      new Ord[(A, B)] {
        def cmp(me: (A, B), you: (A, B)): Int = {
          val c1 = ordA.cmp(me._1, you._1)
          if (c1 != 0) c1
          else { ordB.cmp(me._2, you._2) }
        }
      }
    }

    val b = new Bag[(Int, (Int, Int))]
    println(b.add((3, (3, 4))).add((3, (2, 7))).add ((4, (0, 0))).toList)
  }
}
