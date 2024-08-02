package org.example;

import java.util.List;

import static org.example.Parse.listToJson;
import static org.example.Parse.*;


public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

//         задание 1
        List<Employee> listCSV = parseCSV(columnMapping, fileName);
        String jsonCSV = listToJson(listCSV);
        writeString(jsonCSV);

//         задание 2
        List<Employee> listXML = parseXML();
        String jsonXML = listToJson(listXML);
        writeString(jsonXML);

//          задание 3
        String json = readString("new_data.json");
        List<Employee> list = jsonToList(json);
        System.out.println("> Task :Main.main()");

        String deletesSquareBracket = list.toString();
        deletesSquareBracket = deletesSquareBracket.substring(1,
                deletesSquareBracket.length() - 1);
        System.out.println(deletesSquareBracket);

    }
}