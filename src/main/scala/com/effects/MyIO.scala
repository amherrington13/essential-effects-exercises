package com.effects

final case class MyIO[+A](unsafeRun: () => A) {
  def map[B](f: A => B): MyIO[B] =
    MyIO(() => f(unsafeRun()))
  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    MyIO(() => f(unsafeRun()).unsafeRun())
}

object MyIO {
  def putStrLn(str: => String): MyIO[Unit] = MyIO(() => println(str))
}

object MyIOApp extends App {
  val hello = MyIO.putStrLn("Hello, ")
  val friends = MyIO.putStrLn("functional programming friends!")
  val program = for {
    _ <- hello
    _ <- friends
  } yield ()

  program.unsafeRun()
}
