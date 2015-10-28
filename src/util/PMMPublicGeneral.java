package util;

public class PMMPublicGeneral {
  public static boolean isThisWordInWords(String word,Object[] words){
    if (words.length < 1){
      return false;
    }
    for (int i = 0 ; i < words.length ; i++){
      if (word.equals(words[i].toString())){
        return true;
      }
    }
    return false;
  }

  public static String Integer2String(Integer i, int ilen, String mask){
    String ret = i.toString();
    if (ret.length() < ilen){
      if ( mask.length() <1 )
        mask = " ";
      while ( ret.length() < ilen )
        ret = mask + ret;
      ret = ret.substring(ret.length() - ilen);
    }
    return ret;
  }
}

