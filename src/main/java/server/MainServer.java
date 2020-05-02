package server;

import entity.DataResult;

public class MainServer {
    public static DataResult getAnswer(String question){
        return DataResult.success("success","我什么也不知道");
    }
}
