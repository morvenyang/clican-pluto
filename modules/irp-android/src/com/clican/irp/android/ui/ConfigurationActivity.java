package com.clican.irp.android.ui;

import roboguice.activity.RoboActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.clican.irp.android.R;
import com.clican.irp.android.model.HttpProxy;
import com.clican.irp.android.service.ConfigurationService;
import com.google.inject.Inject;

public class ConfigurationActivity extends RoboActivity {

	@Inject
	private ConfigurationService configurationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.configuration);
		EditText proxyHostEdit = (EditText) findViewById(R.id.proxy_host_edit);
		EditText proxyPortEdit = (EditText) findViewById(R.id.proxy_port_edit);
		proxyPortEdit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		EditText proxyUserNameEdit = (EditText) findViewById(R.id.proxy_user_name_edit);
		EditText proxyPasswordEdit = (EditText) findViewById(R.id.proxy_password_edit);
		HttpProxy httpProxy = configurationService.getHttpProxy();
		if (httpProxy != null) {
			proxyHostEdit.getText().clear();
			proxyHostEdit.getText().append(httpProxy.getProxyHost());
			proxyPortEdit.getText().clear();
			proxyPortEdit.getText().append(httpProxy.getProxyPort().toString());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EditText proxyHostEdit = (EditText) findViewById(R.id.proxy_host_edit);
		EditText proxyPortEdit = (EditText) findViewById(R.id.proxy_port_edit);
		EditText proxyUserNameEdit = (EditText) findViewById(R.id.proxy_user_name_edit);
		EditText proxyPasswordEdit = (EditText) findViewById(R.id.proxy_password_edit);
		Integer proxyPort = null;
		if (proxyPortEdit.getText().toString() != null&&proxyPortEdit.getText().toString().trim().length()>0) {
			proxyPort = new Integer(proxyPortEdit.getText().toString());
		}
		configurationService.configureHttpProxy(proxyHostEdit.getText()
				.toString(), proxyPort, proxyUserNameEdit.getText().toString(),
				proxyPasswordEdit.getText().toString());
	}

}
