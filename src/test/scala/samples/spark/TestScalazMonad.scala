package samples.spark

import org.scalatest.FlatSpec
import scalaz.Monad

import scala.io.StdIn


class TestScalazMonad extends FlatSpec{
    "Scalaz IO Monad" should "" in {
        trait MyIO[+A] { self =>
            def run: A
            // Functor
            def map[B](f: A => B): MyIO[B] = new MyIO[B] {
                def run = f(self.run)
            }
            // Monad
            def flatMap[B](f: A => MyIO[B]): MyIO[B] = f(run) /*new MyIO[B] {
                def run = f(self.run).run
            }*/
        }

        object MyIO {
            /** 新建一个 trait MyIO 实例 */
            //def apply[A](a: A) = new MyIO[A] { def run = a }
            def apply[A](a: A)(implicit ioMonad: Monad[MyIO]) = ioMonad.point(a)

            implicit val ioMonad = new Monad[MyIO] {
                /** point 函数生成高阶类型: T[A] **/
                def point[A](a: => A) = new MyIO[A] { def run = a }

                /** bind 从 point 生成的 T[A] 中读取数据，然后根据 T.flatMap 提供的 f 转成 T[B] **/
                def bind[A, B](myIO: MyIO[A])(f: A => MyIO[B]): MyIO[B] = myIO flatMap f
            }
        }

        def ask(prompt: String): MyIO[String] = MyIO{
            println(prompt)
            StdIn.readLine()  // 从 stdio 读取
        } // 参数成为 run，在 flatMap的时候被 f 调用读取。

        def tell(msg: String): MyIO[Unit] = MyIO{
            println(msg)
        }

        val res: MyIO[Unit] = for {
            first <- ask("What's your first name?")
            last <- ask("What's your last name?")
            _ <- tell(s"Hello $first $last!")
        } yield()

        println(res)
    }

    "Scalaz Bag Monad" should "" in {
        trait Bag[A] { self =>
            def content: A
            // Monad
            def flatMap[B](f: A => Bag[B]): Bag[B] = f(content) /*new Bag[B] {
                def content = f(self.content).content
            }*/
        }

        object Bag {
            def apply[A](a: A)(implicit bagMonad:Monad[Bag]) = bagMonad.point(a)

            implicit object bagMonad extends Monad[Bag] {
                /** point 函数生成高阶类型: Bag[A] **/
                def point[Int](a: => Int) = new Bag[Int] { def content = a }

                /** bind 从 point 生成的 Bag[A] 中读取数据，然后根据 Bag.flatMap 提供的 f 转成 Bag[B] **/
                def bind[Int,B](bag: Bag[Int])(f: Int => Bag[B]): Bag[B] = bag flatMap f
            }
        }

        val res = Bag(3) flatMap {a => Bag(4) flatMap {b => Bag(5) flatMap  {c => Bag(a+b+c) }}}
        println(res.content)  // 12
    }
}
