**Chatbot**

An AI-powered personal assistant built with Java Spring Boot and the OpenAI API, capable of remembering user information across sessions.


**Features**

🤖 Conversational chatbot powered by OpenAI GPT models

🧠 Persistent memory (remembers user’s name, favorite color, hobbies, pets, birthday, etc.)

🌐 Clean web interface with real-time chat

⚡ Spring Boot backend with REST API integration

💾 Memory stored in a JSON file (memory.json)


**Architecture**

User (Browser)
   │
   ▼
Frontend (HTML)
   │   Sends/receives messages via REST API
   ▼
Spring Boot Backend
   ├── ChatController   → Handles API requests
   ├── ChatService      → Extracts facts, builds prompts, calls OpenAI
   └── MemoryService    → Stores and retrieves user facts from memory.json
   │
   ▼
memory.json (local persistence)
   │
   ▼
OpenAI API (GPT-4o-mini)
   │
   ▼
Bot’s Response

**Explanation:**

* The frontend sends messages to /api/chat.

* The backend (ChatService) processes them:

* Extracts facts like “favorite color is red”

* Stores them in memory.json via MemoryService

* Builds a prompt and sends it to OpenAI API


The response comes back, and the bot replies while remembering past conversations.


**Project Structure**

src/
 ├── main/
 │   ├── java/com/stanchat/
 │   │   ├── controller/ChatController.java   # REST API
 │   │   ├── service/ChatService.java         # Core chatbot logic
 │   │   └── service/MemoryService.java       # Memory persistence
 │   └── resources/
 │       ├── application.properties           # Configuration
 │       └── static/index.html                # Chat UI
 └── test/...


**Setup**

**Step:1 Clone the Repository**

git clone https://github.com/yourusername/stan-chatbot.git
cd stan-chatbot

**Step:2 Configure API Key**

Edit src/main/resources/application.properties:

openai.api.key=sk-your-key-here
openai.model=gpt-4o-mini
bot.name=Arina
server.port=8080

**Step:3 Run the Application**

Using Maven:

mvn spring-boot:run

Or build and run JAR:

mvn clean package
java -jar target/stan-chatbot-0.0.1-SNAPSHOT.jar


**Usage**

1. Open browser → http://localhost:8080


2. Start chatting with Arina 🤖


**Example:**

You: My favorite color is red  
Bot: Got it! Thanks for sharing.  

You: What’s my favorite color?  
Bot: You told me your favorite color is red.


🧠 Memory Example (memory.json)

{
  "test_user": [
    "favorite color is red (at 2025-08-29T22:15:10)",
    "name is Anurima (at 2025-08-29T22:16:05)",
    "lives in Bangalore (at 2025-08-29T22:17:20)"
  ]
}

✅ Even after restarting, STAN will remember these facts.


**Future Improvements:**

Switch from JSON file to database storage for scalability

Improve UI with a modern frontend framework (React/Angular)

Add voice support (speech-to-text / text-to-speech)

Deploy to cloud platforms (Heroku, AWS, Azure)
