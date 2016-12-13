package com.example.nicolas.frag_list;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicolas on 13/12/16.
 */

public class Fragmentlist extends ListFragment {


    private static final List<Map<String, String>> items = new ArrayList<Map<String, String>>();
    private static final String[] keys = { "Nombre", "Descripcion" };
    private static final int[] controlIds = { android.R.id.text1,
            android.R.id.text2 };


    ProgressDialog prgDialog;
    private static List<Map<String, String>> listas = new ArrayList<Map<String, String>>();;

    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("line1", "Мурзик");
        map.put("line2", "Агент 003");
        items.add(map);
        map = new HashMap<String, String>();
        map.put("line1", "Барсик");
        map.put("line2", "Агент 004");
        items.add(map);
        map = new HashMap<String, String>();
        map.put("line1", "Васька");
        map.put("line2", "Агент 005");
        items.add(map);
        map = new HashMap<String, String>();
        map.put("line1", "Рыжик");
        map.put("line2", "Агент 006");
        items.add(map);
        map = new HashMap<String, String>();
        map.put("line1", "Кузя");
        map.put("line2", "Агент 007");
        items.add(map);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.list_fragment,container,false);

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Conectando......");
        prgDialog.setCancelable(false);



        return view;
    }

    private void cargalist() {


        AsyncHttpClient client = new AsyncHttpClient();

        prgDialog.show();

        client.get("http://elca.sytes.net:2122/app_android/operaciones/get_pedido.php", new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(String response) {

                updatelis(response);


            }


            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                //prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Dispositivo Sin Conexión a Internet",
                            Toast.LENGTH_LONG).show();
                }
                prgDialog.hide();

            }


        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cargalist();

        System.out.println(listas);

        ListAdapter adapter = new SimpleAdapter(getActivity(), listas,
                android.R.layout.simple_list_item_2, keys, controlIds);
        setListAdapter(adapter);




    }


    public void updatelis(final String response)
    {

        List<Map<String, String>> eventos = new ArrayList<>();

        try {
//                    int success;
            JSONArray arr = new JSONArray(response);
            System.out.println(response);

            System.out.println(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                HashMap<String,String> map = new HashMap<String, String>();
                System.out.println(obj);

                map.put("id_eventos",obj.get("id_eventos").toString());
                map.put("Nombre",obj.get("Nombre").toString());
                map.put("Descripcion",obj.get("Descripcion").toString());
                map.put("Foto",obj.get("Foto").toString());
                eventos.add(map);


            }
            listas=eventos;
           // System.out.println(listas);
            prgDialog.hide();
//
//



//
//
        } catch (JSONException e) {
            e.printStackTrace();
            prgDialog.hide();
        }



    }





}
