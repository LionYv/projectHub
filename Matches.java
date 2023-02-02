package com.example.theproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import static org.jsoup.Jsoup.connect;

public  class Matches implements Runnable , Serializable {
     String HomeTeam = "";
    String AwayTeam = "";
    String league = "";
    Match [] matches = null;
    String  HomeGoals;
    String AwayGoals;
    String countdown;
    String timeOfGame;
    String minOfGame;
    String status;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");

    Map<String, String> links = new LinkedHashMap<>();
    int cnt = 0;

    public  Matches()
    {


        /*
        links.put("Finland", "https://www.worldometers.info/img/flags/fi-flag.gif");
        links.put("Belgium", "https://www.worldometers.info/img/flags/be-flag.gif");
        links.put("Wales", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Flag_of_Wales_%281959%29.svg/256px-Flag_of_Wales_%281959%29.svg.png");
        links.put("Belarus", "https://www.worldometers.info/img/flags/bo-flag.gif");
        links.put("Norway", "https://www.worldometers.info/img/flags/no-flag.gif");
        links.put("Latvia", "https://www.worldometers.info/img/flags/lg-flag.gif");
        links.put("Gibraltar", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/02/Flag_of_Gibraltar.svg/256px-Flag_of_Gibraltar.svg.png");
        links.put("Montenegro", "https://www.worldometers.info/img/flags/mj-flag.gif");
        links.put("Netherlands", "https://www.worldometers.info/img/flags/nl-flag.gif");
        links.put("Turkey", "https://www.worldometers.info/img/flags/tu-flag.gif");
        links.put("France", "https://www.worldometers.info/img/flags/fr-flag.gif");
        links.put("Kazakhstan", "https://www.worldometers.info/img/flags/kz-flag.gif");
        links.put("Estonia", "https://www.worldometers.info/img/flags/en-flag.gif");
        links.put("Israel", "https://upload.wikimedia.org/wikipedia/en/thumb/8/85/Israel_football_association.svg/256px-Israel_football_association.svg.png");
        links.put("maccabi haifa" , "https://upload.wikimedia.org/wikipedia/en/d/db/Maccabi_Haifa_FC_Logo_2020.png");

        links.put("barcelona", "https://upload.wikimedia.org/wikipedia/en/thumb/4/47/FC_Barcelona_%28crest%29.svg/1200px-FC_Barcelona_%28crest%29.svg.png");
        links.put("real sociedad", "https://upload.wikimedia.org/wikipedia/he/e/e6/Real_Sociedad_logo.svg.png1.png");
        links.put("real madrid", "https://upload.wikimedia.org/wikipedia/en/thumb/5/56/Real_Madrid_CF.svg/1200px-Real_Madrid_CF.svg.png");
        links.put("sevilla", "https://upload.wikimedia.org/wikipedia/en/thumb/3/3b/Sevilla_FC_logo.svg/700px-Sevilla_FC_logo.svg.png");
        links.put("atlético madrid", "https://upload.wikimedia.org/wikipedia/en/thumb/c/c1/Atletico_Madrid_logo.svg/1200px-Atletico_Madrid_logo.svg.png");
        links.put("betis", "https://pbs.twimg.com/profile_images/1451103607701520384/beIScbig_400x400.jpg");
        links.put("rayo vallecano", "https://upload.wikimedia.org/wikipedia/he/1/17/Rayo_Vallecano_logo.png");
        links.put("osasuna", "https://upload.wikimedia.org/wikipedia/en/thumb/d/db/Osasuna_logo.svg/1200px-Osasuna_logo.svg.png");
        links.put("athletic bilbao", "https://upload.wikimedia.org/wikipedia/en/thumb/9/98/Club_Athletic_Bilbao_logo.svg/1200px-Club_Athletic_Bilbao_logo.svg.png");
        links.put("valencia", "https://upload.wikimedia.org/wikipedia/en/thumb/c/ce/Valenciacf.svg/256px-Valenciacf.svg.png");
        links.put("espanyol", "https://upload.wikimedia.org/wikipedia/en/thumb/d/d6/Rcd_espanyol_logo.svg/1200px-Rcd_espanyol_logo.svg.png");
        links.put("mallorca", "https://upload.wikimedia.org/wikipedia/en/thumb/e/e0/Rcd_mallorca.svg/1200px-Rcd_mallorca.svg.png");
        links.put("celta vigo", "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/RC_Celta_de_Vigo_logo.svg/1200px-RC_Celta_de_Vigo_logo.svg.png");
        links.put("granada", "https://upload.wikimedia.org/wikipedia/en/thumb/d/d5/Logo_of_Granada_Club_de_F%C3%BAtbol.svg/1200px-Logo_of_Granada_Club_de_F%C3%BAtbol.svg.png");
        links.put("alaves", "https://static.wikia.nocookie.net/fifa/images/1/16/DeportivoAlaves.png/revision/latest?cb=20161228185954");
        links.put("levante", "https://upload.wikimedia.org/wikipedia/en/thumb/7/7b/Levante_Uni%C3%B3n_Deportiva%2C_S.A.D._logo.svg/1200px-Levante_Uni%C3%B3n_Deportiva%2C_S.A.D._logo.svg.png");
        links.put("elche", "https://upload.wikimedia.org/wikipedia/en/thumb/a/a7/Elche_CF_logo.svg/1200px-Elche_CF_logo.svg.png");
        links.put("cádiz", "https://upload.wikimedia.org/wikipedia/en/thumb/5/58/C%C3%A1diz_CF_logo.svg/1200px-C%C3%A1diz_CF_logo.svg.png");
        links.put("getafe", "https://upload.wikimedia.org/wikipedia/en/thumb/4/46/Getafe_logo.svg/1200px-Getafe_logo.svg.png");


        links.put("chelsea", "https://upload.wikimedia.org/wikipedia/en/thumb/c/cc/Chelsea_FC.svg/256px-Chelsea_FC.svg.png");
        links.put("manchester city", "https://upload.wikimedia.org/wikipedia/en/thumb/e/eb/Manchester_City_FC_badge.svg/1200px-Manchester_City_FC_badge.svg.png");
        links.put("west ham", "https://upload.wikimedia.org/wikipedia/en/thumb/c/c2/West_Ham_United_FC_logo.svg/256px-West_Ham_United_FC_logo.svg.png");
        links.put("liverpool", "https://upload.wikimedia.org/wikipedia/en/thumb/0/0c/Liverpool_FC.svg/256px-Liverpool_FC.svg.png");
        links.put("manchester united", "https://upload.wikimedia.org/wikipedia/en/thumb/7/7a/Manchester_United_FC_crest.svg/256px-Manchester_United_FC_crest.svg.png");
        links.put("arsenal", "https://upload.wikimedia.org/wikipedia/en/thumb/5/53/Arsenal_FC.svg/256px-Arsenal_FC.svg.png");
        links.put("brighton", "https://upload.wikimedia.org/wikipedia/en/thumb/f/fd/Brighton_%26_Hove_Albion_logo.svg/256px-Brighton_%26_Hove_Albion_logo.svg.png");
        links.put("wolverhampton", "https://upload.wikimedia.org/wikipedia/en/thumb/f/fc/Wolverhampton_Wanderers.svg/256px-Wolverhampton_Wanderers.svg.png");
        links.put("tottenham", "https://upload.wikimedia.org/wikipedia/en/thumb/b/b4/Tottenham_Hotspur.svg/256px-Tottenham_Hotspur.svg.png");
        links.put("crystal palace", "https://upload.wikimedia.org/wikipedia/en/thumb/0/0c/Crystal_Palace_FC_logo.svg/256px-Crystal_Palace_FC_logo.svg.png");
        links.put("everton" , "https://upload.wikimedia.org/wikipedia/en/thumb/7/7c/Everton_FC_logo.svg/256px-Everton_FC_logo.svg.png");
        links.put("southampton" , "https://upload.wikimedia.org/wikipedia/en/thumb/c/c9/FC_Southampton.svg/1200px-FC_Southampton.svg.png");
        links.put("leicester city" , "https://upload.wikimedia.org/wikipedia/en/thumb/2/2d/Leicester_City_crest.svg/1200px-Leicester_City_crest.svg.png");
        links.put("watford" , "https://upload.wikimedia.org/wikipedia/en/thumb/e/e2/Watford.svg/800px-Watford.svg.png");
        links.put("burnley" , "https://upload.wikimedia.org/wikipedia/en/thumb/6/62/Burnley_F.C._Logo.svg/1200px-Burnley_F.C._Logo.svg.png");
        links.put("aston villa" , "https://upload.wikimedia.org/wikipedia/en/thumb/f/f9/Aston_Villa_FC_crest_%282016%29.svg/800px-Aston_Villa_FC_crest_%282016%29.svg.png");
        links.put("brentford" , "https://upload.wikimedia.org/wikipedia/en/thumb/2/2a/Brentford_FC_crest.svg/1200px-Brentford_FC_crest.svg.png");
        links.put("norwich", "https://upload.wikimedia.org/wikipedia/en/thumb/8/8c/Norwich_City.svg/800px-Norwich_City.svg.png");
        links.put("leeds", "https://upload.wikimedia.org/wikipedia/en/thumb/5/54/Leeds_United_F.C._logo.svg/800px-Leeds_United_F.C._logo.svg.png");



        links.put("bayern munich" , "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/FC_Bayern_M%C3%BCnchen_logo_%282017%29.svg/256px-FC_Bayern_M%C3%BCnchen_logo_%282017%29.svg.png");
        links.put("dortmund" , "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Borussia_Dortmund_logo.svg/256px-Borussia_Dortmund_logo.svg.png");
        links.put("freiburg" , "https://upload.wikimedia.org/wikipedia/en/thumb/6/6d/SC_Freiburg_logo.svg/256px-SC_Freiburg_logo.svg.png");
        links.put("wolfsburg" , "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Logo-VfL-Wolfsburg.svg/256px-Logo-VfL-Wolfsburg.svg.png");
        links.put("leipzig" , "https://upload.wikimedia.org/wikipedia/en/thumb/0/04/RB_Leipzig_2014_logo.svg/256px-RB_Leipzig_2014_logo.svg.png");
        links.put("bayer 04 leverkusen" , "https://upload.wikimedia.org/wikipedia/en/thumb/5/59/Bayer_04_Leverkusen_logo.svg/256px-Bayer_04_Leverkusen_logo.svg.png");
        links.put("mainz" , "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Logo_Mainz_05.svg/256px-Logo_Mainz_05.svg.png");
        links.put("union berlin" , "https://www.fc-union-berlin.de/cache/1-FC-Union-Berlin-e979b4b72734cc0af9d1b159719235b9.png");
        links.put("mönchengladbach" , "https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Borussia_M%C3%B6nchengladbach_logo.svg/256px-Borussia_M%C3%B6nchengladbach_logo.svg.png");
        links.put("hoffenheim" , "https://upload.wikimedia.org/wikipedia/he/d/d4/TSG_Hoffenheim.png");
        links.put("köln" , "https://upload.wikimedia.org/wikipedia/en/thumb/5/53/FC_Cologne_logo.svg/1200px-FC_Cologne_logo.svg.png");
        links.put("bochum" , "https://upload.wikimedia.org/wikipedia/commons/thumb/7/72/VfL_Bochum_logo.svg/1200px-VfL_Bochum_logo.svg.png");
        links.put("frankfurt" , "https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/Eintracht_Frankfurt_Logo.svg/1200px-Eintracht_Frankfurt_Logo.svg.png");
        links.put("hertha" , "https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Hertha_BSC_Logo_2012.svg/1200px-Hertha_BSC_Logo_2012.svg.png");
        links.put("stuttgart" , "https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/VfB_Stuttgart_1893_Logo.svg/1200px-VfB_Stuttgart_1893_Logo.svg.png");
        links.put("augsburg" , "https://upload.wikimedia.org/wikipedia/en/thumb/c/c5/FC_Augsburg_logo.svg/1200px-FC_Augsburg_logo.svg.png");
        links.put("bielefeld" , "https://upload.wikimedia.org/wikipedia/commons/thumb/2/26/Arminia-wappen-2021.svg/1024px-Arminia-wappen-2021.svg.png");
        links.put("fürth " , "https://upload.wikimedia.org/wikipedia/en/thumb/f/f2/SpVgg_Greuther_F%C3%BCrth_logo_%282017%29.svg/1200px-SpVgg_Greuther_F%C3%BCrth_logo_%282017%29.svg.png");



        links.put("napoli" , "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/SSC_Neapel.svg/256px-SSC_Neapel.svg.png");
        links.put("ac milan" , "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Logo_of_AC_Milan.svg/256px-Logo_of_AC_Milan.svg.png");
        links.put("inter milan" , "https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/FC_Internazionale_Milano_2021.svg/256px-FC_Internazionale_Milano_2021.svg.png");
        links.put("atalanta" , "https://upload.wikimedia.org/wikipedia/en/thumb/6/66/AtalantaBC.svg/256px-AtalantaBC.svg.png");
        links.put("lazio" , "https://upload.wikimedia.org/wikipedia/en/thumb/c/ce/S.S._Lazio_badge.svg/256px-S.S._Lazio_badge.svg.png");
        links.put("as roma" , "https://upload.wikimedia.org/wikipedia/en/thumb/f/f7/AS_Roma_logo_%282017%29.svg/256px-AS_Roma_logo_%282017%29.svg.png");
        links.put("fiorentina" , "https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/ACF_Fiorentina.svg/256PX-ACF_Fiorentina.svg.png");
        links.put("juventus" , "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Juventus_FC_2017_icon_%28black%29.svg/256px-Juventus_FC_2017_icon_%28black%29.svg.png");
        links.put("bologna" , "https://upload.wikimedia.org/wikipedia/en/thumb/5/5b/Bologna_F.C._1909_logo.svg/256px-Bologna_F.C._1909_logo.svg.png");
        links.put("hellas" , "https://upload.wikimedia.org/wikipedia/en/thumb/9/92/Hellas_Verona_FC_logo_%282020%29.svg/640px-Hellas_Verona_FC_logo_%282020%29.svg.png");
        links.put("empoli" , "https://upload.wikimedia.org/wikipedia/en/e/e9/Empoli_F.C._logo_%282021%29.png");
        links.put("torino" , "https://upload.wikimedia.org/wikipedia/en/thumb/2/2e/Torino_FC_Logo.svg/256px-Torino_FC_Logo.svg.png");
        links.put("sassuolo" , "https://upload.wikimedia.org/wikipedia/en/thumb/1/1c/US_Sassuolo_Calcio_logo.svg/1200px-US_Sassuolo_Calcio_logo.svg.png");
        links.put("spezia" , "https://tmssl.akamaized.net/images/wappen/head/14893.png?lm=1504611067");
        links.put("udinese" , "https://upload.wikimedia.org/wikipedia/it/thumb/a/ae/Logo_Udinese_Calcio_2010.svg/1200px-Logo_Udinese_Calcio_2010.svg.png");
        links.put("sampdoria" , "https://upload.wikimedia.org/wikipedia/en/thumb/d/d2/U.C._Sampdoria_logo.svg/1200px-U.C._Sampdoria_logo.svg.png");
        links.put("venezia" , "https://upload.wikimedia.org/wikipedia/en/thumb/4/4f/Venezia_FC_logo.svg/1200px-Venezia_FC_logo.svg.png");
        links.put("genoa" , "https://upload.wikimedia.org/wikipedia/en/thumb/6/6c/Genoa_C.F.C._logo.svg/1200px-Genoa_C.F.C._logo.svg.png");
        links.put("cagliari" , "https://upload.wikimedia.org/wikipedia/en/thumb/6/61/Cagliari_Calcio_1920.svg/1200px-Cagliari_Calcio_1920.svg.png");
        links.put("salernitana" , "https://upload.wikimedia.org/wikipedia/en/thumb/8/85/US_Salernitana_1919_logo.svg/1200px-US_Salernitana_1919_logo.svg.png");



        links.put("paris saint" , "https://upload.wikimedia.org/wikipedia/en/thumb/a/a7/Paris_Saint-Germain_F.C..svg/256px-Paris_Saint-Germain_F.C..svg.png");
        links.put("lens" , "https://upload.wikimedia.org/wikipedia/en/thumb/c/cc/RC_Lens_logo.svg/256px-RC_Lens_logo.svg.png");
        links.put("nice" , "https://upload.wikimedia.org/wikipedia/en/thumb/2/2e/OGC_Nice_logo.svg/256px-OGC_Nice_logo.svg.png");
        links.put("marseille" , "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/Olympique_Marseille_logo.svg/256px-Olympique_Marseille_logo.svg.png");
        links.put("rennes" , "https://upload.wikimedia.org/wikipedia/en/thumb/9/9e/Stade_Rennais_FC.svg/256px-Stade_Rennais_FC.svg.png");
        links.put("montpellier" , "https://upload.wikimedia.org/wikipedia/en/thumb/a/a8/Montpellier_HSC_logo.svg/256px-Montpellier_HSC_logo.svg.png");
        links.put("lyon" , "https://upload.wikimedia.org/wikipedia/en/thumb/c/c6/Olympique_Lyonnais.svg/256px-Olympique_Lyonnais.svg.png");
        links.put("strasbourg" , "https://upload.wikimedia.org/wikipedia/en/thumb/8/80/Racing_Club_de_Strasbourg_logo.svg/256px-Racing_Club_de_Strasbourg_logo.svg.png");
        links.put("angers" , "https://upload.wikimedia.org/wikipedia/en/6/6c/Angers_SCO.png");
        links.put("nantes" , "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/FC-Nantes-blason-rvb.png/256px-FC-Nantes-blason-rvb.png");
        links.put("monaco" , "https://upload.wikimedia.org/wikipedia/en/thumb/b/ba/AS_Monaco_FC.svg/256px-AS_Monaco_FC.svg.png");
        links.put("brest" , "https://upload.wikimedia.org/wikipedia/en/thumb/0/05/Stade_Brestois_29_logo.svg/1200px-Stade_Brestois_29_logo.svg.png");
        links.put("reims" , "https://upload.wikimedia.org/wikipedia/en/thumb/1/19/Stade_de_Reims_logo.svg/200px-Stade_de_Reims_logo.svg.png");
        links.put("troyes" , "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/ES_Troyes_AC.svg/1200px-ES_Troyes_AC.svg.png");
        links.put("bordeaux" , "https://upload.wikimedia.org/wikipedia/en/5/5d/F.C._Girondins_de_Bordeaux_logo.png");
        links.put("lorient" , "https://upload.wikimedia.org/wikipedia/en/thumb/4/4d/AS_Nancy_logo.svg/1200px-AS_Nancy_logo.svg.png");
        links.put("metz" , "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/FC_Metz_2021_Logo.svg/1200px-FC_Metz_2021_Logo.svg.png");
        links.put("clermont" , "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/FC_Metz_2021_Logo.svg/1200px-FC_Metz_2021_Logo.svg.png");
        links.put("metz" , "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/FC_Metz_2021_Logo.svg/1200px-FC_Metz_2021_Logo.svg.png");
        links.put("saint-" , "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Logo_AS_Saint-%C3%89tienne.svg/1200px-Logo_AS_Saint-%C3%89tienne.svg.png");

/////////// /////
        links.put("club bruges" , "https://upload.wikimedia.org/wikipedia/en/thumb/d/d0/Club_Brugge_KV_logo.svg/256px-Club_Brugge_KV_logo.svg.png");
        links.put("porto" , "https://upload.wikimedia.org/wikipedia/en/thumb/f/f1/FC_Porto.svg/256px-FC_Porto.svg.png");
        links.put("ajax" , "https://upload.wikimedia.org/wikipedia/en/thumb/7/79/Ajax_Amsterdam.svg/256px-Ajax_Amsterdam.svg.png");
        links.put("sporting lis" , "https://upload.wikimedia.org/wikipedia/en/thumb/e/e1/Sporting_Clube_de_Portugal_%28Logo%29.svg/256px-Sporting_Clube_de_Portugal_%28Logo%29.svg.png");
        links.put("besiktas" , "https://upload.wikimedia.org/wikipedia/en/thumb/7/7b/Besiktas_JK.svg/256px-Besiktas_JK.svg.png");
        links.put("sheriff" , "https://upload.wikimedia.org/wikipedia/en/thumb/2/27/FC_Sheriff.svg/256px-FC_Sheriff.svg.png");
        links.put("shakhtar" , "https://upload.wikimedia.org/wikipedia/en/thumb/a/a1/FC_Shakhtar_Donetsk.svg/256px-FC_Shakhtar_Donetsk.svg.png");
        links.put("benfica" , "https://upload.wikimedia.org/wikipedia/en/thumb/a/a2/SL_Benfica_logo.svg/256px-SL_Benfica_logo.svg.png");
        links.put("dynamo kyiv" , "https://upload.wikimedia.org/wikipedia/commons/thumb/d/df/FC_Dynamo_Kyiv_logo.svg/256px-FC_Dynamo_Kyiv_logo.svg.png");
        links.put("villarreal" , "https://upload.wikimedia.org/wikipedia/en/thumb/7/70/Villarreal_CF_logo.svg/1200px-Villarreal_CF_logo.svg.png");
        links.put("young boys" , "https://upload.wikimedia.org/wikipedia/en/thumb/6/6b/BSC_Young_Boys_logo.svg/256px-BSC_Young_Boys_logo.svg.png");
        links.put("lille" , "https://upload.wikimedia.org/wikipedia/en/thumb/3/3f/Lille_OSC_2018_logo.svg/256px-Lille_OSC_2018_logo.svg.png");
        links.put("zenit" , "https://upload.wikimedia.org/wikipedia/commons/thumb/9/96/FC_Zenit_1_star_2015_logo.svg/256px-FC_Zenit_1_star_2015_logo.svg.png");
        links.put("malmo" , "https://upload.wikimedia.org/wikipedia/en/thumb/e/ef/Malmo_FF_logo.svg/256px-Malmo_FF_logo.svg.png");
        links.put("salzburg" , "https://upload.wikimedia.org/wikipedia/en/thumb/7/77/FC_Red_Bull_Salzburg_logo.svg/1200px-FC_Red_Bull_Salzburg_logo.svg.png");
        Map<String, String> nations = new HashMap<String, String>();
        Map<String, String> laliga = new HashMap<String, String>();
        Map<String, String> premier = new HashMap<String, String>();
        Map<String, String> bundesliga = new HashMap<String, String>();
        Map<String, String> seria = new HashMap<String, String>();
        Map<String, String> ligue = new HashMap<String, String>();
        Map<String, String> champions = new HashMap<String, String>();
        DatabaseReference ref = database.getReference("teams");
        int counter= 0;

        for (Map.Entry<String , String> entry : links.entrySet())
        {

            if (entry.getKey().equals("barcelona"))
            {
                counter++;
            }
            if (entry.getKey().equals("chelsea"))
            {
                counter++;
            }
            if (entry.getKey().equals("bayern munich"))
            {
                counter++;
            }
            if (entry.getKey().equals("napoli"))
            {
                counter++;
            }
            if (entry.getKey().equals("paris saint"))
            {
                counter++;
            }
            if (entry.getKey().equals("club bruges"))
            {
                counter++;
            }
            if (counter == 0)
            {
               nations.put(entry.getKey() , entry.getValue());
            }
            if (counter == 1)
            {
                laliga.put(entry.getKey() , entry.getValue());
            }
            if (counter == 2)
            {
                premier.put(entry.getKey() , entry.getValue());
            }
            if (counter == 3)
            {
                bundesliga.put(entry.getKey() , entry.getValue());
            }
            if (counter == 4)
            {
                seria.put(entry.getKey() , entry.getValue());

            }
            if (counter == 5)
            {
                ligue.put(entry.getKey() , entry.getValue());
            }
            if (counter == 6)
            {
                champions.put(entry.getKey() , entry.getValue());

            }
        }
        for (Map.Entry<String , String> entry: nations.entrySet())
        {
            ref.child("nations").child(entry.getKey()).setValue(entry.getValue());
        }
        for (Map.Entry<String , String> entry: laliga.entrySet())
        {
            ref.child("la liga").child(entry.getKey()).setValue(entry.getValue());
        }
        for (Map.Entry<String , String> entry: premier.entrySet())
        {
            ref.child("premier league").child(entry.getKey()).setValue(entry.getValue());
        }
        for (Map.Entry<String , String> entry: seria.entrySet())
        {
            ref.child("seria a").child(entry.getKey()).setValue(entry.getValue());
        }
        for (Map.Entry<String , String> entry: ligue.entrySet())
        {
            ref.child("ligue 1").child(entry.getKey()).setValue(entry.getValue());
        }
        for (Map.Entry<String , String> entry: bundesliga.entrySet())
        {
            ref.child("bundesliga").child(entry.getKey()).setValue(entry.getValue());
        }
        for (Map.Entry<String , String> entry: champions.entrySet())
        {
            ref.child("champions league").child(entry.getKey()).setValue(entry.getValue());
        }
        */



        DatabaseReference ref = database.getReference("teams");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt = 0;
                for (DataSnapshot s : snapshot.getChildren())
                {
                    for (DataSnapshot ab : s.getChildren())
                    {
                        links.put(ab.getKey() , (String) ab.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    public Match[] running(String favTeam) throws IOException, ParseException {
        System.out.println("FAV TEAM:  " + favTeam);
           return printCL(favTeam);
        }

        @Override
        public void run() {/*
            Match[] matches = null;
            for (int i = 0; i < 1; i++) {



                try {
                   matches = printCL("a");

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < matches.length; i++) {
                if (matches[i] != null) {
                    Match m = matches[i];

                }
                }
                */

            }



        public  Match[] printCL(String favTeam) throws IOException, ParseException {
            System.out.println("favvvvvvvvv : " + favTeam);
            String urla = "https://www.bbc.com/sport/football/scores-fixtures";
            Document Main = connect(urla).timeout(60*1000).get();
            System.out.println(Main.text());
            Elements headers = Main.select("h3.sp-c-match-list-heading");
            Elements countGames = Main.select("li.gs-o-list-ui__item"); // used to count how many games there are
            matches = new Match[countGames.size()+15]; // plus ten for safety
            for (int i = 0; i < headers.size(); i++) {

                 league = headers.eq(i).text();

                // System.out.println("_______________" + league + "________________");

                if (league.contains("'") ||league.equals("Championship") ||league.contains("Coupe") ||/* league.toLowerCase().contains("copa del rey") || */league.contains("Australian") || league.contains("Belgian") || league.contains("Russian")||league.contains("Friendlies") || league.equals("The FA Cup")|| league.equals("The FA Trophy") /*||league.equals("EFL Trophy") */|| league.contains("Irish")||league.contains("Cymru") || league.contains("League One") || league.equals("League Two")||league.contains("Welsh") || league.contains("Highland League")|| league.contains("Australian") || league.contains("Lowland League") || league.contains("Greek")|| league.contains("Eliteserien") || league.contains("National League South") || league.contains("Scottish Cup") || league.contains("National League North")|| league.contains("Scottish") || league.contains("National League") || league.contains("Scottish") /*|| league.contains("Scottish league")*/) {
                    league = "adawdawpdwapdadwadaw";
                }


                Elements games = Main.select("div.qa-match-block:matches" + "(" + league + ")").select("li");
                /*
                if (games.text().contains(favTeam)) {
                    System.out.println("ASFDAWDAW: " + games.text());
                    for (Element game : games)
                    {
                        HomeTeam  /* ONLY IF GAME STARTED*//* = game.select("span.sp-c-fixture__team-name.sp-c-fixture__team-name--home").select("span.sp-c-fixture__team-name-wrap").select("span.sp-c-fixture__team-name-trunc").text();
                        HomeGoals = game.select("span.sp-c-fixture__number.sp-c-fixture__number--home").text();
                        AwayGoals = game.select("span.sp-c-fixture__number.sp-c-fixture__number--away").text();
                        minOfGame = game.select("span.sp-c-fixture__status:contains(mins)").text();
                        status = game.select("span.sp-c-fixture__status").text();

                        if (HomeTeam.isEmpty())
                        {
                            HomeTeam = game.select("span.sp-c-fixture__team--time-home").select("span.qa-full-team-name").text();
                            AwayTeam = game.select("span.sp-c-fixture__team--time-away").select("span.qa-full-team-name").text();
                        }

                        if (status.isEmpty() && HomeGoals.isEmpty()) {
                            status = "hasnt started";
                            timeOfGame = timeCal(game.select("span.sp-c-fixture__number.sp-c-fixture__number--time").text() + ":00");
                            countdown = GenerateTimes(timeOfGame);
                        }

                        Match m = new Match(league, HomeTeam, AwayTeam, HomeGoals, AwayGoals, timeOfGame, countdown, minOfGame, status, makeFromLink(HomeTeam.toLowerCase()), makeFromLink(AwayTeam.toLowerCase()));
                        matches[cnt] = m;
                        cnt++;
                    }
                }
                */
                        // 4 = null // 3 --4 /// 2--3//1---2///0--1/ x -- 0
                        //[0 ,1 , 2 , 3, 4 , null ,]
                //System.out.println("------------------");
                for (Element game : games) {
                                                                                                // DO NOT PUT .PARENT 3 TIMES
                    if (!( game.parent().parent().text().contains("Russian")) && !(game.parent().parent().text().contains("Women's"))) {


                        HomeTeam  /* ONLY IF GAME STARTED*/ = game.select("span.sp-c-fixture__team-name.sp-c-fixture__team-name--home").select("span.sp-c-fixture__team-name-wrap").select("span.sp-c-fixture__team-name-trunc").text();

                        AwayTeam  /* ONLY IF GAME STARTED*/ = game.select("span.sp-c-fixture__team-name.sp-c-fixture__team-name--away").select("span.sp-c-fixture__team-name-wrap").select("span.sp-c-fixture__team-name-trunc").text();
                        HomeGoals = game.select("span.sp-c-fixture__number.sp-c-fixture__number--home").text();
                        AwayGoals = game.select("span.sp-c-fixture__number.sp-c-fixture__number--away").text();
                        minOfGame = game.select("span.sp-c-fixture__status:contains(mins)").text();
                        status = game.select("span.sp-c-fixture__status").text();

                        boolean HasStarted = !HomeTeam.isEmpty();
                        timeOfGame = "";
                        countdown = "";
                        if (!HasStarted) {

                            HomeTeam = game.select("span.sp-c-fixture__team--time-home").select("span.qa-full-team-name").text();
                            AwayTeam = game.select("span.sp-c-fixture__team--time-away").select("span.qa-full-team-name").text();
                        }



                    /*
                    if (status.contains("mins")) {
                        status = "in play";
                    }

                     */
                    /*
                    if (status.contains("+")) {
                        minOfGame = status;
                        status = "extra time";
                    }

                     */

                        if (status.isEmpty() && HomeGoals.isEmpty()) {
                            status = "hasnt started";
                            timeOfGame = timeCal(game.select("span.sp-c-fixture__number.sp-c-fixture__number--time").text() + ":00");
                            countdown = GenerateTimes(timeOfGame);
                        }

                        Match m = new Match(league, HomeTeam, AwayTeam, HomeGoals, AwayGoals, timeOfGame, countdown, minOfGame, status, makeFromLink(HomeTeam.toLowerCase()), makeFromLink(AwayTeam.toLowerCase()));
                        //System.out.println(m.toString());

                        if (cnt < matches.length) {
                            matches[cnt] = m;
                        }
                        cnt++;


                        // System.out.println("-------");
                        // System.out.println("------------------");
                    }
                }
            }
            int indx =-1;
            for (int i = 0; i <matches.length ; i++)
            {
                if (matches[i]!= null )
                {
                    System.out.println(matches[i].getHomeTeam() + matches[i].getAwayTeam());
                    if ( matches[i].getHomeTeam().toLowerCase().contains(favTeam) || matches[i].getAwayTeam().toLowerCase().contains(favTeam))
                    {

                        indx = i;
                    }
                }
            }
            if (indx != -1) {
                Match save = matches[indx];
                matches[indx] = null;
                for (int i = indx; i > 0; i--) {
                    matches[indx] = matches[indx - 1];
                    matches[indx - 1] = null;
                }
                matches[0] = save;
                matches[0].setLeague("(favorite) " + matches[0].getLeague());
                //[1 ,2 , 3 , (4), 5 , 6]
                // save 4 and make him null --> [1 ,2 , 3 , null , 5 ,6] num = 4;
                // go from 4 down so that 3 is 4 and then 3 is null // then 2 is 3 and 2 is null up until 0 which you put 4 in
            }

            return matches;

        }
        public Bitmap makeFromLink(String name)
        {

            URL url = null;

            Bitmap bmp = null;
            try {
                url = new URL(isin(name));


                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        public String isin(String name)
        {
            if (name.equals("")){ return "https://static.thenounproject.com/png/4422219-200.png";}
            for (Map.Entry<String , String > entry : links.entrySet())
            {
                if (name.contains( entry.getKey()))
                {
                    System.out.println(name + " contains " + entry.getKey());
                    if (entry.getValue().equals("")){return "https://mpng.subpng.com/20180606/sho/kisspng-201718-uefa-champions-league-2018-uefa-champion-champions-5b17f2c9b13bc3.730975131528296137726.jpg";}
                    return entry.getValue();
                }
            }
            return "https://static.thenounproject.com/png/4422219-200.png";
        }



        public static String timeCal(String timeGame) { // used to add 2 hrs to the time of game since i recieve the times in uk time which is 2 hrs before ist
            String hr = timeGame.charAt(0) + "" + timeGame.charAt(1);
            int actual = Integer.parseInt(hr);
            if (actual + 2 < 10) {
                hr = "0" + String.valueOf(actual + 2) + timeGame.charAt(2) + timeGame.charAt(3) + timeGame.charAt(4) + ":00";
            } else {
                hr = String.valueOf(actual + 2) + timeGame.charAt(2) + timeGame.charAt(3) + timeGame.charAt(4) + ":00";
            }
            return hr;

        }

        public static String GenerateTimes(String timeOfGame) // generates two strings - one is the time of game and other is the time rn both in format: ("hh:mm:ss")
        {
            String hrOfGame;
            // getting hr
            hrOfGame = timeOfGame.charAt(0) + "" + timeOfGame.charAt(1);

            int hr = Integer.parseInt(hrOfGame);
            int min = Integer.parseInt(timeOfGame.charAt(3) + "" + timeOfGame.charAt(4));
            long gameInmilli = 1000*(hr*3600 + min*60); // converting game time to milliseconds

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

            String timeGame = formatter.format(new Date(gameInmilli)); // making game into a string

            formatter.setTimeZone(TimeZone.getTimeZone("GMT+2")); // UPDATE: WINTER CLOCK SO GMT+3 ISNT VALID IT IS NOW GMT +2

            Date date = new Date(System.currentTimeMillis()); // making current time to a date
            String timeRN = formatter.format(date); // converting current time date to string
            return   CountdownCalculate(timeGame , timeRN);
        }
        // calculates the time between 2 times - now and time of game
        public static String CountdownCalculate (String timeGame , String timeRN)
        {
            String hr , Smin , Ssec;
            int secondsgame = ToSec(timeGame , "thegame");
            int secondsRn = ToSec(timeRN , "");
            int dif = secondsgame - secondsRn;
            if (dif < 0)
            {
                System.out.println("---");
                System.out.println(timeGame);
                System.out.println(timeRN);
                System.out.println("dif : " + dif);
                System.out.println("---");
                return "-1";
            }
            int hrs = (int) (dif / 3600);
            int min = (int) (dif - hrs * 3600) / 60;
            int sec = (int) (dif - hrs * 3600 - min * 60);
            hr = hrs + "";
            Smin = min +"";
            Ssec = sec+"";
            if (hrs < 10)
            {
                hr = "0" + hrs;
            }
            if (min < 10)
                Smin = "0" + min;
            if (sec < 10)
            {
                Ssec = "0" + sec ;
            }

            return hr + ":" + Smin + ":" + Ssec;

        }
        // converts a String of time("hh:mm:ss") to seconds
        public static int ToSec(String time , String game)
        {

         String StringHr = time.charAt(0) + "" + time.charAt(1);
         String Stringmin = time.charAt(3) +""+ time.charAt(4);
         String Stringsec = time.charAt(6) + "" + time.charAt(7);
         int hr = Integer.parseInt(StringHr);
         if (hr < 10) // means time is 01:30 or 00:30 etc
         {
             if (game.equals("thegame"))
             hr += 24;
         }
         int min = Integer.parseInt(Stringmin);
         int sec = Integer.parseInt(Stringsec);
         sec += hr*3600 + min*60;
         return sec;

        }

    }


