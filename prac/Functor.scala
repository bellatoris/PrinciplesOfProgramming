import scala.language.higherKinds

object Main {
  def main(args: Array[String]): Unit = {
    trait Functor[F[_]] {
      def map[A, B](f: A => B)(x: F[A]): F[B]
    }

    def compose[F[_], A, B, C](g: B => C)(f: A => B)(a: F[A])(implicit proxy: Functor[F]): F[C] = 
      proxy.map(g)(proxy.map(f)(a))

    sealed abstract class MyTree[A]
    case class Empty[A]() extends MyTree[A]
    case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]

    implicit val ListFuncor: Functor[List] = new Functor[List] {
      def map[A, B](f: A => B)(x: List[A]) = x.map(f)
    }

    implicit val MyTreeFunctor: Functor[MyTree] = new Functor[MyTree] {
      def map[A, B](f: A => B)(x: MyTree[A]): MyTree[B] = x match {
        case Empty() => Empty()
        case Node(v, l, r) => Node(f(v), map(f)(l), map(f)(r)) 
      }
    }

    compose((x: Int) => x * x)((x: Int) => x + x)(List(1, 2, 3))
    
    val t : MyTree[Int] = Node(3,Node(4,Empty(),Empty()),Node(2,Empty(),Empty()))
    compose((x: Int)=> x * x)((x: Int) => x + x)(t)
  }
}
