package com.clican.irp.android.ui;

import java.util.ArrayList;
import java.util.Map;

import roboguice.activity.RoboActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.clican.irp.android.R;
import com.clican.irp.android.http.impl.HttpGatewayImpl;
import com.clican.irp.android.service.LoginService;
import com.google.inject.Inject;

public class LoginActivity extends RoboActivity {

	@Inject
	private LoginService loginService;

	private Map<String, String> loginServers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);
		try {
			Spinner productCustomerSpinner = (Spinner) findViewById(R.id.product_customer_spinner);
			loginServers = loginService.queryLoginServers();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item,
					new ArrayList<String>(loginServers.keySet()));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			productCustomerSpinner.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Button loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText userNameEdit = (EditText) findViewById(R.id.username_edit);
				EditText passwordEdit = (EditText) findViewById(R.id.password_edit);
				Spinner productCustomerSpinner = (Spinner) findViewById(R.id.product_customer_spinner);
				HttpGatewayImpl.URL_PREFIX = loginServers
						.get(productCustomerSpinner.getSelectedItem()
								.toString());
				try {
					loginService.login(userNameEdit.getText().toString(),
							passwordEdit.getText().toString(), null);
					Intent i = new Intent(LoginActivity.this,
							IrpAndroidActivity.class);
					startActivity(i);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

}
