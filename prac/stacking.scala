object Main {
  def main(args: Array[String]): Unit = {
    trait IntStack {
      def get(): (Int, IntStack)
      def put(x: Int): IntStack
    }

    class BasicIntStack protected (xs: List[Int]) extends IntStack {
      def this() = this(Nil)
      protected def mkStack(xs: List[Int]): IntStack = new BasicIntStack(xs)
      def get(): (Int, IntStack) = (xs.head, mkStack(xs.tail))
      def put(x: Int): IntStack = mkStack(x :: xs)
    }

    trait Doubling extends IntStack {
      println("Double")
      abstract override def put(x: Int): IntStack = super.put(2 * x)
    }
    trait Incrementing extends IntStack {
      println("Increment")
      abstract override def put(x: Int): IntStack = super.put(x + 1)
    }
    trait Filtering extends IntStack {
      println("Filter")
      abstract override def put(x: Int): IntStack = 
        if (x >= 0) super. put(x) else this
    }

    class DIFIntStack protected (xs: List[Int]) extends BasicIntStack(xs)
                                                with Doubling
                                                with Incrementing
                                                with Filtering {
      def this() = this(Nil)
      override def mkStack(xs: List[Int]): IntStack = new DIFIntStack(xs)
    }

    val s0 = new DIFIntStack()
    val s1 = s0.put(3)
    val s2 = s1.put(-2)
    val s3 = s2.put(4)
    val (v1, s4) = s3.get()
    println(v1, s4)
    val (v2, s5) = s4.get()
    println(v2, s5)

  }
}
