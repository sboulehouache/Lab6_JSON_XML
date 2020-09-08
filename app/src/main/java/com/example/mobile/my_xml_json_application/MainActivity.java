package com.example.mobile.my_xml_json_application;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
//import org.xml.sax.InputSource;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    Button jsonBtn, xmlBtn, externXmlBtn;
    JSONObject jsonObject;
    String data = "";
    TextView textViewJson, textViewXml, externXmlView;
    public static final String F_NAME="mydocument.xml";
    public static String F_PATH="";
    File dbFile;
    String TAG="JSON_XML";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> items=new ArrayList<String>();

        XmlPullParser xmlPullParser=getResources().getXml(R.xml.words);

        try {
            while (xmlPullParser.getEventType()!=XmlPullParser.END_DOCUMENT) {
                if (xmlPullParser.getEventType()==XmlPullParser.START_TAG) {
                    if (xmlPullParser.getName().equals("word")) {
                        items.add(xmlPullParser.getAttributeValue(0));
                        Log.i("TAG", xmlPullParser.getAttributeValue(0)+": \n");
                    }
                }

                xmlPullParser.next();
            }
        }
        catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        textViewJson = (TextView) findViewById(R.id.textViewJson);
        jsonBtn = (Button) findViewById(R.id.jsonbtn);
        final String stringJson=" {\"Employee\" :[" +
                "{\"id\":\"01\",\"name\":\"Mohamed\",\"salary\":\"500000\"}" +
                ",{\"id\":\"02\",\"name\":\"Ahmed\",\"salary\":\"660000\"}" +
                ",{\"id\":\"03\",\"name\":\"Sami\",\"salary\":\"600000\"}]}";
        /*final String stringJson="" +
                "{" +
                "\"Employee \" :[" +
                    "{" +
                        "\"id\":\"01\"," +
                        "\"name\":\"Mohamed\"," +
                        "\"salary\":\"500000\"" +
                    "},"+
                    "{" +
                        "\"id\":\"02\"," +
                        "\"name\":\"Ahmed\"," +
                        "\"salary\":\"500000\"" +
                    "},"+
                    "{" +
                        "\"id\":\"03\"," +
                        "\"name\":\"Sami\"," +
                        "\"salary\":\"600000\"" +
                    "}" +
                "]" +
                "}";*/



        jsonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String stringJson = getJSONData(MainActivity.this, "employe.json");
                    jsonObject = new JSONObject(stringJson);
                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = jsonObject.optJSONArray("Employee");
                    //Iterate the jsonArray and print the info of JSONObjects
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = Integer.parseInt(jsonObject.optString("id").toString());
                        String name = jsonObject.optString("name").toString();
                        if(name.matches("Ahmed")) {
                            float salary = Float.parseFloat(jsonObject.optString("salary").toString());
                            data += "Node " + i + " : \n id= " + id + " \n Name= " + name + " \n Salary= " + salary + " \n ";
                        }
                    }
                    textViewJson.setText(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        textViewXml = (TextView) findViewById(R.id.textViewXml);
        xmlBtn=(Button)findViewById(R.id.xmlbtn);
        xmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xml = "<message>HELLO!</message>";
                org.jdom.input.SAXBuilder saxBuilder = new SAXBuilder();
                try {
                    org.jdom.Document doc = saxBuilder.build(new StringReader(xml));
                    String message = doc.getRootElement().getText();
                    textViewXml.setText(message);
                } catch (JDOMException e) {
                    // handle JDOMException
                } catch (IOException e) {
                    // handle IOException
                }
            }
        });
        externXmlView = (TextView) findViewById(R.id.externexmlView);
        externXmlBtn=(Button)findViewById(R.id.externexmlbtn);
        externXmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream assestFile=null;
                        org.jdom.input.SAXBuilder saxBuilder = new SAXBuilder();
                        try {
                            //assestFile = getAssets().open("mydocument.xml");
                            assestFile = getResources().openRawResource(R.raw.mydocument);
                            Log.i("TAG",assestFile.toString()+"");
                            InputSource inStream = new InputSource(assestFile);
                            Document document = saxBuilder.build(inStream);
                            Element root = document.getRootElement();
                            List listEtudiants = root.getChildren("Livre");
                            Iterator i = listEtudiants.iterator();
                            while(i.hasNext()) {
                                Log.i("TAG", assestFile.toString() + "___________"+listEtudiants.size());
                                Element courant = (Element)i.next();
                                final String numero=(courant.getChild("numero")).getText();
                                final String intitule=(courant.getChild("intitule")).getText();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("TAG", numero+": ");
                                        Log.i("TAG", intitule+"\n");
                                        externXmlView.append(numero+" : ");
                                        externXmlView.append(intitule+"\n");
                                    }
                                });
                            }

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                            Log.e("TAG","error1");}
                        catch (Exception e) {
                            e.printStackTrace();
                            Log.e("TAG","error");
                        }
                    }
                }).start();


            }
        });

    }
   /* public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }*/
   public String getJSONData(Context context, String textFileName) {
       String strJSON;
       StringBuilder buf = new StringBuilder();
       InputStream json;
       try {
           json = context.getAssets().open(textFileName);
           //json = getResources().openRawResource(R.raw.employe);
           BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

           while ((strJSON = in.readLine()) != null) {
               buf.append(strJSON);
           }
           in.close();
       } catch (IOException e) {
           e.printStackTrace();
       }

       return buf.toString();
   }
}
