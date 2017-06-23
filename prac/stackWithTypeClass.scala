import scala.language.higherKinds

object Main {
  def main(args: Array[String]): Unit = {
    trait IntStack[A] {
      def get(s: A): (Int, A)
      def put(s: A)(x: Int): A
    }
    
    trait Doubling[A] extends IntStack[A] {
      abstract override def put(s: A)(x: Int): A = super.put(s)(2 * x)
    }

    trait Incrementing[A] extends IntStack[A] {
      abstract override def put(s: A)(x: Int): A = super.put(s)(x + 1)
    }

    trait Filtering[A] extends IntStack[A] {
      abstract override def put(s: A)(x: Int): A =
        if (x >= 0) super.put(s)(x) else s
    }

    trait ListStackImpl extends IntStack[List[Int]] {
      def get(s: List[Int]) = (s.head, s.tail)
      def put(s: List[Int])(x: Int) = x :: s
    }

    val stkDIF: IntStack[List[Int]] = new ListStackImpl
                                      with Doubling[List[Int]]
                                      with Incrementing[List[Int]]
                                      with Filtering[List[Int]]

    /*
    val s0 = List[Int]()
    val s1 = stkDIF.put(s0)(3) 
    val s2 = stkDIF.put(s1)(-2) 
    val s3 = stkDIF.put(s2)(4) 
    val (v1,s4) = stkDIF.get(s3) 
    val (v2,s5) = stkDIF.get(s4)

    class SortedStack protected (xs: List[Int]) {
      override val toString = "Stack: " + xs.toString
      def this() = this(Nil)
      def get = (xs.head, new SortedStack(xs.tail))
      def put(x: Int) = {
        def go(l: List[Int]): List[Int] = l match {
          case Nil => x :: Nil
          case hd :: tl => if (x <= hd) x :: l else hd :: go(tl)
        }
        new SortedStack(go(xs))
      }
    }

    trait SortedStackImpl extends IntStack[SortedStack] {
      def get(s: SortedStack) = s.get
      def put(s: SortedStack)(x: Int) = s.put(x)
    }

    val sortedDIF: IntStack[SortedStack] = new SortedStackImpl
                                           with Doubling[SortedStack]
                                           with Incrementing[SortedStack]
                                           with Filtering[SortedStack]

    val s0 = new SortedStack
    val s1 = sortedDIF.put(s0)(3)
    val s2 = sortedDIF.put(s1)(-2)
    val s3 = sortedDIF.put(s2)(4) 
    val (v1,s4) = sortedDIF.get(s3) 
    val (v2,s5) = sortedDIF.get(s4)
    */

    class SortedStack protected (private val xs: List[Int]) {
      override val toString = "Stack: " + xs.toString
      def this() = this(Nil)
    }

    object SortedStack {
      trait Impl extends IntStack[SortedStack] {
        def get(s: SortedStack) = (s.xs.head, new SortedStack(s.xs.tail))
        def put(s: SortedStack)(x: Int) = {
          def go(l: List[Int]): List[Int] = l match {
            case Nil => x :: Nil
            case hd :: tl => if (x <= hd) x :: l else hd :: go(tl)
          }
          new SortedStack(go(s.xs))
        }
      }
    }
   
    val sortedDIF: IntStack[SortedStack] = new SortedStack.Impl
                                            with Doubling[SortedStack]
                                            with Incrementing[SortedStack]
                                            with Filtering[SortedStack]

    val s0 = new SortedStack
    val s1 = sortedDIF.put(s0)(3)
    val s2 = sortedDIF.put(s1)(-2)
    val s3 = sortedDIF.put(s2)(4) 
    val (v1,s4) = sortedDIF.get(s3) 
    val (v2,s5) = sortedDIF.get(s4)

  }
}
