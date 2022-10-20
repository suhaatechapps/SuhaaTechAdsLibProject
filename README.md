# SuhaaTechAdsLibProject

1-	Add Repository:

		repositories {
			maven { url 'https://jitpack.io' }
		}

2-	Add Library:

  		implementation 'com.github.suhaatechapps:SuhaaTechAdsLibProject:9.1.0.6'
3-	Add In Application CLass:
	
		if (TrueConstants.INSTANCE.isNetworkAvailable(TrueAdManager.context) &&
                    TrueConstants.INSTANCE.isNetworkSpeedHigh()) {
                TrueAdLimitationObj.INSTANCE.limitationFun(this, "ca-app-pub-3940256099942544/1033173712", true, true, 4, 3, 0, 1, true);
            }
	
