package co.starec.runningapp.runther.framework.base

import android.support.annotation.AnimRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View

class FragmentNavigator {
    private val mFragmentManager: FragmentManager
    private var mRootFragmentTag: String? = null

    private var mDefaultInAnim: Int = 0
    private var mDefaultOutAnim: Int = 0
    private var mDefeaultPopInAnim: Int = 0
    private var mDefeaultPopOutAnim: Int = 0


    @IdRes
    private val mDefaultContainer: Int

    /**
     * This constructor should be only called once per

     * @param fragmentManager  Your FragmentManger
     * *
     * @param defaultContainer Your container id where the fragments should be placed
     * *
     * @param defaultInAnim    Your default animation for the "In Animation" it will be added on each
     * *                         transaction.
     * *
     * @param defaultOutAnim   Your default animation for the "Out Animation" it will be added on each
     * *                         transaction.
     */
    @JvmOverloads constructor(fragmentManager: FragmentManager, @IdRes defaultContainer: Int, @AnimRes defaultInAnim: Int = NO_RES_SET, @AnimRes defaultOutAnim: Int = NO_RES_SET) {
        mFragmentManager = fragmentManager
        mDefaultContainer = defaultContainer
        mDefaultInAnim = defaultInAnim
        mDefaultOutAnim = defaultOutAnim
    }

    /**
     * This constructor should be only called once per

     * @param fragmentManager  Your FragmentManger
     * *
     * @param defaultContainer Your container id where the fragments should be placed
     * *
     * @param defaultInAnim    Your default animation for the "In Animation" it will be added on each
     * *                         transaction.
     * *
     * @param defaultOutAnim   Your default animation for the "Out Animation" it will be added on each
     * *                         transaction.
     */
    constructor(fragmentManager: FragmentManager, @IdRes defaultContainer: Int, @AnimRes defaultInAnim: Int, @AnimRes defaultOutAnim: Int, @AnimRes defaultPopInAnim: Int, @AnimRes defaultPopOutAnim: Int) {
        mFragmentManager = fragmentManager
        mDefaultContainer = defaultContainer
        mDefaultInAnim = defaultInAnim
        mDefaultOutAnim = defaultOutAnim
        mDefeaultPopInAnim = defaultPopInAnim
        mDefeaultPopOutAnim = defaultPopOutAnim
    }


    /**
     * @return the current active fragment. If no fragment is active it return null.
     */
    val activeFragment: Fragment
        get() {
            val tag: String = if (mFragmentManager.backStackEntryCount == 0) {
                mRootFragmentTag as String
            } else {
                mFragmentManager
                        .getBackStackEntryAt(mFragmentManager.backStackEntryCount - 1).name
            }
            return mFragmentManager.findFragmentByTag(tag)
        }

    /**
     * @return the current active fragment tag. If no fragment is active it return null.
     */
    val activeFragmentTag: String
        get() {
            val tag: String
            if (mFragmentManager.backStackEntryCount == 0) {
                tag = mRootFragmentTag as String
            } else {
                tag = mFragmentManager
                        .getBackStackEntryAt(mFragmentManager.backStackEntryCount - 1).name
            }
            return tag
        }

    /**
     * Pushes the fragment, and add it to the history (BackStack) if you have set
     * a default animation it will be added to the transaction.

     * @param fragment The fragment which will be added
     */
    fun goTo(fragment: Fragment) {
        if (mDefeaultPopInAnim == 0 && mDefeaultPopOutAnim == 0)
            goTo(fragment, mDefaultInAnim, mDefaultOutAnim)
        else
            goTo(fragment, mDefaultInAnim, mDefaultOutAnim, mDefeaultPopInAnim, mDefeaultPopOutAnim)
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack) with the specific animation.
     * You have to set both animation.

     * @param fragment The fragment which will be added
     * *
     * @param inAnim   Your custom "In Animation"
     * *
     * @param outAnim  Your custom "Out Animation"
     */
    fun goTo(fragment: Fragment, @AnimRes inAnim: Int, @AnimRes outAnim: Int) {
        val transaction = mFragmentManager.beginTransaction()
        transaction.addToBackStack(getTag(fragment))
        if (inAnim != NO_RES_SET && outAnim != NO_RES_SET) {
            transaction.setCustomAnimations(inAnim, outAnim)
        }
        transaction.replace(mDefaultContainer, fragment, getTag(fragment))
        transaction.commit()
        mFragmentManager.executePendingTransactions()
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack) with the specific animation.
     * You have to set both animation.

     * @param fragment   The fragment which will be added
     * *
     * @param inAnim     Your custom "In Animation"
     * *
     * @param outAnim    Your custom "Out Animation"
     * *
     * @param popInAnim  Your custom "Pop In Animation"
     * *
     * @param popOutAnim Your custom "Pop Out Animation"
     */
    fun goTo(fragment: Fragment, @AnimRes inAnim: Int, @AnimRes outAnim: Int, @AnimRes popInAnim: Int, @AnimRes popOutAnim: Int) {
        val transaction = mFragmentManager.beginTransaction()
        transaction.addToBackStack(getTag(fragment))
        if (inAnim != NO_RES_SET && outAnim != NO_RES_SET) {
            transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim)
        }
        transaction.replace(mDefaultContainer, fragment, getTag(fragment))
        transaction.commit()
        mFragmentManager.executePendingTransactions()
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack) if you have set
     * a default animation it will be added to the transaction.

     * @param fragment The fragment which will be added
     */
    fun goTo(fragment: Fragment, view: View) {
        val transaction = mFragmentManager.beginTransaction()
        transaction.addSharedElement(view, view.transitionName)
        transaction.replace(mDefaultContainer, fragment, getTag(fragment))
        transaction.addToBackStack(getTag(fragment))
        transaction.commit()
    }

    /**
     * This is just a helper method which returns the simple name of
     * the fragment.

     * @param fragment That get added to the history (BackStack)
     * *
     * @return The simple name of the given fragment
     */
    fun getTag(fragment: Fragment): String {
        return fragment.javaClass.simpleName
    }

    /**
     * Set the ic_new root fragment. If there is any entry in the history (BackStack)
     * it will be deleted.

     * @param rootFragment The ic_new root fragment
     */
    fun setRootFragment(rootFragment: Fragment) {
        if (size > 0) {
            this.clearHistory()
        }
        mRootFragmentTag = getTag(rootFragment)
        this.replaceFragment(rootFragment)
    }

    val rootFragment: String
        get() {
            if (size > 0) {
                this.clearHistory()
            }
            return mRootFragmentTag as String
        }

    /**
     * Replace the current fragment with the given one, without to add it to backstack.
     * So when the users navigates away from the given fragment it will not appaer in
     * the history.

     * @param fragment The ic_new fragment
     */
    private fun replaceFragment(fragment: Fragment) {
        mFragmentManager.beginTransaction()
                .replace(mDefaultContainer, fragment, getTag(fragment))
                .commit()
        mFragmentManager.executePendingTransactions()
    }

    /**
     * Goes one entry back in the history
     */
    fun goOneBack() {
        mFragmentManager.popBackStackImmediate()
    }

    /**
     * @return The current size of the history (BackStack)
     */
    val size: Int
        get() = mFragmentManager.backStackEntryCount

    /**
     * @return True if no Fragment is in the History (BackStack)
     */
    val isEmpty: Boolean
        get() = size == 0

    /**
     * Goes the whole history back until to the first fragment in the history.
     * It would be the same if the user would click so many times the back button until
     * he reach the first fragment of the app.
     */
    fun gotToTheRootFragmentBack() {
        while (size >= 1) {
            goOneBack()
        }
    }

    /**
     * Clears the whole history so it will no BackStack entry there any more.
     */
    fun clearHistory() {
        while (mFragmentManager.backStackEntryCount > 0) {
            mFragmentManager.popBackStackImmediate()
        }
    }

    /**
     * Return fragment in BackStack has tag
     * @param tag
     * *
     * @return
     */
    fun getFragmentByTag(tag: String): Fragment {
        return mFragmentManager.findFragmentByTag(tag)
    }

    /**
     * Goes entry back to defind fragment by tag in the history
     */
    fun goBackToFragment(tag: String) {
        mFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    companion object {

        private val NO_RES_SET = 0
    }

    fun show(homeFragment: Fragment) {
        hideAll()
        val transaction = mFragmentManager.beginTransaction()
        if (!homeFragment.isAdded) {
            transaction.add(mDefaultContainer,homeFragment, getTag(homeFragment))
        }
        transaction.show(homeFragment)
        transaction.commit()
    }

    fun hide(homeFragment: Fragment) {
        val transaction = mFragmentManager.beginTransaction()
        transaction.hide(homeFragment)
        transaction.commit()
    }

    fun add(homeFragment: Fragment, addStack: Boolean) {
        val transaction = mFragmentManager.beginTransaction()
        transaction.add(mDefaultContainer, homeFragment, getTag(homeFragment))
        if (addStack)
            transaction.addToBackStack(getTag(homeFragment))
        transaction.commit()
    }

    private fun hideAll() {
        val transaction = mFragmentManager.beginTransaction()
        for (fragment in mFragmentManager.fragments) {
            transaction.hide(fragment)
        }
        transaction.commit()
    }
}
/**
 * This constructor should be only called once per

 * @param fragmentManager  Your FragmentManger
 * *
 * @param defaultContainer Your container id where the fragments should be placed
 */