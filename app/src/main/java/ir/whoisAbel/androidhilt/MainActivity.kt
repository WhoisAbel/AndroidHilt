package ir.whoisAbel.androidhilt

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

/*
* If you want to inject your dependencies in activity,
* you only need to add @AndroidEntryPoint in your activity class.
* However, if you want to inject your dependencies in fragment,
* you need to add @AndroidEntryPoint in both fragment and activity that hosts the fragment.
*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /*
    * Field Injection:
    *
    * Use @Inject lateinit var on the class field where you
    * want Hilt automatically create the instance for you.
    *
    * In this example, Hilt will automatically create the SomeClass instance for you.
    */
    @Inject
    lateinit var someClass: SomeClass

    // Add this variable for learn @Scopes concepts
    @Inject
    lateinit var someClassTwo: SomeClass

    @Inject
    lateinit var useSomeInterface: UseSomeInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println(
            someClass
                .doAThing() + ", identityHashCode is:" + System.identityHashCode(someClass)
                .toString()
        )
        println(
            someClassTwo
                .doSomeOtherThing() + ", identityHashCode is:" + System.identityHashCode(
                someClassTwo
            )
                .toString()
        )

        println(
            useSomeInterface
                .getAThing() + ", identityHashCode is:" + System.identityHashCode(
                someClassTwo
            )
                .toString()
        )

    }
}

/*
* Constructor Injection:
*
* Hilt doesn't know how to create the SomeClass and SomeOtherClass.
* To do that, you need to add @Inject constructor() into the classes.
*/
@ActivityScoped
class SomeClass @Inject constructor(
    private val someOtherClass: SomeOtherClass
) {
    fun doAThing(): String = "Look I did a thing!"

    fun doSomeOtherThing(): String = someOtherClass.doSomeOtherThing()
}

class SomeOtherClass @Inject constructor() {
    fun doSomeOtherThing(): String = "Look I did some other thing!"
}

// ====================================================================================
// Add this section for learn @Scopes
@AndroidEntryPoint
class MyFragment : Fragment() {

    @Inject
    lateinit var someClass: SomeClass

}

// ====================================================================================
// Add this section for learn Constructor Injection Problems
// 1. Interface
// 2. Third-Party library

interface SomeInterface {
    fun getAThing(): String
}

class SomeInterfaceImpl @Inject constructor(
    private val someString: String
) : SomeInterface {
    override fun getAThing(): String {
        return someString
    }
}

class UseSomeInterface @Inject constructor(
    private val someInterfaceImpl: SomeInterface,
    private val gson: Gson
) {
    fun getAThing(): String {
        return "Look I got: ${someInterfaceImpl.getAThing()}"
    }
}

// ====================================================================================
/* Add this section for learn Hilt Modules, Binds and Provides -> Constructor Injection Solution
* 1. Interface
* 2. Third-Party library
*
* We have two solution for provide Interface and Third-Party
* 1. @Bind: Not work for all scenario.
* 2. @Provide: Work for all scenario.
*/

// First way:
/*
@InstallIn(ActivityComponent::class)
@Module
abstract class MyModuleForBinds {

    @ActivityScoped
    @Binds
    abstract fun bindSomeInterface(
        someInterfaceImpl: SomeInterfaceImpl
    ): SomeInterface

// Notice: We can not provide Gson with @Bind.

}
*/

// Second way:
@InstallIn(SingletonComponent::class)
@Module
class MyModuleForProvides {

    @Singleton
    @Provides
    fun provideSomeString(): String{
        return "A Thing"
    }

    @Singleton
    @Provides
    fun provideSomeInterface(
        someString: String
    ): SomeInterface {
        return SomeInterfaceImpl(someString)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

}