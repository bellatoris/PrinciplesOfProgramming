import scala.annotation._


object Main {
  def main(args: Array[String]): Unit = {
    
    sealed abstract class MyOption[A]
    case class MyNone[A]() extends MyOption[A]
    case class MySome[A](some: A) extends MyOption[A]

    sealed abstract class MyList[A] {
      def append(xs: MyList[A]): MyList[A]
    }
    case class MyNil[A]() extends MyList[A] {
      def append(xs: MyList[A]) = xs
    }
    case class MyCons[A](hd: A, tl: MyList[A]) extends MyList[A] {
      def append(xs: MyList[A]) = MyCons(hd, tl.append(xs))
    }


    {
      sealed abstract class BTree[A]
      case class Leaf[A]() extends BTree[A]
      case class Node[A](value: A, left: BTree[A], right: BTree[A]) extends BTree[A]
    
      val x: MyList[Int] = MyCons(3, MyNil())
      val y: MyList[String] = MyCons("abc", MyNil())

      type BSTree[A] = BTree[(Int, A)]
      @tailrec
      def lookup[A](t: BSTree[A], key: Int): MyOption[A] = t match {
        case Leaf() => MyNone()
        case Node(kv, l, r) => {
          if (kv._1 == key) MySome(kv._2)
          else if (key > kv._1) lookup(r, key)
          else lookup(l, key)
        }
      }
      def t : BSTree[String] = Node((5,"My5"), Node((4,"My4"),Node((2,"My2"),Leaf(),Leaf()),Leaf()), Node((7,"My7"),Node((6,"My6"),Leaf(),Leaf()),Leaf()))

      println(lookup(t, 7))
      println(lookup(t, 3))
      
      def lookup2[A](t: BSTree[A], key: Int): MyOption[A] = {
        @tailrec 
        def nested(list: MyList[BSTree[A]]): MyOption[A] = list match {
          case MyNil() => MyNone()
          case MyCons(tree, tail) => tree match {
            case Leaf() => nested(tail)
            case Node(kv, l, r) => {
              if (kv._1 == key) MySome(kv._2)
              else nested(MyCons(l, MyCons(r, MyNil())).append(tail))
            }
          }
        }

        nested(MyCons(t, MyNil()))
      }

      println(lookup2(t, 7))
      println(lookup2(t, 3))
    
    }

    {
      sealed abstract class BSTree[A]
      case class Leaf[A]() extends BSTree[A]
      case class Node[A](key: Int, value: A, left: BSTree[A], right: BSTree[A]) extends BSTree[A]
      def lookup[A](t: BSTree[A], key: Int): MyOption[A] = t match {
        case Leaf() => MyNone()
        case Node(k, v, l, r) => {
          if (k == key) MySome(v)
          else if (key > k) lookup(r, key)
          else lookup(l, key)
        }
      }

      def t: BSTree[String] = Node(5,"My5", Node(4,"My4",Node(2,"My2",Leaf(),Leaf()),Leaf()), Node(7,"My7",Node(6,"My6",Leaf(),Leaf()),Leaf()))
            
      println(lookup(t, 7))
      println(lookup(t, 3))
    }
  }
}
