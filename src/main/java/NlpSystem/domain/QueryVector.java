package NlpSystem.domain;

import FeatureExtractionUtils.Word2VEC;

import java.util.Vector;

public class QueryVector {
    Vector<Float> vector = new Vector<Float>();

    public void setVector(Vector<Float> vector) {
        this.vector = vector;
    }

    public Vector<Float> getVector() {
        return vector;
    }

    public void Calc(Word2VEC word2VEC, Vector<String>ques){
        String temp=ques.get(0);
        float tempf[] =word2VEC.getWordVector(temp);
        float[] quesTemp = new float[word2VEC.getWordVector(ques.get(0)).length];
        for(String s:ques){
            float[] q = word2VEC.getWordVector(s);
            if(q == null){
                System.out.println(ques);
                System.out.print("!!!!");
            }
            for(int i = 0;i<q.length;i++){
                quesTemp[i] += q[i];
            }
        }
        for(float f:quesTemp){
            vector.add(f);
        }
    }

    public Vector<Float> minus(QueryVector b){
        Vector<Float> result = new Vector<Float>();

        for(int i = 0;i<b.getVector().size();i++){
            float j = vector.get(i) - b.getVector().get(i);
            result.add(j);
        }
        return result;
    }

    public double COS(QueryVector b){
        double result = 0.0;

        double fz = 0.0,fm1 = 0.0,fm2 = 0.0;
        for(int i = 0;i<vector.size();i++){
            fz += vector.get(i) * b.getVector().get(i);
            fm1 += vector.get(i) * vector.get(i);
            fm2 += b.getVector().get(i) * b.getVector().get(i);
        }

        fm1 = Math.sqrt(fm1) * Math.sqrt(fm2);

        result = fz/fm1;
        return  result;
    }
}
