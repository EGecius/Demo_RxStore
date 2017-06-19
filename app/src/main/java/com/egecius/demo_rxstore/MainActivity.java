package com.egecius.demo_rxstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

	private EditText nameEditText;
	private Button addButton;

	private final Presenter presenter = new Presenter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		nameEditText = (EditText) findViewById(R.id.nameEditText);
		addButton = (Button) findViewById(R.id.addButton);

		setOnclickListeners();
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
}
