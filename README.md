# Your Car Your Way

<div>
<img alt="Static Badge" src="https://img.shields.io/badge/Java-%23ff7b0c">
<img alt="Static Badge" src="https://img.shields.io/badge/Spring_Boot-5bd84c">
<img alt="Static Badge" src="https://img.shields.io/badge/Angular-%23F44336">
<img alt="Static Badge" src="https://img.shields.io/badge/TypeScript-3178c6">
</div>

'Your Car Your Way - Chat feature' is a proof of concept of a single feature from a larger project. This feature is aimed at providing the basic functionalities and security for a chat support. Helping customers to reach out for help with any issues related to a car location service.

## Start the project

Git clone:

> git clone https://github.com/ClementAver/OC-YourCarYourWay

### Setup

To initialize the database, run : ycyw_back\ressources\ycyw_db_script_generate.sql

To populate the databse, run : ycyw_back\ressources\ycyw_db_script_populate.sql

Don't forget to check the 'application.properties' file to define the correct user.

## Back

Install dependencies:

> cd ycyw_back

> mvn install

Launch Back-end:

> mvn exec:java -D exec.mainClass="com.openclassrooms.ycyw_back.YcywBackApplication"

## Front

Install dependencies:

> cd ycyw_front

> npm install

Launch Front-end:

> npm start