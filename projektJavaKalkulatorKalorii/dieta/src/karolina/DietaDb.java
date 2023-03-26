package karolina;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DietaDb {

    private Connection conn = null;
    private static final String tabelaProduktow = "produkty";

    public DietaDb() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/licznikkalorii?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
        conn = DriverManager.getConnection(url, "karolina", "test");
    }

    public void closeDietaData() throws SQLException {
        conn.close();
    }

    public List<Produkt> getAllRow(Produkt produkt) throws SQLException {

        List<Produkt> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tabelaProduktow + " ORDER BY nazwaProduktu";
        PreparedStatement pstmn = null;

        try {
            pstmn = conn.prepareStatement(sql);
            ResultSet rs = pstmn.executeQuery();
            while(rs.next())
            {
                int i = rs.getInt("idProduktu");
                String s1 = rs.getString("nazwaProduktu");
                Double d1 = rs.getDouble("wartoscEnergetyczna");
                Double d2 = rs.getDouble("bialka");
                Double d3 = rs.getDouble("tluszcze");
                Double d4 = rs.getDouble("weglowodany");
                Double d5 = rs.getDouble("ilosc");
                String s2 = rs.getString("jednostkaMiary");
                list.add(new Produkt(i, s1, d1, d2, d3, d4, d5, s2));
            }
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return list;
    }

    public int dodajProdukt(Produkt produkt) throws SQLException {
        String sql = "INSERT INTO " + tabelaProduktow + " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmn = null;
        int id;

        try {
            pstmn = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmn.setString(1, produkt.getNazwaProduktu());
            pstmn.setDouble(2, produkt.getWartoscEnergetyczna());
            pstmn.setDouble(3, produkt.getBialka());
            pstmn.setDouble(4, produkt.getTluszcze());
            pstmn.setDouble(5, produkt.getWeglowodany());
            pstmn.setDouble(6, produkt.getIlosc());
            pstmn.setString(7, produkt.getJednostkaMiary());
            pstmn.executeUpdate();

            ResultSet rs = pstmn.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return id;
    }

    public void usunProdukt(Produkt produkt) throws SQLException {
        String sql = "DELETE FROM Produkty WHERE idProduktu = ?";
        PreparedStatement pstmn = null;
        try {
            pstmn = conn.prepareStatement(sql);
            pstmn.setInt(1, produkt.getIdProduktu());
            pstmn.executeUpdate();
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
    }

    /*public void updateRow(Produkt produkt) throws SQLException {
        String sql = "UPDATE " + tabelaProduktow + " " +
                "SET nazwaProduktu = ?, wartoscEnergetyczna = ?, bialka = ?, tluszcze = ?, weglowodany = ?, ilosc = ?, jednostkaMiary = ?" +
                "WHERE idProduktu = ?";
        PreparedStatement pstmn = null;

        try {
            pstmn = conn.prepareStatement(sql);
            pstmn.setString(1, produkt.getNazwaProduktu());
            pstmn.setDouble(2, produkt.getWartoscEnergetyczna());
            pstmn.setDouble(3, produkt.getBialka());
            pstmn.setDouble(4, produkt.getTluszcze());
            pstmn.setDouble(5, produkt.getWeglowodany());
            pstmn.setDouble(6, produkt.getIlosc());
            pstmn.setString(7, produkt.getJednostkaMiary());
            pstmn.setInt(8, produkt.getIdProduktu());
            pstmn.executeUpdate();
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
    }*/

    /*public void dajProdukt(Produkt produkt) throws SQLException {
        int b= 0;
        //
        String sql = "DELETE FROM produkty WHERE idProduktu = ?;";
        //String sql = "DELETE FROM " + tabelaProduktow + " WHERE idProduktu = ?";
        PreparedStatement pstmn = null;
        try {
            pstmn = conn.prepareStatement(sql);
            //pstmn.setInt(1, produkt.getIdProduktu());
            //System.out.println(stmn);
            pstmn.setInt(1, produkt.getIdProduktu());
            //pstmn.executeUpdate(sql);
            pstmn.executeUpdate();
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        //return String.valueOf(b);
    }*/

    //################################################
    //##########          POSILKI          ##########
    //################################################

    public List<Posilek> dajPosilki(Uzytkownik uzytkownik, Timestamp time1, Timestamp time2) throws SQLException {
        List<Posilek> list = new ArrayList<>();

        PreparedStatement pstmn = null;
        try {
            String sql = "SELECT * FROM Posilki " +
                    "WHERE idUzytkownika = ? " +
                    "AND Posilki.godzinaPosilku BETWEEN ? AND ?;";

            pstmn = conn.prepareStatement(sql);
            pstmn.setInt(1, uzytkownik.getIdUzytkownika());
            pstmn.setTimestamp(2, time1);
            pstmn.setTimestamp(3, time2);
            ResultSet rs = pstmn.executeQuery();

            while (rs.next()) {
                int i1 = rs.getInt("idPosilku");
                Timestamp t1 = rs.getTimestamp("godzinaPosilku");
                String s1 = rs.getString("nazwaPosilku");
                list.add(new Posilek(i1, t1, s1));
            }
        } finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return list;
    }

    //################################################
    //##########          ZJEDZONE          ##########
    //################################################

    public int dodajZjedzonyProdukt(Produkt produkt, Posilek posilek, Uzytkownik uzytkownik, Timestamp time) throws SQLException {

        PreparedStatement pstmn = null;
        int idPosilku = 0, id = 0;

        Timestamp godzina = new Timestamp(time.getTime() - 30 * 60 * 1000); // minus 30 minut
        String selectPosilek = "SELECT idPosilku FROM Posilki " +
                "WHERE idUzytkownika = ? " +
                "AND godzinaPosilku BETWEEN ? AND ? ORDER BY godzinaPosilku DESC LIMIT 1";

        try {
            pstmn = conn.prepareStatement(selectPosilek);
            pstmn.setInt(1, uzytkownik.getIdUzytkownika());
            pstmn.setTimestamp(2, godzina);
            pstmn.setTimestamp(3, time);
            ResultSet rs = pstmn.executeQuery();

            if(rs.next()) {
                idPosilku = rs.getInt(1);
            }
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }

        if (idPosilku == 0) { // dodaj nowy posilek

            String insertPosilek = "INSERT INTO Posilki VALUES (DEFAULT, ?, ?, ?)";

            try {
                pstmn = conn.prepareStatement(insertPosilek, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmn.setInt(1, uzytkownik.getIdUzytkownika());
                pstmn.setTimestamp(2, posilek.getGodzinaPosilku());
                pstmn.setString(3, posilek.getNazwaPosilku());
                pstmn.executeUpdate();

                ResultSet rs = pstmn.getGeneratedKeys();
                rs.next();
                idPosilku = rs.getInt(1);
            } finally {
                if (pstmn != null) {
                    pstmn.close();
                }
            }
        }

        // dodaj produkt do zjedzonych
        String sql = "INSERT INTO Zjedzone VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pstmn = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmn.setInt(1, idPosilku);
            pstmn.setString(2, produkt.getNazwaProduktu());
            pstmn.setDouble(3, produkt.getWartoscEnergetyczna());
            pstmn.setDouble(4, produkt.getBialka());
            pstmn.setDouble(5, produkt.getTluszcze());
            pstmn.setDouble(6, produkt.getWeglowodany());
            pstmn.setDouble(7, produkt.getIlosc());
            pstmn.setString(8, produkt.getJednostkaMiary());
            pstmn.executeUpdate();

            ResultSet rs = pstmn.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }

        return id;
    }

    public void usunZjedzonyProdukt(Produkt produkt) throws SQLException {

        int idPosilku = 0;
        PreparedStatement pstmn = null;

        String sql = "SELECT idPosilku FROM Zjedzone WHERE id = ?";
        try {
            pstmn = conn.prepareStatement(sql);
            pstmn.setInt(1, produkt.getIdProduktu());

            ResultSet rs = pstmn.executeQuery();

            if (rs.next()) {
                idPosilku = rs.getInt("idPosilku");
            }
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }


        sql = "DELETE FROM Zjedzone WHERE id = ?";
        try {
            pstmn = conn.prepareStatement(sql);
            pstmn.setInt(1, produkt.getIdProduktu());
            pstmn.executeUpdate();
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }


        int licznik = 0;
        sql = "SELECT COUNT(*) FROM Zjedzone WHERE idPosilku = ?";
        try {
            pstmn = conn.prepareStatement(sql);
            pstmn.setInt(1, idPosilku);

            ResultSet rs = pstmn.executeQuery();

            if (rs.next()) {
                licznik = rs.getInt(1);
            }
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }

        if (licznik == 0) {
            sql = "DELETE FROM Posilki WHERE idPosilku = ?";
            try {
                pstmn = conn.prepareStatement(sql);
                pstmn.setInt(1, idPosilku);
                pstmn.executeUpdate();
            }
            finally {
                if (pstmn != null) {
                    pstmn.close();
                }
            }
        }

    }

/*
    public List<Produkt> dajZjedzone() throws SQLException {
        List<Produkt> listaUzytkownikow = new ArrayList<>();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat dzisiejszaData = new SimpleDateFormat("dd-MM-yyyy 00:00:00");

        int idPosilku = 0;
        int idPosilku2 = 0;

        PreparedStatement pstmn = null;
        try {
            String sql = "SELECT Zjedzone.idPosilku FROM ((Posilki " +
                    "INNER JOIN Zjedzone ON Posilki.idPosilku = Zjedzone.idPosilku)" +
                    "INNER JOIN Uzytkownicy ON Posilki.idUzytkownika = Uzytkownicy.idUzytkownika)" +
                    "WHERE Posilki.nazwaPosilku > '" + dzisiejszaData.format(timestamp) + "';";

            pstmn = conn.prepareStatement(sql);
            ResultSet rs = pstmn.executeQuery();
            while (rs.next()) {
                idPosilku = rs.getInt(1);
                if (idPosilku == idPosilku2) {
                    continue;
                }
                idPosilku2 = idPosilku;

                String select2 = "SELECT * FROM Zjedzone WHERE idPosilku = " + idPosilku;
                pstmn = conn.prepareStatement(select2);
                ResultSet rs2 = pstmn.executeQuery();
                while (rs2.next()) {
                        int i = rs2.getInt("idPosilku");
                        String s1 = rs2.getString("nazwaProduktu");
                        Double d1 = rs2.getDouble("wartoscEnergetyczna");
                        Double d2 = rs2.getDouble("bialka");
                        Double d3 = rs2.getDouble("tluszcze");
                        Double d4 = rs2.getDouble("weglowodany");
                        Double d5 = rs2.getDouble("ilosc");
                        String s2 = rs2.getString("jednostkaMiary");
                        listaUzytkownikow.add(new Produkt(i, s1, d1, d2, d3, d4, d5, s2));
                }
            }
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return listaUzytkownikow;
    }

*/

    public List<Produkt> dajDzisiajZjedzone(Uzytkownik uzytkownik, Timestamp time1, Timestamp time2) throws SQLException {
        List<Produkt> list = new ArrayList<>();

        PreparedStatement pstmn = null;
        try {
            String sql = "SELECT * FROM ((Posilki " +
                    "INNER JOIN Zjedzone ON Posilki.idPosilku = Zjedzone.idPosilku)" +
                    "INNER JOIN Uzytkownicy ON Posilki.idUzytkownika = Uzytkownicy.idUzytkownika)" +
                    "WHERE Posilki.idUzytkownika = ? AND Posilki.godzinaPosilku BETWEEN ? AND ?;";

            pstmn = conn.prepareStatement(sql);
            pstmn.setInt(1, uzytkownik.getIdUzytkownika());
            pstmn.setTimestamp(2, time1);
            pstmn.setTimestamp(3, time2);
            ResultSet rs = pstmn.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String s1 = rs.getString("nazwaProduktu");
                Double d1 = rs.getDouble("wartoscEnergetyczna");
                Double d2 = rs.getDouble("bialka");
                Double d3 = rs.getDouble("tluszcze");
                Double d4 = rs.getDouble("weglowodany");
                Double d5 = rs.getDouble("ilosc");
                String s2 = rs.getString("jednostkaMiary");
                list.add(new Produkt(id, s1, d1, d2, d3, d4, d5, s2));
            }
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return list;
    }


    public List<Produkt> dajZjedzone(Posilek posilek) throws SQLException {
        List<Produkt> list = new ArrayList<>();

        PreparedStatement pstmn = null;
        try {
            String sql = "SELECT * FROM Zjedzone WHERE idPosilku = ?";

            pstmn = conn.prepareStatement(sql);
            pstmn.setInt(1, posilek.getIdPosilku());
            ResultSet rs = pstmn.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String s1 = rs.getString("nazwaProduktu");
                Double d1 = rs.getDouble("wartoscEnergetyczna");
                Double d2 = rs.getDouble("bialka");
                Double d3 = rs.getDouble("tluszcze");
                Double d4 = rs.getDouble("weglowodany");
                Double d5 = rs.getDouble("ilosc");
                String s2 = rs.getString("jednostkaMiary");
                list.add(new Produkt(id, s1, d1, d2, d3, d4, d5, s2));
            }
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return list;
    }
    //#######################################


    public int dodajUzytkownika(Uzytkownik uzytkownik) throws SQLException {
        String sql = "INSERT INTO Uzytkownicy VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmn = null;
        int id;

        try {
            pstmn = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmn.setString(1, uzytkownik.getNazwaUzytkownika());
            pstmn.setString(2, uzytkownik.getPlec());
            pstmn.setInt(3, uzytkownik.getWiek());
            pstmn.setDouble(4, uzytkownik.getWaga());
            pstmn.setDouble(5, uzytkownik.getWzrost());
            pstmn.setInt(6, uzytkownik.getPoziomRuchu());
            pstmn.setInt(7, uzytkownik.getCel());
            pstmn.setDouble(8, uzytkownik.getWartoscEnergetyczna());
            pstmn.setDouble(9, uzytkownik.getBialka());
            pstmn.setDouble(10, uzytkownik.getTluszcze());
            pstmn.setDouble(11, uzytkownik.getWeglowodany());
            pstmn.executeUpdate();

            ResultSet rs = pstmn.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return id;
    }

    public List<Uzytkownik> dajListeUzytkownikow() throws SQLException {

        List<Uzytkownik> list = new ArrayList<>();
        String sql = "SELECT * FROM Uzytkownicy";
        PreparedStatement pstmn = null;

        try {
            pstmn = conn.prepareStatement(sql);
            ResultSet rs = pstmn.executeQuery();
            while(rs.next())
            {
                Uzytkownik uzytkownik = new Uzytkownik();
                uzytkownik.setIdUzytkownika(rs.getInt("idUzytkownika"));
                uzytkownik.setNazwaUzytkownika(rs.getString("nazwaUzytkownika"));
                uzytkownik.setPlec(rs.getString("plec"));
                uzytkownik.setWiek(rs.getInt("wiek"));
                uzytkownik.setWaga(rs.getDouble("waga"));
                uzytkownik.setWzrost(rs.getDouble("wzrost"));
                uzytkownik.setPoziomRuchu(rs.getInt("poziomRuchu"));
                uzytkownik.setCel(rs.getInt("cel"));
                uzytkownik.setWartoscEnergetyczna(rs.getDouble("wartoscEnergetyczna"));
                uzytkownik.setBialka(rs.getDouble("bialka"));
                uzytkownik.setTluszcze(rs.getDouble("tluszcze"));
                uzytkownik.setWeglowodany(rs.getDouble("weglowodany"));
                list.add(uzytkownik);
            }
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return list;
    }

    public Uzytkownik dajUzytkownika(int id) throws SQLException {

        Uzytkownik uzytkownik = new Uzytkownik();
        String sql = "SELECT * FROM Uzytkownicy WHERE idUzytkownika = " + id;
        PreparedStatement pstmn = null;

        try {
            pstmn = conn.prepareStatement(sql);
            ResultSet rs = pstmn.executeQuery();
            while(rs.next())
            {
                uzytkownik.setIdUzytkownika(rs.getInt("idUzytkownika"));
                uzytkownik.setNazwaUzytkownika(rs.getString("nazwaUzytkownika"));
                uzytkownik.setPlec(rs.getString("plec"));
                uzytkownik.setWiek(rs.getInt("wiek"));
                uzytkownik.setWaga(rs.getDouble("waga"));
                uzytkownik.setWzrost(rs.getDouble("wzrost"));
                uzytkownik.setPoziomRuchu(rs.getInt("poziomRuchu"));
                uzytkownik.setCel(rs.getInt("cel"));
                uzytkownik.setWartoscEnergetyczna(rs.getDouble("wartoscEnergetyczna"));
                uzytkownik.setBialka(rs.getDouble("bialka"));
                uzytkownik.setTluszcze(rs.getDouble("tluszcze"));
                uzytkownik.setWeglowodany(rs.getDouble("weglowodany"));
            }
        }
        finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return uzytkownik;
    }


    public List<Produkt> dajProdukty(String nazwa) throws SQLException {
        List<Produkt> list = new ArrayList<>();

        PreparedStatement pstmn = null;
        try {
            String sql = "SELECT * FROM produkty WHERE nazwaProduktu LIKE ? ORDER BY nazwaProduktu";
            String maska = nazwa + "%";
            if (nazwa.length() > 2) {
                maska = "%" + maska;
            }

            pstmn = conn.prepareStatement(sql);
            pstmn.setString(1, maska);
            ResultSet rs = pstmn.executeQuery();

            while (rs.next()) {
                int i = rs.getInt("idProduktu");
                String s1 = rs.getString("nazwaProduktu");
                Double d1 = rs.getDouble("wartoscEnergetyczna");
                Double d2 = rs.getDouble("bialka");
                Double d3 = rs.getDouble("tluszcze");
                Double d4 = rs.getDouble("weglowodany");
                Double d5 = rs.getDouble("ilosc");
                String s2 = rs.getString("jednostkaMiary");
                list.add(new Produkt(i, s1, d1, d2, d3, d4, d5, s2));
            }
        } finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return list;
    }

    /**
     * Pobiera informacje o produkcie "nazwa"
     * @param nazwa nazwa produktu
     * @return informacje o produkcie
     * @throws SQLException
     */
    public String getProductsInfo(String nazwa) throws SQLException {
        List<String> list = new ArrayList<>();

        String info = "";
        PreparedStatement pstmn = null;
        try {
            //String sql = "SELECT * FROM produkty WHERE nazwaproduktu = 'aGrest'";
            String sql = "SELECT * FROM produkty WHERE nazwaProduktu = '" + nazwa + "'";

            pstmn = conn.prepareStatement(sql);
            ResultSet rs = pstmn.executeQuery();

            String nazwaProduktu;
            int wartoscEnergetyczna;
            float b, t, w;
            while (rs.next()) {
                nazwaProduktu = rs.getString("nazwaProduktu");
                wartoscEnergetyczna = rs.getInt("wartoscEnergetyczna");
                b = rs.getFloat("bialka");
                t = rs.getFloat("tluszcze");
                w = rs.getFloat("weglowodany");
                info = nazwaProduktu + " wartoscEnergetyczna: " + wartoscEnergetyczna +
                        ", bialka: " + b + ", tluszcze: " + t + ", weglowodany: " + w;
                System.out.println(info);
            }
        } finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return info;
        //return listaUzytkownikow;
    }

    // ###################################################
    // ###     STATYSTYKI
    // ###################################################


    public List<Punkt> dajWartosciHistoryczne(Uzytkownik uzytkownik, Timestamp time1, Timestamp time2) throws SQLException{
        List<Punkt> list = new ArrayList<>();


        PreparedStatement pstmn = null;
        try {
            String sql = "SELECT DATE(godzinaPosilku) AS dzien, SUM(Zjedzone.wartoscEnergetyczna), " +
                    "SUM(Zjedzone.bialka), SUM(Zjedzone.tluszcze), SUM(Zjedzone.weglowodany) " +
                    "FROM ((Posilki " +
                    "INNER JOIN Zjedzone ON Posilki.idPosilku = Zjedzone.idPosilku)" +
                    "INNER JOIN Uzytkownicy ON Posilki.idUzytkownika = Uzytkownicy.idUzytkownika)" +
                    "WHERE Posilki.idUzytkownika = ? AND Posilki.godzinaPosilku BETWEEN ? AND ? " +
                    "GROUP BY dzien;";


            pstmn = conn.prepareStatement(sql);

            pstmn.setInt(1, uzytkownik.getIdUzytkownika());
            pstmn.setTimestamp(2, time1);
            pstmn.setTimestamp(3, time2);
            ResultSet rs = pstmn.executeQuery();

            while (rs.next()) {
                Punkt punkt = new Punkt();
                punkt.setX(rs.getString("dzien"));
                punkt.setY1(rs.getDouble(2));
                punkt.setY2(rs.getDouble(3));
                punkt.setY3(rs.getDouble(4));
                punkt.setY4(rs.getDouble(5));
                list.add(punkt);
            }
        } finally {
            if (pstmn != null) {
                pstmn.close();
            }
        }
        return list;


    }


}
