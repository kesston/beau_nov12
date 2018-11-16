package Workshop01.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@WebServlet(urlPatterns = {"/weather"})
public class WeatherServlet extends HttpServlet {

    private static final String weather_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String APPID = "dd2affadfe34c4a1b6bdd3bb53f03277";
            
            
            
    private Client client;

    //Initalize the client   
    @Override
    public void init() throws ServletException {
        client = ClientBuilder.newClient();
    }

    //Cleanup
    @Override
    public void destroy() {
        client.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cityName = req.getParameter("cityName");

        //if cityname is not provided, then default to singapore
        if ((cityName == null) || (cityName.trim().length() <= 0)) {
            cityName = "Singapore";
        }

        //Create the target
        WebTarget target = client.target(weather_URL);
        //set the query string
        //target = client.target (Weather_URL)
        target = target.queryParam("q", cityName);
        target = target.queryParam("appid", APPID);
        target = target.queryParam("units", "metric");

        //Creat invocation, except JSON as result
        Invocation.Builder inv = target.request(MediaType.APPLICATION_JSON);

        // make the call using nGET HTTP method
        JsonObject result = inv.get(JsonObject.class);
        JsonArray weatherDetails = result.getJsonArray("weather");

        log("RESULT: " + result);

        //Echo back the same
        //setc 200 ok status code
        resp.setStatus(HttpServletResponse.SC_OK);

        //Set the content ntype/media type
        //we are returning text/html
        resp.setContentType(MediaType.TEXT_HTML);

        try (PrintWriter pw = resp.getWriter()) {
            pw.print("<h2>The weather " + cityName.toUpperCase()+ "</h2>");
            for (int i = 0; i < weatherDetails.size(); i++) {
                JsonObject wd = weatherDetails.getJsonObject(i);
                String main = wd.getString("main");
                String description = wd.getString("description");
                String icon = wd.getString("icon");

                pw.print("div");
                pw.print("%S &dash; %S" + main + description);
                pw.printf("<img src=\"http://openweathermap.org/img/w/%s.png\">", icon);

                pw.print("</div>");
            }
        }
    }

}
