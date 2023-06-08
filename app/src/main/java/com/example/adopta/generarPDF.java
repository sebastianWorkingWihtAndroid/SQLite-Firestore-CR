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
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.adopta.entidades.Usuarios;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class generarPDF extends AppCompatActivity {

    ByteArrayOutputStream stream;
    private final static String ETIQUETA_ERROR = "ERROR PDF";
    private File archivoPDF;

    EditText buscarCB,fechav, vereda, finca, nombre, cccedula, telefono,
            visita, objeto, observaciones,departamento, municipios, actividad,usuario,nam,sif
            ,recomendaciones,dosisr;
    Button buscarB, buscarA ,pdf,btngpdf;
    String depa = "";
    String muni = "";
    String acti = "";
    Uri imageUri;


    ImageButton imageButton, imageButton3,img_foto,imagfoto;
    DBHelper DB;
    ImageView imageView10;
    private  lienzo lienzo;

    private Bitmap bitmap,gfoto;
    int PICK_IMAGE_REQUEST=1;

    private Bitmap firma1, fima2,fotocel;
    Bitmap logoU, logoP, tam1,tam2,logos, tam5,f1,f2,ftbit;
    //ANCHO DEL PDF
    int width=598;
    int height=800;
    int cedula=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_pdf);

        //BOTONES PARA LA FIRMA
        imageButton = findViewById(R.id.imageButton);
        imageButton3 = findViewById(R.id.imageButton3);


        imageView10 =findViewById(R.id.imageView10);

        //SE GUARDA EL MAPA DE BITS EN ESA VARIABLE
        img_foto=findViewById(R.id.btncamara);

        fechav = findViewById(R.id.fechaB);
        departamento = findViewById(R.id.departamentos);
        municipios = findViewById(R.id.municipios);
        vereda = findViewById(R.id.vereda);
        finca = findViewById(R.id.finca);
        nombre = findViewById(R.id.nombre);
        cccedula = findViewById(R.id.cedulaB);
        telefono = findViewById(R.id.telefono);
        visita = findViewById(R.id.novisitaB);
        actividad = findViewById(R.id.actividadd);
        objeto = findViewById(R.id.objeto);
        observaciones = findViewById(R.id.observaciones);
        recomendaciones = findViewById(R.id.recomendacion);
        dosisr = findViewById(R.id.dosisr);
        buscarB = findViewById(R.id.buscarB);
        buscarA = findViewById(R.id.buscarA);
        usuario=findViewById(R.id.codigo);
        nam=findViewById(R.id.nam);
        btngpdf=findViewById(R.id.generarpdf);
        imagfoto=findViewById(R.id.imagfoto);
        sif=findViewById(R.id.sif);


        logos = BitmapFactory.decodeResource(getResources(), R.drawable.logos);
        tam5=Bitmap.createScaledBitmap(logos,500,81,false);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int suma= month+1;

        fechav.setText(""+year+" "+suma+" "+day);
        //LLAMADO A LA BASE DE DATOS
        DB = new DBHelper(this, "recordv", null, 1);
        cargar();

        buscarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuarios usuarios = new Usuarios();
                DB.buscarUsuarios(usuarios,cccedula.getText().toString());
                fechav.setText(usuarios.getFechavista());
                visita.setText(usuarios.getVisitavista());
                objeto.setText(usuarios.getObjetovista());
                sif.setText(usuarios.getFirma());
                observaciones.setText(usuarios.getObservacionesvista());
                recomendaciones.setText(usuarios.getRecomendaciones());
                dosisr.setText(usuarios.getDosis());
            }
        });

        buscarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuarios usuarios = new Usuarios();
                DB.buscarUsuariosA(usuarios,cccedula.getText().toString());
                departamento.setText(usuarios.getDepartamentovista());
                municipios.setText(usuarios.getMunicipiovista());
                vereda.setText(usuarios.getVeredavista());
                finca.setText(usuarios.getFincavista());
                nombre.setText(usuarios.getNombrevista());
                telefono.setText(usuarios.getTelefonovista());
                actividad.setText(usuarios.getActividadvista());

            }
        });


        //permisos
        ActivityCompat.requestPermissions(this, new String[]{
                WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        crearPfdVisita();

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

        imagfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });


    }

    //CARGAR LA IMAGEN DESDE LA GALERIA
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
            imagfoto.setImageURI(imageUri);

            imagfoto.buildDrawingCache();
            fotocel = imagfoto.getDrawingCache();
            //Button btnc = new Button(this);
        }
    }

    private void cargar(){
        Cursor cursor= DB.getReadableDatabase().rawQuery("select nombre,cedula from usuarios",null);
        while (cursor.moveToNext()){
            String user=cursor.getString(0);
            String ced=cursor.getString(1);
            usuario.setText(user);
            nam.setText(ced);

        }
    }

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
        AlertDialog.Builder salvarDibujo = new AlertDialog.Builder(generarPDF.this);
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
        AlertDialog.Builder salvarDibujo = new AlertDialog.Builder(generarPDF.this);
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



    private void crearPfdVisita() {
        btngpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(generarPDF.this, "PDF GENERADO EN DESCARGAS", Toast.LENGTH_SHORT).show();

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
                canvas.drawText("DEPARTAMENTO: " + departamento.getText().toString(), 170, 170, myPaint);

                //CAMPO MUNICIPIOS
                myPaint.setTextSize(10);
                canvas.drawText("MUNICIPIO: " + municipios.getText().toString(), 300, 170, myPaint);
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
                canvas.drawText("ACTIVIDAD: " + actividad.getText().toString(), 50, 320, myPaint);
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
                StaticLayout mTextLayoutCon = new StaticLayout(recomendaciones.getText().toString(), mTextPaintCon, canvas.getWidth(),
                        Layout.Alignment.ALIGN_NORMAL, 2.0f, 0.0f, false);
                int a=50;//x
                int b=500;//y

                for (String line: recomendaciones.getText().toString().split("\n")){
                    canvas.drawText(line,a,b,mTextPaintCon);
                    b += mTextPaintCon.getTextSize();

                }

                //TITULO DE LA TABLA DOSIS
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("DOSIS", 68, 530, titlePaint);

                TextPaint mTextPaintDo=new TextPaint();
                StaticLayout mTextLayoutDo = new StaticLayout(dosisr.getText().toString(), mTextPaintDo, canvas.getWidth(),
                        Layout.Alignment.ALIGN_NORMAL, 2.0f, 0.0f, false);
                int c=50;//x
                int d=545;//y

                for (String line: dosisr.getText().toString().split("\n")){
                    canvas.drawText(line,c,d,mTextPaintDo);
                    c += mTextPaintDo.getTextSize();

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
                canvas.drawText("FIRMAS", width/2,575,titlePaint);

                //SI FIRMA
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(12);
                canvas.drawText("¿PUEDE FIRMAR EL PRODUCTOR? " + sif.getText().toString(), 150, 590, titlePaint);

                //DIBUJANDO FIRMA 1
                f1=Bitmap.createScaledBitmap(firma1,300,120,false);
                canvas.drawBitmap(f1,10, 650,myPaint);
                canvas.drawText("FIRMA DEL PRODUCTOR", 150,750,titlePaint);
                //DIBUJANDO FIRMA 2
                f2=Bitmap.createScaledBitmap(fima2,300,120,false);
                canvas.drawBitmap(f2,300, 650,myPaint);
                canvas.drawText("FIRMA DEL PROFESIONAL", 425,750,titlePaint);

                //PINTANDO LA FOTO
                ftbit=Bitmap.createScaledBitmap(fotocel,220,200,false);
                canvas.drawText("REGISTRO FOTOGRAFICO", 500, 415, titlePaint);
                canvas.drawBitmap(ftbit,400, 200,myPaint);



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