object Main {
  def main(args: Array[String]): Unit = {
    {
      // name tuple type
      object foo {
        val a = 3
        def b = a + 1 // 4
        def f(x: Int) = b + x
        def f(x: String) = "hello " + x
      }

      println(foo.f(3))
      println(foo.f("gil"))

      def g(x: { val a: Int; def b: Int; def f(x: Int): Int;
                 def f(x: String): String }) = 
        x.f(3)

      println(g(foo))
    }
    {
      val gn = 0
      object foo {
        val a = 3
        def b = a + 1 // 4
        val f(x: Int): Int = b + x + gn
      }

      println(foo.f(3)) // 7 same scoping rule

      type Foo = { val a: Int; def b: Int; def f(x: Int): Int }
      def g(x: Foo) = {
        val gn = 10
        x.f(3)
      }

      println(g(foo))
    } 
  }
}
