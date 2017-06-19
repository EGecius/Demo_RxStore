package com.egecius.demo_rxstore;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

	private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 13;
	private final Presenter presenter = new Presenter();
	private EditText nameEditText;
	private Button addButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		nameEditText = (EditText) findViewById(R.id.nameEditText);
		addButton = (Button) findViewById(R.id.addButton);
		ensurePermissionsGranted();

		setOnclickListeners();

		prepopulateField();
	}

	private void ensurePermissionsGranted() {
		String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
		if (isPermissionGrantNeeded(permission)) {
			requestPermission(permission);
		}
	}

	private void requestPermission(final String permission) {
		ActivityCompat.requestPermissions(this, new String[]{permission},
				MY_PERMISSIONS_REQUEST_READ_CONTACTS);
	}

	private boolean isPermissionGrantNeeded(final String permission) {
		return ContextCompat.checkSelfPermission(this, permission) !=
				PackageManager.PERMISSION_GRANTED;
	}

	private void setOnclickListeners() {
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				String name = nameEditText.getText().toString();
				clearText();
				presenter.onAddClicked(name);
			}
		});
	}

	private void clearText() {
		nameEditText.setText("");
	}

	private void prepopulateField() {
		nameEditText.setText("Edward");
	}
}
