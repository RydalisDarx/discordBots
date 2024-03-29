package com.github.zastrixarundell.torambot.commands.toramwebsite;

import com.github.zastrixarundell.torambot.ToramBot;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;

public class News implements MessageCreateListener
{

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent)
    {

        if (!messageCreateEvent.getMessageAuthor().isRegularUser())
            return;

        if (!messageCreateEvent.getMessageContent().toLowerCase().startsWith(ToramBot.getPrefix() + "news"))
            return;

        if (ToramBot.TimeOut(messageCreateEvent))
            return;

        try
        {
            Document document = Jsoup.connect("https://en.toram.jp/#news").get();

            Elements links = document.select("a[href]");

            for (Element link : links)
                if (link.attr("href").contains("en.toram.jp/information/detail/?information_id="))
                {

                    document = Jsoup.connect("https:" + link.attr("href")).get();

                    Elements divs = document.getElementsByClass("useBox newsBox");
                    Elements Headers = document.getElementsByTag("h1");
                    Element header = Headers.first();
                    Element div = divs.first();

                    String text = div.text();
                    text = text.substring(header.ownText().length() + 12, 267 + header.ownText().length());
                    text = text + "... open to read more!";

                    EmbedBuilder embed = new EmbedBuilder().
                            setTitle(header.ownText())
                            .setDescription(text)
                            .setUrl("https:" + link.attr("href"))
                            .setThumbnail(ToramBot.logo())
                            .setImage(link.getElementsByTag("img").attr("src"));

                    if (messageCreateEvent.getServer().isPresent())
                        if (messageCreateEvent.getServer().get().getHighestRole(messageCreateEvent.getApi().getYourself()).isPresent())
                        {
                            Role role = messageCreateEvent.getServer().get().getHighestRole(messageCreateEvent.getApi().getYourself()).get();
                            if (role.getColor().isPresent())
                            {
                                Color color = role.getColor().get();
                                embed.setColor(color);
                            }
                        }

                    messageCreateEvent.getChannel().sendMessage(embed);
                    break;
                }
        }
        catch (Exception ignore)
        {
        }

        ToramBot.updateTime();
    }

}
