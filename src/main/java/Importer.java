import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.mysql.jdbc.Driver;
/**
 * Created by AlperSalviz on 4.5.2017.
 */
public class Importer {

    private static int i = 0;

    public static void main(String[] args) {
        String csvFile = "/Users/admin/Desktop/java-project/implant/src/main/resources/MASSON.csv";
        BufferedReader bufferedReader = null;
        String line = "";
        String csvSplitBy = ",";
        int x = 0;
        ArrayList<String> id = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<String> implantCenter = new ArrayList<String>();
        ArrayList<String> city = new ArrayList<String>();
        ArrayList<String> patientId = new ArrayList<String>();
        ArrayList<String> patientName = new ArrayList<String>();
        ArrayList<String> HVADID = new ArrayList<String>();
     //   ArrayList<String> qty = new ArrayList<String>();
        ArrayList<String> patientAdress = new ArrayList<String>();
        ArrayList<String> patientCallNumbers = new ArrayList<String>();
        ArrayList<String> statusDate = new ArrayList<String>();
        ArrayList<String> onDevice = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF8"));
            while ((line = bufferedReader.readLine()) != null ){
                String[] implant = line.split(csvSplitBy);

                id.add(implant[0]);
                date.add(implant[1]);
                implantCenter.add(implant[2]);
                city.add(implant[3]);
                patientId.add(implant[4]);
                patientName.add(implant[5]);
                HVADID.add(implant[6]);
          //      qty.add(implant[7]);
                patientAdress.add(implant[9]);
                patientCallNumbers.add(implant[10]);
                statusDate.add(implant[11]);
                onDevice.add(implant[12]);
                status.add(implant[13]);
            }

            System.out.println("Country [code= " + status.get(0) + "]");
            //System.out.print("ID "+ id.get(5));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        String myUrl = "jdbc:mysql://localhost:3306/masmedikal?characterEncoding=UTF-8";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(myUrl, "root", "");

            for ( i = 0 ; i<id.size();i++){
                //String getHospitalQuery = "select * from city Where city Like %dasd%";
                //hospitalInsertQuery = " insert into hospital (name_hops, city_hops)"
                      //  + " values (?, ?, ?, ?)";

                String cityID ="";
                String hospitalID ="";
                Statement statement = conn.createStatement();
                ResultSet resultSetCity = statement.executeQuery("select * from city Where city Like '%"+city.get(i)+"%'");

                if (!resultSetCity.next() ) {
                    String cityInsertQuery = " insert into city (city)"
                                 + " values (?)";
                    PreparedStatement preparedStmtCity = conn.prepareStatement(cityInsertQuery, Statement.RETURN_GENERATED_KEYS);
                    preparedStmtCity.setString (1, city.get(i));

                     preparedStmtCity.execute();

                   // ResultSet resultSetCityLast = statement.executeQuery("select last_insert_id() as last_id from city");
                        ResultSet resultSetCityLast = preparedStmtCity.getGeneratedKeys();
                        if(resultSetCityLast.next()){

                            cityID = String.valueOf(resultSetCityLast.getInt(1));
                        }
                        resultSetCityLast.close();

                } else {
                         cityID = resultSetCity.getString("ID");

                }

                ResultSet resultSetHospital = statement.executeQuery("select * from hospital Where name_hosp Like '%"+implantCenter.get(i)+"%'");
                if (!resultSetHospital.next() ) {
                    String hospitalInsertQuery = " insert into hospital (name_hosp , city_hosp)"
                            + " values (?,?)";
                    PreparedStatement preparedStmtHospital = conn.prepareStatement(hospitalInsertQuery, Statement.RETURN_GENERATED_KEYS);
                    preparedStmtHospital.setString (1, implantCenter.get(i));
                    preparedStmtHospital.setString (2, cityID);

                    preparedStmtHospital.execute();

                    ResultSet resultSetHospitalLast = preparedStmtHospital.getGeneratedKeys();
                    if(resultSetHospitalLast.next()){

                        hospitalID = String.valueOf(resultSetHospitalLast.getInt(1));
                    }
                    resultSetHospitalLast.close();
                } else {
                        hospitalID = resultSetHospital.getString("ID");


                }
                ResultSet resultSetpatient = statement.executeQuery("select * from patient Where patientID Like '%"+patientId.get(i)+"%'");
                if (!resultSetpatient.next() ) {
                    String patientInsertQuery = " insert into patient (operatorID  ,firstname_pat ,adress_pat,patientID)"
                            + " values (?,?,?,?)";
                    PreparedStatement preparedStmtPatient = conn.prepareStatement(patientInsertQuery, Statement.RETURN_GENERATED_KEYS);
                    preparedStmtPatient.setString (1, "1");
                    preparedStmtPatient.setString (2, patientName.get(i));
                    preparedStmtPatient.setString (3, patientAdress.get(i));
                    preparedStmtPatient.setString (4, patientId.get(i));

                    preparedStmtPatient.execute();

                }

                String[] patinetNumbers = patientCallNumbers.get(i).split("-|/");

                for (int y =0 ; y<patinetNumbers.length;y++){
                    ResultSet resultSetPhone = statement.executeQuery("select * from phone_no_pat Where phone_no Like '%"+patinetNumbers[y]+"%'");
                    if (!resultSetPhone.next() ) {
                        String phoneInsertQuery = " insert into phone_no_pat (patientID,phone_no)"
                                + " values (?,?)";
                        PreparedStatement preparedStmtPhone = conn.prepareStatement(phoneInsertQuery, Statement.RETURN_GENERATED_KEYS);
                        preparedStmtPhone.setString (1, patientId.get(i));
                        preparedStmtPhone.setString (2, patinetNumbers[y]);

                        preparedStmtPhone.execute();

                    }


                }
                ResultSet resultSetImplant = statement.executeQuery("select * from implant Where patientID Like '%"+patientId.get(i)+"%'");
                if (!resultSetImplant.next() ) {
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                    java.sql.Date implantDate = null;
                    java.sql.Date statusDatei = null;
                    try {
                        java.util.Date utilDate = format.parse(date.get(i));
                        implantDate = new java.sql.Date(utilDate.getTime());

                        java.util.Date utilDates = format.parse(statusDate.get(i));
                        statusDatei = new java.sql.Date(utilDates.getTime());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int isOnDevice = status.get(i).indexOf("On Device");

                    String implantInsertQuery = " insert into implant (patientID  ,HVADPumpID ,hospitalID, implant_date , status_date ,patient_status, descr_dev_of_pat ,stay_duration_of_dev ,qty ,on_device)"

                            + " values (?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement preparedStmtImplant = conn.prepareStatement(implantInsertQuery, Statement.RETURN_GENERATED_KEYS);
                    preparedStmtImplant.setString (1, patientId.get(i));
                    preparedStmtImplant.setString (2, HVADID.get(i));
                    preparedStmtImplant.setString (3, hospitalID);
                    preparedStmtImplant.setString (4, String.valueOf(implantDate));
                    preparedStmtImplant.setString (5, String.valueOf(statusDatei));
                    preparedStmtImplant.setString (6, status.get(i));
                    preparedStmtImplant.setString (7, "");
                    long diff = statusDatei.getTime() - implantDate.getTime();
                    int diffDay = ((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1);
                    preparedStmtImplant.setString (8, String.valueOf(diffDay));
                    preparedStmtImplant.setString (9, "1");
                    if (isOnDevice == -1){
                        preparedStmtImplant.setString (10, "0");
                    }else {
                        preparedStmtImplant.setString (10, "1");
                    }

                    preparedStmtImplant.execute();

                }





            }
           System.out.print("Yeah Baby İşlem tamam");

/*
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, "alper");
            preparedStmt.setString (2, "alper");
            preparedStmt.setString (3, "alper");
            preparedStmt.setString (4, "alper");
*/
            // execute the preparedstatement
         ///   preparedStmt.execute();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
