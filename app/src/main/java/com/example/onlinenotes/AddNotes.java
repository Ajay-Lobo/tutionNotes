package com.example.onlinenotes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddNotes extends AppCompatActivity {
    private EditText titleText, descriptionText;
    private Button clearButton, uploadButton,SelectFileButton;
    private Database1 database1; // Changed variable name for consistency
    private ProgressBar progressBar;
    ImageView ViewFile;
    TextView FileNameTextView;
    private final int FILE_REQ_CODE = 1000;
    private Uri selectedFileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        // Initialize UI elements
        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        clearButton = findViewById(R.id.clearButton);
        uploadButton = findViewById(R.id.uploadButton);
        progressBar = findViewById(R.id.progressBar2);

        ViewFile = findViewById(R.id.imageView2);
        SelectFileButton = findViewById(R.id.button5);
        FileNameTextView = findViewById(R.id.file_name_textview);

        // Initialize the database
        database1 = new Database1(this);

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

        clearButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Clear the text in the TitleText and DescriptionText EditText fields
                titleText.getText().clear();
                titleText.setHint("Title");
                descriptionText.getText().clear();
                descriptionText.setHint("Description");

                // Clear the selected file URI and hide the filename TextView
                selectedFileUri = null;
                FileNameTextView.setText("");
                FileNameTextView.setVisibility(View.GONE);


                // Clear the ImageView by setting it to a null image
                ViewFile.setImageResource(android.R.color.transparent);
            }
        });


        // Set an OnClickListener for the "Upload" button
        // Set an OnClickListener for the "Upload" button
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleText.getText().toString();
                String description = descriptionText.getText().toString();

                if (title.length() < 4) {
                    showToast("Title is too short (minimum 4 characters).");
                } else if (selectedFileUri != null) {
                    // Get the file data as a byte array
                    byte[] fileData = getFileData(selectedFileUri);

                    if (fileData != null) {
                        Log.d("Debug", "File data retrieved successfully.");
                        // Attempt to upload data to the database, including the file data as BLOB
                        boolean uploadSuccessful = database1.uploadData(title, description, fileData);

                        if (uploadSuccessful) {
                            Log.d("Debug", "Upload successful.");
                            progressBar.setVisibility(View.VISIBLE);
                            new android.os.Handler().postDelayed(new Runnable() {
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    navigateToDashboard();
                                }
                            }, 3000);
                        } else {
                            Log.e("Error", "Upload failed.");
                            showToast("Upload failed.");
                        }
                    } else {
                        Log.e("Error", "Failed to retrieve file data.");
                        showToast("Failed to retrieve file data.");
                    }
                } else {
                    Log.e("Error", "No file selected.");
                    showToast("Please select a file to upload.");
                }
            }
        });


// Helper method to convert the selected file's data to a byte array
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

    private byte[] getFileData(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            if (inputStream != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String getFileName(Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();
        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
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
    private void navigateToDashboard() {
        Intent intent = new Intent(AddNotes.this, TeacherDashboard.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent going back to it from the dashboard
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
