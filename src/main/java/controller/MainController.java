package controller;

import SpiderUtils.Spider;
import entity.DataResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import server.MainServer;

@CrossOrigin
@Controller
public class MainController {

    @RequestMapping("/getAnswer")
    @ResponseBody
    public DataResult getAnswer(@RequestParam("question")String question){
        return MainServer.getAnswer(question);
    }

    @RequestMapping("/updateQuestionDBAuto")
    @ResponseBody
    public DataResult updateDB(){
        return MainServer.updateDB();
    }

    @RequestMapping("/updateQuestionDBManual")
    @ResponseBody
    public DataResult updateDBManual(@RequestParam("question") String question,@RequestParam("answer")String answer){
        return MainServer.updateDBManual(question,answer);
    }
}
