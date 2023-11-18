package com.example.biosense.utils;

import android.content.Context;
import android.widget.Toast;

public class MensagensToast {

	public static void showMessage (Context ctx, String message){
		Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
	}

	public static void showMessage(Context ctx, int resource) {
		Toast.makeText(ctx, resource, Toast.LENGTH_LONG).show();
	}

}
