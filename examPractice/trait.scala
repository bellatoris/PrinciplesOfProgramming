object Main {
  def main(args: Array[String]): Unit = {
    class A(val a: Int) {
      def this() = this(0)
    }
    trait B {
      def f(x: Int): Int = x
    }
    trait C extends A with B {
      def g(x: Int): Int = x + a
    }
    trait D extends B {
      def h(x: Int): Int = f(x + 50)
    }
    class E extends A(10) with C with D {
      override def f(x: Int) = x * a
    }
    val e = new E

    val c = new A(10) with C
    println(c.g(10))

    abstract class Iter[A] {
      def getValue: Option[A]
      def getNext: Iter[A]
    }
    class ListIter[A](val list: List[A]) extends Iter[A] {
      def getValue = list.headOption
      def getNext = new ListIter(list.tail)
    }
    /*
    abstract class Dict[K, V](eq: (K, K) => Boolean) {
      def add(k: K, v: V): Dict[K, V]
      def find(k: K): Option[V]
    }
    */
    trait Dict[K, V] {
      def add(k: K, v: V): Dict[K, V]
      def find(k: K): Option[V]
    }

    class ListIterDict[K, V](eq: (K, K) => Boolean, 
      list: List[(K, V)]) extends ListIter[(K, V)](list) with Dict[K, V] {
      def add(k: K, v: V) = new ListIterDict(eq, (k, v) :: list)
      def find(k: K): Option[V] = list.filter(pair => eq(k, pair._1)).map(pair => pair._2).headOption
    }

    val d0 = new ListIterDict[Int, String]((x, y) => x == y, Nil)
    val d1 = d0.add(4, "four").add(3, "three")
    println(d1.find(5))
  }
}
