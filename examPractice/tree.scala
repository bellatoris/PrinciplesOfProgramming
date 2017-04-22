import scala.annotation._


object Main {
  def main(args: Array[String]): Unit = {
    sealed abstract class BTree
    case class Leaf() extends BTree
    case class Node(value: Int, left: BTree, right: BTree) extends BTree

    sealed abstract class IList[A] {
      def append(xs: IList[A]): IList[A]
    }
    case class INil[A]() extends IList[A] {
      def append(xs: IList[A]) = xs
    }
    case class ICons[A](hd: A, tl: IList[A]) extends IList[A] {
      def append(xs: IList[A]) = ICons(hd, tl.append(xs))
    }

    def find(t: BTree, x: Int): Boolean = t match {
      case Leaf() => false
      case Node(value, left, right) => {
        if (value == x) true
        else find(left, x) || find(right, x)
      }
    }

    def find2(t: BTree, x: Int): Boolean = {
      @tailrec
      def nestedFind(list: IList[BTree]): Boolean = list match {
        case INil() => false
        case ICons(hd, tl) => hd match {
          case Leaf() => nestedFind(tl)
          case Node(value, left, right) => {
            if (value == x) true
            else nestedFind(ICons(left, ICons(right, INil())).append(tl))
          }
        }
      }

      nestedFind(ICons(t, INil()))
    }

    def sum(t: BTree): Int = {
      @tailrec
      def nestedSum(acc: Int, list: IList[BTree]): Int = list match {
        case INil() => acc
        case ICons(tree, tail) => tree match {
          case Leaf() => nestedSum(acc, tail)
          case Node(value, left, right) => nestedSum(acc + value, 
                        ICons(left, ICons(right, INil())).append(tail))
        }
      }
      nestedSum(0, ICons(t, INil()))
    }

    def t: BTree = Node(5,Node(4,Node(2,Leaf(),Leaf()),Leaf()), Node(7,Node(6,Leaf(),Leaf()),Leaf()))
    println(find(t, 7))
    println(find2(t, 7))
    println(find2(t, 10))
    println(sum(t))
  }
}
