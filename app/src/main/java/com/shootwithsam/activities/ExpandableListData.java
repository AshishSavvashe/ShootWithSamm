package com.shootwithsam.activities;

import com.shootwithsam.models.Pojo_Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SAI on 9/1/2017.
 */

public class ExpandableListData {
    private ArrayList<Pojo_Notification> arrayListNotification;
    ArrayList<Pojo_Notification> notify = new ArrayList<Pojo_Notification>();
    String[] Notification;
    String strNotification = "";

    public static HashMap<String, List<Pojo_Notification>> getData(ArrayList<Pojo_Notification> arrayList) {
        HashMap<String, List<Pojo_Notification>> expandableListDetail = new HashMap<String, List<Pojo_Notification>>();

        List<Pojo_Notification> Unseen = new ArrayList<Pojo_Notification>();
        List<Pojo_Notification> Seen = new ArrayList<Pojo_Notification>();

        for (int index = 0; index < arrayList.size(); index++) {
            Pojo_Notification data = arrayList.get(index);
            if (data != null) {
                if (data.getStrIsSeen().equalsIgnoreCase("1"))
                    Unseen.add(data);
                else
                    Seen.add(data);
            }
        }

        expandableListDetail.put("Unseen", Unseen);
        expandableListDetail.put("Seen", Seen);
        return expandableListDetail;
    }

}
