package com.tipo666.vicaria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;

    //defining a database reference
    private DatabaseReference databaseReference;

    //our new views
    private EditText editTextName, editTextAddress;
    private Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //getting the views from xml resource
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSave = (Button) findViewById(R.id.buttonSave);



        buttonSave.setOnClickListener(this);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //displaying logged in user name
        textViewUserEmail.setText("Hola "+user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener(this);

    }

    private void saveUserInformation() {
        //TOmando valores desde la base de datos

        String name = editTextName.getText().toString().trim();
        String add = editTextAddress.getText().toString().trim();

        //Creando el objeto de UserInformation
        UserInformation userInformation = new UserInformation(name, add);

        //Tomando el usuario que esta logueado
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Guardando datos a la base de datos de FireBase
        /*
        Primero creamos un nuevo hijo en Firebase con un ID único del usuario logueado
        * Y luego para ese usuario bajo su ID único guardamos los datos
        * Para guardar los datos se usa el método setValues, este método toma un objeto normal de Java
        * */
        databaseReference.child(user.getUid()).setValue(userInformation);

        //Desplegamos el Toast con la informacin guardada
        Toast.makeText(this, "Información almacenada...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {

        if(view == buttonSave){
            saveUserInformation();
        }

        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
