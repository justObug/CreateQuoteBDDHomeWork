# How to Fix the "Cannot Resolve Symbol" Issues

## Problem
The "cannot resolve symbol" errors occur because IntelliJ IDEA hasn't imported the Maven dependencies specified in your pom.xml file.

## Solution Steps

### Step 1: Fix Import Statement
I've already fixed the import statement for RestClient in QuoteStepsDefs.java. It was changed from:
```java
import utils.RestClient;
```
to:
```java
import org.example.utils.RestClient;
```

### Step 2: Reload Maven Dependencies
To fix the "cannot resolve symbol 'io'" errors, you need to reload the Maven dependencies in IntelliJ IDEA:

1. Look for the **Maven** tool window on the right side of IntelliJ IDEA
2. Click the **Reload Projects** button (circular arrow icon)
   
OR

1. Right-click on the **pom.xml** file in the Project view
2. Select **Maven** → **Reload project**

### Step 3: Alternative Methods (If Step 2 doesn't work)
If the above doesn't work, try these additional steps:

1. **Invalidate Caches and Restart:**
   - Go to **File** → **Invalidate Caches and Restart** → **Invalidate and Restart**
   - After IntelliJ restarts, reload the Maven project again

2. **Check Project SDK:**
   - Go to **File** → **Project Structure** → **Project**
   - Ensure the Project SDK is set to Java 11 or higher
   - Click **Apply** and **OK**

3. **Manually Trigger Maven Import:**
   - Open the **Terminal** tab in IntelliJ IDEA
   - Run the command: `mvn clean compile`

### Step 4: Verify Dependencies
After reloading, check that the following dependencies are properly imported:
- Cucumber Java (io.cucumber)
- RestAssured (io.restassured)
- TestNG (org.testng)

These should resolve all the "cannot resolve symbol" errors in your QuoteStepsDefs.java file.

## Additional Notes
- The RestClient.java file has been created in the correct location
- Your pom.xml file is properly formatted with all required dependencies
- Once Maven dependencies are loaded, all import errors should disappear