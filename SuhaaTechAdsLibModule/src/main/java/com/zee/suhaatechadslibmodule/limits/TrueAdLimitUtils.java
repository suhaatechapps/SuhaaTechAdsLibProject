package com.zee.suhaatechadslibmodule.limits;

import android.content.Context;
import android.util.Log;

/**
 * Created by Malik Zeeshan Habib (True Muslim) on 14,May,2022
 * https://www.truemuslimstudio.com
 */
public class TrueAdLimitUtils {

    public static String adUnitId;
    public static String adsType;
    public static final String TAG = "TrueAdLimitUtils";


    public static boolean isBanned(Context context, String unitId, String adType) {
        adUnitId = unitId;
        adsType = adType;
        boolean limitActivated = TruePrefUtils.getInstance().init(context, unitId).getLimitActivated();
        boolean adActivated = TruePrefUtils.getInstance().init(context, unitId).getAdActivated();
        int clicksLimit = TruePrefUtils.getInstance().init(context, unitId).getClicksLimit();
        int impressionsLimit = TruePrefUtils.getInstance().init(context, unitId).getImpressionsLimit();
        int clicksCount = TruePrefUtils.getInstance().init(context, unitId).getClicksCount();
        int impressionsCount = TruePrefUtils.getInstance().init(context, unitId).getImpressionsCount();
        long timeEndBan = TruePrefUtils.getInstance().init(context, unitId).getTimeEndBan();
        Log.d("TrueAdLimitUtils", "isBanned: " + unitId + " Click Limits : " + clicksLimit + " Impression Limit :" + impressionsLimit + " Ad Type: " + adType + "\n Click Limit Activated: " + limitActivated);

        boolean banned = false;

        /**If ad deactivated*/
        if (!adActivated) {
            banned = true;
            Log.d(TAG, "isAdActivated: " + clicksLimit);
        }

        /**If Limit Activated*/
        if (!limitActivated) {
            banned = true;
            Log.d(TAG, "isLimitActivated: " + clicksLimit);
        }

        /**If limit is 0 or not defined will show ads anyway*/
        if (clicksLimit == 0 || impressionsLimit == 0)
            banned = true;
        Log.d(TAG, "ClickCount : " + clicksCount + "Click Limit: " + clicksLimit + "Impression Count: " + impressionsCount + "Impression Limit: " + impressionsLimit+"Impression Limit:" + impressionsLimit);

        /**If unit is banned from showing ad for a period of time*/
        if (System.currentTimeMillis() < timeEndBan) {
            banned = true;
            Log.d(TAG, "Time End: " + timeEndBan);
        }

        if (clicksCount >= clicksLimit || impressionsCount >= impressionsLimit) {
            Log.d("TrueAdLimitUtils", "Impression Count: " + impressionsCount);
            TruePrefUtils.getInstance().init(context, unitId).zResetClicksCounter();
            TruePrefUtils.getInstance().init(context, unitId).zResetImpressionsCounter();
            TruePrefUtils.getInstance().init(context, unitId).zUpdateBanTime();
            banned = true;
        }
        return banned;
    }
}
