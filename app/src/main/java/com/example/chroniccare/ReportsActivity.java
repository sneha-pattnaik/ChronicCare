package com.example.chroniccare;

import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReportsActivity extends AppCompatActivity {

    private static final String PDF_ASSET_PATH = "reports/AJ_20260222_4f9a2c1b.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_viewer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        WebView webView = findViewById(R.id.reportWebView);
        configureWebView(webView);

        try {
            String base64Pdf = encodePdfAsset();
            String html = buildPdfViewerHtml(base64Pdf);
            webView.loadDataWithBaseURL(
                    "https://localhost/",
                    html,
                    "text/html",
                    "UTF-8",
                    null
            );
        } catch (IOException e) {
            Toast.makeText(this, "Unable to load report", Toast.LENGTH_SHORT).show();
        }
    }

    private void configureWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
    }

    private String encodePdfAsset() throws IOException {
        try (InputStream inputStream = getAssets().open(PDF_ASSET_PATH);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
        }
    }

    private String buildPdfViewerHtml(String base64Pdf) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
                "<style>" +
                "body{margin:0;padding:0;background:#ffffff;font-family:sans-serif;}" +
                "#container{width:100%;padding:12px;box-sizing:border-box;}" +
                "canvas{width:100%;height:auto;margin:0 0 16px 0;border:1px solid #eee;box-shadow:0 1px 4px rgba(0,0,0,0.08);}" +
                "</style>" +
                "<script src='https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.min.js'></script>" +
                "</head>" +
                "<body>" +
                "<div id='container'></div>" +
                "<script>" +
                "const base64Data = '" + base64Pdf + "';" +
                "const raw = atob(base64Data);" +
                "const len = raw.length;" +
                "const bytes = new Uint8Array(len);" +
                "for (let i = 0; i < len; i++) { bytes[i] = raw.charCodeAt(i); }" +
                "pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.worker.min.js';" +
                "const container = document.getElementById('container');" +
                "pdfjsLib.getDocument({data: bytes}).promise.then(function(pdf) {" +
                "  const total = pdf.numPages;" +
                "  const renderPage = function(pageNumber) {" +
                "    return pdf.getPage(pageNumber).then(function(page) {" +
                "      const viewport = page.getViewport({scale: 1.2});" +
                "      const canvas = document.createElement('canvas');" +
                "      const context = canvas.getContext('2d');" +
                "      canvas.height = viewport.height;" +
                "      canvas.width = viewport.width;" +
                "      container.appendChild(canvas);" +
                "      return page.render({canvasContext: context, viewport: viewport}).promise;" +
                "    });" +
                "  };" +
                "  let chain = Promise.resolve();" +
                "  for (let i = 1; i <= total; i++) {" +
                "    chain = chain.then(() => renderPage(i));" +
                "  }" +
                "});" +
                "</script>" +
                "</body>" +
                "</html>";
    }
}
