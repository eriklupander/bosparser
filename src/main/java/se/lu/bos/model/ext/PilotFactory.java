package se.lu.bos.model.ext;

import java.util.Arrays;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-08
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class PilotFactory {

    private static Random rnd1 = new Random();
    private static Random rnd2 = new Random();
    private static Random rnd3 = new Random();

    private static String[] firstNamesGerman = {"Hans", "Rudolf","Erich", "Klaus", "Jürgen", "Steffen", "Mats", "Werner", "Helmut", "Adolf", "Heinrich", "Torsten", "Karl-Heinz", "Stefan", "Rudi", "Ralf", "Michael", "Mueller"};
    private static String[] lastNamesGerman = {"Hummels", "Mertesacker", "Kohler", "Völler", "Klinsmann", "Jancker", "Rummenigge", "Schwartz", "Weiss", "Frings", "Böhme", "Braun", "Brehme", "Lahm", "Loew", "Thomas"};
    private static String randomFirstName;

    public static Pilot getEnlistedPilot() {
        Pilot pilot = randomlyNamedPilot();
        pilot.setRank(getRandomRankOfLevel(0));
        return pilot;
    }


    public static Pilot getNcoPilot() {
        Pilot pilot = randomlyNamedPilot();
        pilot.setRank(getRandomRankOfLevel(1));
        return pilot;
    }

    public static Pilot getOfficerPilot() {
        Pilot pilot = randomlyNamedPilot();
        pilot.setRank(getRandomRankOfLevel(2));
        return pilot;
    }

    public static String getRandomFirstName() {
        return firstNamesGerman[Math.abs(rnd1.nextInt(firstNamesGerman.length - 1))];
    }

    public static String getRandomLastName() {
        return lastNamesGerman[Math.abs(rnd2.nextInt(lastNamesGerman.length - 1))];
    }

    private static Pilot randomlyNamedPilot() {
        Pilot p = new Pilot();
        p.setFirstName(getRandomFirstName());
        p.setLastName(getRandomLastName());

        return p;
    }

    private static Rank getRandomRankOfLevel(int level) {
        Rank[] values = Rank.values();
        Long rankCount = Arrays.asList(values).stream().filter(r -> r.getRankTypeCode() == level).count();
        int index = rnd3.nextInt(rankCount.intValue());
        return Arrays.asList(values).stream().filter(r -> r.getRankTypeCode() == level).skip(index).findFirst().get();
    }
}
