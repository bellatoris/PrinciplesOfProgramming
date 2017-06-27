package pp201701.mid2
import pp201701.mid2.Data.DataBundle._

/*
 *  Midterm of Principles of Programming 2017
 *  
 *  General Instructions:
 *  - './compile.sh' compiles your code
 *  - './run.sh' runs a simple test case given in Test.scala
 *  - Before submission, check your code by running './run.sh'. Uncompilable code will get 0 points.
 *  - Do not use imperative features. (ex. for, while, var, etc)
 *  - Compress the 'src' folder with '.zip' format for submission.
 */

object Main {
  /*
   Problem 2: Binary Search Tree
   Related to HW3-2

   Implement 'insert' & 'lookup' for Binary Search Trees.

   For each Node, every key in the left subtree should be less than the root's key.
   Likewise, every key in the right subtree should be greater than the root's key.

   For 'insert' and 'lookup', comparator[cmp] will be given as an argument.
   The interpretation of the result of cmp(k1, k2):
     negative: k1 < k2
     zero:     k1 == k2
     positive: k1 > k2
   */
  def emptyBST[K, V]: BSTree[K, V] = Leaf()

  /* If the key already exists in the tree, then overwrite the value. */
  def insert[K, V]
    (t: BSTree[K, V], keyValue: (K, V), cmp: K => K => Int): BSTree[K, V] = t match {
    case Leaf() => Node(keyValue, Leaf(), Leaf())
    case Node(kv, l, r) => {
      if (cmp(keyValue._1)(kv._1) == 0) Node(keyValue, l, r)
      else if (cmp(keyValue._1)(kv._1) > 0) Node(kv, l, insert(r, keyValue, cmp))
      else Node(kv, insert(l, keyValue, cmp), r)
    }
    
  }

  /* For your test */
  def insertList[K, V]
    (seed: BSTree[K, V], keyValues: List[(K, V)], cmp: K => K => Int) =
    keyValues.foldLeft(seed)((tree, keyValue) => insert(tree, keyValue, cmp))

  def lookup[K, V](t: BSTree[K, V], key: K, cmp: K => K => Int): Option[V] = t match {
    case Leaf() => None
    case Node(kv, l, r) => {
      if (cmp(key)(kv._1) == 0) Some(kv._2)
      else if(cmp(key)(kv._1) > 0) lookup(r, key, cmp)
      else lookup(l, key, cmp)
    }
  }

}
