object Main {
  def main(args: Array[String]): Unit = {
    object gee {
      val a: Int = 10
      def b: Int = a + 20
      def f(z: Int): Int = b + 20 + z
    }
    type gee_type = { val a: Int; def b: Int; def f(z: Int): Int }

    class foo_type(x: Int, y: Int) {
      val a: Int = x
      def b: Int = a + y
      def f(z: Int): Int = b + y + z
    }
    val foo: foo_type = new foo_type(10, 20)

    def zoo_type(x: Int, y: Int) = new {
      val a = x
      val b = a + y
      def f(z: Int): Int = b + y + z
    }

    class foo_type2(x: Int, y: Int) {
      val a: Int = x
      def b: Int = a + 20
      def f(z: Int): Int = b + 20 + z
    }

    val gee1: gee_type = zoo_type(10, 20)
    val gee2: gee_type = new foo_type(10, 20)

    class MyList[A](v: A, nxt: Option[MyList[A]]) {
      val value: A = v
      val next: Option[MyList[A]] = nxt
    }
    type YourList[A] = Option[MyList[A]]

    val t: YourList[Int] = Some(new MyList(3, Some(new MyList(4, None))))
  }
}
