package samples.spark

import org.scalatest.FlatSpec
import org.apache.spark.sql.SparkSession

class TestMonad extends FlatSpec{
    "map" should "be able to be filtered" in {
        val res = List(1,"6",3,"4",2,5).filter{ case _:Int => true; case _=>false }.map(_.asInstanceOf[Int]).reduce((a,b) => if(a>b)a else b )
        println(res)
    }

    "中缀表达式匹配实际上是对 case class 进行匹配的语法糖" should "" in {
        /**
          * 例子一：
          * case class 可以作为 match case 的中缀匹配 */
        case class Cont(a:Int, b:Int)
        Cont(1,2) match {
            case a Cont b => println(a, b)
            case _ => println("No")
        }

        // 这里的 "::" 不是 List 运算符，Scala 提供了一个同名的 case class。包括 +: 等符号都有。
        val l = ::(1, List(2, 3, 4))
        l match {
            case h :: t => println(t, h)
        }

        /** 例子二:
          * 含有两个泛型参数的类可以表达为中缀数据类型
          */
        trait |+|[A, B] {def apply(l:A,r:B):A}
        class Add extends |+|[Int, String] {  // 也可以定义为 object Add extends ...
            override def apply(l: Int, r: String) = l + r.toInt
        }
        def add: Int |+| String = new Add   // “Int |+| String” 可以作为数据类型，等同于  |+|[Int, String]
        println(add(1,"2"))
    }

    "eat-conversion 是 lambda calculus 的一种表达形式，和 partially applied function 不一样" should "" in {
        // https://wiki.haskell.org/Eta_conversion
        def foo(a:Any, b:Any)(f:(Any,Any)=>Unit) = f(a,b)
        def bar(x:Any,y:Any) = println(x, y)
        foo("aaa", "bbb"){bar _}  // partially applied function: 等价于 bar(_,_)。运行时会先生成 by-value 值。
        foo("aaa", "bbb"){bar}    // eta-reduction: 编译器自动展开成 (x,y)=>bar(x,y)称为 eta-abstraction，bi-direction 合称 eat-conversion。
    }

    "Monad" should "" in {
        trait SemiGroup[A] {
            def op(a1: A, a2: A): A
        }

        trait Monoid[A] extends SemiGroup[A]{
            def zero:A
        }

        trait Functor[T[_]] {
            def map[A,B](a:T[A])(f:A=>B):T[B]
        }

        trait Applicative[T[_]] {
            def ap[A, B](a: T[A])(f: A => T[A => B]): T[B]
        }

        trait Monad[A]{
            def flatMap[A,B, T[_]](a:T[A])(f:A => T[B]):T[B]
        }
    }
}
