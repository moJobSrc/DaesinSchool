import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private val DEFAULT_VALUE_STRING = ""
    private val DEFAULT_VALUE_BOOLEAN = false
    private val DEFAULT_VALUE_INT = -1
    private val DEFAULT_VALUE_LONG = -1L
    private val DEFAULT_VALUE_FLOAT = -1f

    /**
     *
     * String 값 저장
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setString(key: String?, value: String?) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }


    /**
     *
     * boolean 값 저장
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setBoolean(key: String?, value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }


    /**
     *
     * int 값 저장
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setInt( key: String?, value: Int) {
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }


    /**
     *
     * long 값 저장
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setLong( key: String?, value: Long) {
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.apply()
    }


    /**
     *
     * float 값 저장
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setFloat( key: String?, value: Float) {
        val editor = prefs.edit()
        editor.putFloat(key, value)
        editor.apply()
    }


    /**
     *
     * String 값 로드
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getString( key: String?): String? {
        return prefs.getString(key, DEFAULT_VALUE_STRING)
    }


    /**
     *
     * boolean 값 로드
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getBoolean( key: String?): Boolean {
        return prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN)
    }


    /**
     *
     * int 값 로드
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getInt( key: String?): Int {
        return prefs.getInt(key, DEFAULT_VALUE_INT)
    }


    /**
     *
     * long 값 로드
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getLong(key: String?): Long {
        return prefs.getLong(key, DEFAULT_VALUE_LONG)
    }


    /**
     *
     * float 값 로드
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getFloat(key: String?): Float {
        return prefs.getFloat(key, DEFAULT_VALUE_FLOAT)
    }


    /**
     *
     * 키 값 삭제
     *
     * @param context
     *
     * @param key
     */
    fun removeKey(key: String?) {
        val edit = prefs.edit()
        edit.remove(key)
        edit.apply()
    }


    /**
     *
     * 모든 저장 데이터 삭제
     *
     * @param context
     */
    fun clear() {
        val edit = prefs.edit()
        edit.clear()
        edit.apply()
    }
}