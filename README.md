# Atypon Java and DevOps Training  
Atypon Final Project Using Spring, Spring Security, Thymeleaf with Spring extras security extension. The same project was implementd first using traditional MVC Servlets/ JSPs click [here](https://github.com/MamounH/InMemoryServletWebProject) to view it.

To make the Web application Maintainable, Extendable and Scalable, SOLID principles, Design Patterns, ACID Criteria, Clean Code principles and Effective Java items
were applied. Also to view the full detailed project report please click [here](https://github.com/MamounH/InMemorySpringProject/blob/master/ProjectReport/ProjectReport.pdf)!. 

![](Screenshots/login.PNG)

## Overview

The system has 3 levels of users: Students, Instructors, Admins. 

![](Screenshots/Contextdiagram.png)



**Administrator**: 
- Add, Update, Delete, View all users.

![](Screenshots/adminuse.png)


**Editor**: 
- Add, Update, Delete, View all Books records.
- Add, Update, Delete, View all Quotes records.

![](Screenshots/editoruse.png)


**Viewer**: 
- Can only view Books/Quotes.

![](Screenshots/Viewer.png)


## Technology Stack
- Java 16
- Spring Core, Boot, MVC, Security
- Thymeleaf rendring engine and Bootstrap CSS framework for the front-end, with Thymeleaf extras for security. 
- Junit
- Maven Build Tool
- Jenkins for CI/CD
- GitHub for VS
- AWS and Docker for deployment.
- Datadog for montioring. 
- SSL digital certificate to encrypt trafic and help to secure information such as users data.

![](Screenshots/devops.png)
DevOps Pipeline.

## Database Architecture

i have built a three-tier architecture. It’s the most widely used architecture to design a DBMS. This architecture will separate and organize the application into three tiers:  

![](Screenshots/Database3.png)

One of the main benefits of this architecture that we have a separation of
functionality we can have each tier on a separate server platform.
Also, we can develop each tier simultaneously without effecting or impacting
other tiers. The security will be maximised because the presentation tier and data
tier cannot communicate directly because the application tier is working as a
firewall preventing incorrect or malicious data to be inserted to the database. 

## Concurrent Access to database records

The sole purpose of this project is to build every thing from scratch that’s
why I avoided using ConcurrentHashMap in the first version and in this
version of this project too. AccessSynch class is used to synchronize access
to the database records using Reentrant ReadWrite Locks.

![](Screenshots/locking.png)

As we can see i have used Reentrant ReadWrite lock for each object in a
HashMap so it can be used when multiple threads are using the database thus
allowing concurrent access instead of locking the whole table.

The threadQueue HashMap will store the number of threads that are using or
are in queue to use the record in the locks HashMap, so when there are no
threads using the record, we can safely remove the record from the HashMap. 

A lock object is also used to synchronize access to parts of code in the
AccessSynch class that will be called only by one thread at a time. i.e., getting
a record lock thus preventing two threads from acquiring the lock at the same time. 

## Junit 

According to Michael feathers in his book working Effectively with Legacy Code he defines a legacy code as a code without tests, There’s no excuse to not write tests, many test frameworks out there, in this
system I used JUnit testing framework and manual testing. 

![](Screenshots/junit.png)

## AWS 

Amazon Web Services were used in the project, as several instances were created for Jenkins Server and Web Servers for both versions of the project.

![](Screenshots/aws.png)

## Jenkins

For Continuous Integration/Deployment i created a jenkins server on aws and linked it to the web servers on aws and datadog and to this GitHub repo. 

![](Screenshots/JenkinsDashBoard.png)

Jenkins AWS Server Status:
![](Screenshots/JenkinsStatus.png)

Every Failure i have encountered was a closer step to sucess....
![](Screenshots/JenkinsBuild.png)

![](Screenshots/CI_CDTest.png)

## Datadog

Datadog will be collecting data and providing analytics that will be very helpful for future decisions i.e., Performance, Errors and more. 

Datadog Infrastructure List:
![](Screenshots/datadog1.png)

AWS EC2 instance analytics on Datadog: 
![](Screenshots/datadog2.png)

Also, to do more observation and to monitor the pipeline itself I configured and connected the Jenkins server itself to Datadog itself. 

![](Screenshots/datadog3.png)

 Datadog Agent status on Jenkins Server:
![](Screenshots/datadog4.png)

## SSL Certificate
To enable the website to move from HTTP to HTTPS i created my own SSl Certificate using PKCS#12 as the binary format for storing the certificate chain and the private key in a single, encryptable file. RSA (Rivest–Shamir–Adleman) as the key algorithm with key size of 2048

![](Screenshots/SSL1.png)


<p float="left">
  <img src="/Screenshots/SSL2.png" width="270"   />
  <img src="/Screenshots/SSL3.png" width="270"  /> 
  <img src="/Screenshots/SSL4.png"width="270"   />
</p>


## Canva Pro and Creately 
These websites have nothing to do with the technical detailes of the project but they did a great a job helping me to design nice diagrams and UML diagrams for the project report that was very informative. 
![](Screenshots/canva.png)


<p float="left">
  <img src="/Screenshots/SSL2.png" width="270"   />
  <img src="/Screenshots/SSL3.png" width="270"  /> 
  <img src="/Screenshots/SSL4.png"width="270"   />
</p>



## Screenshots Samples

<p float="left">
  <img src="/Screenshots/FailedLogin.png"width="270"   />
  <img src="/Screenshots/AdminUsers.png" width="270"   />
  <img src="/Screenshots/AdminCourses.png" width="270"  /> 
</p>


<p float="left">
  <img src="/Screenshots/UpdateGrade.png"width="270"   />
  <img src="/Screenshots/StudentView.png" width="270"   />
  <img src="/Screenshots/DeletingCourse.png" width="270"  /> 
</p>

<p float="left">
  <img src="/Screenshots/FailedLogin.png"width="270"   />
  <img src="/Screenshots/ErrorMapping.png" width="270"   />
  <img src="/Screenshots/FixBugJob.png" width="270"  /> 
</p>



For more screen shots please click [here!](/Screenshots/)



### Contact

Mamoun Abu Koush - :e-mail: Mamounhayel@gmail.com

LinkedIn: [Mamoun Hayel](https://www.linkedin.com/in/mamounhayel/)

