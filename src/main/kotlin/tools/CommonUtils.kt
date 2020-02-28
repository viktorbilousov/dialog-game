package tools

class CommonUtils {
    companion object{
        public fun intToStr(int: Int) : String{
            if(int >= 0) return "+$int"
            else return "$int"
        }
        public fun spaces(int: Int) : String{
           var res = "";
            for(i in 0 until int) res += " ";
            return res;
        }
    }
}