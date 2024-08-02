package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Parse {

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<Employee> parseXML() {
        List<Employee> list = new ArrayList<>();
        Long id = null;
        String firstName = "";
        String lastName = "";
        String country = "";
        int age = 0;

        File fileXML = new File("data.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            doc = factory.newDocumentBuilder().parse(fileXML);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Node root = doc.getFirstChild();
        NodeList rootChilds = root.getChildNodes();
        for (int i = 0; i < rootChilds.getLength(); i++) {

            if (rootChilds.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            NodeList employee = rootChilds.item(i).getChildNodes();

            for (int j = 0; j < employee.getLength(); j++) {

                if (employee.item(j).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                switch (employee.item(j).getNodeName()) {
                    case "id": {
                        id = Long.valueOf((employee.item(j).getTextContent()));
                        System.out.println(id);
                        break;
                    }
                    case "firstName": {
                        firstName = employee.item(j).getTextContent();
                        System.out.println(firstName);
                        break;
                    }
                    case "lastName": {
                        lastName = employee.item(j).getTextContent();
                        System.out.println(lastName);
                        break;
                    }
                    case "country": {
                        country = employee.item(j).getTextContent();
                        System.out.println(country);
                        break;
                    }
                    case "age": {
                        age = Integer.parseInt(employee.item(j).getTextContent());
                        System.out.println(age);
                        break;
                    }
                }
            }
            Employee employees = new Employee(id, firstName, lastName, country, age);
            list.add(employees);
        }
        return list;
    }


    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(list, listType);

        return json;
    }


    public static void writeString(String json) {

        try (FileWriter file = new FileWriter("new_data.json")) {
            file.write(json.toString());
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static String readString(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return String.valueOf(stringBuilder);
    }


    public static List<Employee> jsonToList(String json) {
        List<Employee> list = new ArrayList<>();
        JSONParser parser = new JSONParser();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            JSONArray array = (JSONArray) parser.parse(json);
            for (Object item : array) {
                JSONObject jsonObject = (JSONObject) item;
                Employee employee = gson.fromJson(String.valueOf(jsonObject), Employee.class);
                list.add(employee);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}

