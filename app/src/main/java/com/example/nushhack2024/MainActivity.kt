package com.example.nushhack2024

import android.app.Activity
import android.app.VoiceInteractor
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nushhack2024.ui.theme.Nushhack2024Theme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.client.OpenAIHost.Companion.OpenAI
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.tasks.await
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import java.util.logging.Handler

data class Task(
    val name: String,
    val description: String,
    val dueDate: String
)
var storedVerificationId: String? = null
var otpdone = true
lateinit var tasklist : MutableList<Task>
var curuser : User? = null
val firebaseAuth = FirebaseAuth.getInstance()
lateinit var database : DatabaseReference
var studentlist : MutableList<User> = mutableListOf()
var studentkeys : ArrayList<String> = arrayListOf()
var map : HashMap<String, User> = hashMapOf()
var usernameToUid : HashMap<String, String> = hashMapOf()
lateinit var openAiApiKey : String
var projlist : ArrayList<String> = arrayListOf()
lateinit var curchat : String
var userlist : ArrayList<User> = arrayListOf()
data class User(val username : String, val isAdmin: Boolean, val channels: ArrayList<String>)
data class Message(
    val sender: String = "",
    val message: String = "",
    val timestamp: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
) {
    // Firebase needs a no-argument constructor for deserialization
    constructor() : this("", "", com.google.firebase.Timestamp.now())  // No-arg constructor
}
lateinit var fsdb : FirebaseFirestore
lateinit var chatRef: CollectionReference
lateinit var context : Context
var tasks : HashMap<String, MutableList<Task>> = hashMapOf()
var notinit = false

val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        // If verification is completed successfully, sign in the user
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Handle successful login
                    Log.d("Auth", "Verification completed and signed in!")
                } else {
                    // Handle error
                    Log.e("Auth", "Verification failed: ${task.exception?.message}")
                }
            }
    }

    override fun onVerificationFailed(e: FirebaseException) {
        // Handle verification failure (e.g., incorrect phone number format, etc.)
        Log.e("Auth", "Verification failed: ${e.message}")
    }

    override fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        // Store the verificationId for later use when verifying the entered code
        storedVerificationId = verificationId
        // Optionally store the token if you need to resend the code later
        Log.d("Auth", "Code sent to the phone number: $verificationId")
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database("https://nushhack2024-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        fsdb = Firebase.firestore
        openAiApiKey = "placeholder-key"
        notinit = false
        tasklist = arrayListOf()
        Log.d("retrieve", "logged in")

        /*
        tasks["Project 1"] = arrayListOf(Task("Prepare documentation", "write stuff", "11-11-2024"), Task("Touch some grass", "impossible task", "15-11-2024"), Task("Get some sleep", "zzz", "10-11-2024"))
        tasks["Project 2"] = arrayListOf(Task("Some random thing", "idk", "1-1-2025"))
        tasks["Project 3"] = arrayListOf()
        database.child("tasks").setValue(tasks)

         */
        database.child("tasks").get().addOnSuccessListener {
            // Cast the retrieved data to the correct type
            val tmp = it.value as? HashMap<String, List<Map<String, String>>>  // Assuming tasks is a map of project name to list of task data

            // Iterate over each project
            tmp?.forEach { (project, taskList) ->
                // Initialize the list of tasks for each project, or retrieve the existing one
                val projectTasks = tasks.getOrPut(project) { mutableListOf() }



                // Iterate over the tasks and add each one to the list
                taskList.forEach { taskData ->
                    // Extract task details from the map
                    val name = taskData["name"] ?: ""
                    val description = taskData["description"] ?: ""
                    val dueDate = taskData["dueDate"] ?: ""

                    // Add the task to the project's list of tasks
                    projectTasks.add(Task(name, description, dueDate))
                }
            }

            // Optionally log the first task of "Project 1" to verify it's correctly retrieved
            tasks["Project 1"]?.get(0)?.let { task ->
                Log.d("retrieve", task.name)
            }
        }



        /*
        database.child("userlist").get().addOnSuccessListener {
            var tmp = it.value as HashMap<String, HashMap<*, *>>
            for((k, u) in tmp) {
                studentkeys.add(k)
                studentlist.add(
                    User(
                        u["username"].toString(),
                        (u["isAdmin"] as? Boolean)
                            ?: false,
                    )
                )
                map[k] = User(
                    u["username"].toString(),
                    (u["isAdmin"] as? Boolean)
                        ?: false,
                )
                //Log.i("firebase", studentlist[studentlist.size - 1].name)
            }
            //Log.i("firebase", studentlist[0].name)
        }.addOnFailureListener{
            Log.wtf("firebase", "you suck")
        }

         */
        /*
        map["M9yNzNOqr2N7IwzIH6Uoy2uszSx2"] = User("skibidi sigma", true, arrayListOf("Project 1", "Project 2", "Project 3"))
        map["TJZ5xNZMcoOzoQSG3dlhnq6g3Hn1"] = User("beta male", false, arrayListOf("Project 1"))
        map["oyiJ6fC8WydKZllZ1CafhXM2Hir1"] = User("third wheeler", false, arrayListOf("Project 2", "Project 3"))
        database.child("users").setValue(map)

         */
        database.child("users").get().addOnSuccessListener {
            Log.d("retrieve", "map loaded")
            // Cast the retrieved data to the expected type
            val userMap = it.value as? HashMap<String, Map<String, Any>> // Assuming each user ID maps to a user object
            Log.d("retrieve", "users retrieve")
            // Iterate through the map and create User objects
            userMap?.forEach { (userId, userData) ->
                // Extract the user's name, status, and projects from the map
                val name = userData["username"] as? String ?: ""
                val status = userData["admin"] as? Boolean ?: false
                // Use ArrayList to ensure projects is an ArrayList<String>
                val projects = (userData["channels"] as? List<String>)?.let { ArrayList(it) } ?: ArrayList()

                // Create a User object
                val user = User(name, status, projects)

                // Store this user in the users map
                map[userId] = user
                usernameToUid[name] = userId
                userlist += user
                // Log the user data for verification
                Log.d("retrieve", "User: $name, Status: $status, Projects: ${projects.joinToString()}")
            }
        }

        setContent {
            Nushhack2024Theme {
                // Initialize NavController
                val navController = rememberNavController()

                // Define the NavHost to set up navigation
                NavHost(
                    navController = navController,
                    startDestination = "login" // Start with the login screen
                ) {
                    composable("login") {
                        LoginScreen(onLoginSuccess = {
                            // Navigate to HomeScreen after login
                            navController.navigate("home")
                        }, onRegister = {})
                    }

                    composable("home") {
                        HomeScreen(navController = navController)
                    }
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Option 1") }
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showDialog by remember{mutableStateOf(false)}
    var name by remember { mutableStateOf("") }  // State for the first text field
    var desc by remember { mutableStateOf("") }  // State for the second text field
    var currentChatId by remember { mutableStateOf(projlist[0]) }
    Log.d("dsz", projlist.size.toString())
    key(drawerState.isClosed) {
        // Scaffold with a Modal Drawer
        ModalNavigationDrawer(
            modifier = Modifier.fillMaxSize(),
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.width(200.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // You can place your profile and navigation links here
                        ProfileCard("skib", curuser?.username, firebaseAuth.currentUser?.email)
                        TextButton(
                            onClick = {
                                firebaseAuth.signOut()
                                navController.navigate("login")
                                curuser = null
                            }, modifier = Modifier
                                .height(35.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text("Logout", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                        }
                        if(curuser?.isAdmin == true) {
                            TextButton(
                                onClick = { showDialog = true }, modifier = Modifier
                                    .height(35.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    "Create project",
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                                )
                            }
                        }
                        projlist.forEach { item ->
                            NavigationDrawerItem(
                                label = { Text(item) },
                                selected = false,
                                onClick = {
                                    currentChatId = item
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    Log.d("chatid", currentChatId)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                    }
                }
            }
        ) {
            // Main content
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Skibizz") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply { if (isClosed) open() else close() }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(Icons.Filled.Person, contentDescription = "Settings")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            ) { contentPadding ->
                if (showDialog) {
                    val selectedMembers = remember { mutableStateListOf<String>() }
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Enter Information") },
                        text = {
                            Column {
                                // ... (existing name and description fields)
                                TextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = { Text("Project Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                // Member selection section
                                Text(text = "Select Members:")
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(userlist) { member ->
                                        if(member.username != curuser?.username) {
                                            Row(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 8.dp)
                                            ) {
                                                Checkbox(
                                                    checked = selectedMembers.contains(member.username),
                                                    onCheckedChange = {
                                                        if (it) {
                                                            selectedMembers.add(member.username)
                                                        } else {
                                                            selectedMembers.remove(member.username)
                                                        }
                                                    }
                                                )
                                                Column(
                                                    Modifier.align(Alignment.CenterVertically),
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(text = member.username)
                                                }
                                            }
                                        }
                                        else{
                                            selectedMembers.add(member.username)
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                // Handle form submission, including adding selected members to the project
                                // ...
                                for (uname in selectedMembers) {
                                    map[usernameToUid[uname]]?.channels?.add(name)

                                }
                                //projlist.add(name)
                                database.child("users").setValue(map)
                                showDialog = false
                                name = ""
                                desc = ""
                                selectedMembers.clear()
                            }) {
                                Text("Submit")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showDialog = false
                                name = ""
                                desc = ""
                                selectedMembers.clear()
                            }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                Box(modifier = Modifier.fillMaxSize().zIndex(1f)) {
                    // List of users with the current project
                    val usersWithProj = mutableListOf<String>()
                    for ((_, user) in map) {
                        if (user.channels.contains(currentChatId)) {
                            usersWithProj.add(user.username)
                        }
                    }

                    // Check if the menu should be expanded
                    if (expanded) {
                        Box(
                            modifier = Modifier
                                .padding(top = 100.dp)
                                .fillMaxHeight()
                                .width(150.dp)
                                .background(
                                    color = Color(0xFFF5F5F5),
                                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                                )
                                .align(Alignment.CenterEnd)
                                .shadow(8.dp, shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                                .padding(16.dp)
                                .offset(x = if (expanded) 0.dp else 250.dp)
                        ) {
                            Column(modifier = Modifier.verticalScroll(scrollState)) {
                                Text(
                                    text = "Project Members",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                // Display each user in the list with improved style
                                usersWithProj.forEach { username ->
                                    Card(
                                        colors = CardDefaults.cardColors(Color(0xFFEEEEEE)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable {
                                                selectedOption = username
                                                expanded = false // Close menu on option click
                                            },
                                        elevation = CardDefaults.cardElevation(4.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = username,
                                            modifier = Modifier.padding(8.dp),
                                            color = Color(0xFF333333),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }


                // Main content (tabs and content)
                    Column(modifier = Modifier.padding(contentPadding)) {
                        TabRow(selectedTabIndex = selectedTab) {
                            // Chat Tab
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = {
                                    Text(
                                        "Chat",
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                selectedContentColor = Color.White,
                                unselectedContentColor = Color.Gray
                            )

                            // Files Tab
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = {
                                    Text(
                                        "Tasks",
                                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                selectedContentColor = Color.White,
                                unselectedContentColor = Color.Gray
                            )
                        }

                        // Content based on selected tab
                        when (selectedTab) {
                            0 -> ChatPage(currentChatId) // Add your chat page content here
                            1 -> TaskPage(currentChatId)
                        // Add your files page content here
                        }
                    }
                }
            }
        }

}



var isVerificationSent = false
var verificationId = ""
var code = ""
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
        onLoginSuccess: () -> Unit,
        onRegister: () -> Unit
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var username by remember{mutableStateOf("")}
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        var isLoading by remember { mutableStateOf(false) }

        // Prevent going back
        BackHandler {
            //null to prevent going back
        }

        // Main Column layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Centered Title
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth().padding(top=10.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email TextField with Enhanced Styling
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                placeholder = { Text("Enter your email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Password TextField with Enhanced Styling
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username= it },
                label = { Text("Username (for signup)") },
                placeholder = { Text("Enter your username") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Person Icon")
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Login Button
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        signInWithEmailAndPassword(email, password, onLoginSuccess, context)
                    } else {
                        Toast.makeText(context, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account? ", color = Color.Gray)
                TextButton(onClick = {firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
                    if (task.isSuccessful) {
                        val userId = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                        val uzers: HashMap<String, HashMap<String, Any>> = hashMapOf()
                        // Save additional user info (e.g., username) in Firestore
                        uzers[userId] =
                            hashMapOf(
                            "admin" to false,
                            "channels" to listOf("Project 1"),
                            "username" to username)


                        database.child("users")
                            .updateChildren(uzers as Map<String,Any>)
                            .addOnSuccessListener {
                                Log.i("DIDDY","it worked")
                                Toast.makeText(context,"Please sign in again!",Toast.LENGTH_LONG).show()
                                throw RuntimeException("This is a crash")
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(applicationContext,"Signup failed",Toast.LENGTH_SHORT).show()
                            }
                    }else {
                        Toast.makeText(applicationContext, "Empty fields", Toast.LENGTH_SHORT)
                            .show()
                    }}}) {
                    Text("Sign Up", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

    /*otpdone = true
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var username by remember{mutableStateOf("")}
    val context = LocalContext.current
    BackHandler {
        //null to prevent going back
    }
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrect = false // Disables autocorrect
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            Log.i("fuck", "$email $password fuck yourself")
            signInWithEmailAndPassword(email, password, onLoginSuccess, context)
        }) {
            Text("Login")
        }
        Button(onClick = {
            email = "yxkhor711@gmail.com"
            password="123456"
            Log.i("fuck", "$email $password fuck yourself")
            signInWithEmailAndPassword(email, password, onLoginSuccess, context)
        }) {
            Text("Quick Login")
        }*/
        /*Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = username,
            onValueChange = { username= it },
            label = { Text("Username (for signup)") }
        )
        Button(onClick = {
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
                if (task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener

                    // Save additional user info (e.g., username) in Firestore
                    val userData = hashMapOf(
                        "username" to username,
                        "email" to email
                    )

                    database
                        .updateChildren(userData as Map<String, Any>)
                        .addOnSuccessListener {
                            curuser = map[firebaseAuth.currentUser?.uid]
                            projlist = curuser!!.channels
                            onLoginSuccess()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(applicationContext,"Signup failed",Toast.LENGTH_SHORT).show()
                        }
                }else{
                    Toast.makeText(applicationContext,"Empty fields",Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Sign Up")
        }*/
        /*
        Spacer(modifier = Modifier.height(16.dp))

        if (isVerificationSent) {
            TextField(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                label = { Text("Verification Code") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                verifyOtp(verificationCode, verificationId, onLoginSuccess, context)
            }) {
                Text("Verify OTP")
            }
        } else {
            Button(onClick = {
                sendVerificationCodeToEmail(email) { onVerificationCodeSent(context) }
            }) {
                Text("Send Verification Code")
            }
        }

         */



fun sendVerificationCodeToEmail(email: String, onSuccess: () -> Unit) {
    val actionCodeSettings = ActionCodeSettings.newBuilder()
        .setUrl("https://www.example.com/finishSignUp") // Replace with your own URL
        .setHandleCodeInApp(true) // Ensure the link is handled in your app, not in the browser
        .build()

    // Call Firebase Cloud Functions to send the verification code to email.
    // Alternatively, you can implement your own solution to send the email here.
    firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // OTP sent successfully to email
                verificationId = task.result?.toString() ?: ""
                isVerificationSent = true
                onSuccess()
            }

        }
}

fun signInWithEmailAndPassword(
    email: String,
    password: String,
    onLoginSuccess: () -> Unit,
    context: Context
) {
    if(email.equals("") || password.equals("")){
        Toast.makeText(context, "Please input email and password!", Toast.LENGTH_SHORT).show()

    }
    else {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(otpdone){
                        Toast.makeText(
                            context,
                            "Login successful!",
                            Toast.LENGTH_SHORT
                        ).show()
                        curuser = map[firebaseAuth.currentUser?.uid]
                        Log.d("curuid", firebaseAuth.currentUser?.uid ?: "NULL")
                        Log.d("curuser", curuser?.username ?: "NULL")
                        projlist = curuser?.channels ?: arrayListOf()
                        Log.d("sz", projlist.size.toString() + " HI")
                        onLoginSuccess()
                    }
                    else{
                        Toast.makeText(
                            context,
                            "Verify your email through a verification code",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    Toast.makeText(
                        context,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
/*
fun requestOtp(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    val functions = Firebase.functions
    val data = hashMapOf("email" to email)

    functions.getHttpsCallable("sendOtp").call(data)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { e ->
            onFailure(e.message ?: "Failed to send OTP")
        }
}
*/
fun verifyOtp(
    otp: String,
    verificationId: String,
    onLoginSuccess: () -> Unit,
    context: Context
) {
    // Verify the OTP entered by the user (this is a simplified version)
    if (otp == verificationId) {
        Toast.makeText(context, "OTP Verified!", Toast.LENGTH_SHORT).show()
        otpdone = true
    } else {
        Toast.makeText(context, "Incorrect OTP", Toast.LENGTH_SHORT).show()
    }
}

fun sendMessage(messageText: String) {
    val message = curuser?.username?.let { Message(it,messageText,com.google.firebase.Timestamp.now()) }
    notinit = true
    if (message != null) {
        chatRef.add(message)

    }
    else{
        Toast.makeText(context, "null message", Toast.LENGTH_SHORT)
    }
}

fun sendAiMessage(messageText: String) {
    val message = Message("AI Assistant",messageText,com.google.firebase.Timestamp.now())
    if (!message.message.contains("As an") && !message.message.contains("can't answer") && !message.message.contains("cannot answer") && !message.message.contains("I'm sorry") && !message.message.contains("I apologize")) {
        chatRef.add(message)
    }
    else{
        Log.d("stupid_ai", "null message")
    }
}
fun onVerificationCodeSent(context: Context) {
    // This function is triggered when the code is sent.
    // You can update UI or show a message.
    Toast.makeText(context, "Verification code sent", Toast.LENGTH_SHORT).show()
}

fun signInWithEmailAndPassword(
    context: Context,
    email: String,
    password: String,
    onLoginSuccess: () -> Unit
) {
    val auth = firebaseAuth
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                // Assuming you get the user's phone number here
                val phoneNumber = "+1234567890" // Example phone number
                //sendVerificationCode(context, phoneNumber)
                onLoginSuccess()
            } else {
                Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}
fun sendVerificationCode(context: Context, phoneNumber: String) {
    val options = PhoneAuthOptions.newBuilder(firebaseAuth)
        .setPhoneNumber(phoneNumber)       // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(context as Activity)  // Use passed context here
        .setCallbacks(callbacks)           // OnVerificationStateChangedCallbacks
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}


@Composable
fun ProfileCard(
    profileImageUrl: String,
    username: String?,
    email: String?
) {
    Card(
        modifier = Modifier
            .size(200.dp, 100.dp) // 200x100 rectangle size
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Picture
            Image(
                painter = painterResource(R.drawable.pfp),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text information
            Column {
                if (username != null) {
                    Text(
                        text = username,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        softWrap = true, // Enables line wrapping
                        maxLines = 2, // Maximum lines to allow wrapping; adjust as needed
                        overflow = TextOverflow.Ellipsis // Optionally adds ellipsis if text is too long
                    )
                }

                if (email != null) {
                    Text(
                        text = email,
                        fontStyle = FontStyle.Italic,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        softWrap = true, // Enables line wrapping
                        maxLines = 2, // Maximum lines to allow wrapping; adjust as needed
                        overflow = TextOverflow.Ellipsis // Optionally adds ellipsis if text is too long
                    )
                }
            }
        }
    }
}
    var lm = ""
@Composable
fun ChatPage(chatId : String) {
    Log.d("chatpageid", chatId)
    context = LocalContext.current
    curchat = chatId
    chatRef = fsdb.collection("chats").document(chatId).collection("messages")
    val context = LocalContext.current
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }

    // Fetch and listen for new messages
    LaunchedEffect(chatId) {
        listenForMessages(chatId) { newMessages ->
            messages.clear()
            messages.addAll(newMessages.reversed())

            newMessages.lastOrNull()?.let { newMessage ->
                if (newMessage.sender != "AI Assistant" && notinit && newMessage.message != lm) {
                    lm = newMessage.message
                    Log.d("stupid_ai", "Last message: $newMessage")
                    handleNewMessage(chatId, newMessage.message)
                }
            }
        }

    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Message display area
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(messages) { message ->
                ClickableTextMessage(message)
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        // Message input area
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") }
            )
            Button(onClick = {
                if (messageText.isNotBlank()) {
                    sendMessage(messageText)
                    messageText = ""
                } else {
                    Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Send")
            }
        }
    }
}
fun listenForMessages(chatId: String, onMessagesChanged: (List<Message>) -> Unit) {
    chatRef = fsdb.collection("chats").document(chatId).collection("messages")
    chatRef.orderBy("timestamp").addSnapshotListener { snapshots, e ->
        if (e != null) {
            Log.w("ChatUI", "Listen failed.", e)
            return@addSnapshotListener
        }
        val newMessages = snapshots?.mapNotNull { doc ->
            doc.toObject(Message::class.java)
        } ?: emptyList()
        onMessagesChanged(newMessages)

    }
}

fun fetchPreviousMessages(chatId: String, onResult: (List<String>) -> Unit) {
    val chatRef = fsdb.collection("chats").document(chatId).collection("messages")
    chatRef.orderBy("timestamp").get().addOnSuccessListener { snapshot ->
        val messages = snapshot.documents.map { doc ->
            doc.getString("message") ?: ""
        }
        onResult(messages)
    }.addOnFailureListener { e ->
        Log.e("fetchMessages", "Error fetching messages", e)
        onResult(emptyList()) // Return empty list in case of failure
    }
}

fun handleNewMessage(chatId: String, newMessage: String) {
    Log.i("stupid_ai", "hi im here")

    // Fetch previous messages from Firestore using a callback
    fetchPreviousMessages(chatId) { messages ->
        // Check if the new message is a question
        if (newMessage.endsWith("?") || newMessage.lowercase(Locale.ROOT).contains("@ai")) {
            Log.i("stupid_ai", "hi im a question")
            // Build the prompt for OpenAI based on chat history and the new question
            val context = buildContextForQuestion(messages, newMessage)
            Log.d("qcontext", context)

            // Call OpenAI API to get the answer
            getAnswerFromOpenAI(context) { answer ->
                Log.i("stupid_ai", answer)
                // Send or display the answer
                sendAiMessage(answer)
            }
        }
    }
}

fun buildContextForQuestion(messages: List<String>, question: String): String {
    // Limit the number of messages to prevent too much context being sent to OpenAI
    val contextMessages = messages.takeLast(20) // Take last 5 messages for context
    val contextTasks = tasks[curchat]
    val taskStrings : MutableList<String> = mutableListOf()
    contextTasks!!.forEach { t ->
        taskStrings += t.name + " is due at " + t.dueDate + "\n"
    }
    return contextMessages.joinToString("\n") + "\nTasks and deadlines: \n" + taskStrings.joinToString("\n") +  "\n Prompt: $question"
}
fun getAnswerFromOpenAI(context: String, onResult: (String) -> Unit) {
    val url = "https://api.openai.com/v1/chat/completions"
    val jsonBody = JSONObject().apply {
        put("model", "gpt-4") // Specify the model you're using
        put("messages", JSONArray().apply {
            put(JSONObject().apply {
                put("role", "system")
                put("content", "You are a helpful assistant.")
            })
            put(JSONObject().apply {
                put("role", "user")
                put("content", context)
            })
        })
    }

    val client = OkHttpClient()
    val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody.toString())
    Log.d("api", "Bearer $openAiApiKey")
    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer $openAiApiKey")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()  // Handle the error appropriately
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                val jsonResponse = JSONObject(responseBody)
                val answer = jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                onResult(answer)  // Return the answer via the callback
            } else {
                Log.wtf("stupid_ai","Error: ${response.code}")
            }
        }
    })
}
    @Composable
    fun ClickableTextMessage(message: Message) {
        var tosend = message.sender + ": " + message.message
        val context = LocalContext.current
        val annotatedString = buildAnnotatedString {
            val regex = Regex("((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]+(\\/[a-zA-Z0-9#]+\\/?)*")
            var lastIndex = 0
            regex.findAll(tosend).forEach { matchResult ->
                val matchStart = matchResult.range.first
                val matchEnd = matchResult.range.last + 1
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(tosend.substring(lastIndex, matchStart))
                }

                pushStringAnnotation(tag = "URL", annotation = matchResult.value)
                withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                    append(tosend.substring(matchStart, matchEnd))
                }
                pop()
                lastIndex = matchEnd
            }
            if (lastIndex < tosend.length) {
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(tosend.substring(lastIndex))
                }

            }
        }

        ClickableText(
            text = annotatedString,
            modifier = Modifier.padding(8.dp),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset).firstOrNull()?.let { annotation ->
                    // Handle the link click, open in browser
                    val url = annotation.item
                    val formattedUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
                        url
                    } else {
                        "https://$url" // Prepend "https://" if missing
                    }

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(formattedUrl)
                    }

                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "No application found to open the link", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    @Composable
    fun TaskPage(chatId: String) {
        key(chatId) {
            tasklist = tasks[chatId] ?: arrayListOf()
            val sortedTasks = tasklist.sortedBy { task ->
                // Convert the dueDate string to a LocalDate object for comparison
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                LocalDate.parse(task.dueDate, formatter)
            }.toMutableList()
            var taskstate by remember { mutableStateOf(sortedTasks) }
            var isDialogOpen by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxSize()) {
            if(tasks[chatId] == null || tasks[chatId]!!.size == 0){
                Text("No tasks due!")
            }

            else {


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Display each task as a Card
                    taskstate.forEach { task ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp // Use this to set the desired elevation
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = task.name,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Due: ${task.dueDate}",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                // Floating Action Button to open dialog


            }
                FloatingActionButton(
                    onClick = { isDialogOpen = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }

                // Show the dialog if isDialogOpen is true
                if (isDialogOpen) {
                    AddTaskDialog(
                        onAddTask = { newTask ->
                            taskstate += newTask
                            taskstate = taskstate.sortedBy { task ->
                                // Convert the dueDate string to a LocalDate object for comparison
                                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                LocalDate.parse(task.dueDate, formatter)
                            }.toMutableList()
                            tasklist = taskstate
                            tasks[chatId] = tasklist
                            database.child("tasks").setValue(tasks)
                            isDialogOpen = false // Close the dialog after adding
                        },
                        onDismiss = { isDialogOpen = false }
                    )
                }
            }
        }
    }

    @Composable
    fun AddTaskDialog(onAddTask: (Task) -> Unit, onDismiss: () -> Unit) {
        var taskName by remember { mutableStateOf("") }
        var taskDescription by remember { mutableStateOf("") }
        var taskDueDate by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add New Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        label = { Text("Task Name") }
                    )
                    OutlinedTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        label = { Text("Description") }
                    )
                    OutlinedTextField(
                        value = taskDueDate,
                        onValueChange = { taskDueDate = it },
                        label = { Text("Due Date") },
                        placeholder = { Text("e.g., 2023-12-31") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (taskName.isNotBlank() && taskDueDate.isNotBlank()) {
                        onAddTask(
                            Task(
                                name = taskName,
                                description = taskDescription,
                                dueDate = taskDueDate
                            )
                        )
                    }
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}