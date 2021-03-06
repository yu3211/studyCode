package kotlin_part1

import kotlin.reflect.KProperty

/**
 * Created by AU on 2017/07/13.
 */

// 演算子オーバロード--------------------------------------------------------------
// 等価性-------------------------------------------------------------------------
// 中置呼び出し--------------------------------------------------------------------
// 分解宣言-----------------------------------------------------------------------
// データクラス--------------------------------------------------------------------
// ネストしたクラス-----------------------------------------------------------------
// オブジェクト式-------------------------------------------------------------------
// オブジェクト宣言-----------------------------------------------------------------
// コンパニオンオブジェクト----------------------------------------------------------
// 代数的データ型-------------------------------------------------------------------
// 例外----------------------------------------------------------------------------
// メソッドの関数オブジェクト---------------------------------------------------------
// 委譲プロパティ-------------------------------------------------------------------
// アノテーション-------------------------------------------------------------------


fun main(args: Array<String>){
    // ネストしたクラス-------------------------------------------------------------------------
    // 内部クラス(inner class)
    // 外部クラスのオブジェクトの参照を囲むような、内部のクラスも定義可能(修飾しinnerを付けて、単なる内側のクラスと区別する)
    data class User2(val id: Long, val name: String){
        inner class Action{
            fun show(): String = "$name($id)"
        }
    }
    val user = User2(123, "Taro")
    val action : User2.Action = user.Action()
    println(action.show()) // Taro(123)


    // オブジェクト式---------------------------------------------------------------------------
    // オブジェクトリテラル：キーワードobjectの後に波括弧を展開し、オブジェクトを定義し、生成して返す
    val myObject = object{ fun greet(){ println("Hello") } }
    println(myObject) //
    println(myObject.greet()) // Hello

    val ggreeter = object: Ggreeter{ override fun greet(){println("Hello")} }
    println("ggreeter.greet() : " + ggreeter.greet()) // Hello


    // オブジェクト宣言---------------------------------------------------------------------------
    // シングルトンなクラスを定義する(class → objectを使用することで、クラス名で唯一のオブジェクトを参照することができるようになる)
    // コンストラクタを記述することはできず、呼び出すこともできない
    JapaneseGreeter4.greet("たろう") // こんにちは、たろうさん
    val greeter4: JapaneseGreeter4 = JapaneseGreeter4
    greeter4.greet("じろう") // こんにちは、じろうさん


    // コンパニオンオブジェクト-------------------------------------------------------------------
    // 1つのクラスにつき1つまでしか存在できない
    // コンパニオンオブジェクトは名前を省略することが可能
    val dummy = User5.Pool.DUMMY
    println("${dummy.id}, ${dummy.name}") // 0, dummy
    println(User5.DUMMY.name) // dummy

    // 名前を省略した場合の比較(Companionを利用する)
    println(if(User6.Companion.DUMMY === User6.DUMMY) true else false)


    // 代数的データ型---------------------------------------------------------------------------
    // 直和型
    class Cons<T>(val head: T, val tail: MyList<T>) : MyList<String> {override fun toString() = "$head:$tail"}
    println(Cons("foo", Cons("baz", Nil)))

    fun headString(list: MyList<*>): String =when (list){is Cons<*> -> list.head.toString() else -> "要素なし"}
    println(headString(Cons("foo",Nil))) // foo
    println(headString(Nil)) // 要素なし

    // シールドクラス(継承可能な範囲をコントロールする)
    // class Hoge: MyList2<Any>() error

    fun headString(list2: MyList2<*>): String =
            when(list2){
                is MyList2.Cons<*> -> list2.head.toString()
                is MyList2.Nil -> "要素なし"
            }
    println(headString(Cons("foo",Nil))) // foo
    println(headString(Nil)) // 要素なし

    // 列挙型
    val myFavoriteSize: DrinkSizeType = DrinkSizeType.Large
    println(myFavoriteSize) // kotlin_part1.DrinkSizeType$Large@41629346

    // enum
    val myFavoriteSize2: DrinkSizeType2 = DrinkSizeType2.LARGE
    print(myFavoriteSize2) // LARGE
    println(DrinkSizeType3.SMALL.milliliter) // 300

    println(DrinkSizeType4.MEDIUM.message()) // 無難な選択だ
    val types: Array<DrinkSizeType4> = DrinkSizeType4.values()
    println(types.toList()) //[SMALL, MEDIUM, LARGE]
    println(DrinkSizeType4.valueOf("LARGE")) // LARGE
    // println(DrinkSizeType4.valueOf("EXTRA_LARGE")) // IllegalArgumentExcepiton
    println(DrinkSizeType4.values().map { type -> type.ordinal }) // [0, 1, 2]


    // 例外-----------------------------------------------------------------------------------
    // try-catchによる例外の捕捉は義務付けられていない
    class MyException(message: String): Exception(message)
    // throw MyException("例外だよ")

    // try-catch-finally
    println(try{ println(1)} catch(e: Exception){println(2)} finally { println(3)})

    println(try{"123".toInt()} catch(e: Exception){null}) // 123

    println(try {"ONE".toInt()}
    catch (e:Exception) {null}
    finally { println("finally") })


    // メソッドの関数オブジェクト-----------------------------------------------------------------
    // 通常の感と同じようにメソッドの関数オブジェクトを取得することも可能。
    println(5.inc()) // 6
    val method = Int::inc
    println(method) // fun kotlin.Int.inc(): kotlin.Int

    // レシーバの型.(引数の型リスト)　-> 戻り値の型
    // メソッドの関数オブジェクトは、メソッドのように呼び出すということです
    val method2: Int.()-> Int = Int::inc
    println(123.method2()) // 124

    // public infix fun and(other: Int): Int
    val andObject2: Int.(Int) -> Int = Int::and
    println("andObject"+ 5.andObject2(10)) // 2

    // A.(B)->Cのような型は、型(A,B)->Cのサブタイプ
    val a2: Int.(Int) -> Int = Int::and
    val b2: (Int, Int) -> Int = a2
    println(b2(0b1100,0b1000)) // 8

    // ラムダ式
    println(listOf(1,2,3).map{it.inc()}) // [2,3,4]
    println(listOf(1,2,3).map(Int::inc)) // [2,3,4]

    val length: String.() -> Int = String::length
    println(listOf("Java", "Kotlin").map(length)) // [4,6]


    // 委譲プロパティ---------------------------------------------------------------------------
    // プロパティのアクセスがあった際に、その後の処理を別のオブジェクトに委譲します。
    class MyClass2{
        var _str: String? = null
        var str: String? by object {
            operator fun getValue(thisRef: MyClass2,
                                  property: KProperty<*>): String?{
                println("${property.name}がgetされました")
                return _str
            }

            operator fun setValue(thisRef: MyClass2,
                                  property: KProperty<*>, value: String?) {
                println("${property.name}に${value}がsetされました")
                _str = value
            }
        }
    }

    val myClass2 = MyClass2() // strがgetされました
    println(myClass2.str) // null
    myClass2.str = "ラーメン" //strにラーメンがsetされました
    println(myClass2.str) // strがgetされました ラーメン


    // アノテーション---------------------------------------------------------------------------
    /*
    class UserService{
        @Inject
        lateinit var userRepository: UserRepository
    }
    */

}

// オブジェクト式----------------------------------------------------------------------------
interface Ggreeter { fun greet() }

// オブジェクト宣言----------------------------------------------------------------------------
interface Greeter4 { fun greet(name: String) }
object JapaneseGreeter4: Greeter4{ override fun greet(name: String){ println("こんにちは、${name}さん") }}

// コンパニオンオブジェクト----------------------------------------------------------------------------
// シングルトンオブジェクトをクラス内に定義するには、キーワードcompanionを修飾する
class User5(val id: Long, val name: String){companion object Pool {
    val DUMMY = User5(0, "dummy")
}}

// object名省略----------------------------------------------------------------------------
class User6(val id: Long, val name: String){companion object {
    val DUMMY = User6(0, "dummy")
}}

// 代数的データ型-----------------------------------------------------------------------------
interface MyList<out T>
object Nil: MyList<Nothing>{fun toSting()="Nil"}

//シールドクラスとは、そのクラスの継承可能な範囲を制御するようなクラスのこと
// 修飾子sealedを伴い、そのクラスを継承可能なのは、ネスとされたクラスに限定されたMyListをシールドクラスとして、改めてリストを実施します
sealed class MyList2<out T>{
    object Nil: MyList2<Nothing>(){
        override fun toString() = "Nil"
    }
    class Cons<T>(val head: T, val tail: MyList2<T>): MyList2<T>(){override fun toString() = "$head:$tail"}
}

// 列挙型
sealed class DrinkSizeType{
    object small: DrinkSizeType()
    object Medium: DrinkSizeType()
    object Large: DrinkSizeType()
}

enum class DrinkSizeType2{
    SMALL,MEDIUM,LARGE
}

enum class DrinkSizeType3(val milliliter: Int){
    SMALL(300), MEDIUM(350), LARGE(500)
}

enum class DrinkSizeType4(val milliliter: Int){
    SMALL(300) {override fun message(): String = "少ないよ"}
    , MEDIUM(350){override fun message(): String = "無難な選択だ"}
    , LARGE(500){override fun message(): String = "流石に飲み過ぎ"};

    abstract fun message(): String
}