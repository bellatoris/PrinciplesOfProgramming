object Main {
  def main(args: Array[String]): Unit = {
    class FooType(x: Int, y: Int) {
      val a: Int = x
      def b: Int = a + y
      def f(z: Int): Int = b + y + z
    }

    class GeeType(x: Int) extends FooType(x + 1, x + 2) {
      override def f(z: Int) = b + z
      val c: Int = f(x) + b
    }

    (new GeeType(30)).c
    def test(f: FooType) = f.a + f.b
    test(new FooType(10, 20))
    test(new GeeType(30))

    class MyList[A]()

    class MyNil[A]() extends MyList[A]
    object MyNil { def apply[A]() = new MyNil[A]() }

    class MyCons[A](val hd: A, val tl: MyList[A]) extends MyList[A]
    object MyCons { def apply[A](hd: A, tl: MyList[A]) = new MyCons[A](hd, tl) }

    val t: MyList[Int] = MyCons(3, MyNil())

    class MyTree[A]()

    class Leaf[A]() extends MyTree[A]
    object Leaf {
      def apply[A]() = new Leaf[A]()
    }

    class Node[A](val value: A, val left: MyTree[A], 
                  val right: MyTree[A]) extends MyTree[A]
    object Node {
      def apply[A](v: A, l: MyTree[A] = Leaf[A](), r: MyTree[A] = Leaf[A]()) = 
        new Node[A](v, l, r)
    }

    val tree: MyTree[Int] = Node(3)
  }
}
