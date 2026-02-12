# Setting Up ngrok for Dr.GPT API

## Step 1: Install ngrok

### macOS
```bash
brew install ngrok
```

### Or download from
https://ngrok.com/download

## Step 2: Start Your Flask Server
```bash
cd /path/to/your/flask/project
python app.py
```

Your Flask server should be running on `http://localhost:5000`

## Step 3: Start ngrok Tunnel
Open a new terminal and run:
```bash
ngrok http 5000
```

You'll see output like:
```
Forwarding  https://xxxx-xxxx-xxxx.ngrok-free.app -> http://localhost:5000
```

## Step 4: Update Android App

Copy the HTTPS URL from ngrok and update `RetrofitClient.java`:
```java
private static final String BASE_URL = "https://your-ngrok-url.ngrok-free.app/";
```

## Step 5: Test

### Test ngrok URL in browser first:
```
https://your-ngrok-url.ngrok-free.app/api/chat
```

### Test with cURL:
```bash
curl -X POST https://your-ngrok-url.ngrok-free.app/api/chat \
  -H "Content-Type: application/json" \
  -H "ngrok-skip-browser-warning: true" \
  -d '{"message": "test", "session_id": "123"}'
```

### Test in Android App:
1. Rebuild the app
2. Open Dr.GPT
3. Send a message

## Troubleshooting

### 403 Error
✅ **Fixed!** Added `ngrok-skip-browser-warning: true` header in RetrofitClient

### Connection Refused
- Make sure Flask server is running
- Check ngrok tunnel is active
- Verify the URL is correct

### ngrok Session Expired
- Free ngrok URLs expire after 2 hours
- Restart ngrok to get a new URL
- Update BASE_URL in RetrofitClient.java

## Keep ngrok Running

Both terminals must stay open:
1. Terminal 1: Flask server (`python app.py`)
2. Terminal 2: ngrok tunnel (`ngrok http 5000`)

## Current Setup
- Base URL: `https://rosetta-runtgenological-vectorially.ngrok-free.dev/`
- Header added: `ngrok-skip-browser-warning: true`
- Ready to test!
