package se.lu.bos.model.ext;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-08
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public enum Rank {
      FLIEGER(0, 0, ""), GEFREITER(0, 1, ""), OBERGEFREITER(0, 2, ""), HAUPTGEFREITER(0, 3, ""), STABSGEFREITER(0, 4, ""),
     UNTEROFFICER(1, 5, ""), FAHNENJUNKER(1, 6, ""), UNTERFELDWEBEL(1, 7, ""), FÄHNRICH(1, 8, ""), FELDWEBEL(1, 9, ""), OBERFELDWEBEL(1, 10, ""), OBERFÄNRICH(1, 11, ""), STABSFELDWEBEL(1, 12, ""),
    LEUTNANT(2, 13, ""), OBERLEUTNANT(2, 14, ""), HAUPTMANN(2, 15, ""), MAJOR(2, 16, ""), OBERSTLEUTNANT(2, 17, ""), OBERST(2, 18, "");

    private int rankTypeCode; // 0 == Enlisted, 1 == NCO, 2 == Officer
    private int rankIndex;
    private String name;

    private Rank(int rankTypeCode, int rankIndex, String name) {
        this.rankTypeCode = rankTypeCode;
        this.rankIndex = rankIndex;
        this.name = name;
    }

    public int getRankTypeCode() {
        return rankTypeCode;
    }

    public int getRankIndex() {
        return rankIndex;
    }

    public String getName() {
        return name;
    }
}
