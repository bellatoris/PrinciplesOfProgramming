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

    def sumElements[I](xs: I)(implicit proxy: IntStack[I]): Int = {
      proxy.get(xs)._1
    }

    trait ListStackImpl extends IntStack[List[Int]] {
      def get(s: List[Int]) = (s.head, s.tail)
      def put(s: List[Int])(x: Int) = x :: s
    }

    implicit def listStack: IntStack[List[Int]] = new ListStackImpl 
                                                  with Doubling[List[Int]]
                                                  with Incrementing[List[Int]]
                                                  with Filtering[List[Int]]

    val s0 = List[Int]()
    val s1 = listStack.put(s0)(3)
    println(sumElements(s1))
        

    class SortedStack (val xs: List[Int]) {
      override val toString = "Stack: " + xs.toString
      def this() = this(Nil)
    }

    implicit def sortedStack: IntStack[SortedStack] = new IntStack[SortedStack] {
      def get(s: SortedStack) = (s.xs.head, new SortedStack(s.xs.tail))
      def put(s: SortedStack)(x: Int) = {
        def go(l: List[Int]): List[Int] = l match {
          case Nil => x :: Nil
          case hd :: tl => if (x <= hd) x :: l else hd :: go(tl)
        }
        new SortedStack(go(s.xs))
      }     
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

    trait A {
      def h(a: Int): Int
    }

    trait B extends A {
      def h(a: Int): Int = a
    }
    val a: B = new A with B
  }
}
