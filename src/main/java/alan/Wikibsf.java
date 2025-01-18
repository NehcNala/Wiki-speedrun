package alan;

import alan.wikiPage.Link;
import alan.wikiPage.WikiPage;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.net.MalformedURLException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import java.net.URL;

import java.util.*;
import java.io.File;

public class Wikibsf{
    final ObjectMapper mapper = new ObjectMapper();
    Queue<String> Queue = new LinkedList<String>();
    ArrayList<String> Visited = new ArrayList<>();
    Map<String, String> pageMap = new HashMap<String, String>();
    String goalPage;
    String currentPage;
    public Wikibsf(File input){
        try{
            Input inputs = mapper.readValue(input, Input.class);
            conductBFS(inputs.getStart(), inputs.getFinish());

        }
        catch (StreamReadException e){
            System.out.println(1);
        }
        catch(DatabindException e){
            System.out.println(2);
        }
        catch(IOException e){
            System.out.println("exception with IO");
        }
        
    }
    public void conductBFS(String startingPage, String goalPage){
        Visited.add(startingPage);
        currentPage = startingPage;
        while (!Visited.contains(goalPage)){
            searchAPI(currentPage);
            currentPage = Queue.poll();
        }
        String printingPage = goalPage;
        System.out.println("Path:");
        while (!startingPage.equals(printingPage)){
            System.out.println(printingPage);
            printingPage = pageMap.get(printingPage);
        }
        System.out.println(startingPage);
        System.out.println(true);
    }
    public void searchAPI(String x){
        String formattedx = x.replaceAll(" ", "_");
        try {
            URL searchUrl = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&prop=links&titles="+ formattedx + "&formatversion=2&pllimit=500");
            WikiPage linksFromPage = mapper.readValue(searchUrl, WikiPage.class);
            List<Link> linkList = linksFromPage.getQuery().getPages().get(0).getLinks();
            addLinkstoQueue(linkList);
            
        }
        catch (MalformedURLException e){
            System.out.println("You have entered an invalid URL");
        }
        catch (StreamReadException e){
            System.out.println(1);
        }
        catch(DatabindException e){
            System.out.println(2);
        }
        catch(IOException e){
            System.out.println("exception with IO");
        }
    }
    public void addLinkstoQueue(List<Link> linkList){
        if (linkList != null && linkList.size() > 0 ){
            for (Link link : linkList){
                String title = link.getTitle();
                if (!Visited.contains(title)){
                    if(!title.startsWith("Category:") || !title.startsWith("Template:") || !title.startsWith("Portal:")){
                        this.Visited.add(title);
                        this.Queue.add(title);
                        this.pageMap.put(title, currentPage);
                    }  
                }
                System.out.println(title);
                
            }
        }
    }
}