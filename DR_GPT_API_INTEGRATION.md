# Dr.GPT API Integration

## Setup Complete ✅

### API Configuration
- **Base URL**: `http://10.0.2.2:5000/` (Android emulator localhost)
- **Endpoint**: `/api/chat`
- **Method**: POST

### Features Implemented

1. **Retrofit Integration**
   - HTTP logging for debugging
   - 30-second timeout for connections
   - GSON converter for JSON parsing

2. **Session Management**
   - Unique session ID per user
   - Stored in SharedPreferences
   - Persists across app restarts

3. **Error Handling**
   - Network failure detection
   - Server error handling (4xx, 5xx)
   - User-friendly error messages
   - Detailed logging for debugging

4. **UI Feedback**
   - Loading indicator during API calls
   - Toast messages for errors
   - Real-time message display

## Testing

### 1. Start Your Flask Server
```bash
python app.py
```

### 2. Test with cURL (from terminal)
```bash
curl -X POST http://localhost:5000/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "I have a fever", "session_id": "test123"}'
```

### 3. Test in Android App
1. Open ChronicCare app
2. Navigate to Dr.GPT tab
3. Type a message: "I have a headache"
4. Send and wait for response

## Debugging

### Check Logcat for:
```
Tag: DrGPTActivity
- API Response: [bot response]
- API Error: [error code and message]
- API Failure: [connection error]
```

### Common Issues:

**Connection Failed**
- Ensure Flask server is running on port 5000
- Use `http://10.0.2.2:5000/` for emulator
- Use `http://YOUR_IP:5000/` for physical device

**Server Error**
- Check Flask server logs
- Verify API endpoint is `/api/chat`
- Ensure request format matches

## API Request Format
```json
{
  "session_id": "uuid-string",
  "message": "user message here"
}
```

## API Response Format
```json
{
  "response": "bot response here",
  "session_id": "uuid-string"
}
```

## Code Structure
- `api/ChatRequest.java` - Request model
- `api/ChatResponse.java` - Response model
- `api/DrGPTApiService.java` - Retrofit interface
- `api/RetrofitClient.java` - HTTP client configuration
- `DrGPTActivity.java` - UI and API integration
