package com.example.onlinenotes;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddNotes extends AppCompatActivity {

    EditText TitleText,DescriptionText;
    Button SelectFileButton,ClearButton,UploadButton;
    ImageView ViewFile;
    TextView FileNameTextView;
    private final int FILE_REQ_CODE = 1000;
    private Uri selectedFileUri; // Store the URI of the selected file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        TitleText = findViewById(R.id.titleText);
        DescriptionText = findViewById(R.id.descriptionText);
        ViewFile = findViewById(R.id.imageView2);
        SelectFileButton = findViewById(R.id.button5);
        FileNameTextView = findViewById(R.id.file_name_textview);

        ClearButton = findViewById(R.id.clearButton);
        UploadButton = findViewById(R.id.uploadButton);


        //select the file from gallery
        SelectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryFile = new Intent(Intent.ACTION_GET_CONTENT);
                galleryFile.setType("*/*"); // Allow all file types
                startActivityForResult(galleryFile, FILE_REQ_CODE);
            }
        });

        // Set an onClickListener for the thumbnail
        ViewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFileUri != null) {
                    openFile(selectedFileUri);
                }
            }
        });

        // Set an onClickListener for the "Clear" button
        ClearButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Clear the text in the TitleText and DescriptionText EditText fields
                TitleText.getText().clear();
                TitleText.setText("Title");
                DescriptionText.getText().clear();
                DescriptionText.setText("Description");

                // Clear the selected file URI and hide the filename TextView
                selectedFileUri = null;
                FileNameTextView.setText("");
                FileNameTextView.setVisibility(View.GONE);


                // Clear the ImageView by setting it to a null image
                ViewFile.setImageResource(android.R.color.transparent);
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FILE_REQ_CODE) {
                // Handle the selected file here
                if (data != null && data.getData() != null) {
                    String fileType = getContentResolver().getType(data.getData());

                    if (fileType != null) {
                        // Store the selected file's URI
                        selectedFileUri = data.getData();
                        // Get the filename from the URI
                        String fileName = getFileName(selectedFileUri);
                        // Display the filename in the TextView
                        FileNameTextView.setText(fileName);
                        FileNameTextView.setVisibility(View.VISIBLE);

                        if (fileType.startsWith("image")) {
                            // Display selected image
                            ViewFile.setImageURI(selectedFileUri);
                        } else if (fileType.startsWith("application/pdf")) {
                            // Handle PDF file and generate a thumbnail
                            generatePdfThumbnail(selectedFileUri);
                        } else {
                            // Handle other file types as needed
                        }
                    }
                }
            }
        }
    }
    private String getFileName(Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();
        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                cursor.close();
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }

    private void generatePdfThumbnail(Uri pdfUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(pdfUri, "r");
            if (parcelFileDescriptor != null) {
                PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                PdfRenderer.Page page = pdfRenderer.openPage(0);

                Bitmap pdfThumbnail = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(pdfThumbnail, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                ViewFile.setImageBitmap(pdfThumbnail);

                page.close();
                pdfRenderer.close();
                parcelFileDescriptor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFile(Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to open the file", Toast.LENGTH_SHORT).show();
        }
    }



}
