# Cyber Security Base - Course Project I

This is a web application developed for project I in cyber security base course (https://cybersecuritybase.mooc.fi/module-3.1)

This application demonstrates 5 common security vulnerabilities included in 2017 OWASP top 10 list (https://www.owasp.org/images/7/72/OWASP_Top_10-2017_%28en%29.pdf.pdf)

This is NOT a secure application and the implementation is NOT complete or precise. Do NOT reuse this code.

## Run

This is a standard spring boot application, just clone the repository, import in your favorite IDE and run it

## Vulnerabilities

There are three users with different authorities:
ted - ted (normal user)
hacker - hacker (normal user)
admin - admin (administrator)

To reproduce vulnerabilities, login into the application with both users and OWASP Zap active.
Follow the links you will see in the navigation bar to map URLs, then follow instructions below.

1. 2013-A8-Cross-Site Request Forgery (CSRF)

The entire web application is vulnerable to CSRF due to Spring Security misconfiguration.

The vulnerability is evident since CSRF token is missing in all forms, but it can can be discovered using OWASP Zap too:

1. Click Manage Add-ons on main taskbar
2. Select Marketplace tab
3. Install Active and Passive scanner rules (beta) addons
4. Scan the website using Active Scan
5. You will see "Absence of Anti-CSRF Tokens" and "Anti CSRF tokens scanner" alerts

SOLUTION: use anti CSRF tokens configuring Spring security properly by removing line 31 in SecurityConfiguration.java (https://github.com/simoneborgio/cybersecuritybase-project-1/blob/master/src/main/java/sec/project/config/SecurityConfiguration.java)

2. 2017-A7-Cross-Site Scripting (XSS)

1. Navigate to Create page (http://localhost:8080/form)
2. Input: <script>alert("attack");</script> in any field
3. Submit the form
4. You will see an alert in the next page and in sign up list page

Search field in http://localhost:8080/signups suffers the same vulnerability. A Zap Active scan can reveal it:

1. Once the website is mapped, right click on website folder in sites list
2. Open Attack menu
3. Select Active Scan...
4. Leave the defualt settings
5. Run the scan
6. In Alerts tab you should see "Cross Site Scripting (Reflected)" warning

SOLUTION: validate user inputs and escape outputs. In this project: search for any occurrence of th:utext (there are 12) and replace them with the safe attribute th:text.
For example, signups.html at line 22 (https://github.com/simoneborgio/cybersecuritybase-project-1/blob/master/src/main/resources/templates/signups.html)

3. 2017-A1-Injection

1. Navigate to List page (http://localhost:8080/signups)
2. In search field type: ' OR ''='
3. Click on Search button
4. You will see more records than you should (i.e. if you are using ted's account, you will see "Sign up by Ben - Ben's address")

4. 2017-A5-Broken Access

1. while authenticated as ted, navigate to http://localhost:8080/form
2. Insert any name
3. In address field type: Address 1', 'admin'), ('Name 2', 'Address 2
4. Submit the form
5. In the result page you will see this message "2 items saved", that is strange
6. Even more scary: you created one record for admin!

SOLUTION: all methods in class SignupRepositoryImpl (https://github.com/simoneborgio/cybersecuritybase-project-1/blob/master/src/main/java/sec/project/repository/SignupRepositoryImpl.java) are insecure and vulnerable to SQL injection.
Once again, escape user input and use prepared statements instead of native queries or even better let Spring data JPA manage the data access

4. 2017-A3-Sensitive Data Exposure

Authenticated users can access other users saved sign ups using URL format http://localhost:8080/signups/{id}
where {id} is an integer counter, incremented by one after each subscription and and therefore predictable.

1. Login as ted (normal user)
2. Navigate to http://localhost:8080/signups/2
3. You will see one of admin's sign ups

SOLUTION: in SignupController (https://github.com/simoneborgio/cybersecuritybase-project-1/blob/master/src/main/java/sec/project/controller/SignupController.java), line 78, compare current user principal name with user attribute in Signup object read from database. Show the signup only if they match, otherwise return an error like HTTP 403 Forbidden

5. 2017-A6-Security Misconfiguration

Administration menu is hidden for users missing the ADMIN role (ted), but the functionality is still accessible:

1. Login as ted (normal user)
2. Navigate to http://localhost:8080/admin

SOLUTION: secure access to methods exposed by class AdminController configuring Spring Security in SecurityConfiguration class at line 25 (https://github.com/simoneborgio/cybersecuritybase-project-1/blob/master/src/main/java/sec/project/config/SecurityConfiguration.java) adding this rule: .antMatchers("/admin").hasAuthority("ADMIN")