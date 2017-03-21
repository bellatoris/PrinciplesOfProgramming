def sum(n: Int): Int = {
  if (n == 0) 0
  else n + sum(n-1)
}

def tailSum(n: Int): Int = {
  def nestedSum(n: Int, sum: Int): Int = {
    if (n == 0) sum
    else nestedSum(n-1, sum + n)
  }
  nestedSum(n, 0)
}

println(tailSum(1000000))
println(sum(1000000))
