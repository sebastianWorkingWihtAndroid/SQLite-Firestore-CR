package com.example.adopta;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class insercion extends AppCompatActivity {

    int PICK_IMAGE_REQUEST=1;
    EditText cedulaprofesional,fechav, vereda, finca, nombre, cccedula, telefono, visita, objeto, observaciones,consi,dosi;
    Spinner departamento, municipios, actividad,spinerFirma;
    Button gdatos ,pdf,generarpdf;
    String depa = "";
    String muni = "";
    String acti = "";
    String sfirm =  "";
    ImageButton imageButton, imageButton3,img_foto, abrirYTomar;
    DBHelper DB;
    ImageView imageView10;
    private  lienzo lienzo;

    private Bitmap bitmap,gfoto;
    public  static int CAMERA;
    Uri imageUri;

    private Bitmap firma1, fima2;
    Bitmap logoU, logoP, tam1,tam2;
    //ANCHO DEL PDF
    int width=1800;
    int height=2000;
    String rutaImagen;
    private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insercion);

        //BOTONES PARA LA FIRMA
        imageButton = findViewById(R.id.imageButton);
        imageButton3 = findViewById(R.id.imageButton3);
        imageView10 =findViewById(R.id.imageView10);
        //SE GUARDA EL MAPA DE BITS EN ESA VARIABLE
        fechav = findViewById(R.id.fechaB);
        departamento = findViewById(R.id.departamento);
        municipios = findViewById(R.id.municipio);
        vereda = findViewById(R.id.vereda);
        finca = findViewById(R.id.finca);
        nombre = findViewById(R.id.nombre);
        cccedula = findViewById(R.id.cedulaB);
        cedulaprofesional=findViewById(R.id.cedulaprofesional);
        telefono = findViewById(R.id.telefono);
        visita = findViewById(R.id.novisitaB);
        actividad = findViewById(R.id.actividad);
        objeto = findViewById(R.id.objeto);
        observaciones = findViewById(R.id.observaciones);
        consi = findViewById(R.id.consi);
        dosi = findViewById(R.id.dosi);
        spinerFirma = findViewById(R.id.spinerFirma);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int suma= month+1;
        fechav.setText(""+year+" "+suma+" "+day);
        //BOTON CREAR PDF
        generarpdf = findViewById(R.id.pdf);
        //BOTON INSERTAR DATOS
        //LLAMADO A LA BASE DE DATOS
        DB = new DBHelper(this, "recordv", null, 1);
        //LLAMADO A FIRESTORE
        mfirestore = FirebaseFirestore.getInstance();
        cargar();

        ArrayAdapter<CharSequence> adaptadorfirma = ArrayAdapter.createFromResource(this, R.array.sifirma, android.R.layout.simple_spinner_item);
        adaptadorfirma.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinerFirma.setAdapter(adaptadorfirma);

        spinerFirma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sfirm = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        generarpdf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (fechav.getText().toString().isEmpty()) {
                    fechav.setError("ingrese fecha");
                } else if (cccedula.getText().toString().isEmpty()) {
                    cccedula.setError("ingrese cedula");
                } else if (visita.getText().toString().isEmpty()) {
                    visita.setError("ingrese fecha final");

                } else if (objeto.getText().toString().isEmpty()) {
                    objeto = findViewById(R.id.objeto);

                } else if (observaciones.getText().toString().isEmpty()) {
                    observaciones = findViewById(R.id.observaciones);

                } else {

                    String fec = fechav.getText().toString();
                    String vis = visita.getText().toString();
                    String ced = cccedula.getText().toString();
                    String obj = objeto.getText().toString();
                    String ob = observaciones.getText().toString();
                    String cons = consi.getText().toString();
                    String dos = dosi.getText().toString();
                    String cedp = cedulaprofesional.getText().toString();

                    posPet(cedp,fec,vis,ced,obj,ob,cons,dos,sfirm);
                    Boolean insertara = DB.insertarDatosB(fec, ced, vis, obj,sfirm, ob,cons,dos);

                    if (insertara == true) {
                        Toast.makeText(insercion.this, "REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), VistaVisitas.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(insercion.this, "ERROR,NO SE REGISTRÓ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Consulta.class);
                        startActivity(intent);
                    }
                }

            }
        });

        //permisos
        ActivityCompat.requestPermissions(this, new String[]{
                WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }
    private void cargar(){
        Cursor cursor= DB.getReadableDatabase().rawQuery("select cedula from usuarios",null);
        while (cursor.moveToNext()){
            String cedp=cursor.getString(0);
            cedulaprofesional.setText(cedp);

        }
    }

    private void posPet(String cedp,String fec,String vis, String ced,String obj,String ob,String cons,String dos
            ,String sfirm) {

        Map<String,Object> map = new HashMap<>();
        map.put("cedulaprofesional",cedp);
        map.put("fecha",fec);
        map.put("visita",vis);
        map.put("cedula",ced);
        map.put("objeto",obj);
        map.put("observaciones",ob);
        map.put("recomendaciones",cons);
        map.put("dosis",dos);
        map.put("firma",sfirm);

        mfirestore.collection("tb_visitas")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Guardado en NUBE",Toast.LENGTH_LONG);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG);
                    }
                });


    }


    /*

    //ABRIR LA CAMARA
    private  void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imagePath = UriMayorQ();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imagePath);
        startActivityForResult(intent, CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA){
            if (resultCode== Activity.RESULT_OK){
                Toast.makeText(this, "Imagen Capturada", Toast.LENGTH_SHORT).show();
                abrirYTomar.setImageURI(imageUri);
            }
        }
    }

    //Aqui se Crea la imagen
    private Uri UriMayorQ(){
        Uri objuri=null;
        ContentResolver contentResolver = getContentResolver();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            objuri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else{
            objuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String imgName=cccedula.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,imgName+".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/"+"Record/");
        Uri finalUri = contentResolver.insert(objuri,contentValues);
        imageUri=finalUri;
        return finalUri;
    }
    */

}