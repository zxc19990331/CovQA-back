package NlpSystem.domain;

public class WordCode {
    private char TopLevel;
    private char MiddleLevel;
    private int SmallLevel;
    private char WordGroup;
    private int SingleGroup;
    private char Code;
    private String InitWord = new String();//原本的单词

    public String getInitWord() {
        return InitWord;
    }

    public void setInitWord(String initWord) {
        InitWord = initWord;
    }

    public char getTopLevel() {
        return TopLevel;
    }
    public void setTopLevel(char topLevel) {
        TopLevel = topLevel;
    }
    public char getMiddleLevel() {
        return MiddleLevel;
    }
    public void setMiddleLevel(char middleLevel) {
        MiddleLevel = middleLevel;
    }
    public int getSmallLevel() {
        return SmallLevel;
    }
    public void setSmallLevel(int smallLevel) {
        SmallLevel = smallLevel;
    }
    public char getWordGroup() {
        return WordGroup;
    }
    public void setWordGroup(char wordGroup) {
        WordGroup = wordGroup;
    }
    public int getSingleGroup() {
        return SingleGroup;
    }
    public void setSingleGroup(int singleGroup) {
        SingleGroup = singleGroup;
    }
    public char getCode() {
        return Code;
    }
    public void setCode(char code) {
        Code = code;
    }

    public WordCode(String code){
        char[] codeChar = new char[8];
        code.getChars(0,8,codeChar,0);
        TopLevel = codeChar[0];
        MiddleLevel = codeChar[1];
        SmallLevel = Integer.valueOf(String.valueOf(codeChar,2,2));
        WordGroup = codeChar[4];
        SingleGroup = Integer.valueOf(String.valueOf(codeChar,5,2));
        Code = codeChar[7];
    }

    public double Minus(WordCode B){
        double score = 0;
        if (TopLevel == B.getTopLevel())
            score+=1;
            if(MiddleLevel == B.getMiddleLevel()){
                score+=2;
                if(SmallLevel == B.getSmallLevel()){
                    score+=3;
                    if(WordGroup == B.WordGroup){
                        score+=4;
                        if(SingleGroup == B.getSingleGroup()){
                            score+=5;
                            if(Code == '='){
                                score *=1.1;
                        }
                    }
                }
            }
        }
        if(this.getInitWord().equals(B.getInitWord())){
                return  15.5;
        }
        return score;
    }

  /*  public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TopLevel);
        stringBuilder.append(MiddleLevel);
        if(SmallLevel<10){
            stringBuilder.append("0" + SmallLevel);
        }else {
            stringBuilder.append(SmallLevel);
        }
        stringBuilder.append(WordGroup);
        if(SmallLevel<10){
            stringBuilder.append("0" + SingleGroup);
        }else {
            stringBuilder.append(SingleGroup);
        }
        stringBuilder.append(Code);

        stringBuilder.append("," + InitWord);
        return  stringBuilder.toString();
    }*/
  public String toString(){
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(TopLevel);
      stringBuilder.append(MiddleLevel);
      if(SmallLevel<10){
          stringBuilder.append("0" + SmallLevel);
      }else {
          stringBuilder.append(SmallLevel);
      }
      stringBuilder.append(WordGroup);
      if(SingleGroup<10){
          stringBuilder.append("0" + SingleGroup);
      }else {
          stringBuilder.append(SingleGroup);
      }
      stringBuilder.append(Code);

      stringBuilder.append("," + InitWord);
      return  stringBuilder.toString();
  }

}
