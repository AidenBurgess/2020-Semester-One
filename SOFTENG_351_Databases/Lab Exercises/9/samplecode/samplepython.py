import mysql.connector

mydb = mysql.connector.connect(
    host = "127.0.0.1",
    port = "3306",
    user = "", # Your user UPI
    passwd = "", # Your passwrod
    db = "", # Your database
 )


mycursor = mydb.cursor()

# Please adapt the following parts to complete Lab 9
# For the useage of .execute() please refer to https://www.w3schools.com/python/python_mysql_getstarted.asp
sql = "SELECT * FROM DEPARTMENT"
mycursor.execute(sql)

myresult = mycursor.fetchall()

for x in myresult:
  print(x)


