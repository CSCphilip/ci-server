package server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;
import jdk.nashorn.internal.parser.JSONParser;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ajax.JSON;


/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);
        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code
        String who = request.getHeader("user-agent");
        String buildOK = "build not done";
        String notifyOK = "notification not sent";

        String URL = getRepoURL();
        String cloneOK = cloneRepo(URL);


        if(cloneOK.contains("Cloning OK")){
          buildOK = buildAndTest("path");
        }

        if(buildOK.contains("Build OK")){
          notifyOK = notify(buildOK);
        }

        System.out.println("Request handled");

        if(notifyOK.contains("Notification sent successfully")){
          System.out.println(notifyOK);
        }

      /*  if(who.contains("GitHub-Hookshot")){ //this branch is called if the request is a webhook
            //this reads the body of the webhook as a string that is formatted as a json
            BufferedReader br = request.getReader();
            String str;
            StringBuilder wholeStr = new StringBuilder();
            while ((str = br.readLine()) != null) {
                wholeStr.append(str);
            }
            String ss = wholeStr.toString();
            //this interprets the string as a json object so that it's parameters can be pulled
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(ss);
            //this extracts the branch in which the event occurred as lastOne
            String refs = jsonObject.get("ref").toString();
            String[] sss = refs.split("/");
            String lastOne = sss[sss.length - 1];
            //this extracts the url of the repository where the event occurred as git_url
            String git_url = jsonObject.getJSONObject("repository").get("git_url").toString();
            //Process p1 = Runtime.getRuntime().exec("cd C:\\Users\\Kalle\\git\\cloneplace");
            String git_url_fixed = git_url.replaceFirst("git", "https");
            //this clones the specified branch of the specified repository to the folder specified in folder_path
            String folder_path = " C:\\Users\\Kalle\\git\\cloneplace";
            Process p = Runtime.getRuntime().exec("git clone -b" + " " + lastOne + " " + git_url_fixed + folder_path);

            //this I don't quite know what it does
            InputStream fis = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader fg = new BufferedReader(isr);
            String line = null;
            //System.out.println("git clone -b" + " " + lastOne + " " + git_url_fixed + " ..\\new");
            while ((line = fg.readLine()) != null) {
                System.out.println(line);
                response.getWriter().println(line);
            }
        } */
    }

    public int dummyFunction() {
        //dummy function to start testing
        System.out.println("Calling dummyFunction");
        return 1;
    }

    public String getRepoURL(){
      //gets the URL for repository to be cloned
        System.out.println("Getting repository URL");
        String dummyReturn = "https://github.com/DD2480-Group-15/Assignment_2/";
        return dummyReturn;
    }

    public String cloneRepo(String url){
      //clones the repository, returns status of how it went
      System.out.println("Cloning repository "+ url);
      String cloneStatus = "Cloning OK";
      return cloneStatus;
    }

    public String buildAndTest(String path){
      //builds the specified repo path using Maven and returns the status of the build
      System.out.println("Running mvn package");
      String buildStatus = "Build and test ok";
      return buildStatus;
    }

    public String notify(String status){
      //sends notification of the build to the webhook
      System.out.println("Notifying GitHub of build status");
      String notificationStatus = "Notification sent successfully";
      return notificationStatus;
    }



    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
