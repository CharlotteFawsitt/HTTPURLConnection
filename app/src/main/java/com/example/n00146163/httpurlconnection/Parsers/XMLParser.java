package com.example.n00146163.httpurlconnection.Parsers;

/**
 * Created by n00146163 on 13/03/2018.
 */
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.example.n00146163.httpurlconnection.Model.Patient;

public class XMLParser {

    public static List<Patient> parseFeed(String content) {

        try {

            // Establish Variables that you need to keep track of where you are
            // are you in a data item you care about
            boolean inDataItemTag = false;
            //Which XML tag are you current in
            String currentTagName = "";
            // the Dog object you are currently constructing from the XML
            Patient p = null;
            // Full list of flowers as you pull them out of the XML
            List<Patient> patientsList = new ArrayList<>();


            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            // content is the complete XML content that was passed in from the calling program
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            // XMLPullParser generates events. Once for each start tag, end tag and for text events
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();

                        // if starting a new Dog in the XML create a new Patient object to start building it up.
                        if (currentTagName.equals("patient")) {
                            inDataItemTag = true;
                            p = new Patient();
                            patientsList.add(p);
                        }
                        break;

                    // if you find an end task check to see if its the end of a Patient object
                    case XmlPullParser.END_TAG:
                        // if leaving current product
                        if (parser.getName().equals("patient")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && p != null) {
                            switch (currentTagName) {
                                case "patientId":
                                    p.setPatientId(parser.getText());
                                    break;
                                case "name":
                                    p.setName(parser.getText());
                                    break;
                                case "gender":
                                    p.setGender(parser.getText());
                                    break;
                                case "phoneNumber":
                                    p.setPhoneNumber(parser.getText());
                                    break;
                                case "nextApp" :
                                    p.setNextApp(parser.getText());
                                    break;
                                case "photo" :
                                    p.setPhoto(parser.getText());
                                default:
                                    break;
                            }
                        }
                        break;
                }

                eventType = parser.next();

            }

            // return the complete list that was generated above
            return patientsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

}

