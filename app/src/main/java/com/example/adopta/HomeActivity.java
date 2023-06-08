package com.example.adopta;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import android.util.Base64;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {
    ByteArrayOutputStream stream;
    private final static String ETIQUETA_ERROR = "ERROR PDF";
    private File archivoPDF;
    EditText fechav, vereda, finca, nombre, cccedula, telefono, visita, objeto, observaciones,
            usuario,nam,consideraciones,dosis;
    Spinner departamento, municipios, actividad,sifirma;
    Button gdatos ,pdf;
    String depa = "";
    String muni = "";
    String acti = "";
    String fir =  "";
    Uri imageUri;
    public static int CAMERA=10;
    StatticLayout statticLayout;
    ImageButton imageButton, imageButton3,img_foto;
    ImageButton abrirYTomar;
    DBHelper DB;
    ImageView imageView10;
    private  lienzo lienzo;

    private Bitmap bitmap;
    int PICK_IMAGE_REQUEST=1;

    private Bitmap firma1, fima2,gfoto;
    Bitmap logoU, logoP, tam1,tam2,logosgr,logomin,tam3,tam4,logos, tam5,f1,f2,galeriafoto,ftbit,resizeImage;
    //ANCHO DEL PDF
    int width=600;
    int height=800;
    OutputStream outputStream;
    String rutaImagen;

    private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //BOTONES PARA LA FIRMA
        imageButton = findViewById(R.id.imageButton);
        imageButton3 = findViewById(R.id.imageButton3);
        imageView10 =findViewById(R.id.imageView10);
        //SE GUARDA EL MAPA DE BITS EN ESA VARIABLE
        img_foto=findViewById(R.id.btncamara);
        fechav = findViewById(R.id.fechaB);
        departamento = findViewById(R.id.departamento);
        municipios = findViewById(R.id.municipio);
        vereda = findViewById(R.id.vereda);
        finca = findViewById(R.id.finca);
        nombre = findViewById(R.id.nombre);
        cccedula = findViewById(R.id.cedulaB);
        telefono = findViewById(R.id.telefono);
        visita = findViewById(R.id.novisitaB);
        actividad = findViewById(R.id.actividad);
        objeto = findViewById(R.id.objeto);
        observaciones = findViewById(R.id.observaciones);
        consideraciones = findViewById(R.id.consideraciones);
        dosis = findViewById(R.id.dosis);
        sifirma=findViewById(R.id.firma);
        usuario=findViewById(R.id.codigo);
        nam=findViewById(R.id.name);
        abrirYTomar=findViewById(R.id.btncamara);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int suma= month+1;
        fechav.setText(""+year+" "+suma+" "+day);
        //cargarPreferencias();
        //BOTON CREAR PDF
        pdf = findViewById(R.id.pdf);
        //BOTON INSERTAR DATOS
        gdatos = findViewById(R.id.generarpdf);

        mfirestore = FirebaseFirestore.getInstance();


        //VARIABLES EN DONDE SE CARGAN LAS IMAGENES AL PDF

        /*
        logoU = BitmapFactory.decodeResource(getResources(), R.drawable.logounipdf);
        tam1=Bitmap.createScaledBitmap(logoU,128,204,false);

        logoP = BitmapFactory.decodeResource(getResources(), R.drawable.logosgrpdf);
        tam2=Bitmap.createScaledBitmap(logoP,300,168,false);

        logosgr = BitmapFactory.decodeResource(getResources(), R.drawable.sistemageneral);
        tam3=Bitmap.createScaledBitmap(logoU,820,158,false);

        logomin = BitmapFactory.decodeResource(getResources(), R.drawable.minciencia);
        tam4=Bitmap.createScaledBitmap(logoU,356,154,false);

        logos = BitmapFactory.decodeResource(getResources(), R.drawable.logos);
        tam5=Bitmap.createScaledBitmap(logos,1100,157,false);
        */


        logos = BitmapFactory.decodeResource(getResources(), R.drawable.logos);
        tam5=Bitmap.createScaledBitmap(logos,500,81,false);
        abrirYTomar.buildDrawingCache();
         galeriafoto = abrirYTomar.getDrawingCache();


        //LLAMADO A LA BASE DE DATOS
        DB = new DBHelper(this, "recordv", null, 1);

        //ADAPTADORES PARA LOS SPINERS

        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.departamento, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_item);
        departamento.setAdapter(adaptador);

        ArrayAdapter<CharSequence> adaptadorm = ArrayAdapter.createFromResource(this, R.array.municipio, android.R.layout.simple_spinner_item);
        adaptadorm.setDropDownViewResource(android.R.layout.simple_spinner_item);
        municipios.setAdapter(adaptadorm);

        ArrayAdapter<CharSequence> adaptadora = ArrayAdapter.createFromResource(this, R.array.actividad, android.R.layout.simple_spinner_item);
        adaptadora.setDropDownViewResource(android.R.layout.simple_spinner_item);
        actividad.setAdapter(adaptadora);

        ArrayAdapter<CharSequence> adaptadorb = ArrayAdapter.createFromResource(this, R.array.sifirma, android.R.layout.simple_spinner_item);
        adaptadorb.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sifirma.setAdapter(adaptadorb);

        //ESTABLECIENDO LOS ADAPTADORES

        departamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                depa = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        municipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                muni = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        actividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                acti = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sifirma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fir = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abriDialogoFirma(Gravity.CENTER);
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abriDialogoFirma2(Gravity.CENTER);
            }
        });

        //LLAMAR USUARIO Y CEDULA EN REGISTRO
        cargar();


        gdatos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (fechav.getText().toString().isEmpty()) {
                    fechav.setError("ingrese fecha");
                } else if (finca.getText().toString().isEmpty()) {
                    finca.setError("ingrese fecha final");
                } else if (nombre.getText().toString().isEmpty()) {
                    nombre.setError("ingrese fecha final");

                } else if (telefono.getText().toString().isEmpty()) {
                    telefono = findViewById(R.id.cedula);

                } else if (visita.getText().toString().isEmpty()) {
                    visita = findViewById(R.id.cedula);

                } else {

                    String fec = fechav.getText().toString();
                    String ver = vereda.getText().toString();
                    String fin = finca.getText().toString();
                    String nom = nombre.getText().toString();
                    String ced = cccedula.getText().toString();
                    String tel = telefono.getText().toString();
                    String vis = visita.getText().toString();
                    String obj = objeto.getText().toString();
                    String ob = observaciones.getText().toString();
                    String con = consideraciones.getText().toString();
                    String dos = dosis.getText().toString();
                    String user = usuario.getText().toString();
                    String nm = nam.getText().toString();

                    //GUARDAR EN FIRESTORE
                    posPet(user,nm,fec,depa,muni,ver,fin,nom,ced,tel,vis,acti,obj,ob,con,dos,fir);


                    //GUARDAR EN SQLITE
                    Boolean insertara = DB.insertarDatosHome(fec, depa, muni, ver, fin, nom, ced,
                            tel, vis, acti, obj, ob,con,dos,fir, firma1, fima2, bitmap);

                    if (insertara == true) {

                       // Toast.makeText(HomeActivity.this, "REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Vista.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeActivity.this, "ERROR,NO SE REGISTRÓ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Consulta.class);
                        startActivity(intent);
                    }

                }

            }
        });

        abrirYTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });

        //permisos
        ActivityCompat.requestPermissions(this, new String[]{
                WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        crearPfd();
        /*
        abrirYTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abrirCamara();
            }
        });

         */
    }

    private void posPet(String user, String nm,String fec,String depa,String muni,String ver,String fin,String nom,String
            ced,String tel,String vis,String acti,String obj,String ob,String con,String
            dos,String fir) {

        Map <String,Object> map = new HashMap<>();
        map.put("profesional",user);
        map.put("documentoprofesional",nm);
        map.put("fecha",fec);
        map.put("departamento",depa);
        map.put("municipio",muni);
        map.put("vereda",ver);
        map.put("finca",fin);
        map.put("nombre",nom);
        map.put("cedula",ced);
        map.put("telefono",tel);
        map.put("visita",vis);
        map.put("actividad",acti);
        map.put("objeto",obj);
        map.put("observaciones",ob);
        map.put("recomendaciones",con);
        map.put("dosis",dos);
        map.put("firma",fir);

        mfirestore.collection("Registros")
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

    private void cargar(){
        Cursor cursor= DB.getReadableDatabase().rawQuery("select nombre,cedula from usuarios",null);
        while (cursor.moveToNext()){
            String user=cursor.getString(0);
            String ced=cursor.getString(1);
            usuario.setText(user);
            nam.setText(ced);

        }
    }

    /*
    //PREFERECNIAS PARA MOSTRAR EN PDF
    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
        String name=preferences.getString("nam","name");
        String user=preferences.getString("user","Error");
        String clav=preferences.getString("cl","Error");
        String clava=preferences.getString("cla","Error");

        usuario.setText(user);
        nam.setText(name);
    }
    */
    //DIALOG DE LA FIRMA2
    private void abriDialogoFirma2(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fotoiten);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);


        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);

        }

        Button calcelar = dialog.findViewById(R.id.calcelar);
        Button limpiarpantalla =dialog.findViewById(R.id.limpiarpantalla);
        lienzo =dialog.findViewById(R.id.lienzo);
        Button guarddarfirma =dialog.findViewById(R.id.guarddarfirma);
        calcelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        limpiarpantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lienzo.NuevoDibujo();
            }
        });
        guarddarfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                procesoGuardarFirma2();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //GUARDAR LA FIRMA 2
    private void procesoGuardarFirma2() {
        AlertDialog.Builder salvarDibujo = new AlertDialog.Builder(HomeActivity.this);
        salvarDibujo.setTitle("SALVAR FIRMA");
        salvarDibujo.setMessage("¿GUARDAR FIRMA?");
        salvarDibujo.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                lienzo.setDrawingCacheEnabled(true);
                fima2= Bitmap.createBitmap( lienzo.getDrawingCache());
                imageButton3.setImageBitmap(fima2);

                if (fima2 != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "¡se guardo correctamente!", Toast.LENGTH_SHORT);
                    savedToast.show();

                } else {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "¡Error! La imagen no ha podido ser salvada.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                lienzo.destroyDrawingCache();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        salvarDibujo.show();
    }
    //DIALOG DE LA FIRMA
    private void abriDialogoFirma(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fotoiten);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);

        }

        Button calcelar = dialog.findViewById(R.id.calcelar);
        Button limpiarpantalla =dialog.findViewById(R.id.limpiarpantalla);
        lienzo =dialog.findViewById(R.id.lienzo);
        Button guarddarfirma =dialog.findViewById(R.id.guarddarfirma);


        calcelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        limpiarpantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lienzo.NuevoDibujo();
            }
        });

        guarddarfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                procesoGuardarFirma();
                dialog.dismiss();


            }
        });
        dialog.show();
    }
    //GUARDAR LA FIRMA N1
    private void procesoGuardarFirma() {
        AlertDialog.Builder salvarDibujo = new AlertDialog.Builder(HomeActivity.this);
        salvarDibujo.setTitle("SALVAR FIRMA");
        salvarDibujo.setMessage("¿GUARDAR FIRMA?");
        salvarDibujo.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                lienzo.setDrawingCacheEnabled(true);
                Bitmap coverditos ;

                firma1= Bitmap.createBitmap( lienzo.getDrawingCache());
                imageButton.setImageBitmap(firma1);

                if(firma1!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),

                            "¡se guardó correctamente!", Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "¡Error! La imagen no ha podido ser salvada.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                lienzo.destroyDrawingCache();
            }
        });

        salvarDibujo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        salvarDibujo.show();
    }

    //OBTENER MAPA DE BIT
    public String getStringImagen(Bitmap  bmp){
        ByteArrayOutputStream baos =new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imagebyte=baos.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }
    //GUARDAR FOTO DESDE LA GALERIA
    public void onclick() {
     cargarImagen();

    }

    private void cargarImagen() {
        Intent galeria = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        galeria.setType("image/");
        startActivityForResult(galeria,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            imageUri = data.getData();
            abrirYTomar.setImageURI(imageUri);

            abrirYTomar.buildDrawingCache();
             gfoto = abrirYTomar.getDrawingCache();
            //Button btnc = new Button(this);
        }
    }

    //______________________
    private void crearPfd(){
        pdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                Toast.makeText(HomeActivity.this, "¡PDF GENERADO!", Toast.LENGTH_SHORT).show();

                PdfDocument myPdfDocument = new PdfDocument();
                Paint myPaint = new Paint();
                Paint titlePaint = new Paint();

                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(width, height, 1).create();
                PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                Canvas canvas = myPage1.getCanvas();

                //TITULO DE LA UNIVERSIDAD
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(16);
                canvas.drawText("UNIVERSIDAD DE CÓRDOBA", width / 2, 110, titlePaint);

                //LOGO DE LA UNIVERSIDAD
                canvas.drawBitmap(tam5, 35, 10, myPaint);


                //TITULO DE LA DATOS
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(14);
                canvas.drawText("MEDICINA VETERINARIA Y ZOOTECNIA", width / 2, 130, titlePaint);
                canvas.drawText("EXTENSIÓN", width / 2, 150, titlePaint);

                //PRIMER CAMPO DATOS
                myPaint.setTextSize(10);
                canvas.drawText("AÑO/MES/DÍA: " + fechav.getText().toString(), 50, 170, myPaint);

                // CAMPO DEPARTAMENTO
                myPaint.setTextSize(10);
                canvas.drawText("DEPARTAMENTO: " + departamento.getSelectedItem().toString(), 170, 170, myPaint);

                //CAMPO MUNICIPIOS
                myPaint.setTextSize(10);
                canvas.drawText("MUNICIPIO: " + municipios.getSelectedItem().toString(), 300, 170, myPaint);
                //CAMPO VEREDA Y FINCA

                myPaint.setTextSize(10);
                canvas.drawText("VEREDA: " + vereda.getText().toString(), 400, 170, myPaint);
                canvas.drawText("NOMBRE DE LA FINCA:  " + finca.getText().toString(), 50, 185, myPaint);

                //TITULO DE LA TABLA UNICACION
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("INFORMACIÓN DEL PRODUCTOR", 136, 215, titlePaint);

                //CAMPO NOMBRE, CEDULA, TELEFONO Y # DE VISITA

                myPaint.setTextSize(10);
                canvas.drawText("NOMBRE DEL PRODUCTOR: " + nombre.getText().toString(), 50, 230, myPaint);
                canvas.drawText("NÚMERO DEL DOCUMENTO (CC):  " + cccedula.getText().toString(), 50, 245, myPaint);
                canvas.drawText("NÚMERO DE CELULAR:  " + telefono.getText().toString(), 50, 260, myPaint);
                canvas.drawText("NÚMERO DE VISITAS:  " + visita.getText().toString(), 50, 275, myPaint);

                //TITULO DE LA TABLA ACTIVIDAD Y EXTENSIONISTA
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("ACTIVIDAD", 80, 305, titlePaint);

                //CAMPO ACTIVIDAD
                myPaint.setTextSize(10);
                canvas.drawText("ACTIVIDAD: " + actividad.getSelectedItem().toString(), 50, 320, myPaint);
                canvas.drawText("NOMBRE DEL EXTENSIONISTA: " + nam.getText().toString(), 50, 335, myPaint);
                canvas.drawText("CEDULA DEL EXTENSIONISTA: " + usuario.getText().toString(), 50, 350, myPaint);

                //TITULO DE LA TABLA OBJETO DE LA VISITA
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("OBJETO DE LA VISITA", 103, 380, titlePaint);
                myPaint.setTextSize(10);
                canvas.drawText("OBJETO DE LA VISITA: " + objeto.getText().toString(), 50, 395, myPaint);

                //TITULO DE LA TABLA OBSERVACIONES
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("OBSERVACIONES", 100, 425, titlePaint);

                TextPaint mTextPaint=new TextPaint();
                StaticLayout mTextLayout = new StaticLayout(observaciones.getText().toString(), mTextPaint, canvas.getWidth(),
                        Layout.Alignment.ALIGN_NORMAL, 2.0f, 0.0f, false);
                int x=50;
                int y=440;

                for (String line: observaciones.getText().toString().split("\n")){
                    canvas.drawText(line,x,y,mTextPaint);
                    y += mTextPaint.getTextSize();

                }

                //TITULO DE LA TABLA CONSIDERACIONES
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("CONSIDERACIONES", 100, 485, titlePaint);

                TextPaint mTextPaintCon=new TextPaint();
                StaticLayout mTextLayoutCon = new StaticLayout(consideraciones.getText().toString(), mTextPaintCon, canvas.getWidth(),
                        Layout.Alignment.ALIGN_NORMAL, 2.0f, 0.0f, false);
                int a=50;//x
                int b=500;//y

                for (String line: consideraciones.getText().toString().split("\n")){
                    canvas.drawText(line,a,b,mTextPaintCon);
                    b += mTextPaintCon.getTextSize();

                }

                //TITULO DE LA TABLA DOSIS
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("DOSIS", 68, 545, titlePaint);

                TextPaint mTextPaintDo=new TextPaint();
                StaticLayout mTextLayoutDo = new StaticLayout(dosis.getText().toString(), mTextPaintDo, canvas.getWidth(),
                        Layout.Alignment.ALIGN_NORMAL, 2.0f, 0.0f, false);
                int c=50;//x
                int d=560;//y

                for (String line: dosis.getText().toString().split("\n")){
                    canvas.drawText(line,c,d,mTextPaintDo);
                    d += mTextPaintDo.getTextSize();

                }

                //CORRIGIENDO TEXTO
                //Paint myPaint = new Paint();
                /*

                Layout.Alignment alignment =  Layout.Alignment.ALIGN_NORMAL;
                float spacingMultiplier = 1;
                float spacingAdition = 0;
                boolean includePadding = false;

                TextPaint miTextPaint = new TextPaint();
                StatticLayout statticLayout = new StatticLayout(observaciones.getText().toString(),miTextPaint
                        ,width,alignment,spacingMultiplier,spacingAdition,includePadding);

                float height = statticLayout.getHeight();
                */
                /*
                int ancho=400;
                TextPaint textPaint = new TextPaint();
                StatticLayout statticLayout = new StatticLayout(observaciones.getText().toString(),
                        textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,false);
                canvas.save();

                float textX=145;
                float textY=530;

                canvas.translate(textX,textY);
                statticLayout.draw(canvas);
                canvas.restore();
*/



                //myPaint.setTextSize(10);
                //canvas.drawText("OBSERVACIONES: " +observaciones.getText().toString(), 50, 530, myPaint);

                //FIRMA 1
                //TITULO DE LA FIRMA 1
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("FIRMAS", width/2,615,titlePaint);

                //SI FIRMA
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("¿PUEDE FIRMAR EL PRODUCTOR? " + sifirma.getSelectedItem().toString(), 150, 630, titlePaint);

                //DIBUJANDO FIRMA 1
                f1=Bitmap.createScaledBitmap(firma1,300,120,false);
                canvas.drawBitmap(f1,10, 650,myPaint);
                canvas.drawText("FIRMA DEL PRODUCTOR", 150,750,titlePaint);
                //DIBUJANDO FIRMA 2
                f2=Bitmap.createScaledBitmap(fima2,300,120,false);
                canvas.drawBitmap(f2,300, 650,myPaint);
                canvas.drawText("FIRMA DEL PROFESIONAL", 425,750,titlePaint);

                //PINTANDO LA FOTO
                ftbit=Bitmap.createScaledBitmap(gfoto,220,200,false);
                canvas.drawText("REGISTRO FOTOGRAFICO", 500, 415, titlePaint);
                canvas.drawBitmap(ftbit,400, 200,myPaint);




                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(20);


                //CIERRE DE PDF
                myPdfDocument.finishPage(myPage1);

                //----------------------------------------------------------------------------------
                //RUTA EN DONDE SE CREA EL PDF Y LA ASIGNACION DEL NOMBRE

                File carpetas = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/ReportesEnPDF");
                File file = new File(carpetas,cccedula.getText().toString()+" Visita "+visita.getText().toString()+".pdf");
                boolean isCrea = false;
                if(!carpetas.exists()) {
                    isCrea = carpetas.mkdir();
                }

                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myPdfDocument.close();

            }
        });
    }
}