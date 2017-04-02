import scala.annotation.tailrec

object Main {
  def main(args: Array[String]): Unit = {
    sealed abstract class BTree[A]
    case class Leaf[A]() extends BTree[A]
    case class Node[A](v: A, left: BTree[A]=Leaf[A](), right: BTree[A]=Leaf[A]()) extends BTree[A]

    val a = Node(1, Node(2, Node(4), Node(5)), Node(3, Node(6), Node(7)))

    def find[A](t: BTree[A], key: A): Boolean = t match {
      case Leaf() => false
      case Node(v, left, right) => 
        if (v == key) true
        else find[A](left, key) || find[A](right, key)
    }

    def tailFind[A](t: BTree[A], key: A): Boolean = {
      @tailrec
      def nestedFind(treeList: List[BTree[A]]): Boolean = treeList match {
        case Nil => false
        case t :: ts => t match {
          case Leaf() => nestedFind(ts)
          case Node(v, left, right) =>
            if (v == key) true
            else nestedFind(left :: right :: ts)
        }
      }

      nestedFind(List(t))
    }

    println(tailFind[Int](a, 3))
    println(tailFind[Int](a, 8))

    def sum(t: BTree[Int]): Int = t match {
      case Leaf() => 0
      case Node(v, left, right) =>
        v + sum(left) + sum(right)
    }
  
    def tailSum(t: BTree[Int]) = {
      @tailrec
      def nestedSum(acc: Int, ts: List[BTree[Int]]): Int = ts match {
        case Nil => acc
        case t :: ts => t match {
          case Leaf() => nestedSum(acc, ts)
          case Node(v, left, right) => nestedSum(acc + v, left :: right :: ts)
        }
      }

      nestedSum(0, List(t))
    }

    def printTree1[A](tree: BTree[A]): Unit = tree match {
      case Leaf() => 
      case Node(v, left, right) => {
        printTree1[A](left)
        println(v)
        printTree1[A](right)
      }
    }

    def printTree2[A](tree: BTree[A]): Unit = tree match {
      case Leaf() => 
      case Node(v, left, right) => {
        println(v)
        printTree2[A](left)
        printTree2[A](right)
      }
    }

    def printTree3[A](tree: BTree[A]): Unit = tree match {
      case Leaf() => 
      case Node(v, left, right) => {
        printTree3[A](left)
        printTree3[A](right)
        println(v)
      }
    }

    println(tailSum(a))
  }
}
