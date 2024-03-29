package com.github.zastrixarundell.torambot.objects;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class ItemObject
{

    private String name, price, proc, app;
    private ArrayList<String> stats = new ArrayList<>();
    private ArrayList<String> obtainedFrom = new ArrayList<>();

    public ItemObject(Element itemData)
    {
        //Name, type and duration
        name = itemData.getElementsByTag("h4").first().text();

        name = name.replace("   ", " ");

        //Price and proc
        Element ppStatTable = itemData.getElementsByClass("stat-table").first();
        Element tableBody = ppStatTable.getElementsByTag("tbody").first();

        Element priceTr = tableBody.getElementsByTag("tr").first();
        price = priceTr.getElementsByTag("td").last().ownText();

        Element procTr = tableBody.getElementsByTag("tr").get(1);
        proc = procTr.getElementsByTag("td").last().ownText();

        //Image
        try
        {
            Element appDiv = itemData.getElementsByClass("app-div").first();
            Element imageTd = appDiv.getElementsByTag("td").first();
            String appUrl = imageTd.attr("background");
            app = "http://coryn.club/" + appUrl.replace(" ", "%20");
        }
        catch (Exception e)
        {
            app = null;
        }

        //Stats
        try
        {

            Element myTabContent = itemData.getElementById("myTabContent");
            Element realStatTable = myTabContent.getElementsByClass("stat-table").last();
            Element statBody = realStatTable.getElementsByTag("tbody").first();

            for (Element trElement : statBody.getElementsByTag("tr"))
                stats.add(trElement.getElementsByTag("td").first().ownText() + ": " +
                        trElement.getElementsByTag("td").get(1).text());
        }
        catch (Exception e)
        {
            stats.add("N/A");
        }

        //ObtainedFrom
        //pad5-table
        try
        {
            Element myTabContent = itemData.getElementById("myTabContent");
            Element obtainedFromTable = myTabContent.getElementsByClass("pad5-table").first();
            Element obtainedFromBody = obtainedFromTable.getElementsByTag("tbody").last();

            for (Element trElement : obtainedFromBody.getElementsByTag("tr"))
            {
                Element tdElement = trElement.getElementsByTag("td").first();

                String value =
                        tdElement.getElementsByTag("font").first().ownText();

                try
                {
                    value = value + " " +
                            tdElement.getElementsByTag("font").last()
                                    .getElementsByTag("a").first().ownText();
                }
                catch (Exception e)
                {
                    value = value + " " +
                            tdElement.getElementsByTag("font").last().ownText();
                }

                obtainedFrom.add(value);
            }
        }
        catch (Exception e)
        {
            obtainedFrom.add("N/A");
        }
    }

    public String getName()
    {
        return name;
    }

    public String getPrice()
    {
        return price;
    }

    public String getProc()
    {
        return proc;
    }

    public String getApp()
    {
        return app;
    }

    public ArrayList<String> getStats()
    {
        return stats;
    }

    public ArrayList<String> getObtainedFrom()
    {
        return obtainedFrom;
    }

}
